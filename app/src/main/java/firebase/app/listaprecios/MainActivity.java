package firebase.app.listaprecios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

import firebase.app.listaprecios.adaptadores.ListaProductosAdapter;
import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    SearchView txtBuscar;

    RecyclerView listaProductos;
    ArrayList<Productos> listaArrayProductos;
    ListaProductosAdapter adapter;
    Button btnVerProdVencer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listaProductos=findViewById(R.id.listaProductos);
        txtBuscar=findViewById(R.id.txtBuscar);
        btnVerProdVencer=findViewById(R.id.btnPrCaducidad);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));

        DbProducto dblista= new DbProducto(MainActivity.this);
        listaArrayProductos=new ArrayList<>();


        adapter=new ListaProductosAdapter(dblista.mostrarProductos());
        listaProductos.setAdapter(adapter);

        txtBuscar.setOnQueryTextListener(this);

        //(3)
        String ProductoBorrado=getIntent().getStringExtra("nombre");
        if(ProductoBorrado!=null){
            Toast.makeText(this,ProductoBorrado+" ELIMINADO",Toast.LENGTH_LONG).show();
        }



        //(6)
        btnVerProdVencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ver_productos_vencer.class);
                startActivity(intent);
            }
        });


    }



    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_principal,menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menuNuevo:
                nuevoRegistro();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    private void nuevoRegistro(){
        Intent intent= new Intent(this,IngresarProductos.class);
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        adapter.filtrado(s);

        return false;
    }
}