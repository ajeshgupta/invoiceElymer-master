package com.InvoiceData_Version1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.InvoiceData_Version1.UserSettings.Employee_data;
import static com.InvoiceData_Version1.UserSettings.Employee_details_list;

public class InvoiceData extends AppCompatActivity {
    private static final int RequestCode = 2;
    private static final int CAM_REQUEST = 3;
    public static final String Directory = "/sdcard/Invoice_image/";
    private static final int SIZE_OF_DATE = 10;
    private static String ImagePath;
    public static String ClickedPic;
    private static String CSVPath;
    public static String SaveCSV;
    private TextView date, item, InvoiceNo, UserName, alert_info;
    private EditText GrossAmount;
    public int day_x, month_x, year_x;
    private static final int Dialog_id = 0;
    private ImageView InvoiceImage, userimage;
    private File Csvfile;
    private String Mobile_no = null, Expense_nature = null;
    private Button getRegistered;
    private View view;
    private List<String> temp_array_filePaths;
    private    ConstraintLayout listConstrained;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_data);
        Initializer();
    }

    private void Initializer() {

        alert_info = (TextView) findViewById(R.id.alert_info);
        getRegistered = (Button) findViewById(R.id.getRegistered);
        getRegistered.setVisibility(View.INVISIBLE);
        UserName = (TextView) findViewById(R.id.UserName);
        UserName.setText("");
        InvoiceNo = (TextView) findViewById(R.id.InvoiceNo);
        date = (TextView) findViewById(R.id.date);
        item = (TextView) findViewById(R.id.item);
        item.setText("");
        GrossAmount = (EditText) findViewById(R.id.GrossAmount);
        userimage = (ImageView) findViewById(R.id.userimage);
        InvoiceImage = (ImageView) findViewById(R.id.InvoiceImage);
        InputFilter[] fa = new InputFilter[1];
        fa[0] = new InputFilter.LengthFilter(5);
        InvoiceNo.setFilters(fa);
        fa[0] = new InputFilter.LengthFilter(SIZE_OF_DATE);
        date.setFilters(fa);
        date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDialog(Dialog_id);
                }
            }
        });
        listConstrained = (ConstraintLayout) findViewById(R.id.listConstrained);
        listConstrained.setVisibility(View.INVISIBLE);
     //   CheckInternet();
        CheckInitialData2();
        //        if(UserName.getText().toString().equals("")&&item.getText().toString().equals("")&& Mobile_no==null && Expense_nature==null)
        //        {
        //            alert_info.setText("You are NOT REGISTERED USER..!");
        //            getRegistered.setVisibility(View.VISIBLE);
        //        }
    }

    private void CheckInitialData()
    {
        File file_Employee_data = new File(Employee_data);
        if (file_Employee_data.exists())
        {
            String mobile = null;
            UserSettings.CsvFileReader csvFileReader = new UserSettings.CsvFileReader();
            csvFileReader.readCsvFile(Employee_data);
            for (int i = 0; i < Employee_details_list.size(); i++)
            {
                mobile = Employee_details_list.get(i).Mobile;
                File file = new File(Directory + mobile + ".csv");
                if (file.exists())
                {
                  //  filehandle(String.valueOf(file.getAbsolutePath()));

                    UserSettings.CsvFileReader csvReader = new UserSettings.CsvFileReader();
                    csvReader.readCsvFile(file.getAbsolutePath());
                    for (int j = 0; j < Employee_details_list.size(); j++) {
                        UserName.setText(Employee_details_list.get(j).Eml_nam);
                        item.setText(Employee_details_list.get(j).Eml_Car);
                        Mobile_no = Employee_details_list.get(j).Mobile;
                        Expense_nature = Employee_details_list.get(j).Exp_natur;
                        userimage.setImageBitmap(BitmapFactory.decodeFile(Employee_details_list.get(j).Eml_img));
                        //// TODO: 31-08-2017 what if, if there are multiple files with different mobiles number,
                        //// TODO: 31-08-2017 create a dialog box show list of files or by default open last record,
                        //// TODO: 31-08-2017 and ask the user if he wishes to open any other recored. or like that..!
                    }
                    alert_info.setText("");
                    getRegistered.setVisibility(View.INVISIBLE);
                    return;
                }
                else
                    {
                       alert_info.setText(" No previous record found\nBecome a REGISTERED USER..!");
                       getRegistered.setVisibility(View.VISIBLE);
                    }
            }
            return;
        }
        else {
            alert_info.setText(" No previous record found\nBecome a REGISTERED USER..!");
            getRegistered.setVisibility(View.VISIBLE);
        }

    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {
        if (id == Dialog_id) {
            final Calendar c = Calendar.getInstance();
            DecimalFormat df = new DecimalFormat("00");
            year_x = Integer.parseInt(df.format(c.get(Calendar.YEAR)));
            month_x = Integer.parseInt(df.format(c.get(Calendar.MONTH)));
            day_x = Integer.parseInt(df.format(c.get(Calendar.DAY_OF_MONTH)));
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month;
            day_x = dayOfMonth;
            date.setText(day_x + "-" + month_x + "-" + year_x);
        }
    };

    public void clickImage(View view) {
        Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new SavePicture().SaveImageFile(item.getText().toString(), ".jpg")));
        startActivityForResult(camera_intent, CAM_REQUEST);
    }

    public void SaveIntoCsvFile(View view) {
        CsvData csvData = new CsvData(UserName.getText().toString(), "Eymer_empl.jpg", item.getText().toString(), InvoiceNo.getText().toString(), date.getText().toString(), GrossAmount.getText().toString(), ClickedPic);
        CsvFileWriter csvFileWriter = new CsvFileWriter();
        Csvfile = new File(String.valueOf(new SavePicture().SaveCsvFile(item.getText().toString(), "" + ".csv")));
        csvFileWriter.writeCsvFile(String.valueOf(Csvfile.getAbsolutePath()), csvData);
        filehandle(Directory+"notSynced.txt",String.valueOf(Csvfile.getAbsolutePath()));
    }
    private void filehandle(String filepath,String data)
    {
        try {
            FileUtils.writeStringToFile(new File(filepath), data+"\n", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
////        byte[] bytes=(filepath+",").getBytes();
//          BufferedWriter writer;
//        try
//        {
////            File file_CsvFile_Path_Not_Synced = new File(Directory+"CsvFile(s)_Not_Synced.csv");
////            FileOutputStream out=new FileOutputStream(new File(Directory+Filename), MODE_APPEND);
//            writer=new BufferedWriter( new FileWriter((Directory+Filename), true));
//            writer.append(filepath+",");
//            writer.close();
//        }
//        catch(Exception e)
//        {
//            Log.e("File not created",e.toString());
//        }
    }
   public List<String> ReadFilenamesFromFileFifo(String Filename)  {
        List<String> filenames= new ArrayList<>();
       String line=null;
       try {
         line = (FileUtils.readFileToString(new File(Directory+Filename)));
                } catch (IOException e) {
           e.printStackTrace();
       }
       //        BufferedReader reader;
//        String line;
//        try {
//            reader = new BufferedReader(new FileReader(new File(Directory+Filename).getAbsolutePath()));
//            line = reader.readLine();
//               for (int i=0; i<line.length();i++)
//                {
//                    filenames.add(line.split(",")[i]);
//                }
//                reader.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
      // filenames.add(line.split(",")[0]);


           for (String retval:line.split(",")) {
               filenames.add(retval);
           }
        return filenames ;
    }

    public void SyncToFtp(View view) {
        task_UploadFiles task_uploadFiles = new task_UploadFiles();
        task_uploadFiles.task_UploadElyFiles.execute();
    }

    public void getRegistered(View view) {
        Intent intent = new Intent(InvoiceData.this, UserSettings.class);
        startActivityForResult(intent, RequestCode);
    }

    private static class SavePicture {

        private File SaveImageFile(String Item, String exe) {
            File folder = new File(Directory);
            if (!folder.exists()) {
                folder.mkdir();
            }
            ClickedPic = makeClickedPic(Item);
            File picfile = new File(Directory + ClickedPic + exe);
            ImagePath = picfile.getAbsolutePath();
            if (picfile.exists()) {
                picfile.delete();
            }
            return picfile;
        }

        private File SaveCsvFile(String Item, String exe) {
            File folder = new File(Directory);
            if (!folder.exists()) {
                folder.mkdir();
            }
            SaveCSV = makeClickedPic(Item);

            File picfile = new File(Directory + SaveCSV + exe);
            CSVPath = picfile.getAbsolutePath();
            if (picfile.exists()) {
                picfile.delete();
            }
            return picfile;
        }

        public static String makeClickedPic(String item) {// eg.car as a item
            Calendar calendar = Calendar.getInstance();
            DecimalFormat df = new DecimalFormat("00");
            String currentDateTimeString = item + "_" + String.valueOf(df.format(calendar.get(calendar.MONTH)));
            currentDateTimeString += String.valueOf(df.format(calendar.get(calendar.DAY_OF_MONTH)));
            currentDateTimeString += String.valueOf(df.format(calendar.get(calendar.HOUR_OF_DAY)));
            currentDateTimeString += String.valueOf(df.format(calendar.get(calendar.MINUTE)));
            currentDateTimeString += String.valueOf(df.format(calendar.get(calendar.SECOND)));
            return currentDateTimeString;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCode) {
            if (resultCode == RESULT_OK) {
                alert_info.setText("You are now REGISTERED USER..!");
                getRegistered.setVisibility(View.INVISIBLE);
                alert_info.setTextColor(Color.BLUE);
                UserName.setText(data.getStringExtra("userName"));
                item.setText(data.getStringExtra("usercar"));
                Mobile_no = data.getStringExtra("userMobile");
                Expense_nature = data.getStringExtra("exp_nature");
                userimage.setImageBitmap(BitmapFactory.decodeFile(data.getStringExtra("UserImage")));
            } else {
                if (UserName.getText().toString().equals("") && item.getText().toString().equals("") && Mobile_no == null && Expense_nature == null) {
                    alert_info.setText("You are NOT REGISTERED USER..!");
                    getRegistered.setVisibility(View.VISIBLE);
                }
            }
        } else if (requestCode == CAM_REQUEST) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Image is clicked..!", Toast.LENGTH_LONG).show();
                Bitmap bitmap = BitmapFactory.decodeFile(ImagePath);
                InvoiceImage.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Image is not clicked..!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class CsvFilePath {
        private String filePath;

        public CsvFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    public class CsvFilePathWriter {
        private static final String COMMA_DELIMITER = ",";
        private static final String NEW_LINE_SEPARATOR = "\n";
        private static final String FILE_HEADER = "filePath";

        public void writeCsvFilePath(String fileName, CsvFilePath csvfilePath) {
            FileWriter fileWriter = null;
            try {

                fileWriter = new FileWriter(fileName);
                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(String.valueOf(csvfilePath.getFilePath()));
                fileWriter.append(NEW_LINE_SEPARATOR);
                Toast.makeText(getApplication(), "CSV file was created successfully !!!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Error in CsvFileWriter ! " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    Toast.makeText(getApplication(), "Error while flushing/closing fileWriter !!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    private class CsvData {
        private String UserName, UserImage, Items, Invoice_Number, Invoice_Date, Invoice_Amt, InvoiceImage;

        public CsvData(String userName, String userImage, String items, String invoice_Number, String invoice_Date, String invoice_Amt, String invoiceImage) {
            UserName = userName;
            UserImage = userImage;
            Items = items;
            Invoice_Number = invoice_Number;
            Invoice_Date = invoice_Date;
            Invoice_Amt = invoice_Amt;
            InvoiceImage = invoiceImage;
        }

        public String getUserName() {
            return UserName;
        }

        public String getUserImage() {
            return UserImage;
        }

        public String getItems() {
            return Items;
        }

        public String getInvoice_Number() {
            return Invoice_Number;
        }

        public String getInvoice_Date() {
            return Invoice_Date;
        }

        public String getInvoice_Amt() {
            return Invoice_Amt;
        }

        public String getInvoiceImage() {
            return InvoiceImage;
        }
    }

    public class CsvFileWriter {
        private static final String COMMA_DELIMITER = ",";
        private static final String NEW_LINE_SEPARATOR = "\n";
        private static final String FILE_HEADER = "UserName,UserImage(.jpg),Items,Invoice_Number," + "Invoice_Date,Invoice_Amt,InvoiceImage(.jpg)";

        public void writeCsvFile(String fileName, CsvData csvData) {
            FileWriter fileWriter = null;
            try {

                fileWriter = new FileWriter(fileName);
                fileWriter.append(FILE_HEADER.toString());
                fileWriter.append(NEW_LINE_SEPARATOR);
                fileWriter.append(String.valueOf(csvData.getUserName()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(csvData.getUserImage());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(csvData.getItems());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(csvData.getInvoice_Number());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(csvData.getInvoice_Date()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(csvData.getInvoice_Amt()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(csvData.getInvoiceImage()));
                fileWriter.append(NEW_LINE_SEPARATOR);
                Toast.makeText(getApplication(), "CSV file was created successfully !!!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplication(), "Error in CsvFileWriter ! " + e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    Toast.makeText(getApplication(), "Error while flushing/closing fileWriter !!!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(InvoiceData.this, UserSettings.class);
                startActivityForResult(intent, RequestCode);

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean CheckInternet() {
        if (!internetConnectionAvailable(3000)) {
            SetSnackbar(view, "Oops !!!! Looks like your internet connection is not available", Color.RED);
            return false;
        }
        return true;
    }

    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
    }

    static void SetSnackbar(View view, String text, int color) {
        Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
        View t_view = snackbar.getView();
        t_view.setBackgroundColor(Color.WHITE);
        TextView snackViewText = (TextView) t_view.findViewById(android.support.design.R.id.snackbar_text);
        snackViewText.setTextSize(22);
        snackViewText.setTextColor(color);
        snackbar.show();
    }

    //=========

    private void CheckInitialData2() {
        File file_Employee_data = new File(Employee_data);
        temp_array_filePaths = new ArrayList<>();
        if (file_Employee_data.exists()) {
            String mobile = null;
            UserSettings.CsvFileReader csvFileReader = new UserSettings.CsvFileReader();
            csvFileReader.readCsvFile(Employee_data);
            for (int i = 0; i < Employee_details_list.size(); i++) {
                mobile = Employee_details_list.get(i).Mobile;
                File tempfile = new File(Directory + mobile + ".csv");
                if (tempfile.exists()) {
                    filehandle(Directory+"initial_tempfile.txt",String.valueOf(tempfile.getAbsolutePath()));
                }
            }
            if (!new File(Directory+"initial_tempfile.txt").exists()) {
                alert_info.setText(" No previous record found, Become a REGISTERED USER..!");
                getRegistered.setVisibility(View.VISIBLE);
                return;
            }
            else {
                  temp_array_filePaths = ReadFilenamesFromFileFifo("initial_tempfile.txt");

                if (temp_array_filePaths.size() < 1) {
                    alert_info.setText(" No previous record found, Become a REGISTERED USER..!");
                    getRegistered.setVisibility(View.VISIBLE);
                    return;
                } else if (temp_array_filePaths.size() == 1) {
                    setFields(temp_array_filePaths, 0);
                    return;
                } else if (temp_array_filePaths.size() > 1) {
                    listConstrained.setVisibility(View.VISIBLE);

                    final ListView initials_userData_List = (ListView) findViewById(R.id.initials_userData_List);
                    initials_userData_List.setAdapter(new ArrayAdapter<String>(InvoiceData.this, android.R.layout.simple_list_item_1, temp_array_filePaths));
                    initials_userData_List.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            setFields(temp_array_filePaths, position);
                            listConstrained.setVisibility(View.INVISIBLE);
                            return;
                        }
                    });
                }
            }
            alert_info.setText("");
            getRegistered.setVisibility(View.INVISIBLE);
            return;
        } else {
            alert_info.setText(" No previous record found\nBecome a REGISTERED USER..!");
            getRegistered.setVisibility(View.VISIBLE);
        }
    }




    private void setFields(List<String> arrayList , int position)
    {
        UserSettings.CsvFileReader csvReader = new UserSettings.CsvFileReader();
        csvReader.readCsvFile(arrayList.get((position)));
        UserName.setText(Employee_details_list.get((position)).Eml_nam);
        item.setText(Employee_details_list.get((position)).Eml_Car);
        Mobile_no = Employee_details_list.get((position)).Mobile;
        Expense_nature = Employee_details_list.get((position)).Exp_natur;
        userimage.setImageBitmap(BitmapFactory.decodeFile(Employee_details_list.get((position)).Eml_img));
    }
}
