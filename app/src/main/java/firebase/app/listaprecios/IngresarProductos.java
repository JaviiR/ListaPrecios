package firebase.app.listaprecios;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Map;

import firebase.app.listaprecios.db.DbHelper;
import firebase.app.listaprecios.db.DbProducto;

public class IngresarProductos extends AppCompatActivity {
    private ImageView imagen;
    private EditText txtNombre, txtPrecio, txtFechaExp;
    private Button btnAgregar,btnBorrar1,btnBorrar2,btnBorrar3,btnBorrar4,btnBorrar5,btnBorrar6,btnBorrar7,btnBorrar8,btnBorrar9;
    private TextView txtCodigoBarra1,txtCodigoBarra2,txtCodigoBarra3,txtCodigoBarra4,txtCodigoBarra5,txtCodigoBarra6,txtCodigoBarra7,txtCodigoBarra8,txtCodigoBarra9;

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
    private String name, precio, fecha,codigo,codigo_2,codigo_3,codigo_4,codigo_5,codigo_6,codigo_7,codigo_8,codigo_9;
    int CB;

    //db helper
    private DbHelper dbHelper;
    //actionBar
    private ActionBar actionBar;
    String codigoBarra;
    String idComprobar,idComprobar2;
    String estado;
    HorizontalScrollView hr;
    ScrollView hr2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_productos);
        //init
        actionBar = getSupportActionBar();
        //tittle
        actionBar.setTitle("Add Record");
        //back button
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        imagen = findViewById(R.id.imgAgregar);
        txtNombre = findViewById(R.id.txtNombre);
        txtPrecio = findViewById(R.id.txtPrecio);
        txtFechaExp = findViewById(R.id.txtFechaExp);
        btnAgregar = findViewById(R.id.btnAgregar);
        txtCodigoBarra1=findViewById(R.id.txtCodigoBarra1);
        txtCodigoBarra2=findViewById(R.id.txtCodigoBarra2);
        txtCodigoBarra3=findViewById(R.id.txtCodigoBarra3);
        txtCodigoBarra4=findViewById(R.id.txtCodigoBarra4);
        txtCodigoBarra5=findViewById(R.id.txtCodigoBarra5);
        txtCodigoBarra6=findViewById(R.id.txtCodigoBarra6);
        txtCodigoBarra7=findViewById(R.id.txtCodigoBarra7);
        txtCodigoBarra8=findViewById(R.id.txtCodigoBarra8);
        txtCodigoBarra9=findViewById(R.id.txtCodigoBarra9);
        btnBorrar1=findViewById(R.id.btnBorrarCodigo1);
        btnBorrar2=findViewById(R.id.btnBorrarCodigo2);
        btnBorrar3=findViewById(R.id.btnBorrarCodigo3);
        btnBorrar4=findViewById(R.id.btnBorrarCodigo4);
        btnBorrar5=findViewById(R.id.btnBorrarCodigo5);
        btnBorrar6=findViewById(R.id.btnBorrarCodigo6);
        btnBorrar7=findViewById(R.id.btnBorrarCodigo7);
        btnBorrar8=findViewById(R.id.btnBorrarCodigo8);
        btnBorrar9=findViewById(R.id.btnBorrarCodigo9);
        hr=findViewById(R.id.SrIngresar);
        hr2=findViewById(R.id.SrIngresarGeneral);
//init db helper
        dbHelper = new DbHelper(this);

        //matrices de permisos de inicio
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                codigoBarra = "NULL";

                hr.setVisibility(View.INVISIBLE);
            } else {
                codigoBarra = extras.getString("CODIGOBARRA");
            }
        } else {
            codigoBarra =savedInstanceState.getSerializable("CODIGOBARRA").toString();
        }

        /*estado= getIntent().getStringExtra("DESABILITAR");

        if(estado.equals("0")){
            hr.setEnabled(false);
            hr.setVisibility(View.INVISIBLE);
        }*/





if(codigoBarra.equals("NULL")){
    txtCodigoBarra1.setHint("CODIGO BARRA #1");
}else {
    txtCodigoBarra1.setText(codigoBarra);
}


      /*  Toast.makeText(this,""+comprobarSiExiste(),Toast.LENGTH_LONG).show();*/

        //si el codigo de barras existe entonces mandar a la vista editar producto
