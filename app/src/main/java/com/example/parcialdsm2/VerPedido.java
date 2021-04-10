package com.example.parcialdsm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

import java.util.ArrayList;
import java.util.List;

public class VerPedido extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private ImageView fotoperfil;
    private String productoB,descripB,PrecioB,ListaProducto="",ListaCantidad="",ListaPrecio="",ListaSubTotal="";
    private  float TotalNumero=0;
    private EditText cantidadEdit;
    private TextView Total,NombreProducto,ProductoPrecio,Subotal,idGoogle,cantidad;
    //Para tomar la informacion del correo
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pedido);
        NombreProducto      =   (TextView) findViewById(R.id.textView10);
        //ProductoPrecio      =   (TextView)findViewById(R.id.textView6);
        idGoogle            =   (TextView)findViewById(R.id.idGoogle2);
        fotoperfil          =   (ImageView)findViewById(R.id.photo2);
        //Subotal             =   (TextView) findViewById(R.id.textView14);
        //cantidad            =   (TextView)findViewById(R.id.textView12);
        Total               =   (TextView)findViewById(R.id.textView7);
        //:::::::::::::::::::: CONEXION CON GOOGLE ::::::::::::::::::::::::::::::::::::::::::::::
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

    }
    //Funciones para consultar las bebidas en las tablas ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void consultarpedidos(String idGoogle)
    {
        SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura

        Cursor fila2 = basededatos.rawQuery("select producto, cantidad,preciounitario,subtotal from pedidos where nombrecliente =  '"+idGoogle+"'",null);
        ListaProducto +="Su Orden es: "+'\n';
        ListaCantidad +="Cantidad"+'\n';
        ListaPrecio +="Precio Unitario"+'\n';
        ListaSubTotal +="Sub Total"+'\n';
        if(fila2.moveToFirst())
        {
            do {
                //Asignamos el valor en nuestras variables para usarlos en lo que necesitemos
                ListaProducto +=fila2.getString(0)+"\n Cantidad: "+fila2.getString(1)+"\n Precio: $"+fila2.getString(2)+"\n Sub total: $"+fila2.getString(3)+"\n ::::::::::::::::"+'\n';
                NombreProducto.setText(ListaProducto);
                TotalNumero = TotalNumero+ Float.parseFloat(fila2.getString(3));

            } while (fila2.moveToNext());

        }
        else
        {
            Toast.makeText(this,"¡¡¡ No ha ordenado nada aun !!!!",Toast.LENGTH_SHORT).show();
        }
        String valorTotal= Float.toString(TotalNumero);
        Total.setText(valorTotal);
        basededatos.close();


    }
    //::::::::::::::::::::: fUNCION PARA TOMAR LA INFORMACION DE LA CUENTA ::::::::::::::::::::::::::::::::
    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
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
            //nameText.setText(account.getDisplayName());
            //emailTextView.setText(account.getEmail());
            idGoogle.setText(account.getDisplayName());
            String idUsuario    =  account.getId().toString();
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(fotoperfil);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image no encontrada", Toast.LENGTH_LONG).show();
            }
            //Glide.with(this).load(account.getPhotoUrl()).into(photoImagenView);
            //Log.d("MIAPP",account.getPhotoUrl().toString());
            consultarpedidos( idUsuario);
        }
        else {
            //  goLogInScreen();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}