package com.example.mselvi.mycallapplication;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.genband.kandy.api.Kandy;
import com.genband.kandy.api.provisioning.KandyValidationMethoud;
import com.genband.kandy.api.services.common.KandyDomain;
import com.genband.kandy.api.services.common.KandyOTPRequestResponseListener;

public class Activation extends AppCompatActivity {
    Button btnPhoneActivation ;
    EditText edtPhoneNumber ;

    String PhoneNumber = null;
    String twoLetterISOCountryCode ="TR";
    String CallerPhonePrefix = null;
    String recievedNumber = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        btnPhoneActivation = findViewById(R.id.btnPhoneActivation);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        Kandy.initialize(getApplication(),null,null);

        KandyDomain kandyDomain = (KandyDomain) Kandy.getSession().getKandyDomain();
        kandyDomain.setAccessToken("DSK12bb47c674634e198beb908723aa2faa");
        Kandy.getGlobalSettings().setKandyHostURL("https://api-apac.kandy.io/prp");

        Kandy.getGlobalSettings().setEnableUseCredentialsInHeader(true);
        Kandy.getGlobalSettings().setEnableLoginWithoutPassword(true);

        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableSubmitIfReady();
            }

        });

        btnPhoneActivation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumber = edtPhoneNumber.getText().toString();
                activationSending();

            }
        });
    }
    private void enableSubmitIfReady() {
        boolean isReady = edtPhoneNumber.getText().toString().length() > 11;
        btnPhoneActivation.setEnabled(isReady);
    }

    //Send activation code to PhoneNumber
    public void activationSending(){
        Kandy.getProvisioning().requestCode(KandyValidationMethoud.SMS, PhoneNumber, twoLetterISOCountryCode, CallerPhonePrefix, new KandyOTPRequestResponseListener() {
            @Override
            public void onRequestSucceded(int i) {
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activation.this,"Activation Sent",Toast.LENGTH_LONG).show();
                    }
                });
                Intent intent = new Intent(Activation.this, UserCheck.class);
                intent.putExtra("String-to-Send",PhoneNumber);
                startActivity(intent);
            }

            @Override
            public void onRequestFailed(int i, String s) {
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Activation.this,"Error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
}
