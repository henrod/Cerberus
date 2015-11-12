package com.example.henrique.cerberus;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by henrique on 09/10/15.
 */
public class Configuration extends Activity {

    public int mode = 1;    //mode = 0 => total security
                             //mode = 1 => driver
    private int id_rasp;

    private RadioGroup radioMode;
    private RadioButton radioButton;

    private String json;
    private boolean go = false;

    private String operationMode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_configuration);

        id_rasp = getIntent().getIntExtra("id_rasp", 0);

        radioMode = (RadioGroup) findViewById(R.id.radioMode);
        ((RadioButton) findViewById(R.id.radioSeg)).toggle();
    }

    public void retornar(View view) {
        ((Button) findViewById(R.id.configButton)).setText("Aguarde...");

        int selectedRadio = radioMode.getCheckedRadioButtonId();

        radioButton = (RadioButton) findViewById(selectedRadio);
        operationMode = radioButton.getText().toString();

        if (operationMode.equals("Manobrista")) operationMode= "M";
        else operationMode = "S";

        (new RetrieveData()).doInBackground();
    }

    class RetrieveData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            //establish server socker

            new Thread(){
                @Override
                public void run() {
                    super.run();

                    try {
                        URL server = new URL(MainActivity.ip_server + "set_config.php?id_java=" + id_rasp +
                                "&config=" + operationMode);
                        BufferedReader in = new BufferedReader(new InputStreamReader(server.openStream()));
                        json = in.readLine();
                        go = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            while (go) {
                                go = false;
                                Intent lock = new Intent(Configuration.this, LockCar.class);
                                lock.putExtra("id_rasp", id_rasp);

                                Toast.makeText(Configuration.this, "Configurações salvas", Toast.LENGTH_LONG).show();

                                startActivity(lock);
                            }
                        }
                    });
                }
            }.start();

            return null;
        }
    }
}
