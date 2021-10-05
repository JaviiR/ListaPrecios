package firebase.app.listaprecios.entidades;

public class Productos {

    private String id;
    private String nombre;
    private String precio;
    private String fecha_exp;
    private String img;

    public Productos() {
    }

    public Productos(String id, String nombre, String precio, String fecha_exp, String img) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.fecha_exp = fecha_exp;
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
