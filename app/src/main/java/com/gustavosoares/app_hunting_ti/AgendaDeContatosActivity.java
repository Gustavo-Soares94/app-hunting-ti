package com.gustavosoares.app_hunting_ti;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class AgendaDeContatosActivity extends AppCompatActivity {

    private final int REQUEST_NEW = 1;
    private final int REQUEST_ALTER = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_de_contatos);

    }

    public void addContato(View view){

        Intent icontato = new Intent(getApplicationContext(), ContatosActivity.class);
        icontato.putExtra("contato", new ContatoInfo());
        startActivityForResult(icontato,REQUEST_NEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW && resultCode == RESULT_OK) {

        } else if (requestCode == REQUEST_ALTER && resultCode == RESULT_OK) {

        }


    }
}