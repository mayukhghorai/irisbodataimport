package com.mcx.iris.util;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ext-MayukhG on 5/23/2018.
 */
@ConfigurationProperties
@Component
public class NetworkShareFileCopy {

    protected static final Log logger = LogFactory.getLog(NetworkShareFileCopy.class);

    private static String server;
    private static String username;
    private static String password;
    private static String protocol;
    private static int port;
    private static int timeoutInMillis;
    private static String bhavCopyDest;
    private static String bhavCopySrc;
    private static String bhavCopy;

    public static void createDirectory(String value){
        try {
            File directory = new File(value);
            if (!directory.exists()) {
                directory.mkdir();
            }
        }
        catch (Exception e){
            logger.error("Problem while creating directory");
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public static void copyFiles(String fileLocationSource,
                                 String fileLocationDestination, String date) {
        boolean status = false;
        logger.info("fileLocationSource= " + fileLocationSource);
        logger.info("fileLocationDestination= " + fileLocationDestination);
        try {
            status = commonsNetFTPSFileSharing(fileLocationSource, fileLocationDestination, date);
        } catch (IOException e) {
            logger.error("Problem while copying files...");
            e.printStackTrace();
        }
//        }
        if (status) {
            logger.info("Completed...");
        } else {
            logger.info("Copy failed. Please check if the file is available at source location...");
        }
    }

    public static boolean commonsNetFTPSFileSharing(String fileLocationSource,
                                                    String fileLocationDestination, String date) throws IOException {
        boolean status = false;
        System.setProperty("javax.net.info", "ssl");

        logger.info("server = " + server + " username = " + username + " port = " + port + " protocol= " + protocol);
        boolean isImpicit = true;

        FTPSClient client = new FTPSClient(protocol, isImpicit);

        client.setDataTimeout(timeoutInMillis);
//        client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        logger.info("################ Connecting to Server ################################");

        try {
            int reply;
            logger.info("################ Connect Call ################################");
            client.connect(server, port);

            client.login(username, password);

            logger.info("################ Login Success ################################");

            //client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setFileType(FTP.NON_PRINT_TEXT_FORMAT);
            client.execPBSZ(0);  // Set protection buffer size
            client.execPROT("P"); // Set data channel protection to private
            client.enterLocalPassiveMode();

            logger.info("Connected to " + server + ".");
            reply = client.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                logger.error("FTP server refused connection.");
//                System.exit(1);
            }
            FTPFile[] ftpFiles =  client.listFiles(fileLocationSource);
            if(ftpFiles!=null && ftpFiles.length >0) {
                logger.info(ftpFiles[0].getName() + " exists");
                status = client.retrieveFile(fileLocationSource, new FileOutputStream(fileLocationDestination));
            }
            else {
                //If yesterday's bhavcopy not found try for older dates
                logger.error(fileLocationSource + " does not exist");
                Date dt = new SimpleDateFormat("yyyyMMdd").parse(date);
                for (int count = 0; count < 10; count++) {
                    Date yesterday = DateUtil.yesterday(dt);
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    Integer month = DateUtil.getMonth(yesterday);
                    Integer year = DateUtil.getCurrentYear(yesterday);
                    fileLocationDestination = bhavCopyDest + bhavCopy + dateFormat.format(yesterday) + ".csv";
                    if (month < 3) {
                        fileLocationSource = bhavCopySrc + (year - 1) + " to MARCH-" + year + "/" + DateUtil.getCurrentMonth(yesterday) + "/" + bhavCopy + dateFormat.format(yesterday) + ".csv";
                    } else {
                        fileLocationSource = bhavCopySrc + year + " to MARCH-" + (year + 1) + "/" + DateUtil.getCurrentMonth(yesterday) + "/" + bhavCopy + dateFormat.format(yesterday) + ".csv";
                    }
                    FTPFile[] ftpFiles3 = client.listFiles(fileLocationSource);
                    if (ftpFiles3 != null && ftpFiles3.length > 0) {
                        logger.info(ftpFiles3[0].getName() + " exists");
                        status = client.retrieveFile(fileLocationSource, new FileOutputStream(fileLocationDestination));
                        break;
                    } else {
                        dt = DateUtil.yesterday(dt);
                        logger.info(fileLocationSource + " does not exist");
                    }
                }
            }
        } catch (Exception e) {
            if (client.isConnected()) {
                try {
                    client.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            logger.error("Could not connect to server.");
            e.printStackTrace();
            status = false;
        } finally {
            //client.disconnect();
            client.logout();
            logger.info("# client disconnected");
        }
        return status;
    }

    @Value("${server.host.name}")
    private void setServer(String server) {
        this.server = server;
    }

    @Value("${server.host.user}")
    private void setUsername(String username) {
        NetworkShareFileCopy.username = username;
    }

    @Value("${server.host.pswd}")
    private void setPassword(String password) {
        this.password = password;
    }

    @Value("${server.host.protocol}")
    private void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    @Value("${server.host.port}")
    private void setPort(int port) {
        this.port = port;
    }

    @Value("${server.host.timeout}")
    private void setTimeoutInMillis(int timeoutInMillis) {
        this.timeoutInMillis = timeoutInMillis;
    }

    @Value("${bhavcopy.dest.location}")
    private void setBhavCopyDest(String bhavCopyDest) {
        this.bhavCopyDest = bhavCopyDest;
    }

    @Value("${bhavcopy.source.location}")
    private void setBhavCopySrc(String bhavCopySrc) {
        this.bhavCopySrc = bhavCopySrc;
    }

    @Value("${file.name.bhavcopy}")
    private void setBhavCopy(String bhavCopy) {
        this.bhavCopy = bhavCopy;
    }
}