package com.example.parcialdsm2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import java.net.Authenticator;

public class InformacionProductos extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient googleApiClient;
    private SignInButton signInButton;
    private TextView usuario,contras;

    public static  final int SIGN_IN_CODE =777;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_productos);
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton    =   (SignInButton)findViewById(R.id.singInButton);
        usuario         =   (TextView)findViewById(R.id.usertext);
        contras         =   (TextView)findViewById(R.id.passwordtext);
        signInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public  void onClick(View v)
            {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"No se puede conectar perro",Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SIGN_IN_CODE)
        {
            GoogleSignInResult result =  Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            hadleSignInResult(result);
        }
    }

    private void hadleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess())
        {
            goMainScreen();
        }
        else
        {
            Toast.makeText(this,"No se pudo conectar viejaso/sa",Toast.LENGTH_LONG).show();
        }
    }

    private void goMainScreen() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //::::::::::::::::::::::::::::::::::::::::::::::::::: Funcion Para ingresar productos ::::::::::::::::::::::::::::::



    public void Dirigiendo(View view) {
        String usuarioclave = "admin" ;
        String contrasclave = "12345";
        if(usuarioclave.equals(usuario.getText().toString()) && contrasclave.equals(contras.getText().toString()))
        {
            Intent intent = new Intent(this,IngresarProductos.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this,"Credenciales Erroneas",Toast.LENGTH_SHORT).show();
        }
    }
}