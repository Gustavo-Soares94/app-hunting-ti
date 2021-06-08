package com.gustavosoares.app_hunting_ti;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.util.Calendar;

public class ContatosActivity extends AppCompatActivity {

    private ContatoInfo contato;

    private View layout;

    private ImageButton foto;
    private EditText nome;
    private EditText fone;
    private EditText email;
    private EditText cargo;
    private EditText senioridade;

    private Button salvar;

    private final int CAMERA = 1;
    private final int GALERIA =2;

    private final String IMAGE_DIR = "/FotosContatos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        contato = getIntent().getParcelableExtra("contato");

        layout = findViewById(R.id.agendaContatosLayout);

        foto = findViewById(R.id.fotoContato);
        nome = findViewById(R.id.nomeContato);
        fone = findViewById(R.id.foneContato);
        email = findViewById(R.id.emailContato);
        cargo = findViewById(R.id.cargoContato);
        senioridade = findViewById(R.id.senioridadeContato);

        nome.setText(contato.getNome());
        fone.setText(contato.getFone());
        email.setText(contato.getEmail());
        cargo.setText(contato.getCargo());
        senioridade.setText(contato.getSenioridade());

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertaImage();
            }
        });

        File imgFile = new File(contato.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            foto.setImageBitmap(bitmap);
        }

        salvar = findViewById(R.id.salvarContato);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contato.setNome(nome.getText().toString());
                contato.setFone(fone.getText().toString());
                contato.setEmail(email.getText().toString());
                contato.setCargo(cargo.getText().toString());
                contato.setSenioridade(senioridade.getText().toString());

                //obrigacao para o cadastro do none
                if(contato.getNome().equals("")){
                    Toast.makeText(ContatosActivity.this   ,"É Necessário um nome para salvar", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent i = new Intent();
                i.putExtra("contato", contato);
                setResult(RESULT_OK, i);
                finish();
            }
        });


    }

    private void alertaImage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione a fonte da imagem");
        builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clicarTirarFoto();
            }
        });
        builder.setNegativeButton("Galeria", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clicaCarregaImagem();
            }
        });

        builder.create().show();
    }

    private void clicarTirarFoto(){

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED){
            requestCameraPermission();
        }else {
            showCamera();
            }
    }
    private void requestCameraPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {

            Snackbar.make(layout, "É necessário permitir para utilizar a câmera!",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(ContatosActivity.this,
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            CAMERA);
                }
            }).show();

        }else{
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA);
        }
    }

    private void showCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, CAMERA);
    }

    private void clicaCarregaImagem() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestGaleriaPermisssion();
        } else {
            showGaleria();
        }
    }
        private void requestGaleriaPermisssion(){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Snackbar.make(layout, "É necessário permitir para utilizar a Galeria!",
                        Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActivityCompat.requestPermissions(ContatosActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                GALERIA);
                    }
                }).show();

            }else{
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        GALERIA);
            }

        }

        private void showGaleria(){
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALERIA);

        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull  String[] permissions, @NonNull  int[] grantResults) {
        switch (requestCode){
            case CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicarTirarFoto();
                }
                break;
            case GALERIA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    clicaCarregaImagem();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED || data == null) {
            return;
        }

        if (requestCode == GALERIA) {
            Uri contentURI = data.getData();

            try {
                //salvando imagem nos contatos
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                contato.setFoto(saveImage(bitmap));
                foto.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA) {
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            contato.setFoto(saveImage(bitmap));
            foto.setImageBitmap(bitmap);
        }
    }

    //validacao para diminuir a qualidade da imagem salva
    private String saveImage(Bitmap bitmap){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50 , bytes);
        File directory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIR);

        if(!directory.exists()){
            directory.mkdir();
        }
        try {
            //definindo nome da foto salva (data atual)
            File f = new File(directory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            //localizar foto na galeria
            MediaScannerConnection.scanFile(this, new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            return f.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}