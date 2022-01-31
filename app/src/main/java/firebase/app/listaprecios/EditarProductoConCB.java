package firebase.app.listaprecios;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import firebase.app.listaprecios.db.DbHelper;

public class EditarProductoConCB extends AppCompatActivity {
    EditText txtNombre, txtPrecio, txtFecha;
    TextView txtCodigo,txtCodigoBarra2,txtCodigoBarra3,txtCodigoBarra4,txtCodigoBarra5,txtCodigoBarra6,txtCodigoBarra7,txtCodigoBarra8,txtCodigoBarra9;;
    Button btnEditar;
    ImageView img;
    boolean estado;

    String id;

    FloatingActionButton fabeditar, fabeliminar;
    private DbHelper dbHelper;
    private String name, precio, fecha,codigo,codigo_2,codigo_3,codigo_4,codigo_5,codigo_6,codigo_7,codigo_8,codigo_9;


    //permission constants
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;
    //constantes de selección de imágenes
    private static final int IMAGE_PICK_CAMERA_CODE = 102;
    private static final int IMAGE_PICK_GALLERY_CODE = 103;
    //arrays de permisos
    private String[] cameraPermissions;//camara y almacenamiento
    private String[] storagePermissions;//solo almacenamiento
    //variables (contendrá datos para guardar)
    private Uri imageUri;
    String Stringimgg;
    String nombre;
    int CB;
    String stringid;

    String codigoIniciado1,codigoIniciado2,codigoIniciado3,codigoIniciado4,codigoIniciado5,codigoIniciado6,codigoIniciado7,codigoIniciado8,codigoIniciado9;

    String idComprobado1,idComprobado2,idComprobado3,idComprobado4,idComprobado5,idComprobado6,idComprobado7,idComprobado8,idComprobado9;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
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
        img=findViewById(R.id.profileIvEditar);
        fabeliminar = findViewById(R.id.fabEliminar);
        //(2)
        fabeditar.setVisibility(View.INVISIBLE);
        fabeliminar.setVisibility(View.INVISIBLE);



