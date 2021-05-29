package com.gustavosoares.app_hunting_ti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO extends SQLiteOpenHelper {

    private static final int VERSAO = 1;
    private final String TABELA = "contatos";
    private static final String DATABASE = "DadosAgenda";

    public ContatoDAO(Context context) {
        super(context, DATABASE, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABELA
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "nome TEXT NOT NULL, "
                + "cargo TEXT, "
                + "email TEXT, "
                + "senioridade TEXT,"
                + "fone TEXT,"
                + "foto TEXT); ";

        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    public List<ContatoInfo> getList(String order){
        List<ContatoInfo> contatos = new ArrayList<>();

        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM" + TABELA + "ORDER BY nome " +
                order + ";", null);

        while(cursor.moveToNext()) {
            ContatoInfo c = new ContatoInfo();

            c.setId(cursor.getLong(cursor.getColumnIndex("id")));
            c.setNome(cursor.getString(cursor.getColumnIndex("nome")));
            c.setCargo(cursor.getString(cursor.getColumnIndex("cargo")));
            c.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            c.setSenioridade(cursor.getString(cursor.getColumnIndex("senioridade")));
            c.setFone(cursor.getString(cursor.getColumnIndex("fone")));
            c.setFoto(cursor.getString(cursor.getColumnIndex("foto")));

            contatos.add(c);
        }

        cursor.close();
        return  contatos;
    }

    public void inserirContato(ContatoInfo c){
        ContentValues values = new ContentValues();

        values.put("id", c.getId());
        values.put("nome", c.getNome());
        values.put("cargo", c.getCargo());
        values.put("email", c.getEmail());
        values.put("senioridade", c.getSenioridade());
        values.put("fone", c.getFone());
        values.put("foto", c.getFoto());

        String[] idParaSerAlterado = {c.getId().toString()};
        getWritableDatabase().update(TABELA, values, "id=?", idParaSerAlterado);

        getWritableDatabase().insert(TABELA, null, values);

    }

    public void alteraContato(ContatoInfo c){
        ContentValues values = new ContentValues();
    }

    public void apagarContato(ContatoInfo c){
        SQLiteDatabase db = getWritableDatabase();
        String[] args = {c.getId().toString()};
        db.delete(TABELA, "id=?", args);
    }
}
