package com.example.simplejonathan;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.liveperson.api.LivePersonCallback;
import com.liveperson.infra.ConversationViewParams;
import com.liveperson.infra.ICallback;
import com.liveperson.infra.InitLivePersonProperties;
import com.liveperson.infra.LPAuthenticationParams;
import com.liveperson.infra.callbacks.InitLivePersonCallBack;
import com.liveperson.messaging.TaskType;
import com.liveperson.messaging.model.AgentData;
import com.liveperson.messaging.sdk.api.LivePerson;
import com.liveperson.messaging.sdk.api.callbacks.LogoutLivePersonCallback;
import com.liveperson.messaging.sdk.api.callbacks.ShutDownLivePersonCallback;
import com.liveperson.messaging.sdk.api.model.ConsumerProfile;

public class MainActivity extends AppCompatActivity {

    //    String brandID = "64353961";
    String brandID;
    String appID = "com.example.simplejonathan";
    LPAuthenticationParams lpAuthenticationParams;
    ConversationViewParams conversationViewParams = new ConversationViewParams().setReadOnlyMode(false);

    Button initButton;
    Button logOutButton;
    Button startConvoButton;
    EditText accountNumberText;

    String myJwt = "";
    String gcmToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        gcmToken = task.getResult().getToken();
//
//                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                initButton = findViewById(R.id.button2);
                startConvoButton = findViewById(R.id.button3);
                logOutButton = findViewById(R.id.button4);
                accountNumberText = (EditText) findViewById(R.id.accountNumber);

                initButton.setEnabled(true);
                logOutButton.setEnabled(false);
                startConvoButton.setEnabled(false);

//                accountNumberText.getText().toString() = "64353961";
                //brandID = accountNumberText.getText().toString();

            }
        });

         myJwt = JWTGenerator.generateJwt(this);

        //final TextView mTextView = (TextView) findViewById(R.id.text);
// ...

//// Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url ="https://calm-mesa-48745.herokuapp.com/messaging/idp/token?sub=WebMsgTest1245&expires=240";
//
//// Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
////                        try {
////                            mTextView.setText("Response is: "+ response.substring(0,500));
////                            createToast("Response is: "+ response.substring(0,500));
////                        } catch (Exception e) {
////                            mTextView.setText("Response is: "+ response);
////                            createToast("Response is: "+ response);
////                        }
//                        myJwt = response;
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                mTextView.setText("That didn't work!");
//                createToast("that didn't work");
//            }
//        });
//
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);


//        startConvoButton.setEnabled(false);
//        logOutButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createToast(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    public void initSDK(View view) {

        lpAuthenticationParams = new LPAuthenticationParams(LPAuthenticationParams.LPAuthenticationType.AUTH);
        lpAuthenticationParams.setHostAppJWT(myJwt);
        lpAuthenticationParams.setAuthKey("");
        lpAuthenticationParams.addCertificatePinningKey("");



        brandID = accountNumberText.getText().toString();

        LivePerson.initialize(
                MainActivity.this,
                new InitLivePersonProperties(brandID, appID, new InitLivePersonCallBack() {
                    @Override
                    public void onInitSucceed() {
                        createToast("Init sucessful");

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                initButton.setEnabled(false);
                                logOutButton.setEnabled(true);
                                startConvoButton.setEnabled(true);

                                //brandID = accountNumberText.getText().toString();
                            }
                        });


                        String firstName = "seb";
                        String lastName = "lacki";

                        ConsumerProfile consumerProfile = new ConsumerProfile.Builder()
                                .setFirstName(firstName)
                                .setLastName(lastName)
                                .build();

                        LivePerson.setUserProfile(consumerProfile);


                    }

                    @Override
                    public void onInitFailed(Exception e) {
                        createToast("Init failed");
                    }
                }));

        registerPusher();
        //createToast("Init SDK Succsessful");
    }

    public void startConversation(View view) {

        LivePerson.showConversation(
                MainActivity.this,
                lpAuthenticationParams,
                conversationViewParams);



    }



    public void logOut(View view) {

//        LivePerson.shutDown(new ShutDownLivePersonCallback() {
//            @Override
//            public void onShutdownSucceed() {
//                createToast("Logout successful");
//
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        initButton.setEnabled(true);
//                        logOutButton.setEnabled(false);
//                        startConvoButton.setEnabled(false);
//
//                    }
//                });
//            }
//
//            @Override
//            public void onShutdownFailed() {
//                createToast("logout failed");
//            }
//        });

        LivePerson.logOut(MainActivity.this, brandID, appID, new LogoutLivePersonCallback() {
            @Override
            public void onLogoutSucceed() {
                createToast("Logout successful");

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        initButton.setEnabled(true);
                        logOutButton.setEnabled(false);
                        startConvoButton.setEnabled(false);

                    }
                });

            }

            @Override
            public void onLogoutFailed() {
                createToast("logout failed");

            }
        });
//    }

}

    public void registerPusher() {

        LivePerson.registerLPPusher(brandID, appID, gcmToken, lpAuthenticationParams, new ICallback<Void, Exception>() {
            @Override
            public void onSuccess(Void aVoid) {
                //createToast("Push notifications registered");

            }

            @Override
            public void onError(Exception e) {
                //createToast("Failed to register push notifications");
            }
        });
    }
}

