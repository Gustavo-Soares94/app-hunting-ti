package com.gustavosoares.app_hunting_ti.db;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlQuerry = String.format("CREATE TABLE %s (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "%s TEXT, %s TEXT)", TaskContract.TABLE, TaskContract.Columns.TAREFA, TaskContract.Columns.PRAZO);
        sqLiteDatabase.execSQL(sqlQuerry);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
    }

}
