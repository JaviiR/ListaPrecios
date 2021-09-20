package firebase.app.listaprecios.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import firebase.app.listaprecios.VerProducto;
import firebase.app.listaprecios.entidades.Productos;

public class DbProducto extends DbHelper{
    Context contextDbProducto;
    public DbProducto(@Nullable Context context) {
        super(context);
        this.contextDbProducto=context;
    }

    public long insertarProducto(String nombre,String precio,String fecha_exp){
       long idd=0;
        DbHelper dbHelper=new DbHelper(contextDbProducto);

        try {



        SQLiteDatabase db= dbHelper.getWritableDatabase();
        ContentValues values=new ContentValues();

        values.put("nombre",nombre);
        values.put("precio",precio);
        values.put("fecha_exp",fecha_exp);
        idd=db.insert(TABLE_NAME,null,values);
       }catch (Exception e){
           e.toString();
       }
       return idd;

    }



    public ArrayList<Productos> mostrarProductos(){
        DbHelper dbhelper=new DbHelper(contextDbProducto);
        SQLiteDatabase db=dbhelper.getWritableDatabase();

        ArrayList<Productos> listaProductos=new ArrayList<>();
        Productos producto=null;
        Cursor cursorProductos=null;

        cursorProductos=db.rawQuery("Select * from "+TABLE_NAME+" order by nombre",null);

            if(cursorProductos.moveToFirst()){
                do{
                    producto= new Productos();
                    producto.setId(cursorProductos.getInt(0));
                    producto.setNombre(cursorProductos.getString(1));
                    producto.setPrecio(cursorProductos.getString(2));
                    producto.setFecha_exp(cursorProductos.getString(3));
                    listaProductos.add(producto);
                }while (cursorProductos.moveToNext());
            }
            cursorProductos.close();
            return listaProductos;

    }

    public Productos verProductos(int id){
        DbHelper dbhelper=new DbHelper(contextDbProducto);
        SQLiteDatabase db=dbhelper.getWritableDatabase();


        Productos producto=null;
        Cursor cursorProductos=null;

        cursorProductos=db.rawQuery("Select * from "+TABLE_NAME+" where id= "+id+" LIMIT 1",null);

        if(cursorProductos.moveToFirst()){

                producto= new Productos();
                producto.setId(cursorProductos.getInt(0));
                producto.setNombre(cursorProductos.getString(1));
                producto.setPrecio(cursorProductos.getString(2));
                producto.setFecha_exp(cursorProductos.getString(3));


        }
        cursorProductos.close();
        return producto;

    }


    public boolean editarProducto(int id,String nombre,String precio,String fecha_exp){
        boolean estado=false;
        DbHelper dbHelper=new DbHelper(contextDbProducto);
        SQLiteDatabase db= dbHelper.getWritableDatabase();

        try {

            db.execSQL("update "+TABLE_NAME+" SET nombre="+"'"+nombre+"'"+" ,precio="+"'"+precio+"'"+" ,fecha_exp="+"'"+fecha_exp+"'"+" where id="+id);
            estado=true;
        }catch (Exception e){
            e.toString();
            estado=false;
        }finally {
            db.close();
        }
        return estado;

    }


        //(1)
    public boolean eliminarProducto(int id){
        boolean estado=false;
        DbHelper dbHelper=new DbHelper(contextDbProducto);
        SQLiteDatabase db= dbHelper.getWritableDatabase();

        try {

            db.execSQL("delete from "+TABLE_NAME+" WHERE id="+id);
            estado=true;
        }catch (Exception e){

            e.toString();
            estado=false;
        }finally {
            db.close();
        }
        return estado;

    }



}
