package com.mcx.iris;

/**
 * Created by Ext-MayukhG on 08-03-2018.
 */

import com.mcx.iris.util.AppConstant;
import com.mcx.iris.util.DateUtil;
import com.mcx.iris.util.DynamicPropertiesFileReader;
import com.mcx.iris.util.NetworkShareFileCopy;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication(scanBasePackages = {"com.mcx.iris", "com.mcx.iris.util"})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@EnableScheduling
public class RunBatchJob implements CommandLineRunner {

    protected static final Log logger = LogFactory.getLog(RunBatchJob.class);
    ApplicationContext context;
    DynamicPropertiesFileReader fileReader;
    JobLauncher instrumentJobLauncher, bhavCopyJobLauncher;
    Job job, job2;

    public static void main(String[] args) {
        SpringApplication.run(RunBatchJob.class, args);
    }

    @Override
    public void run(String... args) {
        configuration();
        periodicDataLoad();
    }

    public void configuration() {
        String[] springConfig =
                {"database.xml",
                        "config/context.xml",
                        "config/jobs/instrument-job-report.xml",
                        "config/jobs/bhavcopy-job-report.xml"
                };
        context = new ClassPathXmlApplicationContext(springConfig);
        Object mysqltemplate = context.getBean("instrumentJDBCTemplate");
        Object scripMasterTemplate = context.getBean("scripMasterJDBCTemplate");
        fileReader = (DynamicPropertiesFileReader) context.getBean("DynamicPropertiesFileReader");
        fileReader.start();
        instrumentJobLauncher = (JobLauncher) context.getBean("instrumentJobLauncher");
        job = (Job) context.getBean("instrumentReportJob");

        bhavCopyJobLauncher = (JobLauncher) context.getBean("bhavCopyJobLauncher");
        job2 = (Job) context.getBean("bhavCopyReportJob");
        NetworkShareFileCopy.createDirectory(getValue(AppConstant.PRODUCT_MASTER_DEST));
        NetworkShareFileCopy.createDirectory(getValue(AppConstant.BHAVCOPY_DEST));
    }

    public void dataTranfer() {
        Integer currYear = DateUtil.getCurrentYear();
        Integer nxtYear = DateUtil.getCurrentYear() + 1;
        //Copy Product_Master file
        String remoteProductMaster = getValue(AppConstant.PRODUCT_MASTER_SOURCE) + getValue(AppConstant.INSTRUMENT);
        String localProductMaster = getValue(AppConstant.PRODUCT_MASTER_DEST) + getValue(AppConstant.INSTRUMENT);
        logger.info("Source = " + remoteProductMaster + " Destination = " + localProductMaster);
        NetworkShareFileCopy.copyFiles(remoteProductMaster, localProductMaster, DateUtil.getYesterday());

        //Copy Bhavcopy file
        String bhavCopyLocation = getValue(AppConstant.BHAVCOPY_SOURCE) + currYear + " to MARCH-" + nxtYear + "/" + DateUtil.yesterdayMonth() + "/";
        String bhavCopyFileName = getValue(AppConstant.BHAVCOPY) + DateUtil.getYesterday() + ".csv";
        String remoteBhavCopy = bhavCopyLocation + bhavCopyFileName;
        String localBhavCopy = getValue(AppConstant.BHAVCOPY_DEST) + bhavCopyFileName;
        logger.info("Source = " + remoteBhavCopy + " Destination = " + localBhavCopy);
        NetworkShareFileCopy.copyFiles(remoteBhavCopy, localBhavCopy, DateUtil.getYesterday());

    }

    @Scheduled(cron = "${scheduling.job.cron}", zone = "Asia/Calcutta")
    private void periodicDataLoad() {
        String bhvaCopyLoc= getValue(AppConstant.BHAVCOPY_DEST);
        String instrumentLoc= getValue(AppConstant.PRODUCT_MASTER_DEST);
//        dataTranfer();

        try {
            logger.info("Start time of Instrument loading: " + System.currentTimeMillis());
            JobExecution execution = instrumentJobLauncher.run(job, new JobParametersBuilder().addString("loc",instrumentLoc)
                    .addLong("time", System.currentTimeMillis()).toJobParameters());
            logger.info("Exit Status is: " + execution.getStatus() + " and End time of Instrument loading: " + System.currentTimeMillis());

            logger.info("Start time of Bhavcopy loading: " + System.currentTimeMillis());
            JobExecution bhavCopyexecution = bhavCopyJobLauncher.run(job2, new JobParametersBuilder().addString("loc",bhvaCopyLoc)
                    .addString("Date", DateUtil.getYesterday())
                    .addLong("time", System.currentTimeMillis()).toJobParameters());
            logger.info("Exit Status is: " + bhavCopyexecution.getStatus() + " and End time of Bhavcopy loading: " + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("Done");
    }

    public String getValue(String constant) {
        return fileReader.getDynamicPropertiesMap().get(constant);
    }
}