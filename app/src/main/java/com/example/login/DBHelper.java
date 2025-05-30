package com.example.login;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    private static final int DATABASE_VERSION = 2;
    public DBHelper(Context context) {
        super(context, DBNAME, null, DATABASE_VERSION); // Usar la nueva versión
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // Tabla de usuarios (personal)
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT)");
        // Nueva tabla de vehículos
        MyDB.execSQL("create Table vehicles(plate TEXT primary key, name TEXT, lastname TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        // Este método se llama cuando la versión de la base de datos cambia.
        // Si estás añadiendo una nueva tabla, debes manejar la migración.
        // Para simplicidad en este ejemplo, simplemente soltamos y recreamos las tablas.
        // En una aplicación real, deberías realizar migraciones que no borren los datos existentes.
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists vehicles"); // Dropear también la tabla de vehículos
        onCreate(MyDB); // Recrear todas las tablas
    }

    // Métodos para la tabla 'users' (personal) - (mantener los existentes)
    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1) return false;
        else            return true;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else            return false;
    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true;
        else            return false;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT username FROM users", null);
        return cursor;
    }

    public Boolean deleteUser(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        long result = MyDB.delete("users", "username = ?", new String[]{username});
        return result > 0;
    }

    // ============== NUEVOS MÉTODOS PARA LA TABLA 'vehicles' ==============

    public Boolean insertVehicle(String plate, String name, String lastname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("plate", plate);
        contentValues.put("name", name);
        contentValues.put("lastname", lastname);
        long result = MyDB.insert("vehicles", null, contentValues);
        if(result==-1) return false;
        else            return true;
    }

    public Cursor getAllVehicles() {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        // Seleccionar todas las columnas para mostrar en la lista
        Cursor cursor = MyDB.rawQuery("SELECT plate, name, lastname FROM vehicles", null);
        return cursor;
    }

    public Boolean checkVehicleByPlate(String plate) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM vehicles WHERE plate = ?", new String[]{plate});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        } else {
            cursor.close();
            return false;
        }
    }

    public Boolean updateVehicle(String oldPlate, String newPlate, String newName, String newLastname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("plate", newPlate);
        contentValues.put("name", newName);
        contentValues.put("lastname", newLastname);
        long result = MyDB.update("vehicles", contentValues, "plate = ?", new String[]{oldPlate});
        return result > 0;
    }

    public Boolean deleteVehicle(String plate) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        long result = MyDB.delete("vehicles", "plate = ?", new String[]{plate});
        return result > 0;
    }
}