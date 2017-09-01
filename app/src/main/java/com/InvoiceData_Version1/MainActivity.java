package com.InvoiceData_Version1;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class MainActivity extends AppCompatActivity
    {
    private TextView txtProgress;
    private ProgressBar progressBar,progressBar1;
    private int pStatus = 0;
    private Handler handler = new Handler();
    CircularProgressBar circularProgressBar1;
    int animationDuration=0;
    private CircularProgressBar circularProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtProgress = (TextView) findViewById(R.id.txtProgress);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        circularProgressBar = getCircularProgressBar();

    }

    public void progressshow(View v)
    {
      Progress(progressBar);
      Progress(progressBar1);
        mordernProgressBar();
    }
    private void Progress(final ProgressBar progressBar)
    {
        pStatus=0;
        new Thread(new Runnable()
            {
            @Override
            public void run()
            {
                while(pStatus > 0)
                {
                    handler.post(new Runnable()
                        {
                        @Override
                        public void run()
                        {
                            progressBar.setProgress(pStatus);
                          //  txtProgress.setText(pStatus + " %");
                        }
                        });
                    try
                    {
                        Thread.sleep(50);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    pStatus++;
                }
            }
            }).start();
    }
    private void mordernProgressBar()
    {
         animationDuration = 2500;// 2500ms = 2,5s
        circularProgressBar.setProgressWithAnimation(100, animationDuration);
//        circularProgressBar.setProgressWithAnimation(0, animationDuration);

    }

    @NonNull
    private CircularProgressBar getCircularProgressBar()
    {
        CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.yourCircularProgressbar);
        circularProgressBar.setColor(ContextCompat.getColor(this, R.color.progressBarColor));
        circularProgressBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
        circularProgressBar.setProgressBarWidth(getResources().getDimension(R.dimen.progressBarWidth));
        circularProgressBar.setBackgroundProgressBarWidth(getResources().getDimension(R.dimen.backgroundProgressBarWidth));
        return circularProgressBar;
    }

        public void stop(View view) {

        }
    }