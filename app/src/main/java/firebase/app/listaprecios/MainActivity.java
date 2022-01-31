package firebase.app.listaprecios;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import firebase.app.listaprecios.adaptadores.ListaProductosAdapter;
import firebase.app.listaprecios.db.DbHelper;
import firebase.app.listaprecios.db.DbProducto;
import firebase.app.listaprecios.entidades.Productos;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
        public final static int PERMISSION_REQUEST_CODE = 1;
    SearchView txtBuscar;

    RecyclerView listaProductos;
   /* ArrayList<Productos> listaArrayProductos;*/
    ListaProductosAdapter adapter;
    Button btnVerProdVencer;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        listaProductos = findViewById(R.id.listaProductos);
        txtBuscar = findViewById(R.id.txtBuscar);
        btnVerProdVencer = findViewById(R.id.btnPrCaducidad);
        listaProductos.setLayoutManager(new LinearLayoutManager(this));

        DbProducto dblista = new DbProducto(MainActivity.this);
        /*listaArrayProductos = new ArrayList<>();*/


        adapter = new ListaProductosAdapter(dblista.mostrarProductos());
        listaProductos.setAdapter(adapter);
//iniciar db helper class
        //dbhelper= new DbHelper(this);
        //loadrecords();
        txtBuscar.setOnQueryTextListener(this);

        //(3)
        String ProductoBorrado = getIntent().getStringExtra("nombre");
        if (ProductoBorrado != null) {
            Toast.makeText(this, ProductoBorrado + " ELIMINADO", Toast.LENGTH_LONG).show();
        }






        //(10)
        btnVerProdVencer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ver_productos_vencer.class);
                /*intent.putExtra("codigo","1");*/
                startActivity(intent);
            }
        });


    }

    /*private void loadrecords() {
        // si no compila cambiar el C_FECHA+"" por C_ADDED_TIMESTAMP+" DESC"
        AdapterRecord adapterRecord=new AdapterRecord(MainActivity.this,dbhelper.getAllRecords());

        listaProductos.setAdapter(adapterRecord);



    }*/

    /*@Override
    protected void onResume() {
        super.onResume();
        loadrecords();// refresh records list
    }*/







    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuNuevoConCB:
                nuevoRegistro();
                return true;
            case R.id.menuNuevo:
                nuevoRegistroSinCB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

//ingresar un producto verificando con el codigo de barras primero
    private void nuevoRegistro() {
        IntentIntegrator integrador=new IntentIntegrator(MainActivity.this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrador.setPrompt("Lector - CDP");
        integrador.setCameraId(0);
        integrador.setBeepEnabled(true);
        integrador.setBarcodeImageEnabled(true);
        integrador.initiateScan();


    }

//ingresar un producto sin codigo de barras
    private void nuevoRegistroSinCB() {
        Intent intent=new Intent(this,IngresarProductos.class);
        /*intent.putExtra("DESABILITAR","0");*/
        startActivity(intent);

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result= IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null){
            if(result.getContents()==null){
                Toast.makeText(this,"Lector Cancelado",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,result.getContents(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this,IngresarProductos.class);

                intent.putExtra("CODIGOBARRA",result.getContents());
                startActivity(intent);
            }
        }else {
            super.onActivityResult(requestCode, resultCode, data);
        }





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