        //haga clic en la vista de imagen para mostrar las imagenes galeria
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrar cuadro de diálogo de selección de imagen
                imagePickDialog();

            }
        });






        dbHelper=  new DbHelper(this);
        //matrices de permisos de inicio
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



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




        showRecordDetails();





        codigoIniciado1=txtCodigo.getText().toString().trim();
        codigoIniciado2=txtCodigoBarra2.getText().toString().trim();
        codigoIniciado3=txtCodigoBarra3.getText().toString().trim();
        codigoIniciado4=txtCodigoBarra4.getText().toString().trim();
        codigoIniciado5=txtCodigoBarra5.getText().toString().trim();
        codigoIniciado6=txtCodigoBarra6.getText().toString().trim();
        codigoIniciado7=txtCodigoBarra7.getText().toString().trim();
        codigoIniciado8=txtCodigoBarra8.getText().toString().trim();
        codigoIniciado9=txtCodigoBarra9.getText().toString().trim();
        /*Toast.makeText(EditarProductoConCB.this, "codigo iniciado 1:  "+codigoIniciado1, Toast.LENGTH_LONG).show();
        Toast.makeText(EditarProductoConCB.this, "codigo iniciado 2:  "+codigoIniciado2, Toast.LENGTH_LONG).show();
        Toast.makeText(EditarProductoConCB.this, "codigo iniciado 3:  "+codigoIniciado3, Toast.LENGTH_LONG).show();
        Toast.makeText(EditarProductoConCB.this, "codigo iniciado 4:  "+codigoIniciado4, Toast.LENGTH_LONG).show();
*/
        //comprobando si existen los codigos que llegan con el producto
        idComprobado1 = comprobarSiExisteConCB(codigoIniciado1);
        idComprobado2 = comprobarSiExisteConCB(codigoIniciado2);
        idComprobado3 = comprobarSiExisteConCB(codigoIniciado3);
        idComprobado4 = comprobarSiExisteConCB(codigoIniciado4);
        idComprobado5 = comprobarSiExisteConCB(codigoIniciado5);
        idComprobado6 = comprobarSiExisteConCB(codigoIniciado6);
        idComprobado7 = comprobarSiExisteConCB(codigoIniciado7);
        idComprobado8 = comprobarSiExisteConCB(codigoIniciado8);
        idComprobado9 = comprobarSiExisteConCB(codigoIniciado9);






        //intentando capturar la img url

        String selectQuery="SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.C_ID+" =\""+id+"\"";
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                //nombre = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                Stringimgg = "" + cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE));
            }while (cursor.moveToNext());
        }


        //CLICK AL CODIGO DE BARRA #1
        txtCodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);


                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=1;
            }
        });

        //CLICK AL CODIGO DE BARRA #2
        txtCodigoBarra2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=2;
            }
        });
        //CLICK AL CODIGO DE BARRA #3
        txtCodigoBarra3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=3;
            }
        });
        //CLICK AL CODIGO DE BARRA #4
        txtCodigoBarra4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=4;
            }
        });
        //CLICK AL CODIGO DE BARRA #5
        txtCodigoBarra5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=5;
            }
        });
        //CLICK AL CODIGO DE BARRA #6
        txtCodigoBarra6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=6;
            }
        });
        //CLICK AL CODIGO DE BARRA #7
        txtCodigoBarra7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=7;
            }
        });
        //CLICK AL CODIGO DE BARRA #8
        txtCodigoBarra8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=8;
            }
        });
        //CLICK AL CODIGO DE BARRA #9
        txtCodigoBarra9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(EditarProductoConCB.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=9;
            }
        });

       /* final DbProducto dbproducto = new DbProducto(EditarProductoConCB.this);
        producto = dbproducto.verProductos(id);

        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(producto.getPrecio());
            txtFecha.setText(producto.getFecha_exp());

        }*/


        btnEditar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String cb1="", cb2="", cb3="", cb4="", cb5="", cb6="", cb7="", cb8="", cb9="";
                if(idComprobado1==null) {
                    cb1 = txtCodigo.getText().toString();
                }
                if(idComprobado2==null) {

                    cb2 = txtCodigoBarra2.getText().toString();
                }
                if(idComprobado3==null) {

                    cb3 = txtCodigoBarra3.getText().toString();
                }
                if(idComprobado4==null) {

                    cb4 = txtCodigoBarra4.getText().toString();
                }
                if(idComprobado5==null) {

                    cb5 = txtCodigoBarra5.getText().toString();
                }
                if(idComprobado6==null) {

                    cb6 = txtCodigoBarra6.getText().toString();
                }
                if(idComprobado7==null) {

                    cb7 = txtCodigoBarra7.getText().toString();
                }
                if(idComprobado8==null) {

                    cb8 = txtCodigoBarra8.getText().toString();
                }
                if(idComprobado9==null) {

                    cb9 = txtCodigoBarra9.getText().toString();
                }
                String id1 = comprobarSiExisteConCB(cb1);
                String id2 = comprobarSiExisteConCB(cb2);
                String id3 = comprobarSiExisteConCB(cb3);
                String id4 = comprobarSiExisteConCB(cb4);
                String id5 = comprobarSiExisteConCB(cb5);
                String id6 = comprobarSiExisteConCB(cb6);
                String id7 = comprobarSiExisteConCB(cb7);
                String id8 = comprobarSiExisteConCB(cb8);
                String id9 = comprobarSiExisteConCB(cb9);

                String nombre = txtNombre.getText().toString();
                String precio = txtPrecio.getText().toString();
                String fecha = txtFecha.getText().toString();

                if(id1==null && id2==null && id3==null && id4==null && id5==null && id6==null && id7==null && id8==null && id9==null) {
                    if (!nombre.equals("") && !precio.equals("")) {
                        if(comprobarCodigoRepetidos()){
                            Toast.makeText(EditarProductoConCB.this, "CODIGO DE BARRAS REPETIDO", Toast.LENGTH_LONG).show();
                        }else {
                            estado = updateData();
                            if (estado != false) {
                                Toast.makeText(EditarProductoConCB.this, nombre + " ACTUALIZADO", Toast.LENGTH_LONG).show();
                                verProducto();
                            } else {
                                Toast.makeText(EditarProductoConCB.this, "ESTE CODIGO DE BARRAS YA ESTA EN USO ", Toast.LENGTH_LONG).show();
                            }
                        }


                    } else if (txtNombre.getText().toString().equals("")) {
                        Toast.makeText(EditarProductoConCB.this, "FALTA NOMBRE DEL PRODUCTO", Toast.LENGTH_LONG).show();
                    } else if (txtPrecio.getText().toString().equals("")) {
                        Toast.makeText(EditarProductoConCB.this, "FALTA PRECIO DEL PRODUCTO", Toast.LENGTH_LONG).show();
                    } else if (txtFecha.getText().toString().equals("")) {
                        Toast.makeText(EditarProductoConCB.this, "ESCRIBIR NULL O ALGUNA FECHA EN FECHA DE EXP DEL PRODUCTO", Toast.LENGTH_LONG).show();
                    }
                }else
                    Toast.makeText(EditarProductoConCB.this, "CODIGO DE BARRAS YA EXISTE", Toast.LENGTH_LONG).show();
               /* Toast.makeText(EditarProductoConCB.this, "ID 1: "+id1, Toast.LENGTH_LONG).show();
                Toast.makeText(EditarProductoConCB.this, "ID 2: "+id2, Toast.LENGTH_LONG).show();
                Toast.makeText(EditarProductoConCB.this, "ID 3: "+id3, Toast.LENGTH_LONG).show();*/
            }
        });

    }



    public boolean comprobarCodigoRepetidos(){
        String cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9;
        cb1=txtCodigo.getText().toString();
        cb2=txtCodigoBarra2.getText().toString();
        cb3=txtCodigoBarra3.getText().toString();
        cb4=txtCodigoBarra4.getText().toString();
        cb5=txtCodigoBarra5.getText().toString();
        cb6=txtCodigoBarra6.getText().toString();
        cb7=txtCodigoBarra7.getText().toString();
        cb8=txtCodigoBarra8.getText().toString();
        cb9=txtCodigoBarra9.getText().toString();
        if((!cb1.isEmpty() && (cb1.equals(cb2) || cb1.equals(cb3) || cb1.equals(cb4) || cb1.equals(cb5) || cb1.equals(cb6) || cb1.equals(cb7) || cb1.equals(cb8) || cb1.equals(cb9)))
                || (!cb2.isEmpty() && (cb2.equals(cb1) || cb2.equals(cb3) || cb2.equals(cb4) || cb2.equals(cb5) || cb2.equals(cb6) || cb2.equals(cb7) || cb2.equals(cb8) || cb2.equals(cb9)))
                || (!cb3.isEmpty() && (cb3.equals(cb1) || cb3.equals(cb2) || cb3.equals(cb4) || cb3.equals(cb5) || cb3.equals(cb6) || cb3.equals(cb7) || cb3.equals(cb8) || cb3.equals(cb9)))
                || (!cb4.isEmpty() && (cb4.equals(cb1) || cb4.equals(cb2) || cb4.equals(cb3) || cb4.equals(cb5) || cb4.equals(cb6) || cb4.equals(cb7) || cb4.equals(cb8) || cb4.equals(cb9)))
                || (!cb5.isEmpty() && (cb5.equals(cb1) || cb5.equals(cb2) || cb5.equals(cb3) || cb5.equals(cb4) || cb5.equals(cb6) || cb5.equals(cb7) || cb5.equals(cb8) || cb5.equals(cb9)))
                || (!cb6.isEmpty() && (cb6.equals(cb1) || cb6.equals(cb2) || cb6.equals(cb3) || cb6.equals(cb4) || cb6.equals(cb5) || cb6.equals(cb7) || cb6.equals(cb8) || cb6.equals(cb9)))
                || (!cb7.isEmpty() && (cb7.equals(cb1) || cb7.equals(cb2) || cb7.equals(cb3) || cb7.equals(cb4) || cb7.equals(cb5) || cb7.equals(cb6) || cb7.equals(cb8) || cb7.equals(cb9)))
                || (!cb8.isEmpty() && (cb8.equals(cb1) || cb8.equals(cb2) || cb8.equals(cb3) || cb8.equals(cb4) || cb8.equals(cb5) || cb8.equals(cb6) || cb8.equals(cb7) || cb8.equals(cb9)))
                || (!cb9.isEmpty() && (cb9.equals(cb1) || cb9.equals(cb2) || cb9.equals(cb3) || cb9.equals(cb4) || cb9.equals(cb5) || cb9.equals(cb6) || cb9.equals(cb7) || cb9.equals(cb8)))
        ) {

            return true;
        }else {
            return false;
        }

    }





    public String comprobarSiExisteConCB(String CB){
        String idCOmprobar2=null;
        String selectQuery="SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.CODIGO_BARRA+" =\""+CB+"\""+" or "+Constants.CODIGO_BARRA_2+" =\""+CB+"\""
                +" or "+Constants.CODIGO_BARRA_3+" =\""+CB+"\""+" or "+Constants.CODIGO_BARRA_4+" =\""+CB+"\""
                +" or "+Constants.CODIGO_BARRA_5+" =\""+CB+"\""+" or "+Constants.CODIGO_BARRA_6+" =\""+CB+"\""
                +" or "+Constants.CODIGO_BARRA_7+" =\""+CB+"\""+" or "+Constants.CODIGO_BARRA_8+" =\""+CB+"\""
                +" or "+Constants.CODIGO_BARRA_9+" =\""+CB+"\"";
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                //nombre = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                idCOmprobar2 = "" + cursor.getString(cursor.getColumnIndex(Constants.C_ID));
            }while (cursor.moveToNext());
        }
        return idCOmprobar2;
    }





    private void verProducto() {
        Intent intent = new Intent(this, VerProducto.class);
        intent.putExtra("RECORD_ID", id);

        startActivity(intent);
    }



    private void showRecordDetails() {
        //get record details


        //consulta para seleccionar el registro en función de la identificación del registro
        String selectQuery="SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.C_ID+" =\""+id+"\"";
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);

        //sigue revisando toda la base de datos para ese registro
        if(cursor.moveToFirst()){
            do{
                //get data
                String id=""+cursor.getInt(cursor.getColumnIndex(Constants.C_ID));
                String name=""+cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                String precio=""+cursor.getString(cursor.getColumnIndex(Constants.C_PRECIO));
                String fecha=""+cursor.getString(cursor.getColumnIndex(Constants.C_FECHA));
                String imgg=""+cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE));
                // String timestampAdded=""+cursor.getString(cursor.getColumnIndex(Constants.C_ADDED_TIMESTAMP));
                // String timestampUpdated=""+cursor.getString(cursor.getColumnIndex(Constants.C_UPDATED_TIMESTAMP));
                String codigoB=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA));
                String codigoB_2=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_2));
                String codigoB_3=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_3));
                String codigoB_4=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_4));
                String codigoB_5=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_5));
                String codigoB_6=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_6));
                String codigoB_7=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_7));
                String codigoB_8=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_8));
                String codigoB_9=""+cursor.getString(cursor.getColumnIndex(Constants.CODIGO_BARRA_9));


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
                if(imgg.equals("null")){
                    //no hay imagen en el registro, mostrar por defecto
                    img.setImageResource(R.drawable.ic_add_image);
                }else {
                    //tener imagen en registro
                    img.setImageURI(Uri.parse(imgg));
                }


            }while (cursor.moveToNext());
        }

        db.close();


    }







    private boolean updateData() {
        //get data
        name = "" + txtNombre.getText().toString().trim();
        precio = "" + txtPrecio.getText().toString().trim();
        codigo=txtCodigo.getText().toString().trim();
        codigo_2=txtCodigoBarra2.getText().toString().trim();
        codigo_3=txtCodigoBarra3.getText().toString().trim();
        codigo_4=txtCodigoBarra4.getText().toString().trim();
        codigo_5=txtCodigoBarra5.getText().toString().trim();
        codigo_6=txtCodigoBarra6.getText().toString().trim();
        codigo_7=txtCodigoBarra7.getText().toString().trim();
        codigo_8=txtCodigoBarra8.getText().toString().trim();
        codigo_9=txtCodigoBarra9.getText().toString().trim();
        int estado;
        if(txtFecha.getText().toString().isEmpty()){
            fecha="NULL";
        }else {
            fecha = "" + txtFecha.getText().toString().trim();
        }
        //save to db
        if(imageUri!=null){
            String timestamp = "" + System.currentTimeMillis();

            estado= dbHelper.updateRecord(
                    ""+id,
                    "" + name,
                    "" + precio,
                    "" + fecha,
                    "" + imageUri,
                    // "" + timestamp,
                    // "" + timestamp
                    codigo,
                    codigo_2,
                    codigo_3,
                    codigo_4,
                    codigo_5,
                    codigo_6,
                    codigo_7,
                    codigo_8,
                    codigo_9
            );

        }else{
            String timestamp = "" + System.currentTimeMillis();
            estado= dbHelper.updateRecord(
                    ""+id,
                    "" + name,
                    "" + precio,
                    "" + fecha,
                    "" + Stringimgg,
                    // "" + timestamp,
                    // "" + timestamp
                    codigo,
                    codigo_2,
                    codigo_3,
                    codigo_4,
                    codigo_5,
                    codigo_6,
                    codigo_7,
                    codigo_8,
                    codigo_9
            );

        }
        if (estado==0)
            return false;
        else
            return true;


    }





    private void imagePickDialog() {
        //opciones para mostrar en el diálogo
        String[] options = {"Camera", "Gallery"};
        //deialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //tittle
        builder.setTitle("Pick Image From");
        //set items/options
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //manejar clicks
                if (i == 0) {
                    //camera clicked
                    if (!checkCameraPermissions()) {
                        requestCameraPermission();
                    } else {
                        //permiso ya concedido
                        pickFromCamera();
                    }
                } else if (i == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        //permiso ya concedido
                        pickFromGallery();
                    }
                }
            }
        });
        //create/show dialog
        builder.create().show();

    }



    private void pickFromGallery() {
        //intención de elegir una imagen de la galería, la imagen se devolverá en el método onActivityResult
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); //Nosotros solo queremos imagenes
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {
        //intención de elegir una imagen de la camara, la imagen se devolverá en el método onActivityResult
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Image_Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image description");
        //poner imagen Uri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intención de abrir la cámara para la imagen
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

   /* private void cargarImagen() {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la Aplicación"),10);

    }*/


    private boolean checkStoragePermission() {
        //comprobar si el permiso de almacenamiento está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result;
    }

    private void requestStoragePermission() {
        //solicitar el permiso de almacenamiento
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermissions() {
        //comprobar si el permiso de almacenamiento está habilitado o no
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermission() {
        //solicitar el permiso de camara
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //La imagen seleccionada de la cámara o galería se recibirá aquí.
        if (resultCode == RESULT_OK) {
            //se elige la imagen
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //elegido de la galería

                //delimitar imagen
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //elegido de la camara

                //delimitar imagen
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                //imagen recortada recibida
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    imageUri = resultUri;
                    //establecer imagen
                    img.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //ERROR
                    Exception error = result.getError();
                    Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }


            //CRISTIAN henao
            //Uri path=data.getData();
            //imagen.setImageURI(path);
        }
        super.onActivityResult(requestCode, resultCode, data);



        //CODIGO BARRA #1
        if(CB==1) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigo.setText(result.getContents());
                    if(codigoIniciado1.equals(result.getContents().toString())){
                        idComprobado1 = comprobarSiExisteConCB(codigoIniciado1);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado1, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado1, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado1!=result.getContents()){
                        {
                            idComprobado1 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado1, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado1, Toast.LENGTH_SHORT).show();*/
                        }

                    }

                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #2
        if(CB==2) {
            IntentResult result2 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result2 != null) {
                if (result2.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra2.setText(result2.getContents());

                    if(codigoIniciado2.equals(result2.getContents().toString())){
                        idComprobado2 = comprobarSiExisteConCB(codigoIniciado2);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado2, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result2.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado2, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado2!=result2.getContents()){
                        {
                            idComprobado2 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado2, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result2.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado2, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }


            }
            else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #3
        if(CB==3) {
            IntentResult result3 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result3 != null) {
                if (result3.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra3.setText(result3.getContents());
                    if(codigoIniciado3.equals(result3.getContents().toString())){
                        idComprobado3 = comprobarSiExisteConCB(codigoIniciado3);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado3, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result3.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado3, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado3!=result3.getContents()){
                        {
                            idComprobado3 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado3, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result3.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado3, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #4
        if(CB==4) {
            IntentResult result4 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result4 != null) {
                if (result4.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra4.setText(result4.getContents());
                    if(codigoIniciado4.equals(result4.getContents().toString())){
                        idComprobado4 = comprobarSiExisteConCB(codigoIniciado4);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado4, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result4.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado4, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado4!=result4.getContents()){
                        {
                            idComprobado4 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado4, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result4.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado4, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #5
        if(CB==5) {
            IntentResult result5 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result5 != null) {
                if (result5.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra5.setText(result5.getContents());
                    if(codigoIniciado5.equals(result5.getContents().toString())){
                        idComprobado5 = comprobarSiExisteConCB(codigoIniciado5);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado5, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result5.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado5, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado5!=result5.getContents()){
                        {
                            idComprobado5 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado5, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result5.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado5, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #6
        if(CB==6) {
            IntentResult result6 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result6 != null) {
                if (result6.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra6.setText(result6.getContents());
                    if(codigoIniciado6.equals(result6.getContents().toString())){
                        idComprobado6 = comprobarSiExisteConCB(codigoIniciado6);
                       /* Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado6, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result6.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado6, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado6!=result6.getContents()){
                        {
                            idComprobado6 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado6, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result6.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado6, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #7
        if(CB==7) {
            IntentResult result7 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result7 != null) {
                if (result7.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra7.setText(result7.getContents());
                    if(codigoIniciado7.equals(result7.getContents().toString())){
                        idComprobado7 = comprobarSiExisteConCB(codigoIniciado7);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado7, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result7.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado7, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado7!=result7.getContents()){
                        {
                            idComprobado7 = comprobarSiExisteConCB(null);
                            /*Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado7, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result7.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado7, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #8
        if(CB==8) {
            IntentResult result8 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result8 != null) {
                if (result8.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra8.setText(result8.getContents());
                    if(codigoIniciado8.equals(result8.getContents().toString())){
                        idComprobado8 = comprobarSiExisteConCB(codigoIniciado8);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado8, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result8.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado8, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado8!=result8.getContents()){
                        {
                            idComprobado8 = comprobarSiExisteConCB(null);
                           /* Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado8, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result8.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado8, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
        //CODIGO BARRA #9
        if(CB==9) {
            IntentResult result9 = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result9 != null) {
                if (result9.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {



                    txtCodigoBarra9.setText(result9.getContents());
                    if(codigoIniciado9.equals(result9.getContents().toString())){
                        idComprobado9 = comprobarSiExisteConCB(codigoIniciado9);
                        /*Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado9, Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "get contents: "+result9.getContents(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(this, "id despues: "+idComprobado9, Toast.LENGTH_SHORT).show();*/
                    }else if(codigoIniciado9!=result9.getContents()){
                        {
                            idComprobado9 = comprobarSiExisteConCB(null);
                          /*  Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "codigo iniciado despues: "+codigoIniciado9, Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "get contents: "+result9.getContents(), Toast.LENGTH_SHORT).show();
                            Toast.makeText(this, "id despues: "+idComprobado9, Toast.LENGTH_SHORT).show();*/
                        }

                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //resultado de permiso permitido / denegado
        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    //Si se permite devuelve verdadero de lo contrario falso
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;


                    if (cameraAccepted && storageAccepted) {
                        //ambos permisos permitidos
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Camera & Storage permissions are required", Toast.LENGTH_SHORT).show();
                    }
                }

            }


            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    //Si se permite devuelve verdadero de lo contrario falso
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        //almacenamiento permiso permitido
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Storage permissions are required....", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }
    }


    private void Limpiar() {

        txtNombre.setText("");
        txtPrecio.setText("");
        txtFecha.setText("");
        txtCodigo.setText("");
        txtCodigoBarra2.setText("");
        txtCodigoBarra3.setText("");
        txtCodigoBarra4.setText("");
        txtCodigoBarra5.setText("");
        txtCodigoBarra6.setText("");
        txtCodigoBarra7.setText("");
        txtCodigoBarra8.setText("");
        txtCodigoBarra9.setText("");
        img.setImageResource(R.drawable.ic_contacto);
    }



}
