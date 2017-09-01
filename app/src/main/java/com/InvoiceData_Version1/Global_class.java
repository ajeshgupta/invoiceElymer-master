package com.InvoiceData_Version1;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sandy on 23-08-2017.
 */

public class Global_class
    {


    private static final String ERROR_Message = "ERROR_Message :";
    private static final String ALERT_Message = "ALERT_Message :";


    public static class l
        {

        static void e(String TAG, Exception errorMsg, String Error_msg_in_string, int
                Error_msg_in_Integer)
        {
            if(errorMsg != null)
            {
                Log.e(TAG, errorMsg.getMessage());
                Log.e(TAG, errorMsg.getLocalizedMessage());
                Log.e(TAG, String.valueOf(errorMsg.getCause()));
                return;
            }
            else
            {
                if(Error_msg_in_string != null)
                {
                }//pending
            }
        }

        static void d(Object Msg)
        {
            String s = null;
            if(Msg.equals(s))
            {
                s = (String) Msg;
                Log.d(ALERT_Message, s);
            } //Toast.makeText(this,Msg,Toast.LENGTH_LONG).show();
        }
        }

    public static class Ftp
        {
        private static final int TWENTYSECONDS = 20;
        private static final int THIRTYSECONDS = 30;
        private static final String Server = "122.160.80.197";
        private static final String User = "cmri";
        private static final String Password = "pass@123";
        private static final int port = 21;
        private FTPClient mFTPClient;


        public Ftp()
        {
            mFTPClient = new FTPClient();
        }

        private void showServerReply(FTPClient ftpClient)
        {
            String[] replies = ftpClient.getReplyStrings();
            if(replies != null && replies.length > 0)
            {
                for(String aReply : replies)
                {
                    l.d(aReply);
                }
            }
        }

        public void connectToServer()
        {
            try
            {
                mFTPClient.setBufferSize(0);
                mFTPClient.setConnectTimeout(TWENTYSECONDS);
                mFTPClient.setDefaultTimeout(TWENTYSECONDS);
                mFTPClient.connect(Server);
                showServerReply(mFTPClient);
                int replyCode = mFTPClient.getReplyCode();
                if(!FTPReply.isPositiveCompletion(replyCode))
                {
                    l.d(String.valueOf("Upload Ely - Server Not Found" + replyCode));
                    mFTPClient.disconnect();
                    return;
                }
                mFTPClient.setSoTimeout(THIRTYSECONDS);
                boolean success = mFTPClient.login(User, Password);
                showServerReply(mFTPClient);
                if(!success)
                {
                    mFTPClient.disconnect();
                    l.d("Could not login to the server");
                    return;
                }
                else
                {
                    l.d("LOGGED IN SERVER");
                }

            }
            catch(IOException e)
            {
                l.e(ERROR_Message, e, null, 0);
            }
        }

        public void NewDir(String FTP_NewfolderName)
        {
            try
            {
                // String[] Innerfolders = new String[]{"Invoice_csv" ,"Invoice_jpg" };
                 switch(mFTPClient.cwd(FTP_NewfolderName))
                    {
                        case 550:
                            mFTPClient.makeDirectory(FTP_NewfolderName);
                            if(!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
                            {
                                mFTPClient.disconnect();
                                l.d("Upload Ely - " + FTP_NewfolderName + " not created");
                                return;
                            }
                            break;
                        default:
                            break;
                    }


            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }

        public void changeDir_P2C(String ToNewDir)
        {
            try
            {
                mFTPClient.changeWorkingDirectory(File.separator +ToNewDir);
//
//                if(FromparentDir.equals(""))
//                {
//                    mFTPClient.changeWorkingDirectory(File.separator + (FromparentDir != "" ?
//                            FromparentDir + File.separator : "") + ToNewDir);
//                }
//                else
//                {
//                    mFTPClient.changeWorkingDirectory(File.separator+ ToNewDir);
//                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            if(!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
            {
                try
                {
                    mFTPClient.disconnect();
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
                l.d("Upload Ely - Folder Not Found 1");
                return;
            }
        }

        public void SaveFileToServer(String FilePath, String FileName)
        {

            try
            {
                mFTPClient.enterLocalPassiveMode();
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                InputStream inputStream = new FileInputStream(new File(FilePath));
                l.d("Start uploading second file");
                OutputStream outputStream = mFTPClient.storeFileStream(FileName);
                byte[] bytesIn = new byte[4096];
                int read = 0;
                while((read = inputStream.read(bytesIn)) != -1)
                {
                    outputStream.write(bytesIn, 0, read);
                }
                inputStream.close();
                outputStream.close();
                boolean completed = mFTPClient.completePendingCommand();
                if(completed)
                {
                    l.d("The second file is uploaded successfully.");

                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

        }

        public void closeFtp()
        {
            try
            {

            }

            finally
            {
                try
                {
                    if(mFTPClient.isConnected())
                    {
                        mFTPClient.logout();
                        mFTPClient.disconnect();
                    }
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }
            }

        }
        }
    }


//    public class task_UploadElyFiles extends AsyncTask<Void, Integer, String>
//        {
//
//        @Override
//        protected String doInBackground(Void... params)
//        {
////            final List<String[]> lines = GMapsActivity.ReadFilenamesFromFileFifo();
////            if (lines.size() == 0)
////            {
////                return "All Complete";
////            }
//          //  boolean[] filesuploaded = new boolean[lines.size()];
//            FTPClient mFTPClient = new FTPClient();
//            final Ftp ftp = new Ftp();
//            try
//            {
//                mFTPClient.setBufferSize(0);
//                mFTPClient.setConnectTimeout(TWENTYSECONDS);
//                mFTPClient.setDefaultTimeout(TWENTYSECONDS);
//                mFTPClient.connect(ftp.Server);
//                if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                {
//                    mFTPClient.disconnect();
//                    return "Upload Ely - Server Not Found";
//                }
//                mFTPClient.setSoTimeout(THIRTYSECONDS);
//                boolean success = mFTPClient.login(ftp.User, ftp.Password);
//                if (!success)
//                {
//                    mFTPClient.disconnect();
//                    return "Upload Ely - Server Not Log In";
//                }
//                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
//                mFTPClient.enterLocalPassiveMode();
//                mFTPClient.changeWorkingDirectory(File.separator + ftp.folder);
//                if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                {
//                    mFTPClient.disconnect();
//                    return "Upload Ely - Folder Not Found";
//                }
//                FileInputStream in = null;
//                for (int i = 0; i < lines.size(); i++)
//                {
//                    mFTPClient.setSoTimeout(THIRTYSECONDS);
//                    File file = new File(FilesLocationInInternalMemory, lines.get(i)[0]);
//                    if (!file.exists())
//                    {
//                        filesuploaded[i] = true;
//                        continue;
//                    }
//                    long lastsavedtime = new LastSyncCSV(file.toString()).getlasttime();
//                    if (lastsavedtime > 0)
//                    {
//                        if (Calendar.getInstance().getTimeInMillis() / 1000 < lastsavedtime + 600)
//                        {
//                            filesuploaded[i] = true;
//                            continue;
//                        }
//                    }
//                    String mriserial = lines.get(i)[1];
//                    String mainfolder = "CM_" + mriserial;
//                    String[] folders = new String[]{"CM_" + mriserial + "_UP", "CM_" + mriserial + "_DN"};
//                    switch (mFTPClient.cwd(mainfolder))
//                    {
//                        case 550:
//                            mFTPClient.makeDirectory(mainfolder);
//                            if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                            {
//                                mFTPClient.disconnect();
//                                return "Upload Ely - " + mainfolder + " not created";
//                            }
//                            break;
//                        default:
//                            break;
//                    }
//                    mFTPClient.changeWorkingDirectory(File.separator + (ftp.folder != "" ? ftp.folder + File.separator : "") + mainfolder);
//                    for (int j = 0; j < folders.length; j++)
//                    {
//                        switch (mFTPClient.cwd(folders[j]))
//                        {
//                            case 550:
//                                mFTPClient.makeDirectory(folders[j]);
//                                if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                                {
//                                    mFTPClient.disconnect();
//                                    return "Upload Ely - " + folders[j] + " not created";
//                                }
//                                break;
//                            default:
//                                break;
//                        }
//                        mFTPClient.changeWorkingDirectory(File.separator + (ftp.folder != "" ? ftp.folder + File.separator : "") + mainfolder);
//                        if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                        {
//                            mFTPClient.disconnect();
//                            return "Upload Ely - Folder Not Found 2";
//                        }
//                    }
//                    mFTPClient.changeWorkingDirectory(File.separator + (ftp.folder != "" ? ftp.folder + File.separator : "") + mainfolder + File.separator + folders[0]);
//                    try
//                    {
//                        in = new FileInputStream(new File(FilesLocationInInternalMemory, file.toString()));
//                    }
//                    catch (FileNotFoundException e)
//                    {
//                        e.printStackTrace();
//                    }
//                    if (PROGRAM_UPLOAD_FILE_SIZE_CHECK)
//                    {
//                        int nooftries = 0;
//                        for (; nooftries < 4; nooftries++)
//                        {
//                            String newfilename = GMapsActivity.Getelyfilename();
//                            boolean res = mFTPClient.storeFile(newfilename, in);
//                            if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                            {
//                                mFTPClient.disconnect();
//                                return "Upload Ely - " + file.toString() + " not saved";
//                            }
//                            in.close();
//                            if (file.length() == mFTPClient.mlistFile(newfilename).getSize())
//                            {
//                                break;
//                            }
//                            try
//                            {
//                                Thread.sleep(1000);
//                            }
//                            catch (InterruptedException e)
//                            {
//                                e.printStackTrace();
//                            }
//                        }
//                        if (nooftries >= 4)
//                        {
//                            return "Upload Ely - File size not matching";
//                        }
//                    }
//                    else
//                    {
//                        String newfilename = GMapsActivity.Getelyfilename();
//                        boolean res = mFTPClient.storeFile(newfilename, in);
//                        if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
//                        {
//                            mFTPClient.disconnect();
//                            return "Upload Ely - " + file.toString() + " not saved";
//                        }
//                        in.close();
//                    }
//                    mFTPClient.changeWorkingDirectory(File.separator + ftp.folder);
//                    new LastSyncCSV(file.toString()).savewithtime();
//                    filesuploaded[i] = true;
//                    try
//                    {
//                        Thread.sleep(1000);
//                    }
//                    catch (InterruptedException e)
//                    {
//                        e.printStackTrace();
//                        return "Upload Ely - Error 1 " + e.getMessage();
//                    }
//                }
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//                return "Upload Ely - Error 2 " + e.getMessage();
//            }
//            finally
//            {
//                try
//                {
//                    mFTPClient.setSoTimeout(TENSECONDS);
//                    mFTPClient.logout();
//                }
//                catch (IOException ex)
//                {
//                    ex.printStackTrace();
//                    return "Upload Ely - Error 3 " + ex.getMessage();
//                }
//                finally
//                {
//                    try
//                    {
//                        mFTPClient.disconnect();
//                    }
//                    catch (IOException e)
//                    {
//                        e.printStackTrace();
//                        return "Upload Ely - Error 4 " + e.getMessage();
//                    }
//                }
//            }
//            for (int i = 0; i < filesuploaded.length; i++)
//            {
//                if (filesuploaded[i])
//                {
//                    if (lines.get(i) != null)
//                    {
//                        File sent = new File(FilesLocationInInternalMemory, lines.get(i)[0]);
//                        sent.delete();
//                    }
//                }
//            }
//            List<String[]> templines = lines;
//            templines.clear(); //this is like creating a new list
//            for (int i = 0; i < filesuploaded.length; i++)
//            {
//                if (!filesuploaded[i])
//                {
//                    templines.add(lines.get(i));
//                }
//            }
//            if (templines.size() == 0)
//            {
//                File filecookie = new File(FilesLocationInInternalMemory, FILE_FIFO);
//                filecookie.delete();
//                GMapsActivity.task_UploadElyFiles_result = true;
//            }
//            else
//            {
//                GMapsActivity.WriteLinesToFileFifo(templines);
//                GMapsActivity.task_UploadElyFiles_result = false;
//            }
//            return "All Complete";
//        }
//        @Override
//        protected void onPostExecute(String string)
//        {
//            super.onPostExecute(string);
//        }
//        public static void filecopy(File src, File dst) throws IOException
//        {
//            FileInputStream inStream = new FileInputStream(src);
//            FileOutputStream outStream = new FileOutputStream(dst);
//            FileChannel inChannel = inStream.getChannel();
//            FileChannel outChannel = outStream.getChannel();
//            inChannel.transferTo(0, inChannel.size(), outChannel);
//            inStream.close();
//            outStream.close();
//        }
//        }
//
//
//
