package com.example.parcialdsm2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class Bebidas extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private Spinner opciones ;
    private ImageView fotoperfil;
    private String productoB,descripB,PrecioB;
    private EditText cantidadEdit;
    private TextView NombreProducto,ProductoPrecio,ProductoDescripcion,idGoogle;
    //Para tomar la informacion del correo
    private GoogleApiClient googleApiClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebidas);
        opciones    =   (Spinner) findViewById(R.id.spinnerC);
        NombreProducto      =   (TextView) findViewById(R.id.txtNombreC);
        ProductoPrecio      =   (TextView)findViewById(R.id.txtprecioC);
        ProductoDescripcion =   (TextView)findViewById(R.id.txtdescripcionC);
        idGoogle            =   (TextView)findViewById(R.id.idtextG1);
        fotoperfil          =   (ImageView)findViewById(R.id.photo1);
        cantidadEdit        =   (EditText)findViewById(R.id.editcantidadC);
        //:::::::::::::::::::: CONEXION CON GOOGLE ::::::::::::::::::::::::::::::::::::::::::::::
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        //::::::::::::::: Consulta de SQLITE ::::::::::::::::::::::::::::::::::::::::::::::::::::::
        SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura
        Cursor fila = basededatos.rawQuery
                ("select codigo, producto,descripcion,precio from bebidas",null);
        List<String> lista = new ArrayList<String>();
        if(fila.moveToFirst())
        {
            do {
                //Asignamos el valor en nuestras variables para usarlos en lo que necesitemos

                lista.add(fila.getString(1));
                ArrayAdapter<String> adapter =  new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,lista);
                opciones.setAdapter(adapter);

                basededatos.close();
            } while (fila.moveToNext());

        }
        else
        {
            Toast.makeText(this,"No existe nada de bebidas, ingrese bebidas",Toast.LENGTH_SHORT).show();
        }

    }
    //Funciones para consultar las bebidas en las tablas ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void consultarbd(View view)
    {
        SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura

        Cursor fila2 = basededatos.rawQuery("select codigo, producto,descripcion,precio from bebidas where producto = '"+opciones.getSelectedItem().toString()+"'",null);

            fila2.moveToFirst();///////////////
            productoB   = fila2.getString(1).toString();
            descripB    = fila2.getString(2).toString();
            PrecioB     = fila2.getString(3).toString();
            NombreProducto.setText(fila2.getString(1));
            ProductoDescripcion.setText(fila2.getString(2));
            ProductoPrecio.setText(fila2.getString(3));

            basededatos.close();


    }
    //:::::::::::::::::::: Funciones para ingresar el pedido del cliente ::::::::::::::::::::::::::::::::::
    //Metodo para registrar los productos :::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void RegistrarBebidas(View view)
    {
        SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura
        String cantidad                =   (cantidadEdit.getText().toString());
        String codigocliente        =   idGoogle.getText().toString();
        String Nombre               =   productoB.toString();
        String Descripcion          =   descripB.toString();
        String Precio               =   PrecioB.toString();
        float  SubTotal             =   Integer.parseInt(cantidad)*Float.parseFloat(Precio);
        if(!codigocliente.isEmpty() && !Nombre.isEmpty() && !Descripcion.isEmpty() && !Precio.isEmpty() ) {
            ContentValues registro = new ContentValues();
            registro.put("nombrecliente", codigocliente);
            registro.put("producto", Nombre);
            registro.put("descripcion", Descripcion);
            registro.put("preciounitario", Precio);
            registro.put("cantidad", cantidad);
            registro.put("subtotal", SubTotal);
            registro.put("estado", 1);
            basededatos.insert("pedidos", null, registro);
            basededatos.close();
            String valornulo = "XXXXXXXXXXXX";
            NombreProducto.setText(valornulo);
            ProductoPrecio.setText(valornulo);
            ProductoDescripcion.setText(valornulo);
            cantidadEdit.setText("");


            Toast.makeText(this, "¡¡ Registro Exitoso !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"¡¡Error, En el registro !!", Toast.LENGTH_SHORT).show();
        }
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
            //account.getId()
            idGoogle.setText(account.getId());
            try{
                Glide.with(this).load(account.getPhotoUrl()).into(fotoperfil);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image no encontrada", Toast.LENGTH_LONG).show();
            }
            //Glide.with(this).load(account.getPhotoUrl()).into(photoImagenView);
            //Log.d("MIAPP",account.getPhotoUrl().toString());
        }
        else {
          //  goLogInScreen();
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}