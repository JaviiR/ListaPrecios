package firebase.app.listaprecios;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;

public class EditarProducto extends AppCompatActivity {
    EditText txtNombre,txtPrecio,txtFecha;
    Button btnEditar;
    boolean estado=false;
    Productos producto;
    int id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        txtNombre=findViewById(R.id.txtNombreEditar);
        txtPrecio=findViewById(R.id.txtPrecioEditar);
        txtFecha=findViewById(R.id.txtFechaExpEditar);
        btnEditar=findViewById(R.id.btnEditar);


        if(savedInstanceState==null){
            Bundle extras=getIntent().getExtras();
            if(extras==null){
                id = Integer.parseInt(null);
            }else{
                id =extras.getInt("ID");
            }
        }else{
            id=(int)savedInstanceState.getSerializable("ID");
        }

        final DbProducto dbproducto=new DbProducto(EditarProducto.this);
        producto=dbproducto.verProductos(id);

        if(producto!=null){
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(producto.getPrecio());
            txtFecha.setText(producto.getFecha_exp());

        }


        btnEditar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String nombre=txtNombre.getText().toString();
                String precio=txtPrecio.getText().toString();
                String fecha=txtFecha.getText().toString();
                if(!nombre.equals("") && !precio.equals("") && !fecha.equals("")){
                  estado=  dbproducto.editarProducto(id,nombre,precio,fecha);
                    if(estado){
                        Toast.makeText(EditarProducto.this,"PRODUCTO ACTUALIZADO",Toast.LENGTH_LONG).show();
                        verProducto();
                    }else{
                        Toast.makeText(EditarProducto.this,"ERROR:NO SE ACTUALIZÓ EL PRODUCTO ",Toast.LENGTH_LONG).show();
                    }
                }else if(txtNombre.getText().toString().equals("")){
                    Toast.makeText(EditarProducto.this,"FALTA NOMBRE DEL PRODUCTO",Toast.LENGTH_LONG).show();
                }else if(txtPrecio.getText().toString().equals("")){
                    Toast.makeText(EditarProducto.this,"FALTA PRECIO DEL PRODUCTO",Toast.LENGTH_LONG).show();
                }else if(txtFecha.getText().toString().equals("")){
                    Toast.makeText(EditarProducto.this,"ESCRIBIR NULL O ALGUNA FECHA EN FECHA DE EXP DEL PRODUCTO",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void verProducto(){
        Intent intent=new Intent(this,VerProducto.class);
        intent.putExtra("ID",id);
        startActivity(intent);
    }



}