package firebase.app.listaprecios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import firebase.app.listaprecios.db.DbHelper;
import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;

public class VerProducto extends AppCompatActivity {
    EditText txtNombre, txtPrecio, txtFecha;
    TextView txtCodigo,txtCodigoBarra2,txtCodigoBarra3,txtCodigoBarra4,txtCodigoBarra5,txtCodigoBarra6,txtCodigoBarra7,txtCodigoBarra8,txtCodigoBarra9;;
    ImageView img;
    Button btnEditar,btnBorrar1,btnBorrar2,btnBorrar3,btnBorrar4,btnBorrar5,btnBorrar6,btnBorrar7,btnBorrar8,btnBorrar9;

    String id;
    String ProEliminado2;
    //db helper
    private DbHelper dbHelper;
    FloatingActionButton fabeditar, fabeliminar;

    ArrayList<Productos> listaProductos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);

        this.listaProductos = listaProductos;


        txtNombre = findViewById(R.id.txtNombreEditar);
        txtPrecio = findViewById(R.id.txtPrecioEditar);
        txtFecha = findViewById(R.id.txtFechaExpEditar);
        txtCodigo=findViewById(R.id.txtCodigoBarraEditar1);
        txtCodigoBarra2=findViewById(R.id.txtCodigoBarraEditar2);
        txtCodigoBarra3=findViewById(R.id.txtCodigoBarraEditar3);
        txtCodigoBarra4=findViewById(R.id.txtCodigoBarraEditar4);
        txtCodigoBarra5=findViewById(R.id.txtCodigoBarraEditar5);
        txtCodigoBarra6=findViewById(R.id.txtCodigoBarraEditar6);
        txtCodigoBarra7=findViewById(R.id.txtCodigoBarraEditar7);
        txtCodigoBarra8=findViewById(R.id.txtCodigoBarraEditar8);
        txtCodigoBarra9=findViewById(R.id.txtCodigoBarraEditar9);
        btnEditar = findViewById(R.id.btnEditar);
        fabeditar = findViewById(R.id.fabEditar);
        fabeliminar = findViewById(R.id.fabEliminar);
        img=findViewById(R.id.profileIvEditar);
        btnBorrar1=findViewById(R.id.btnBorrarCodigo1Editar);
        btnBorrar2=findViewById(R.id.btnBorrarCodigo2Editar);
        btnBorrar3=findViewById(R.id.btnBorrarCodigo3Editar);
        btnBorrar4=findViewById(R.id.btnBorrarCodigo4Editar);
        btnBorrar5=findViewById(R.id.btnBorrarCodigo5Editar);
        btnBorrar6=findViewById(R.id.btnBorrarCodigo6Editar);
        btnBorrar7=findViewById(R.id.btnBorrarCodigo7Editar);
        btnBorrar8=findViewById(R.id.btnBorrarCodigo8Editar);
        btnBorrar9=findViewById(R.id.btnBorrarCodigo9Editar);


        btnBorrar1.setVisibility(View.INVISIBLE);
        btnBorrar2.setVisibility(View.INVISIBLE);
        btnBorrar3.setVisibility(View.INVISIBLE);
        btnBorrar4.setVisibility(View.INVISIBLE);
        btnBorrar5.setVisibility(View.INVISIBLE);
        btnBorrar6.setVisibility(View.INVISIBLE);
        btnBorrar7.setVisibility(View.INVISIBLE);
        btnBorrar8.setVisibility(View.INVISIBLE);
        btnBorrar9.setVisibility(View.INVISIBLE);
        //init db helper class
        dbHelper=  new DbHelper(this);
        //obtener el id de registro del adaptador a través de la intención
        /*Intent intent=getIntent();
        recordID= intent.getStringExtra("RECORD_ID");
        Intent intent2=getIntent();
        recordIDD= intent2.getStringExtra("RECORD_IDDDD");*/


        /*Intent intent3=getIntent();
        imgIntentput=intent3.getStringExtra("imagen_Main_a_ver");
        Intent intent4=getIntent();
        imgputdeEditar=intent4.getStringExtra("imagen_editar_a_ver");
        Toast.makeText(VerProducto.this, "imagen_Main_a_ver: "+imgIntentput, Toast.LENGTH_LONG).show();
        Toast.makeText(VerProducto.this, "imagen_editar_a_ver: "+imgputdeEditar, Toast.LENGTH_LONG).show();*/

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = null;
            } else {
                id = extras.getString("RECORD_ID");
            }
        } else {
            id =savedInstanceState.getSerializable("RECORD_ID").toString();
        }




        //DbProducto dbproducto = new DbProducto(VerProducto.this);
        //producto = dbproducto.verProductos(id);

        //<<<<------->>>>>>>
        showRecordDetails();


        /*if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(producto.getPrecio());
            txtFecha.setText(producto.getFecha_exp());

            btnEditar.setVisibility(View.INVISIBLE);
            txtNombre.setInputType(InputType.TYPE_NULL);
            txtPrecio.setInputType(InputType.TYPE_NULL);
            txtFecha.setInputType(InputType.TYPE_NULL);
        }*/
        btnEditar.setVisibility(View.INVISIBLE);
        txtNombre.setInputType(InputType.TYPE_NULL);
        txtPrecio.setInputType(InputType.TYPE_NULL);
        txtFecha.setInputType(InputType.TYPE_NULL);


        fabeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VerProducto.this, EditarProducto.class);
                intent.putExtra("RECORD_ID", id);


                startActivity(intent);
            }
        });

        String ProEliminado = txtNombre.getText().toString();
        ProEliminado2 = ProEliminado;



        //(4)
        fabeliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerProducto.this);
                builder.setMessage("DESEAS ELIMINAR ESTE PRODUCTO?").setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        boolean estado = dbHelper.deleteFromId(id);
                        if (estado) {


                            VolverAvista();
                        }
                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).show();
            }

        });


    }


    private void VolverAvista() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nombre", ProEliminado2);
        startActivity(intent);
    }



    private void showRecordDetails() {
        //get record details


        //consulta para seleccionar el registro en función de la identificación del registro

            String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME + " WHERE " + Constants.C_ID + " =\"" + id + "\"";
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
                    // String timestampAdded=""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP));
                    // String timestampUpdated=""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP));


              /*  Calendar calendar1=Calendar.getInstance(Locale.getDefault());
                calendar1.setTimeInMillis(Long.parseLong(timestampAdded));
                String timeAdded=""+ DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar1);*/


               /* Calendar calendar2=Calendar.getInstance(Locale.getDefault());
                calendar2.setTimeInMillis(Long.parseLong(timestampUpdated));
                String timeUpdated=""+ DateFormat.format("dd/MM/yyyy hh:mm:aa",calendar2);*/

                    //set data
                    txtNombre.setText(name);
                    txtPrecio.setText(precio);
                    txtFecha.setText(fecha);
                    if(codigoB.equals("null")){
                        txtCodigo.setHint("CODIGO BARRA #1");
                    }else {
                        txtCodigo.setText(codigoB);
                    }
                    if(codigoB_2.equals("null")){
                        txtCodigoBarra2.setHint("CODIGO BARRA #2");

                    }else {
                        txtCodigoBarra2.setText(codigoB_2);
                    }
                    if(codigoB_3.equals("null")){
                        txtCodigoBarra3.setHint("CODIGO BARRA #3");

                    }else {
                        txtCodigoBarra3.setText(codigoB_3);
                    }
                    if(codigoB_4.equals("null")){
                        txtCodigoBarra4.setHint("CODIGO BARRA #4");

                    }else {
                        txtCodigoBarra4.setText(codigoB_4);
                    }
                    if(codigoB_5.equals("null")){
                        txtCodigoBarra5.setHint("CODIGO BARRA #5");

                    }else {
                        txtCodigoBarra5.setText(codigoB_5);
                    }
                    if(codigoB_6.equals("null")){
                        txtCodigoBarra6.setHint("CODIGO BARRA #6");

                    }else {
                        txtCodigoBarra6.setText(codigoB_6);
                    }
                    if(codigoB_7.equals("null")){
                        txtCodigoBarra7.setHint("CODIGO BARRA #7");

                    }else {
                        txtCodigoBarra7.setText(codigoB_7);
                    }
                    if(codigoB_8.equals("null")){
                        txtCodigoBarra8.setHint("CODIGO BARRA #8");

                    }else {
                        txtCodigoBarra8.setText(codigoB_8);
                    }
                    if(codigoB_9.equals("null")){
                        txtCodigoBarra9.setHint("CODIGO BARRA #9");

                    }else {
                        txtCodigoBarra9.setText(codigoB_9);
                    }
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