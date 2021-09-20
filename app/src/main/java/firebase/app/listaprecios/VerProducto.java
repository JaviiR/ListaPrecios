package firebase.app.listaprecios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;

public class VerProducto extends AppCompatActivity {
    EditText txtNombre,txtPrecio,txtFecha;
    Button btnEditar;
    Productos producto;
    int id=0;
    String  ProEliminado2;

    FloatingActionButton fabeditar,fabeliminar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_producto);
        txtNombre=findViewById(R.id.txtNombreEditar);
        txtPrecio=findViewById(R.id.txtPrecioEditar);
        txtFecha=findViewById(R.id.txtFechaExpEditar);
        btnEditar=findViewById(R.id.btnEditar);
        fabeditar=findViewById(R.id.fabEditar);
        fabeliminar=findViewById(R.id.fabEliminar);


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

        DbProducto dbproducto=new DbProducto(VerProducto.this);
        producto=dbproducto.verProductos(id);

        if(producto!=null){
            txtNombre.setText(producto.getNombre());
            txtPrecio.setText(producto.getPrecio());
            txtFecha.setText(producto.getFecha_exp());
            btnEditar.setVisibility(View.INVISIBLE);
            txtNombre.setInputType(InputType.TYPE_NULL);
            txtPrecio.setInputType(InputType.TYPE_NULL);
            txtFecha.setInputType(InputType.TYPE_NULL);
        }


        fabeditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VerProducto.this,EditarProducto.class);
                intent.putExtra("ID",id);
                startActivity(intent);

            }
        });

        String ProEliminado=txtNombre.getText().toString();
        ProEliminado2=ProEliminado;
        //(4)
        fabeliminar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerProducto.this);
                builder.setMessage("DESEAS ELIMINAR ESTE PRODUCTO?").setPositiveButton("SI", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        boolean estado=dbproducto.eliminarProducto(id);
                        if(estado){


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




private void VolverAvista(){
        Intent intent=new Intent(this,MainActivity.class);
        intent.putExtra("nombre",ProEliminado2);
        startActivity(intent);
}




}