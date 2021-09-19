package firebase.app.listaprecios.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import firebase.app.listaprecios.VerProducto;
import firebase.app.listaprecios.R;
import firebase.app.listaprecios.entidades.Productos;

public class ListaProductosAdapter extends RecyclerView.Adapter<ListaProductosAdapter.ProductosViewHolder> {

    ArrayList<Productos> listaProductos;
    ArrayList<Productos> listaOriginal;

    public ListaProductosAdapter(ArrayList<Productos> listaProductos){
        this.listaProductos=listaProductos;
        listaOriginal=new ArrayList<>();
        listaOriginal.addAll(listaProductos);
    }




    @NonNull
    @Override
    public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_producto,null,false);

        return new ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
        holder.viewNombre.setText(listaProductos.get(position).getNombre());
        holder.viewPrecio.setText("S/."+listaProductos.get(position).getPrecio());
        holder.viewFecha.setText(listaProductos.get(position).getFecha_exp());
    }

    public void filtrado(String txtBuscar){
        int longitud=txtBuscar.length();
        if(longitud==0){
            listaProductos.clear();
            listaProductos.addAll(listaOriginal);
        }else{
            if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    List<Productos> coleccion = listaOriginal.stream().filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                listaProductos.clear();
                listaProductos.addAll(coleccion);
                
            }else{
                for (Productos p:listaOriginal) {
                    if(p.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())){
                        listaProductos.add(p);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return listaProductos.size();
    }

    public class ProductosViewHolder extends RecyclerView.ViewHolder {

        TextView viewNombre,viewPrecio,viewFecha;


        public ProductosViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNombre=itemView.findViewById(R.id.viewNombre);
            viewPrecio=itemView.findViewById(R.id.viewPrecio);
            viewFecha=itemView.findViewById(R.id.viewFecha);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context=view.getContext();
                    Intent  intent=new Intent(context, VerProducto.class);
                    intent.putExtra("ID",listaProductos.get(getAdapterPosition()).getId());
                    context.startActivity(intent);
                }
            });

        }
    }
}
