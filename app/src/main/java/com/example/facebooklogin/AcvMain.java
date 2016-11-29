package com.example.facebooklogin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;
import java.util.List;

public class AcvMain extends AppCompatActivity {

    private CallbackManager callbackManager;
    private List<String> permissionNeeds = Arrays.asList( "email" );
    private Button btnLogin;

    private ProfileTracker profileTracker;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        FacebookSdk.sdkInitialize( this );
        setContentView( R.layout.acv_main );
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback( callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess( LoginResult loginResult ) {
                Log.d( "Tag", "로그인이 성공해부렀네?" );
                if ( Profile.getCurrentProfile() != null ) {
                    Log.d( "Tag", "이름 : " + Profile.getCurrentProfile().getFirstName() + Profile.getCurrentProfile().getLastName() + " / " + Profile.getCurrentProfile().getId() );
                } else {
                    profileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged( Profile oldProfile, Profile currentProfile ) {
                            Log.d( "Tag", "curr profile changed 이름 : " + currentProfile.getFirstName() + currentProfile.getLastName() + " / " + currentProfile.getId() );
                            profileTracker.stopTracking();
                        }
                    };
                }
                setLoginButton();
            }

            @Override
            public void onCancel() {
                Log.d( "Tag", "로그인 하려다 맘" );
            }

            @Override
            public void onError( FacebookException error ) {
                Log.d( "Tag", "좆망 : " + error.getLocalizedMessage() );
            }
        } );
        btnLogin = ( Button ) findViewById( R.id.btnLogin );
        setLoginButton();
        if ( Profile.getCurrentProfile() != null ) {
            Log.d( "Tag", "이름 : " + Profile.getCurrentProfile().getFirstName() + Profile.getCurrentProfile().getLastName() );
        }
    }

    private void setLoginButton() {
        if ( AccessToken.getCurrentAccessToken() != null ) {
            btnLogin.setText( "로그아웃" );
            btnLogin.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    LoginManager.getInstance().logOut();
                    setLoginButton();
                }
            } );
        } else {
            btnLogin.setText( "로구인" );
            btnLogin.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    LoginManager.getInstance().logInWithReadPermissions( AcvMain.this, permissionNeeds );
                }
            } );
        }
    }


    /*

        AppEventsLogger.activateApp(this);

//

    //        btnLoginFacebook = (LoginButton) findViewById(R.id.btnLoginFacebook);
    //        btnLoginFacebook.setReadPermissions("email");
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tag", "Login result : " + loginResult.toString());
            }

            @Override
            public void onCancel() {
                Log.d("Tag", "login cancel");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        /*
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tag", "Login result btn : " + loginResult.toString());
            }

            @Override
            public void onCancel() {
                Log.d("Tag", "login cancel btn");
            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        */
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        callbackManager.onActivityResult( requestCode, resultCode, data );
    }
}
