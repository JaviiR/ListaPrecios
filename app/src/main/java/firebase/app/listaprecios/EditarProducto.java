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
import android.provider.MediaStore;
import android.text.InputType;
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
import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;

public class EditarProducto extends AppCompatActivity {
    EditText txtNombre, txtPrecio, txtFecha;
    TextView txtCodigo;
    Button btnEditar;
    ImageView img;
    boolean estado = false;

    String id;

    FloatingActionButton fabeditar, fabeliminar;
    private DbHelper dbHelper;
    private String name, precio, fecha,codigo;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        txtNombre = findViewById(R.id.txtNombreEditar);
        txtPrecio = findViewById(R.id.txtPrecioEditar);
        txtFecha = findViewById(R.id.txtFechaExpEditar);
        txtCodigo=findViewById(R.id.txtCodigoBarraEditar);
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



txtCodigo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        IntentIntegrator integrador=new IntentIntegrator(EditarProducto.this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrador.setPrompt("Lector - CDP");
        integrador.setCameraId(0);
        integrador.setBeepEnabled(true);
        integrador.setBarcodeImageEnabled(true);
        integrador.initiateScan();
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
        //intentando capturar la img url

        String selectQuery="SELECT * FROM "+Constants.TABLE_NAME+" WHERE "+Constants.C_ID+" =\""+id+"\"";
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do {
                nombre = "" + cursor.getString(cursor.getColumnIndex(Constants.C_NAME));
                Stringimgg = "" + cursor.getString(cursor.getColumnIndex(Constants.C_IMAGE));
            }while (cursor.moveToNext());
        }

       /* final DbProducto dbproducto = new DbProducto(EditarProducto.this);
        producto = dbproducto.verProductos(id);

        if (producto != null) {
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(producto.getPrecio());
            txtFecha.setText(producto.getFecha_exp());

        }*/


       btnEditar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String nombre = txtNombre.getText().toString();
                String precio = txtPrecio.getText().toString();
                String fecha = txtFecha.getText().toString();

                if (!nombre.equals("") && !precio.equals("")) {
                    if (!fecha.isEmpty()) {
                        estado = updateData();
                        if (estado) {
                            Toast.makeText(EditarProducto.this, nombre+" ACTUALIZADO", Toast.LENGTH_LONG).show();
                            verProducto();
                        } else {
                            Toast.makeText(EditarProducto.this, "ERROR:NO SE ACTUALIZÓ EL PRODUCTO ", Toast.LENGTH_LONG).show();
                        }
                    } else if (fecha.isEmpty()) {
                        estado = updateData();
                        if (estado) {
                            Toast.makeText(EditarProducto.this, nombre+" ACTUALIZADO", Toast.LENGTH_LONG).show();
                            verProducto();
                        } else {
                            Toast.makeText(EditarProducto.this, "ERROR:NO SE ACTUALIZÓ EL PRODUCTO ", Toast.LENGTH_LONG).show();
                        }
                    }


                } else if (txtNombre.getText().toString().equals("")) {
                    Toast.makeText(EditarProducto.this, "FALTA NOMBRE DEL PRODUCTO", Toast.LENGTH_LONG).show();
                } else if (txtPrecio.getText().toString().equals("")) {
                    Toast.makeText(EditarProducto.this, "FALTA PRECIO DEL PRODUCTO", Toast.LENGTH_LONG).show();
                } else if (txtFecha.getText().toString().equals("")) {
                    Toast.makeText(EditarProducto.this, "ESCRIBIR NULL O ALGUNA FECHA EN FECHA DE EXP DEL PRODUCTO", Toast.LENGTH_LONG).show();
                }
            }
        });

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
        if(txtFecha.getText().toString().isEmpty()){
            fecha="NULL";
        }else {
            fecha = "" + txtFecha.getText().toString().trim();
        }
        codigo=""+txtCodigo.getText().toString().trim();
        //save to db
        if(imageUri!=null){
        String timestamp = "" + System.currentTimeMillis();
        dbHelper.updateRecord(
                ""+id,
                "" + name,
                "" + precio,
                "" + fecha,
                "" + imageUri,
                // "" + timestamp,
                // "" + timestamp
                ""+codigo
        );

        }else{
            String timestamp = "" + System.currentTimeMillis();
            dbHelper.updateRecord(
                    ""+id,
                    "" + name,
                    "" + precio,
                    "" + fecha,
                    "" + Stringimgg,
                    // "" + timestamp,
                    // "" + timestamp
                    ""+codigo
            );

        }
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



        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Lector Cancelado",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();


                txtCodigo.setText(result.getContents());
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
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
        img.setImageResource(R.drawable.ic_contacto);
    }




}