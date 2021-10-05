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
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import firebase.app.listaprecios.db.DbHelper;
import firebase.app.listaprecios.db.DbProducto;

public class IngresarProductos extends AppCompatActivity {
    private ImageView imagen;
    private EditText txtNombre, txtPrecio, txtFechaExp;
    private Button btnAgregar;

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
    private String name, precio, fecha;

    //db helper
    private DbHelper dbHelper;
    //actionBar
    private ActionBar actionBar;


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

//init db helper
        dbHelper = new DbHelper(this);

        //matrices de permisos de inicio
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //haga clic en la vista de imagen para mostrar las imagenes galeria
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mostrar cuadro de diálogo de selección de imagen
                imagePickDialog();

            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {


                if (!txtNombre.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    if (!txtFechaExp.getText().toString().isEmpty()) {
                        long id = inputData();
                        if (id > 0) {
                            Toast.makeText(IngresarProductos.this, "PRODUCTO AGREGADO", Toast.LENGTH_LONG).show();
                            Limpiar();
                        } else {
                            Toast.makeText(IngresarProductos.this, "NO SE PUDO AGREGAR", Toast.LENGTH_LONG).show();
                        }
                    } else if (txtFechaExp.getText().toString().isEmpty()) {
                        long id = inputData();
                        if (id > 0) {
                            Toast.makeText(IngresarProductos.this, "PRODUCTO AGREGADO", Toast.LENGTH_LONG).show();
                            Limpiar();
                        } else {
                            Toast.makeText(IngresarProductos.this, "NO SE PUDO AGREGAR", Toast.LENGTH_LONG).show();
                        }
                    }
                } else if (txtNombre.getText().toString().isEmpty() && txtPrecio.getText().toString().isEmpty()) {
                    Toast.makeText(IngresarProductos.this, "FALTA NOMBRE Y PRECIO DEL PRODUCTO", Toast.LENGTH_LONG).show();

                } else if (txtNombre.getText().toString().isEmpty()) {
                    Toast.makeText(IngresarProductos.this, "FALTA NOMBRE DEL PRODUCTO", Toast.LENGTH_LONG).show();
                } else if (txtPrecio.getText().toString().isEmpty()) {
                    Toast.makeText(IngresarProductos.this, "FALTA PRECIO DEL PRODUCTO", Toast.LENGTH_LONG).show();
                }
            }
        });

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

        //save to db
        String timestamp = "" + System.currentTimeMillis();
        long id = dbHelper.insertRecord(
                "" + name,
                "" + precio,
                "" + fecha,
                "" + imageUri
               // "" + timestamp,
               // "" + timestamp
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


            //CRISTIAN henao
            //Uri path=data.getData();
            //imagen.setImageURI(path);
        }
        super.onActivityResult(requestCode, resultCode, data);

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