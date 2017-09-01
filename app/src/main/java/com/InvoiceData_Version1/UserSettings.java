package com.InvoiceData_Version1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static com.InvoiceData_Version1.InvoiceData.Directory;

public class UserSettings extends AppCompatActivity
    {
        public static final String Employee_data = "/sdcard/Invoice_image/Employee_data.csv";
        private static final Integer[] images = { R.drawable.male_user, R.drawable.image1,
                R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5 };
        Spinner spinner;
        static ArrayList<Employee_details> Employee_details_list;
        private AutoCompleteTextView autoCompleteusernames;
        ArrayList<String> usernames;
        private ImageView userPic;
        private TextView alert,Mobile;
        private ArrayList<String> CarArray;
        private Spinner carSpinner;
        private String carModal=null;
        private RadioGroup CarExpenceNature;
        private RadioButton Exp_nature;
        private boolean isChecked;
        private String Radio_Exp_natur;
        String userimagepath;

        @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
//        download_master download_master=new download_master();
//        download_master.task_Download.execute();
        // -----------
        CsvFileReader csvFileReader=new CsvFileReader();
        csvFileReader.readCsvFile(Employee_data);
        initializer();
        DoStuffsOnCreate();
    }

    private void initializer()
    {
        CarExpenceNature=(RadioGroup)findViewById(R.id.CarExpenceNature) ;
        Mobile=(TextView)findViewById(R.id.Mobile) ;
        alert=(TextView)findViewById(R.id.alert) ;
        CarArray=new ArrayList<>();
        usernames = new ArrayList<>();
        autoCompleteusernames = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        userPic=(ImageView)findViewById(R.id.userPic);
        carSpinner = (Spinner) findViewById(R.id.carSpinner);
    }
    private void DoStuffsOnCreate()
    {
        for(int i=0; i<Employee_details_list.size();i++)
        {
            usernames.add(Employee_details_list.get(i).Eml_nam);
        }
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, usernames);

        autoCompleteusernames.setAdapter(arrayAdapter);
        autoCompleteusernames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View arg0) {
                autoCompleteusernames.setDropDownWidth(300);
                autoCompleteusernames.showDropDown();
            }
        });

        autoCompleteusernames.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(UserSettings.this,
                        arrayAdapter.getItem(position).toString(),
                        Toast.LENGTH_SHORT).show();
                for (int i = 0; i<Employee_details_list.size(); i++)
                    if ((Employee_details_list.get(i).Eml_nam).equals( arrayAdapter.getItem(position).toString()))
                    {
                        userPic.setImageBitmap(BitmapFactory.decodeFile(userimagepath));
                        Mobile.setText(Employee_details_list.get(i).Mobile);

                        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                carModal= CarArray.get(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                carModal= null;
                            }
                        });
                    }
                alert.setText("Valid User..!");
                alert.setTextColor(Color.GREEN);
            }
        });

        autoCompleteusernames.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                for (int i = 0; i<Employee_details_list.size(); i++) {
                    if (s.toString().equals(Employee_details_list.get(i).Eml_nam))
                    {
                        userimagepath=Directory+"picfolder"+ File.separator+Employee_details_list.get(i).Eml_img;
                        userPic.setImageBitmap(BitmapFactory.decodeFile(userimagepath));
                        alert.setText("Valid User..!");
                        alert.setTextColor(Color.GREEN);
                    }
                    else
                    {
                        alert.setText("Not a Valid User..!");
                        alert.setTextColor(Color.RED);
                        userPic.setImageResource(R.drawable.male_user);
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i<Employee_details_list.size(); i++) {
                    if (s.toString().equals(Employee_details_list.get(i).Eml_nam))
                    {
                        userimagepath=Directory+"picfolder"+ File.separator+Employee_details_list.get(i).Eml_img;
                        userPic.setImageBitmap(BitmapFactory.decodeFile(userimagepath));
                        alert.setText("Valid User..!");
                        alert.setTextColor(Color.GREEN);
                        Mobile.setText("");
                        carSpinner.setAdapter(null);
                    }
                    else
                    {
                        Mobile.setText("");
                        alert.setText("Not a Valid User..!");
                        alert.setTextColor(Color.RED);
                        userPic.setImageResource(R.drawable.male_user);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                textchangedListener(s);
            }
        })
        ;
    }

        private void textchangedListener(Editable s) {
            for (int i = 0; i<Employee_details_list.size(); i++) {
                if (s.toString().equals(Employee_details_list.get(i).Eml_nam))
                {
                    userimagepath=Directory+"picfolder"+ File.separator+Employee_details_list.get(i).Eml_img;
                    Bitmap bitmap = BitmapFactory.decodeFile(userimagepath);
                    userPic.setImageBitmap(bitmap);
                    alert.setText("Valid User..!");
                    alert.setTextColor(Color.GREEN);
                    Mobile.setText(Employee_details_list.get(i).Mobile);
                    carSpinner.setAdapter(null);
                    CarArray.add(Employee_details_list.get(i).Eml_Car1);
                    CarArray.add(Employee_details_list.get(i).Eml_Car2);
                    CarArray.add(Employee_details_list.get(i).Eml_Car3);
                    carSpinner.setAdapter(new ArrayAdapter(getApplicationContext(), R.layout.listitems_layout,R.id.title,CarArray));

                }
                else
                {

                    userPic.setImageResource(R.drawable.male_user);
                    alert.setText("Not a Valid User..!");
                    alert.setTextColor(Color.RED);

                }
            }
        }

        public static class CsvFileReader {
            private static final String COMMA_DELIMITER = ",";
            public  void readCsvFile(String fileName) {
                BufferedReader fileReader = null;
                try {
                    Employee_details_list = new ArrayList<Employee_details>();
                    String line = "";
                    fileReader = new BufferedReader(new FileReader(fileName));
                    fileReader.readLine();
                    while ((line = fileReader.readLine()) != null) {
                        String[] tokens = line.split(COMMA_DELIMITER);

                        if(tokens.length==6)
                        {
                            Employee_details employee_details = new Employee_details(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
                            Employee_details_list.add(employee_details);
                        }
                        else
                        {
                            Employee_details employee_details = new Employee_details(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
                            Employee_details_list.add(employee_details);
                        }
//                            if (tokens.length > 0 && tokens.length < 7) {
//                                Employee_details employee_details = new Employee_details(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);
//                                Employee_details_list.add(employee_details);
//
//                            }
//                            else if (tokens.length > 0 && tokens.length < 5) {
//                                Employee_details employee_details = new Employee_details(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]);
//                                Employee_details_list.add(employee_details);
//
//                            }



                    }
                }
                catch (Exception e) {
                    System.out.println("Error in CsvFileReader !!!");
                    e.printStackTrace();}
                finally
                {
                    try {
                        fileReader.close();
                    } catch (IOException e) {
                        System.out.println("Error while closing fileReader !!!");
                        e.printStackTrace();
                    }
                }

            }

        }
        public static class Employee_details
        {
            public String Eml_nam;
            public String Eml_img;
            public String Eml_Car;
            public String Exp_natur;
            private String Eml_Car1;
            private String Eml_Car2;
            private String Eml_Car3;
            public String Mobile;

            public Employee_details(String eml_nam, String eml_img, String eml_Car, String exp_natur, String mobile) {
                Eml_nam = eml_nam;
                Eml_img = eml_img;
                Eml_Car = eml_Car;
                Exp_natur = exp_natur;
                Mobile = mobile;
            }

            public Employee_details(String eml_nam, String eml_img, String eml_Car1, String eml_Car2, String eml_Car3, String mobile) {
                Eml_nam = eml_nam;
                Eml_img = eml_img;
                Eml_Car1 = eml_Car1;
                Eml_Car2 = eml_Car2;
                Eml_Car3 = eml_Car3;
                Mobile = mobile;
            }
        }


        public void SaveDetails(View view)
    {

        if(TextUtils.isEmpty(autoCompleteusernames.getText().toString())) {
            autoCompleteusernames.setError("Required Field");
            return;
        }
        if(TextUtils.isEmpty(Mobile.getText().toString())) {
            autoCompleteusernames.setError("Required Field");
            return;
        }

        CarExpenceNature.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {

                switch(checkedId)
                {
                    case R.id.Petrol_Desiel:
                        Exp_nature=(RadioButton)group.findViewById(R.id.Petrol_Desiel);
                        isChecked = Exp_nature.isChecked();
                        if (isChecked)
                        {
                            Radio_Exp_natur="Petrol/Desiel";
                        }
                        break;
                    case R.id.Repaires:
                        Exp_nature=(RadioButton)group.findViewById(R.id.Repaires);
                        isChecked = Exp_nature.isChecked();
                        if (isChecked)
                        {
                            Radio_Exp_natur="Repaires";
                        }
                        break;
                }
            }
        });
        String Registered_Employee = Directory+Mobile.getText().toString()+".csv";
        new CsvFileWriter().writeCsvFile(Registered_Employee,new CsvData(autoCompleteusernames.getText().toString(),userimagepath,carModal,Radio_Exp_natur,Mobile.getText().toString()));
        sendcallBackIntent();
    }

        private void sendcallBackIntent() {
            Intent sendSelected = new Intent(UserSettings.this, InvoiceData.class);
            sendSelected.putExtra("userName",autoCompleteusernames.getText().toString());
            sendSelected.putExtra("UserImage",userimagepath);
            sendSelected.putExtra("usercar",carModal);
            sendSelected.putExtra("userMobile",Mobile.getText().toString());
            sendSelected.putExtra("exp_nature",Radio_Exp_natur);
            setResult(-1, sendSelected);
            finish();
        }

        private class CsvData
        {
            private String UserName, UserImage,carModal,Registered_mobile,Exp_natur;

            public CsvData(String userName, String userImage, String carModal, String registered_mobile, String exp_natur) {
                UserName = userName;
                UserImage = userImage;
                this.carModal = carModal;
                Registered_mobile = registered_mobile;
                Exp_natur = exp_natur;
            }

            public String getUserName() {
                return UserName;
            }

            public String getUserImage() {
                return UserImage;
            }

            public String getCarModal() {
                return carModal;
            }

            public String getRegistered_mobile() {
                return Registered_mobile;
            }

            public String getExp_natur() {
                return Exp_natur;
            }
        }
        public static class CsvFileWriter {
            private static final String COMMA_DELIMITER = ",";
            private static final String NEW_LINE_SEPARATOR = "\n";
            private static final String FILE_HEADER = "UserName,UserImage(.jpg),carModal,Registered_mobile,Exp_nature";

            public void writeCsvFile(String fileName,CsvData csvData) {
                FileWriter fileWriter = null;
                try {

                    fileWriter = new FileWriter(fileName);
                    fileWriter.append(FILE_HEADER.toString());
                    fileWriter.append(NEW_LINE_SEPARATOR);
                    fileWriter.append(String.valueOf(csvData.getUserName()));
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(csvData.getUserImage());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(csvData.getCarModal());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(csvData.getRegistered_mobile());
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(csvData.getExp_natur());
                    fileWriter.append(NEW_LINE_SEPARATOR);

                } catch (Exception e) {

                    e.printStackTrace();
                } finally {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }
    }
