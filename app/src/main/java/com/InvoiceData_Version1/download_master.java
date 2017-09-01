package com.InvoiceData_Version1;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

    public class download_master
    {
        AsyncTask<Integer, Integer, String> task_Download=new AsyncTask<Integer, Integer, String>()

    {
        static final int TENSECONDS = 10 * 1000;
        static final int TWENTYSECONDS = 20 * 1000;
        static final int THIRTYSECONDS = 30 * 1000;
        private static final String Server = "122.160.80.197";
        private static final String User = "cmri";
        private static final String Password = "pass@123";

        @Override
        protected String doInBackground(Integer... integers)
        {
            FTPClient mFTPClient = new FTPClient();
            try
            {
                mFTPClient.setBufferSize(0);
                mFTPClient.setConnectTimeout(TWENTYSECONDS);
                mFTPClient.setDefaultTimeout(TWENTYSECONDS);
                mFTPClient.connect(Server);
                if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
                {
                    mFTPClient.disconnect();
                    return "Download - Server Not Found";
                }
                mFTPClient.setSoTimeout(THIRTYSECONDS);
                boolean success = mFTPClient.login(User, Password);
                if (!success)
                {
                    return "Download - Server Not Log In";
                }
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.changeWorkingDirectory(File.separator + "Accounts");
                if (!success)
                {
                    return "Download - Folder Not Found";
                }
                String[] files = mFTPClient.listNames();
                File Masterfile = new File("/sdcard/Invoice_image/", "Employee_data.csv");
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(Masterfile));
                success = mFTPClient.retrieveFile("Employee_data.csv", outputStream);
                outputStream.close();

                File allmeterfile = new File("/sdcard/Invoice_image/", "Employee_data.csv");

                Masterfile.renameTo(allmeterfile);
            }
            catch (Exception e)
            {
                String log = e.getMessage();
                Log.d("Errore: ", log);
                return "Download - Error 1 - " + log;
            }
            finally
            {
                try
                {
                    //mFTPClient.setSoTimeout(TENSECONDS);
                    mFTPClient.logout();
                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                    return "Download - Error 2 - " + ex.getMessage();
                }
                finally
                {
                    try
                    {
                        mFTPClient.disconnect();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        return "Download - Error 3 - " + e.getMessage();
                    }
                }
            }
            return "All Complete";
        }
    };
    }

