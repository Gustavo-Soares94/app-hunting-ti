package com.gustavosoares.app_hunting_ti;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btLogin = (Button) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtLogin = (EditText) findViewById(R.id.txt_login);
                EditText txtSenha = (EditText) findViewById(R.id.txt_senha);
                String login = txtLogin.getText().toString();
                String senha = txtSenha.getText().toString();
                if (login.equals(senha)) {
                    Intent intent3 = new Intent(getApplicationContext(), MenuPrincipalActivity.class);
                    startActivity(intent3);
                } else{
                    alert("Nome e Senha devem ser iguais");
                }
            }
        });
    }

    public void irTelaPrincipal(View view){

        Intent intent3 = new Intent(getApplicationContext(), MenuPrincipalActivity.class);
        startActivity(intent3);
    }

    private void alert(String s){

        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

}