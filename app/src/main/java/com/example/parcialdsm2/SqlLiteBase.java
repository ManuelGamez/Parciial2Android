package com.example.parcialdsm2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlLiteBase extends SQLiteOpenHelper {


    public SqlLiteBase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BasedeDatos) {
        BasedeDatos.execSQL("create table platillos (codigo int primary key, producto text, descripcion text, precio real)");
        BasedeDatos.execSQL("create table bebidas   (codigo int primary key, producto text, descripcion text, precio real)");
        BasedeDatos.execSQL("create table pedidos   (nombrecliente  text, producto text, descripcion text, cantidad int, preciounitario real, subtotal real, estado int)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
