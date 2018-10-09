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
import com.genband.kandy.api.access.KandyLoginResponseListener;
import com.genband.kandy.api.provisioning.IKandyValidationResponse;
import com.genband.kandy.api.provisioning.KandyValidationResponseListener;
import com.genband.kandy.api.services.common.KandyDomain;
import com.genband.kandy.api.services.common.KandyUser;

public class UserCheck extends AppCompatActivity {

    Button btnValidate;
    EditText edtValidationCode;
    String accessToken;
    String phone = null;
    String twoLetterISOCountryCode ="TR";
    String CallerPhonePrefix = null;
    String validationCode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_check);

        KandyDomain kandyDomain = (KandyDomain) Kandy.getSession().getKandyDomain();
        kandyDomain.setAccessToken("DSK12bb47c674634e198beb908723aa2faa");
        Kandy.getGlobalSettings().setKandyHostURL("https://api-apac.kandy.io/prp");

        Kandy.getGlobalSettings().setEnableUseCredentialsInHeader(true);
        Kandy.getGlobalSettings().setEnableLoginWithoutPassword(true);

        Bundle extras = getIntent().getExtras();
        phone = extras.getString("String-to-Send");

        btnValidate = findViewById(R.id.btnValidate);
        edtValidationCode = findViewById(R.id.edtValidationCode);

        edtValidationCode.addTextChangedListener(new TextWatcher() {
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

        btnValidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             validationCode = edtValidationCode.getText().toString();
             validation();
            }
        });
    }

    private void enableSubmitIfReady() {
        boolean isReady = edtValidationCode.getText().toString().length() > 5;
        btnValidate.setEnabled(isReady);
    }

    public void validation(){
        Kandy.getProvisioning().validateAndProvision(phone, validationCode, twoLetterISOCountryCode, new KandyValidationResponseListener() {
            @Override
            public void onRequestFailed(int responseCode, String err) {
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(() -> Toast.makeText(UserCheck.this,"Login Error",Toast.LENGTH_LONG).show());

            }

            @Override
            public void onRequestSuccess(IKandyValidationResponse response) {
                accessToken = ((KandyUser)Kandy.getSession().getKandyUser()).getAccessToken();
                response.getUser();
                response.getUserId();
                response.getDomainName();
                accessUser();
            }
        });
    }
    public  void accessUser(){
        Kandy.getAccess().login(accessToken, new KandyLoginResponseListener() {
            @Override
            public void onLoginSucceeded() {
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(() -> Toast.makeText(UserCheck.this,"Logged In",Toast.LENGTH_LONG).show());
                Intent intent = new Intent(UserCheck.this,Dashboard.class);
                startActivity(intent);
            }

            @Override
            public void onRequestFailed(int i, String s) {

            }
        });
    }

}
