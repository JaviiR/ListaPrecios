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
    TextView txtCodigo;
    ImageView img;
    Button btnEditar;

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
        txtCodigo=findViewById(R.id.txtCodigoBarraEditar);
        btnEditar = findViewById(R.id.btnEditar);
        fabeditar = findViewById(R.id.fabEditar);
        fabeliminar = findViewById(R.id.fabEliminar);
        img=findViewById(R.id.profileIvEditar);
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