package firebase.app.listaprecios;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import firebase.app.listaprecios.db.DbHelper;
import firebase.app.listaprecios.entidades.Productos;

public class VerProductoCodigoBarra  extends AppCompatActivity {
    TextView txtNombre, txtPrecio, txtFecha;
    TextView txtCodigo;
    ImageView img;
    Button btnEditar;

    String codigoBarra;
    String ProEliminado2;
    //db helper
    private DbHelper dbHelper;
    FloatingActionButton fabeditar, fabeliminar;

    ArrayList<Productos> listaProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vista_producto_codigo_barra);

        this.listaProductos = listaProductos;


        txtNombre = findViewById(R.id.txtNombreCB);
        txtPrecio = findViewById(R.id.txtPrecioCB);
        txtFecha = findViewById(R.id.txtFechaExpCB);
        txtCodigo=findViewById(R.id.txtCodigoBarraCB);

        img=findViewById(R.id.profileIvCB);
        //init db helper class
        dbHelper=  new DbHelper(this);


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                codigoBarra = null;
            } else {
                codigoBarra = extras.getString("CODIGOBARRA");
            }
        } else {
            codigoBarra =savedInstanceState.getSerializable("CODIGOBARRA").toString();
        }



        //<<<<------->>>>>>>
        showRecordDetails();


        txtCodigo.setVisibility(View.INVISIBLE);
        /*txtNombre.setInputType(InputType.TYPE_NULL);
        txtPrecio.setInputType(InputType.TYPE_NULL);
        txtFecha.setInputType(InputType.TYPE_NULL);*/




        String ProEliminado = txtNombre.getText().toString();
        ProEliminado2 = ProEliminado;



    }



    private void showRecordDetails() {
        //get record details


        //consulta para seleccionar el registro en función de la identificación del registro

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.CODIGO_BARRA + " =\"" + codigoBarra + "\""+" or "+Constants.CODIGO_BARRA_2+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_3+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_4+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_5+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_6+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_7+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_8+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_9+" =\""+codigoBarra+"\"";;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //sigue revisando toda la base de datos para ese registro
        if (cursor.moveToFirst()) {
            do {
                //get data
                String id = "" + cursor.getInt(cursor.getColumnIndex(Constants.C_ID));
                String name = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                String precio = "" + cursor.getString(cursor.getColumnIndex(Constants.C_PRECIO));
                String fecha = "" + cursor.getString(cursor.getColumnIndex(Constants.C_FECHA));
                String imgg = "" + cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE));
                String codigoB=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA));
                String codigoB_2=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_2));
                String codigoB_3=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_3));
                String codigoB_4=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_4));
                String codigoB_5=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_5));
                String codigoB_6=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_6));
                String codigoB_7=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_7));
                String codigoB_8=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_8));
                String codigoB_9=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_9));


                //set data
                txtNombre.setText(name);
                txtPrecio.setText("S/"+precio);
                txtFecha.setText(fecha);
                txtCodigo.setText(codigoB);

                //si el usuario no adjunta la imagen, imageUri será nulo, así que configure una imagen predeterminada en ese caso
                if (imgg.equals("null")) {
                    //no hay imagen en el registro, mostrar por defecto
                    img.setImageResource(R.drawable.ic_not_image);
                } else {
                    //tener imagen en registro
                    img.setImageURI(Uri.parse(imgg));
                }


            } while (cursor.moveToNext());
        }
        db.close();



    }

}
