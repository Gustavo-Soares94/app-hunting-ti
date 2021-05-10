package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editEmail;
    private EditText editSenha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        editEmail = findViewById(R.id.txt_email);
        editSenha = findViewById(R.id.txt_senha);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updtateUi(currentUser);
    }

    private void updtateUi(FirebaseUser user){

        if(user!=null){

        }
    }

    public void login(View view){

        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if(email.equals("")){
            editEmail.setError("Preencha este campo!");
            return;

        }
        if(senha.equals("")){
            editSenha.setError("Preencha este campo!");
            return;
        }

        mAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updtateUi(mAuth.getCurrentUser());
                }else{
                    Toast.makeText(MainActivity.this,"Usuário ou senha incorreta!", Toast.LENGTH_SHORT).show();
                    updtateUi(null);
                }
            }
        });
    }

    public void cadastro(View view){

        Intent i = new Intent(MainActivity.this, CadastroActivity.class);
        startActivity(i);
    }

}
