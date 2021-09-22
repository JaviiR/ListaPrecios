package firebase.app.listaprecios;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import firebase.app.listaprecios.db.DbProducto;

public class IngresarProductos extends AppCompatActivity {

    EditText txtNombre,txtPrecio,txtFechaExp;
    Button btnAgregar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresar_productos);

        txtNombre=findViewById(R.id.txtNombre);
        txtPrecio=findViewById(R.id.txtPrecio);
        txtFechaExp=findViewById(R.id.txtFechaExp);
        btnAgregar=findViewById(R.id.btnAgregar);


        btnAgregar.setOnClickListener(new View.OnClickListener() {

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
        });
    }



    private void Limpiar(){
        txtNombre.setText("");
        txtPrecio.setText("");
        txtFechaExp.setText("");
    }
}