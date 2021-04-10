package com.example.parcialdsm2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Comidas extends AppCompatActivity {
    private Spinner opciones ;
    private TextView NombreProducto,ProductoPrecio,ProductoDescripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas);
        opciones    =   (Spinner) findViewById(R.id.spinnerC);
        NombreProducto      =   (TextView) findViewById(R.id.txtNombreC);
        ProductoPrecio      =   (TextView)findViewById(R.id.txtprecioC);
        ProductoDescripcion =   (TextView)findViewById(R.id.txtdescripcionC);
        SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura
        Cursor fila = basededatos.rawQuery
                ("select codigo, producto,descripcion,precio from platillos",null);
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
    public void consultarbdcomida(View view)
    {
       SqlLiteBase objetoBD = new SqlLiteBase(this,"TacosLocos",null,1);
        SQLiteDatabase basededatos = objetoBD.getWritableDatabase();//abro la BD para escritura y lectura

        Cursor fila2 = basededatos.rawQuery
                ("select codigo, producto,descripcion,precio from platillos where producto = '"+opciones.getSelectedItem().toString()+"'",null);

        fila2.moveToFirst();///////////////
        NombreProducto.setText(fila2.getString(1));
        ProductoDescripcion.setText(fila2.getString(2));
        ProductoPrecio.setText(fila2.getString(3));

        basededatos.close();


    }
}