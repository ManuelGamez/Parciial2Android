package com.example.parcialdsm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ImageView photoImagenView;
    private TextView nameText, idtext;
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoImagenView =   (ImageView)findViewById(R.id.photoImageView3);
        nameText        =   (TextView)findViewById(R.id.nametextView2);
        idtext          =   (TextView)findViewById(R.id.textView5);

        // Estamos creando un login silencioso
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
    }
    public void bebidas(View view)
    {
        Intent i = new Intent(this,Bebidas.class);
        startActivity(i);
    }
    public void platillos(View view)
    {
        Intent i = new Intent(this,Comidas.class);
        startActivity(i);
    }
    public void resumenorden(View view){
        Intent intent = new Intent(this,VerPedido.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult>opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone())
        {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }
        else
        {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess())
        {
            GoogleSignInAccount account = result.getSignInAccount();
            nameText.setText(account.getDisplayName());
            //emailTextView.setText(account.getEmail());
            idtext.setText(account.getId());
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(photoImagenView);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image no encontrada", Toast.LENGTH_LONG).show();
            }
            //Glide.with(this).load(account.getPhotoUrl()).into(photoImagenView);
            //Log.d("MIAPP",account.getPhotoUrl().toString());
        }
        else {
            goLogInScreen();
        }
    }
    private void goLogInScreen() {
        Intent intent= new Intent(this,InformacionProductos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void logOut(View view) {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status)
            {
                if(status.isSuccess())
                {
                    goLogInScreen();
                }
                else
                    Toast.makeText(getApplicationContext(), "No se puede cerrar sesion", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}