package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText editUsuario;
    private EditText editEmail;
    private EditText editSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mAuth = FirebaseAuth.getInstance();

        editUsuario = findViewById(R.id.cadUsuario);
        editEmail = findViewById(R.id.cadEmail);
        editSenha = findViewById(R.id.cadSenha);
    }

    public void salvar(View view){
        String usuario = editUsuario.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String senha = editSenha.getText().toString().trim();

        if(usuario.equals("")){
            editUsuario.setError("Preencha este campo!");
            editUsuario.requestFocus();
            return;
        }

        if(email.equals("")){
            editEmail.setError("Preencha este campo!");
            editEmail.requestFocus();
            return;
        }

        if(senha.equals("")){
            editSenha.setError("Preencha este campo!");
            editSenha.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference userRef = database.getReference("users/" + user.getUid());

                            Map<String, Object> userInfos = new HashMap<>();
                            userInfos.put("usuario", usuario);
                            userInfos.put("email", email);

                            userRef.setValue(userInfos);
                            finish();
                        }else{
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                editSenha.setError("Senha Fraca!");
                                editSenha.requestFocus();
                            }catch (FirebaseAuthInvalidCredentialsException e){
                                editEmail.setError("E-mail inválido!");
                                editEmail.requestFocus();
                            }catch(FirebaseAuthUserCollisionException e){
                                editEmail.setError("E-mail já existe");
                                editEmail.requestFocus();
                            }catch(Exception e){
                                Log.e("Cadastro", e.getMessage());
                            }
                        }
                    }
                });
    }

}