if(comprobarSiExiste()!=null){
    Intent intent=new Intent(IngresarProductos.this,EditarProductoConCB.class);
    intent.putExtra("RECORD_ID",comprobarSiExiste());
    startActivity(intent);
}



        //haga clic en la vista de imagen para mostrar las imagenes galeria
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrar cuadro de diálogo de selección de imagen
                imagePickDialog();

            }
        });
        //CLICK AL CODIGO DE BARRA #1
        txtCodigoBarra1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
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
                IntentIntegrator integrador=new IntentIntegrator(IngresarProductos.this);
                integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrador.setPrompt("Lector - CDP");

                integrador.setCameraId(0);
                integrador.setBeepEnabled(true);
                integrador.setBarcodeImageEnabled(true);
                integrador.initiateScan();
                CB=9;
            }
        });


        btnAgregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                btnagregar();

            }
        });




        btnBorrar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra1.setText("");
                txtCodigoBarra1.setHint("CODIGO BARRA #1");
            }
        });

        btnBorrar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra2.setText("");
                txtCodigoBarra2.setHint("CODIGO BARRA #2");
            }
        });


        btnBorrar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra3.setText("");
                txtCodigoBarra3.setHint("CODIGO BARRA #3");
            }
        });
        btnBorrar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra4.setText("");
                txtCodigoBarra4.setHint("CODIGO BARRA #4");
            }
        });
        btnBorrar5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra5.setText("");
                txtCodigoBarra5.setHint("CODIGO BARRA #5");
            }
        });
        btnBorrar6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra6.setText("");
                txtCodigoBarra6.setHint("CODIGO BARRA #6");
            }
        });
        btnBorrar7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra7.setText("");
                txtCodigoBarra7.setHint("CODIGO BARRA #7");
            }
        });
        btnBorrar8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra8.setText("");
                txtCodigoBarra8.setHint("CODIGO BARRA #8");
            }
        });
        btnBorrar9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtCodigoBarra9.setText("");
                txtCodigoBarra9.setHint("CODIGO BARRA #9");
            }
        });

    }



    public void btnagregar(){
        String cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9;
        cb1=txtCodigoBarra1.getText().toString();
        cb2=txtCodigoBarra2.getText().toString();
        cb3=txtCodigoBarra3.getText().toString();
        cb4=txtCodigoBarra4.getText().toString();
        cb5=txtCodigoBarra5.getText().toString();
        cb6=txtCodigoBarra6.getText().toString();
        cb7=txtCodigoBarra7.getText().toString();
        cb8=txtCodigoBarra8.getText().toString();
        cb9=txtCodigoBarra9.getText().toString();
        String id1=comprobarSiExisteConCB(cb1);
        String id2=comprobarSiExisteConCB(cb2);
        String id3=comprobarSiExisteConCB(cb3);
        String id4=comprobarSiExisteConCB(cb4);
        String id5=comprobarSiExisteConCB(cb5);
        String id6=comprobarSiExisteConCB(cb6);
        String id7=comprobarSiExisteConCB(cb7);
        String id8=comprobarSiExisteConCB(cb8);
        String id9=comprobarSiExisteConCB(cb9);
        if(id1==null && id2==null && id3==null && id4==null && id5==null && id6==null && id7==null && id8==null && id9==null) {
            if (!txtNombre.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {


                    if(comprobarCodigoRepetidos()) {
                        Toast.makeText(IngresarProductos.this, "CODIGO DE BARRAS REPETIDO", Toast.LENGTH_LONG).show();
                    }else{
                        long id = inputData();
                        if (id > 0) {
                            Toast.makeText(IngresarProductos.this, txtNombre.getText().toString() + " AGREGADO", Toast.LENGTH_LONG).show();

                            Limpiar();
                        } else {
                            Toast.makeText(IngresarProductos.this, "CODIGO DE BARRAS YA EXISTE", Toast.LENGTH_LONG).show();
                        }
                    }

            } else if (txtNombre.getText().toString().isEmpty() && txtPrecio.getText().toString().isEmpty()) {
                Toast.makeText(IngresarProductos.this, "FALTA NOMBRE Y PRECIO DEL PRODUCTO", Toast.LENGTH_LONG).show();

            } else if (txtNombre.getText().toString().isEmpty()) {
                Toast.makeText(IngresarProductos.this, "FALTA NOMBRE DEL PRODUCTO", Toast.LENGTH_LONG).show();
            } else if (txtPrecio.getText().toString().isEmpty()) {
                Toast.makeText(IngresarProductos.this, "FALTA PRECIO DEL PRODUCTO", Toast.LENGTH_LONG).show();
            }
        }else
            Toast.makeText(IngresarProductos.this, "CODIGO DE BARRAS YA EXISTE", Toast.LENGTH_LONG).show();
        /*Toast.makeText(IngresarProductos.this, "ID 1: "+id1, Toast.LENGTH_LONG).show();
        Toast.makeText(IngresarProductos.this, "ID 2: "+id2, Toast.LENGTH_LONG).show();*/
    }





    public boolean comprobarCodigoRepetidos(){
        String cb1,cb2,cb3,cb4,cb5,cb6,cb7,cb8,cb9;
        cb1=txtCodigoBarra1.getText().toString();
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


    public String comprobarSiExiste(){

        String selectQuery="SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.CODIGO_BARRA+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_2+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_3+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_4+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_5+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_6+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_7+" =\""+codigoBarra+"\""+" or "+Constants.CODIGO_BARRA_8+" =\""+codigoBarra+"\""
                +" or "+Constants.CODIGO_BARRA_9+" =\""+codigoBarra+"\"";
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                //nombre = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                idComprobar = "" + cursor.getString(cursor.getColumnIndex(Constants.C_ID));
            }while (cursor.moveToNext());
        }
return idComprobar;
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


    private long inputData() {
        //get data
        name = "" + txtNombre.getText().toString().trim();
        precio = "" + txtPrecio.getText().toString().trim();
        if(txtFechaExp.getText().toString().isEmpty()){
         fecha="NULL";
        }else {
            fecha = "" + txtFechaExp.getText().toString().trim();
        }
        codigo=txtCodigoBarra1.getText().toString().trim();
        codigo_2=txtCodigoBarra2.getText().toString().trim();
        codigo_3=txtCodigoBarra3.getText().toString().trim();
        codigo_4=txtCodigoBarra4.getText().toString().trim();
        codigo_5=txtCodigoBarra5.getText().toString().trim();
        codigo_6=txtCodigoBarra6.getText().toString().trim();
        codigo_7=txtCodigoBarra7.getText().toString().trim();
        codigo_8=txtCodigoBarra8.getText().toString().trim();
        codigo_9=txtCodigoBarra9.getText().toString().trim();

        //save to db
        String timestamp = "" + System.currentTimeMillis();
        long id = dbHelper.insertRecord(
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
        return id;
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
                    imagen.setImageURI(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    //ERROR
                    Exception error = result.getError();
                    Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
                }
            }


        }
        super.onActivityResult(requestCode, resultCode, data);


        //CODIGO BARRA #1
        if(CB==1) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(this, "Lector Cancelado", Toast.LENGTH_LONG).show();
                } else {
                    /*Toast.makeText(this, "RESULT 1", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra1.setText(result.getContents());
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
                    /*Toast.makeText(this, "RESULT 2", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra2.setText(result2.getContents());
                }
            } else {
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
                   /* Toast.makeText(this, "RESULT 3", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra3.setText(result3.getContents());
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
                   /* Toast.makeText(this, "RESULT 4", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra4.setText(result4.getContents());
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
                    /*Toast.makeText(this, "RESULT 5", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra5.setText(result5.getContents());
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
                   /* Toast.makeText(this, "RESULT 6", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra6.setText(result6.getContents());
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
                    /*Toast.makeText(this, "RESULT 7", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra7.setText(result7.getContents());
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
                   /* Toast.makeText(this, "RESULT 8", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra8.setText(result8.getContents());
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
                   /* Toast.makeText(this, "RESULT 9", Toast.LENGTH_LONG).show();*/


                    txtCodigoBarra9.setText(result9.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }


//CRISTIAN henao
        //Uri path=data.getData();
        //imagen.setImageURI(path);









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
        txtFechaExp.setText("");
        txtCodigoBarra1.setText("");
        txtCodigoBarra2.setText("");
        txtCodigoBarra3.setText("");
        txtCodigoBarra4.setText("");
        txtCodigoBarra5.setText("");
        txtCodigoBarra6.setText("");
        txtCodigoBarra7.setText("");
        txtCodigoBarra8.setText("");
        txtCodigoBarra9.setText("");
        imagen.setImageResource(R.drawable.ic_add_image);
    }





       /* btnAgregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                DbProducto dbProducto =new DbProducto(IngresarProductos.this);


                if(!txtNombre.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    if(!txtFechaExp.getText().toString().isEmpty()){
                    long id = dbProducto.insertarProducto(txtNombre.getText().toString(), txtPrecio.getText().toString(), txtFechaExp.getText().toString());
                    if (id > 0) {
                        Toast.makeText(IngresarProductos.this, "PRODUCTO AGREGADO", Toast.LENGTH_LONG).show();
                        Limpiar();
                    } else {
                        Toast.makeText(IngresarProductos.this, "NO SE PUDO AGREGAR", Toast.LENGTH_LONG).show();
                    }}else if(txtFechaExp.getText().toString().isEmpty()){
                        long id = dbProducto.insertarProducto(txtNombre.getText().toString(), txtPrecio.getText().toString(),"NULL");
                        if (id > 0) {
                            Toast.makeText(IngresarProductos.this, "PRODUCTO AGREGADO", Toast.LENGTH_LONG).show();
                            Limpiar();
                        } else {
                            Toast.makeText(IngresarProductos.this, "NO SE PUDO AGREGAR", Toast.LENGTH_LONG).show();
                        }
                    }
                }else if(txtNombre.getText().toString().isEmpty() && txtPrecio.getText().toString().isEmpty()){
                    Toast.makeText(IngresarProductos.this,"FALTA NOMBRE Y PRECIO DEL PRODUCTO",Toast.LENGTH_LONG).show();

                }else if(txtNombre.getText().toString().isEmpty()){
                    Toast.makeText(IngresarProductos.this,"FALTA NOMBRE DEL PRODUCTO",Toast.LENGTH_LONG).show();
                }else if(txtPrecio.getText().toString().isEmpty()){
                    Toast.makeText(IngresarProductos.this,"FALTA PRECIO DEL PRODUCTO",Toast.LENGTH_LONG).show();
                }
            }
        });*/
}



   /* private void Limpiar(){
        txtNombre.setText("");
        txtPrecio.setText("");
        txtFechaExp.setText("");
    }
}*/