package com.example.mselvi.mycallapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.genband.kandy.api.Kandy;
import com.genband.kandy.api.provisioning.KandyProvsionResponseListener;
import com.genband.kandy.api.provisioning.KandyValidationMethoud;
import com.genband.kandy.api.services.calls.IKandyCall;
import com.genband.kandy.api.services.calls.IKandyIncomingCall;
import com.genband.kandy.api.services.calls.IKandyOutgoingCall;
import com.genband.kandy.api.services.calls.KandyCallResponseListener;
import com.genband.kandy.api.services.calls.KandyCallServiceNotificationListener;
import com.genband.kandy.api.services.calls.KandyCallState;
import com.genband.kandy.api.services.calls.KandyOutgingVoipCallOptions;
import com.genband.kandy.api.services.calls.KandyRecord;
import com.genband.kandy.api.services.calls.KandyView;
import com.genband.kandy.api.services.common.IKandyUser;
import com.genband.kandy.api.services.common.KandyDomain;
import com.genband.kandy.api.services.common.KandyMediaState;
import com.genband.kandy.api.services.common.KandyMissedCallMessage;
import com.genband.kandy.api.services.common.KandyProvisionDataResponseListener;
import com.genband.kandy.api.services.common.KandyUser;
import com.genband.kandy.api.services.common.KandyUserProvisionData;
import com.genband.kandy.api.services.common.KandyWaitingVoiceMailMessage;
import com.genband.kandy.api.utils.KandyIllegalArgumentException;

public class Dashboard extends AppCompatActivity {

    Button button;
    Button btnCall;
    Button btnAccept;
    Button btnHold;
    String callleNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        KandyDomain kandyDomain = (KandyDomain) Kandy.getSession().getKandyDomain();
        kandyDomain.setAccessToken("DSK12bb47c674634e198beb908723aa2faa");
        Kandy.getGlobalSettings().setKandyHostURL("https://api-apac.kandy.io/prp");

        Kandy.getGlobalSettings().setEnableUseCredentialsInHeader(true);
        Kandy.getGlobalSettings().setEnableLoginWithoutPassword(true);

        button = findViewById(R.id.button);
        btnCall = findViewById(R.id.btnCall);
        btnAccept = findViewById(R.id.btnAccept);
        btnHold = findViewById(R.id.btnhold);
        KandyView localVideoView = findViewById(R.id.kandy_local_video_view);
        KandyView remoteVideoView = findViewById(R.id.kandy_remote_video_view);

        KandyRecord caller = null;
        KandyRecord callee = null;

        try {
            callee = new KandyRecord("optzmeeivotch@testoptusloop.part");
        } catch (KandyIllegalArgumentException e) {
            e.printStackTrace();
        }

        IKandyOutgoingCall currentCall = Kandy.getServices().getCallService().createVoipCall(caller,callee,KandyOutgingVoipCallOptions.START_CALL_WITHOUT_VIDEO);
        currentCall.setLocalVideoView(localVideoView);
        currentCall.setRemoteVideoView(remoteVideoView);

        notifications();

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        currentCall.establish(new KandyCallResponseListener() {
                            @Override
                            public void onRequestSucceeded(IKandyCall iKandyCall) {
                                Handler mHandler = new Handler(getMainLooper());
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Dashboard.this,"Call Sending",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                            @Override
                            public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {
                                Handler mHandler = new Handler(getMainLooper());
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Dashboard.this,"Error",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
            }
        });
        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doHold(currentCall);
            }
        });
    }
    public void notifications(){
      Kandy.getServices().getCallService().registerNotificationListener(new KandyCallServiceNotificationListener() {
          @Override
          public void onIncomingCall(IKandyIncomingCall iKandyIncomingCall) {

              btnAccept.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      iKandyIncomingCall.accept(true, new KandyCallResponseListener() {
                          @Override
                          public void onRequestSucceeded(IKandyCall iKandyCall) {
                          iKandyCall.getCallee();
                          }

                          @Override
                          public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

                          }
                      });
                  }
              });
          }

          @Override
          public void onMissedCall(KandyMissedCallMessage kandyMissedCallMessage) {

          }

          @Override
          public void onWaitingVoiceMailCall(KandyWaitingVoiceMailMessage kandyWaitingVoiceMailMessage) {

          }

          @Override
          public void onCallStateChanged(KandyCallState kandyCallState, IKandyCall iKandyCall) {
                switch (kandyCallState){
                    case DIALING:
                        break;
                    case INITIAL:
                        break;
                    case ON_HOLD:
                        break;
                    case RINGING:
                        break;
                    case TALKING:
                        break;
                    case ANSWERING:
                        break;
                    case TERMINATED:
                          break;
                    case REMOTELY_HELD:
                        break;
                    case ON_DOUBLE_HOLD:
                        break;
                    case SESSION_PROGRESS:
                        break;
                    case UNKNOWN:
                        break;

                }
          }

          @Override
          public void onVideoStateChanged(IKandyCall iKandyCall, boolean b, boolean b1) {

          }

          @Override
          public void onMediaStateChanged(IKandyCall iKandyCall, KandyMediaState kandyMediaState) {

          }

          @Override
          public void onGSMCallIncoming(IKandyCall iKandyCall, String s) {

          }

          @Override
          public void onGSMCallConnected(IKandyCall iKandyCall, String s) {

          }

          @Override
          public void onGSMCallDisconnected(IKandyCall iKandyCall, String s) {

          }
      });

    }
    public void doHold(IKandyCall iKandyCall){
        iKandyCall.hold(new KandyCallResponseListener() {
            @Override
            public void onRequestSucceeded(IKandyCall iKandyCall) {
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Dashboard.this,"Call Hold",Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onRequestFailed(IKandyCall iKandyCall, int i, String s) {

            }
        });

    }
}
