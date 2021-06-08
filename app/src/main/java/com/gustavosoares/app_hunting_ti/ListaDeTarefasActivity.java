package com.gustavosoares.app_hunting_ti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.gustavosoares.app_hunting_ti.db.TaskContract;
import com.gustavosoares.app_hunting_ti.db.TaskDBHelper;

public class ListaDeTarefasActivity extends AppCompatActivity {

    private TaskDBHelper helper;
    private ListView listaTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_tarefas);

        helper = new TaskDBHelper(this );
        listaTarefas = findViewById(R.id.listaTarefas);

        listaTarefas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textoTarefa = view.findViewById(R.id.textoTarefa);
                String tarefa = textoTarefa.getText().toString();

                String sql = String.format("DELETE FROM %s WHERE %s = '%s'", TaskContract.TABLE,
                        TaskContract.Columns.TAREFA, tarefa);

                SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();
                sqLiteDatabase.execSQL(sql);

                Toast.makeText(ListaDeTarefasActivity.this, "Tarefa excluida com sucesso!", Toast.LENGTH_LONG).show();
                updateUI();

                return false;
            }
        });

        final Button addTarefa = findViewById(R.id.addTarefa);
        addTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTarefa();
            }
        });

        updateUI();
    }

    private void addTarefa(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.alert_tarefa, null);

        final EditText textoTarefa = alertView.findViewById(R.id.textoTarefa);
        final EditText textoPrazo = alertView.findViewById(R.id.textoPrazo);

        builder.setView(alertView);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.clear();
                values.put(TaskContract.Columns.TAREFA, textoTarefa.getText().toString());
                values.put(TaskContract.Columns.PRAZO, textoPrazo.getText().toString());

                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                        SQLiteDatabase.CONFLICT_IGNORE);

                updateUI();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();

    }

    private void updateUI(){
        SQLiteDatabase sqLiteDatabase = helper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(TaskContract.TABLE, new String[]{TaskContract.Columns._ID,
                        TaskContract.Columns.TAREFA, TaskContract.Columns.PRAZO},
                null, null, null, null, null);

        SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(
                this,
                R.layout.celula_tarefa,
                cursor,
                new String[]{TaskContract.Columns.TAREFA, TaskContract.Columns.PRAZO},
                new int[]{R.id.textoTarefa, R.id.textoPrazo},
                0
        );

        listaTarefas.setAdapter(listAdapter);
    }
}