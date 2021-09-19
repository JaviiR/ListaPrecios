package firebase.app.listaprecios.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {


    private static final int  DATABASE_VERSION=1;
    private static final String  DATABASE_NAME="Productos.s3db";
    public static final String TABLE_NAME="Lista";
    private static final String CrearLista="CREATE TABLE IF NOT EXISTS "+TABLE_NAME+"(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT not null," +
            "nombre TEXT not null," +
            "precio TEXT not null," +
            "fecha_exp TEXT)";




    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(CrearLista);




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL("DROP TABLE "+TABLE_NAME);
    onCreate(sqLiteDatabase);
    }
}
