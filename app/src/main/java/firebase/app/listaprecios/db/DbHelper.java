package firebase.app.listaprecios.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import firebase.app.listaprecios.Constants;
import firebase.app.listaprecios.EditarProducto;
import firebase.app.listaprecios.entidades.Productos;

public class DbHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Productos.s3db";
    public static final String TABLEe_NAME = "Lista";
    /*private static final String CrearLista="CREATE TABLE IF NOT EXISTS "+TABLEe_NAME+"(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT not null," +
            "nombre TEXT not null," +
            "precio TEXT not null," +
            "fecha_exp TEXT)";*/


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //crear tabla en esa base de datos
        sqLiteDatabase.execSQL(Constants.CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //actualizar la base de datos (si hay alguna estructura, cambie la versión de la base de datos de cambio)
        //eliminar la tabla anterior si existe
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);
        //crear tabla de nuevo
        onCreate(sqLiteDatabase);
    }

    //insertar registro en la base de datos
    public long insertRecord(String name, String precio, String fecha, String image/*, String addedTime, String updateTime*/,String codigoBarra,String codigoBarra_2,String codigoBarra_3,String codigoBarra_4,String codigoBarra_5,String codigoBarra_6,String codigoBarra_7,String codigoBarra_8,String codigoBarra_9) {
        //obtener una base de datos que se pueda escribir porque queremos escribir datos
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //id se insertará automáticamente cuando establezcamos AUTOINCREMENT en la consulta

        //insertar datos
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_PRECIO, precio);
        values.put(Constants.C_FECHA, fecha);
        values.put(Constants.C_IMAGE, image);
        //VERIFICANDO QUE NO INGRESE STRINGS EN VES DE NULLS EN LAS COLUMNAS DE CODIGOS
        //CB1
        if(codigoBarra.equals("")) {
            values.put(Constants.CODIGO_BARRA, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA, codigoBarra);
        }
        //CB2
        if(codigoBarra_2.equals("")) {
            values.put(Constants.CODIGO_BARRA_2, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_2, codigoBarra_2);
        }
        //CB3
        if(codigoBarra_3.equals("")) {
            values.put(Constants.CODIGO_BARRA_3, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_3, codigoBarra_3);
        }
        //CB4
        if(codigoBarra_4.equals("")) {
            values.put(Constants.CODIGO_BARRA_4, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_4, codigoBarra_4);
        }
        //CB5
        if(codigoBarra_5.equals("")) {
            values.put(Constants.CODIGO_BARRA_5, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_5, codigoBarra_5);
        }
        //CB6
        if(codigoBarra_6.equals("")) {
            values.put(Constants.CODIGO_BARRA_6, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_6, codigoBarra_6);
        }
        //CB7
        if(codigoBarra_7.equals("")) {
            values.put(Constants.CODIGO_BARRA_7, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_7, codigoBarra_7);
        }
        //CB8
        if(codigoBarra_8.equals("")) {
            values.put(Constants.CODIGO_BARRA_8, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_8, codigoBarra_8);
        }
        //CB9
        if(codigoBarra_9.equals("")) {
            values.put(Constants.CODIGO_BARRA_9, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_9, codigoBarra_9);
        }

        //values.put(Constants.C_ADDED_TIMESTAMP, addedTime);
       // values.put(Constants.C_UPDATED_TIMESTAMP, updateTime);

        //insertar fila, devolverá el ID de registro del registro guardado
        long id = db.insert(Constants.TABLE_NAME, null, values);

        //cerrar conexión db
        db.close();

        //ID de retorno del registro de inserción
        return id;
    }




    //actualizar registro en la base de datos
    public int updateRecord(String id,String name, String precio, String fecha, String image/*, String addedTime, String updateTime*/,String codigoBarra,String codigoBarra_2,String codigoBarra_3,String codigoBarra_4,String codigoBarra_5,String codigoBarra_6,String codigoBarra_7,String codigoBarra_8,String codigoBarra_9) {
        //obtener una base de datos que se pueda escribir porque queremos escribir datos
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //id se insertará automáticamente cuando establezcamos AUTOINCREMENT en la consulta

        //insertar datos
        values.put(Constants.C_NAME, name);
        values.put(Constants.C_PRECIO, precio);
        values.put(Constants.C_FECHA, fecha);
        values.put(Constants.C_IMAGE, image);
        if(codigoBarra.equals("")) {
            values.put(Constants.CODIGO_BARRA, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA, codigoBarra);
        }
        //CB2
        if(codigoBarra_2.equals("")) {
            values.put(Constants.CODIGO_BARRA_2, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_2, codigoBarra_2);
        }
        //CB3
        if(codigoBarra_3.equals("")) {
            values.put(Constants.CODIGO_BARRA_3, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_3, codigoBarra_3);
        }
        //CB4
        if(codigoBarra_4.equals("")) {
            values.put(Constants.CODIGO_BARRA_4, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_4, codigoBarra_4);
        }
        //CB5
        if(codigoBarra_5.equals("")) {
            values.put(Constants.CODIGO_BARRA_5, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_5, codigoBarra_5);
        }
        //CB6
        if(codigoBarra_6.equals("")) {
            values.put(Constants.CODIGO_BARRA_6, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_6, codigoBarra_6);
        }
        //CB7
        if(codigoBarra_7.equals("")) {
            values.put(Constants.CODIGO_BARRA_7, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_7, codigoBarra_7);
        }
        //CB8
        if(codigoBarra_8.equals("")) {
            values.put(Constants.CODIGO_BARRA_8, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_8, codigoBarra_8);
        }
        //CB9
        if(codigoBarra_9.equals("")) {
            values.put(Constants.CODIGO_BARRA_9, (byte[]) null);
        }else{
            values.put(Constants.CODIGO_BARRA_9, codigoBarra_9);
        }
        //values.put(Constants.C_ADDED_TIMESTAMP, addedTime);
        // values.put(Constants.C_UPDATED_TIMESTAMP, updateTime);
try {
    //insertar fila, devolverá el ID de registro del registro guardado
    int confirmacion=db.update(Constants.TABLE_NAME, values,Constants.C_ID + " =?", new String[]{id});

    //cerrar conexión db
    db.close();
    return confirmacion;

}catch (SQLException e){

    return 0;
}




    }



    public boolean deleteFromId(String id){
SQLiteDatabase db=getWritableDatabase();
db.delete(Constants.TABLE_NAME,Constants.C_ID+" = ?",new String[]{id});
db.close();
return true;

    }







}
