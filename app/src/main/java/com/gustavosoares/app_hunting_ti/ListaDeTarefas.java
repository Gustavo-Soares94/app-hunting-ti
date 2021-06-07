package com.gustavosoares.app_hunting_ti;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ListaDeTarefas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_de_tarefas);

        Button addTarefa = findViewById(R.id.addTarefa);
        addTarefa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTarefa();
            }
        });
    }

    private void addTarefa(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View alertView = getLayoutInflater().inflate(R.layout.alert_tarefa, null);

        EditText textTarefa = alertView.findViewById(R.id.textTarefa);
        EditText textPrazo = alertView.findViewById(R.id.textPrazo);

        builder.setView(alertView);

        builder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //salvar a tarefa
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }
}