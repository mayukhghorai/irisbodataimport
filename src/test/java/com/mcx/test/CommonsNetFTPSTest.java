package com.mcx.test;

import com.mcx.iris.util.DateUtil;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonsNetFTPSTest {
    public static void main(String[] args) throws Exception {
//        System.setProperty("javax.net.debug", "ssl");

        String server = "103.216.76.149";
        String username = "mcxftp";
        String password = "Mcx@1234";
        String remoteProductMaster = "/Common/MCX_ProductMaster.csv";
        String localProductMaster = "cvs2/MCX_ProductMaster.csv";
        String remoteBhavCopy = "/Common/Bhavcopy/APR-2018 to MARCH-2019/September/MCX_MS20180402.csv";
        String localBhavCopy = "cvs2/MCX_MS20180902.csv";
        String protocol = "SSL"; // TLS / null (SSL)
        int port = 990;
        int timeoutInMillis = 10000;
        boolean isImpicit = true;
        String date="20180402";

        FTPSClient client = new FTPSClient(protocol, isImpicit);

        client.setDataTimeout(timeoutInMillis);
//        client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        System.out.println("################ Connecting to Server ################################");

        try
        {
            int reply;
            System.out.println("################ Connect Call ################################");
            client.connect(server, port);

            client.login(username, password);

            System.out.println("################ Login Success ################################");

            //client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setFileType(FTP.NON_PRINT_TEXT_FORMAT);
            client.execPBSZ(0);  // Set protection buffer size
            client.execPROT("P"); // Set data channel protection to private
            client.enterLocalPassiveMode();

            System.out.println("Connected to " + server + ".");
            reply = client.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                client.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }

            FTPFile[] ftpFiles =  client.listFiles(remoteProductMaster);
            if(ftpFiles!=null && ftpFiles.length >0) {
                System.out.println(ftpFiles[0].getName() + " exists");

                boolean retrieved1 = client.retrieveFile(remoteProductMaster, new FileOutputStream(localProductMaster));
                System.out.println(retrieved1 + "==retrieved1");
            }
            FTPFile[] tst2 =  client.listFiles(remoteBhavCopy);
            if(tst2!=null && tst2.length >0) {
                System.out.println(tst2[0].getName() + " exists");
                boolean retrieved2 = client.retrieveFile(remoteBhavCopy, new FileOutputStream(localBhavCopy));
                System.out.println(retrieved2 + "==retrieved2");
            }
            else
            {
                System.out.println(remoteBhavCopy + " does not exist");
                Date dt = new SimpleDateFormat("yyyyMMdd").parse(date);
//                Date dt = DateUtil.convertStringToDate(date);

                for(int count = 0; count<10;count++) {

                    Date yesterday = DateUtil.yesterday(dt);
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

                    Integer month = DateUtil.getMonth(yesterday);
                    Integer year = DateUtil.getCurrentYear(yesterday);
                    localBhavCopy="cvs2/MCX_MS"+dateFormat.format(yesterday)+".csv";
                    if(month<3) {
                        remoteBhavCopy = "/Common/Bhavcopy/APR-"+(year-1)+" to MARCH-"+year+"/"+DateUtil.getCurrentMonth(yesterday)+"/MCX_MS"+dateFormat.format(yesterday)+".csv";
                    }
                    else {
                        remoteBhavCopy = "/Common/Bhavcopy/APR-"+year+" to MARCH-"+(year+1)+"/"+DateUtil.getCurrentMonth(yesterday)+"/MCX_MS"+dateFormat.format(yesterday)+".csv";
                    }

                    FTPFile[] ftpFiles3 =  client.listFiles(remoteBhavCopy);
                    if(ftpFiles3!=null && ftpFiles3.length >0) {
                        System.out.println(ftpFiles3[0].getName() + " exists");

                        boolean retrieved1 = client.retrieveFile(remoteBhavCopy, new FileOutputStream(localBhavCopy));
                        System.out.println(retrieved1 + "==retrieved1");
                        break;
                    }
                    else
                    {
                        dt = DateUtil.yesterday(dt);
                        System.out.println(dt.toString());
                        System.out.println(remoteBhavCopy + " does not exist");
                    }
                }
            }
        }
        catch (Exception e)
        {
            if (client.isConnected())
            {
                try
                {
                    client.disconnect();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
            System.err.println("Could not connect to server.");
            e.printStackTrace();
            return;
        }
        finally
        {
            //client.disconnect();
            client.logout();
            System.out.println("# client disconnected");
        }
    }
}