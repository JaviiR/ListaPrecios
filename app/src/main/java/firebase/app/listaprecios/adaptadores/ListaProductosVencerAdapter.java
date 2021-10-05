package firebase.app.listaprecios.adaptadores;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import firebase.app.listaprecios.R;
import firebase.app.listaprecios.VerProducto;
import firebase.app.listaprecios.entidades.Productos;

public class ListaProductosVencerAdapter extends RecyclerView.Adapter<ListaProductosVencerAdapter.ProductosViewHolder> {

    ArrayList<Productos> listaProductos;
    ArrayList<Productos> listaOriginal;
    Button btnVer;

    public ListaProductosVencerAdapter(ArrayList<Productos> listaProductos) {
        this.listaProductos = listaProductos;
        listaOriginal = new ArrayList<>();
        listaOriginal.addAll(listaProductos);
    }


    @NonNull
    @Override
    public ListaProductosVencerAdapter.ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_item_producto_vencer, null, false);

        return new ListaProductosVencerAdapter.ProductosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaProductosVencerAdapter.ProductosViewHolder holder, int position) {

        Productos model=listaProductos.get(position);
        String idd = model.getId();
        String name = model.getNombre();
        String precio = model.getPrecio();
        String fecha = model.getFecha_exp();
        String imagen = model.getImg();

        holder.viewNombre.setText(name);
        holder.viewPrecio.setText("S/" + precio);
        holder.viewFecha.setText(fecha);

        //si el usuario no adjunta la imagen, imageUri será nulo, así que configure una imagen predeterminada en ese caso
        if(imagen.equals("null")){
            //no hay imagen en el registro, mostrar por defecto
            holder.imgLista.setImageResource(R.drawable.ic_not_image);
        }else {
            //tener imagen en registro
            holder.imgLista.setImageURI(Uri.parse(imagen));
        }
        /*
        holder.viewNombre.setText(listaProductos.get(position).getNombre());
        holder.viewPrecio.setText("S/" + listaProductos.get(position).getPrecio());
        holder.viewFecha.setText(listaProductos.get(position).getFecha_exp());
        hoder.imgLista.setImageURI(Uri.parse(imagen));*/

    }

    public void filtrado(String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            listaProductos.clear();
            listaProductos.addAll(listaOriginal);
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<Productos> coleccion = listaOriginal.stream().filter(i -> i.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())).collect(Collectors.toList());
                listaProductos.clear();
                listaProductos.addAll(coleccion);

            } else {
                for (Productos p : listaOriginal) {
                    if (p.getNombre().toLowerCase().contains(txtBuscar.toLowerCase())) {
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

        TextView viewNombre, viewPrecio, viewFecha;
        ImageView imgLista;

        public ProductosViewHolder(@NonNull View itemView) {

            super(itemView);
            viewNombre = itemView.findViewById(R.id.viewNombreVencer);
            viewPrecio = itemView.findViewById(R.id.viewPrecioVencer);
            viewFecha = itemView.findViewById(R.id.viewFechaVencer);
            imgLista=itemView.findViewById(R.id.imageviewVencer);
            btnVer = itemView.findViewById(R.id.btnVerVencer);


            //lista_item_producto--->
            //Cuando das click en el boton ver de la lista_item_producto

            btnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, VerProducto.class);
                    intent.putExtra("RECORD_ID", listaProductos.get(getAdapterPosition()).getId());
                    // intent.putExtra("imagen_Main_a_ver",listaProductos.get(getAdapterPosition()).getImg());

                    context.startActivity(intent);
                }
            });


            //lista_item_producto--->
            //Cuando das click en el LinearLayout principal de la lista nombre:@+id/LinearPrincipal

            /*itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Context context=view.getContext();
            Intent  intent=new Intent(context, VerProducto.class);
            intent.putExtra("ID",listaProductos.get(getAdapterPosition()).getId());
            context.startActivity(intent);
            }
            });*/

        }
    }
   /* class HolderRecord extends RecyclerView.ViewHolder {
        //views
        ImageView profileIv;
        TextView nameTv, precioTv, fechaTv;
        Button btnVer;

        public HolderRecord(@NonNull View itemView) {
            super(itemView);

            // init views
            profileIv = itemView.findViewById(R.id.imageview);
            nameTv = itemView.findViewById(R.id.viewNombre);
            precioTv = itemView.findViewById(R.id.viewPrecio);
            fechaTv = itemView.findViewById(R.id.viewFecha);

            btnVer = itemView.findViewById(R.id.btnVer);


        }

    }*/

}