package firebase.app.listaprecios;

public class Constants {

    //bdname
    public static final String DB_NAME = "Productos.s3db";
    //db version
    public static final int DB_VERSION = 1;
    //table name
    public static final String TABLE_NAME = "Lista";
    //columns/fields of table
    public static final String C_ID = "id";
    public static final String C_NAME = "nombre";
    public static final String C_IMAGE = "image";
    public static final String C_FECHA = "fecha_exp";
    public static final String C_PRECIO = "precio";
    public static final String C_ADDED_TIMESTAMP = "ADDED_TIME_STAMP";
   public static final String C_UPDATED_TIMESTAMP = "UPDATE_TIME_STAMP";
   public static final String CODIGO_BARRA="codigo";
    public static final String CODIGO_BARRA_2="codigo_2";
    public static final String CODIGO_BARRA_3="codigo_3";
    public static final String CODIGO_BARRA_4="codigo_4";
    public static final String CODIGO_BARRA_5="codigo_5";
    public static final String CODIGO_BARRA_6="codigo_6";
    public static final String CODIGO_BARRA_7="codigo_7";
    public static final String CODIGO_BARRA_8="codigo_8";
    public static final String CODIGO_BARRA_9="codigo_9";
    //create table query
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_NAME + " varCHAR(40),"
            + C_PRECIO + " varCHAR(4),"
            + C_FECHA + " varCHAR(9),"
            + C_IMAGE + " TEXT,"
            + C_ADDED_TIMESTAMP + " TEXT,"
            + C_UPDATED_TIMESTAMP + " TEXT,"
            + CODIGO_BARRA + " TEXT,"
            + CODIGO_BARRA_2 + " TEXT,"
            + CODIGO_BARRA_3 + " TEXT,"
            + CODIGO_BARRA_4 + " TEXT,"
            + CODIGO_BARRA_5 + " TEXT,"
            + CODIGO_BARRA_6 + " TEXT,"
            + CODIGO_BARRA_7 + " TEXT,"
            + CODIGO_BARRA_8 + " TEXT,"
            + CODIGO_BARRA_9 + " TEXT"
            + ")";
}
