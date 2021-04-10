package com.example.parcialdsm2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class IngresarProductos extends AppCompatActivity {
    private EditText editCodigo, editNombre, editDescripcion, editPrecio;
    private int valor;private String letras;
    private TextView prueba2;
    private Switch editSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_productos);
        editCodigo      =   (EditText)findViewById(R.id.editNombre);
        editNombre      =   (EditText)findViewById(R.id.editnombre);
        editDescripcion =   (EditText)findViewById(R.id.editdescripcion);
        editPrecio      =   (EditText)findViewById(R.id.editprecio);
        editSwitch      =   (Switch)findViewById(R.id.switch3);
        prueba2         =   (TextView)findViewById(R.id.pruebas);
    }
    //Metodo del Switch :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void onclick(View view) {
        if (view.getId()== R.id.switch3)
        {
            if(editSwitch.isChecked())
            {
                //Valor Activado tabla bebidas
                letras="Bebida";
                prueba2.setText(letras);
                valor=1;
            }
            else
            {
                //Valor Desactivado tabla platillo
                letras="Platillo";
                prueba2.setText(letras);
                valor=0;
            }
        }
    }
    //Metodo para registrar los productos :::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    public void Registrar(View view)
    {
        SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura

        String codigo       =   editCodigo.getText().toString();
        String Nombre       =   editNombre.getText().toString();
        String Descripcion  =   editDescripcion.getText().toString();
        String Precio       =   editPrecio.getText().toString();

        if(!codigo.isEmpty() && !Nombre.isEmpty() && !Descripcion.isEmpty() && !Precio.isEmpty() && valor==1)
        {
            ContentValues registro  =   new ContentValues();
            registro.put("codigo",codigo);
            registro.put("producto",Nombre);
            registro.put("descripcion",Descripcion);
            registro.put("precio",Precio);

            basededatos.insert("bebidas",null,registro);
            basededatos.close();
            editCodigo.setText("");
            editNombre.setText("");
            editDescripcion.setText("");
            editPrecio.setText("");

            Toast.makeText(this,"¡¡ Registro Exitoso !!", Toast.LENGTH_SHORT).show();
        }else if (!codigo.isEmpty() && !Nombre.isEmpty() && !Descripcion.isEmpty() && !Precio.isEmpty() && valor==0)
        {
            ContentValues registro  =   new ContentValues();
            registro.put("codigo",codigo);
            registro.put("producto",Nombre);
            registro.put("descripcion",Descripcion);
            registro.put("precio",Precio);

            basededatos.insert("platillos",null,registro);
            basededatos.close();
            editCodigo.setText("");
            editNombre.setText("");
            editDescripcion.setText("");
            editPrecio.setText("");

            Toast.makeText(this,"¡¡ Registro Exitoso !!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"¡¡Error, LLene los Campos !!", Toast.LENGTH_SHORT).show();
        }
        }
        public void Menu(View view)
        {
            Intent i = new Intent(this,InformacionProductos.class);
            startActivity(i);
        }


}
