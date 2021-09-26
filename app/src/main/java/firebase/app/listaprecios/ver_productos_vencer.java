package firebase.app.listaprecios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.SearchView;

import java.util.ArrayList;

import firebase.app.listaprecios.adaptadores.ListaProductosAdapter;
import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;
//(8)
public class ver_productos_vencer extends AppCompatActivity implements SearchView.OnQueryTextListener {
    SearchView txtBuscar;
    RecyclerView ListaProductosCaducos;
    ArrayList<Productos> listaArrayProductos;
    ListaProductosAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_productos_vencer);
        ListaProductosCaducos=findViewById(R.id.listaProductosavencer);
        txtBuscar=findViewById(R.id.txtBuscar);
        ListaProductosCaducos.setLayoutManager(new LinearLayoutManager(this));


        DbProducto dbproducto= new DbProducto(ver_productos_vencer.this);

        txtBuscar.setOnQueryTextListener(this);
        listaArrayProductos= new ArrayList<>();
        //(9)
         adapter= new ListaProductosAdapter(dbproducto.mostrarProductosaVencer());
        ListaProductosCaducos.setAdapter(adapter);


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