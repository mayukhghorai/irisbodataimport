package com.mcx.test;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.IOException;

/**
 * Created by Ext-MayukhG on 9/5/2018.
 */
public class FTPS {
    public static void main(String[] args)
    {
        String server = "103.216.76.149";
        int port = 990;
        String user = "mcxftp";
        String pass = "Mcx@1234";

        boolean error = false;
        FTPClient ftp = null;
        try
        {
            ftp = new FTPClient();
            ftp.setConnectTimeout(10);
//            ftp.setAuthValue("SSL");
//            ftp.
//            ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

            int reply;

            ftp.connect("103.216.76.149", 990);
            ftp.getConnectTimeout();
            System.out.println("Connected to ftp.wpmikz.com.cn");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            reply = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(reply))
            {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                System.exit(1);
            }
            ftp.login(user, pass);
            // ... // transfer files
            ftp.setBufferSize(1000);
            ftp.enterLocalPassiveMode();
            // ftp.setControlEncoding("GB2312");
            ftp.changeWorkingDirectory("/");
            ftp.changeWorkingDirectory("/ae"); //path where my files are
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            System.out.println("Remote system is " + ftp.getSystemName());

            String[] tmp = ftp.listNames();  //returns null
            System.out.println(tmp.length);
        }
        catch (IOException ex)
        {
            System.out.println("Oops! Something wrong happened");
            ex.printStackTrace();
        } finally
        {
            // logs out and disconnects from server
            try
            {
                if (ftp.isConnected())
                {
                    ftp.logout();
                    ftp.disconnect();
                }
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}
