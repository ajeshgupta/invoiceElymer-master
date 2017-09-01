package com.InvoiceData_Version1;

import android.os.AsyncTask;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static com.InvoiceData_Version1.InvoiceData.Directory;

public class task_UploadFiles
{
private static final int SIXYSECONDS = 60*1000;
private static final int THIRTYSECONDS = 30*1000;
private static final int TWEENTYSECONDS = 20*1000;
private static final String Server = "122.160.80.197";
private static final String User = "cmri";
private static final String Password = "pass@123";


AsyncTask<String, Integer, String> task_UploadElyFiles = new AsyncTask<String, Integer, String>()
    {

  @Override
  protected String doInBackground(String... files)
  {
//      final ArrayList<String> lines = new InvoiceData().ReadFilenamesFromFileFifo();
//      if (lines.size() == 0)
//      {
//          return "All Complete";
//      }
//      boolean[] filesuploaded = new boolean[lines.size()];
    FTPClient mFTPClient = new FTPClient();
    try
    {
        mFTPClient.setBufferSize(0);
        mFTPClient.setConnectTimeout(SIXYSECONDS);
        mFTPClient.setDefaultTimeout(SIXYSECONDS);
        mFTPClient.connect(Server);
        mFTPClient.setSoTimeout(SIXYSECONDS);
        if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
      {
        mFTPClient.disconnect();
          Global_class.l.d("UploadFiles - Server Not Found");
      }
      boolean success = mFTPClient.login(User, Password);
      if (!success)
      {
        mFTPClient.disconnect();
          Global_class.l.d("UploadFiles - Server Not Log In");
      }
      mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
      mFTPClient.enterLocalPassiveMode();
      success = mFTPClient.changeWorkingDirectory(File.separator + "Accounts");

      FileInputStream in = null;

        File fileupload = new File(Directory, InvoiceData.SaveCSV + ".csv");
        mFTPClient.setSoTimeout(THIRTYSECONDS);
        try
        {
          in = new FileInputStream(fileupload.toString());
          boolean result = mFTPClient.storeFile(InvoiceData.SaveCSV + "" +
                                                        ".csv", in);
          in.close();
          if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
          {
            mFTPClient.disconnect();
              Global_class.l.d( "UploadFiles - " +InvoiceData.SaveCSV + ".csv" + " not saved");
          }
          fileupload.delete();
        }
        catch (FileNotFoundException e)
        {
          e.printStackTrace();
            Global_class.l.d( "UploadFiles - Error 1 - " + e.getMessage());
        }

        //for image------


         fileupload = new File(Directory, InvoiceData.ClickedPic + ".jpg");
        mFTPClient.setSoTimeout(THIRTYSECONDS);
        try
        {
            in = new FileInputStream(fileupload.toString());
            boolean result = mFTPClient.storeFile(InvoiceData.SaveCSV + "" +
                                                          ".jpg", in);
            in.close();
            if (!FTPReply.isPositiveCompletion(mFTPClient.getReplyCode()))
            {
                mFTPClient.disconnect();
                Global_class.l.d( "UploadFiles - " +InvoiceData.SaveCSV + ".jpg" + " not saved");
            }
            fileupload.delete();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Global_class.l.d( "UploadFiles - Error 1 - " + e.getMessage());
        }
        //----

    }
    catch (IOException e)
    {
      e.printStackTrace();
        Global_class.l.d( "UploadFiles - Error 2 - " + e.getMessage());
    }
    finally
    {
      try
      {
        mFTPClient.setSoTimeout(TWEENTYSECONDS);
        mFTPClient.logout();
          mFTPClient.disconnect();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
          Global_class.l.d(  "UploadFiles - Error 3 - " + ex.getMessage());
      }
    }
      return "file-uploaded";
  }
};
//--------------------


//    public class task_UploadElyFiles extends AsyncTask<Void, Integer, String>
//    {
//        @Override
//        protected String doInBackground(Void... params)
//        {
//            final List<String[]> lines = GMapsActivity.ReadFilenamesFromFileFifo();
//            if (lines.size() == 0)
//            {
//                return "All Complete";
//            }
//            boolean[] filesuploaded = new boolean[lines.size()];
//            FTPClient mFTPClient = new FTPClient();
//            final MainActivity.Ftp ftp = new MainActivity.Ftp(MainActivity.PROGRAM_SERVER_ELY);
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
//    }

}
