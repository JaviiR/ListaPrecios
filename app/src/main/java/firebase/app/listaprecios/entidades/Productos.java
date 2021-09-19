package firebase.app.listaprecios.entidades;

public class Productos {

    private int id;
    private String nombre;
    private String precio;
    private String fecha_exp;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getFecha_exp() {
        return fecha_exp;
    }

    public void setFecha_exp(String fecha_exp) {
        this.fecha_exp = fecha_exp;
    }
}
