package com.gustavosoares.app_hunting_ti;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import java.util.List;


public class AgendaDeContatosActivity extends AppCompatActivity {

    private ContatoDAO helper;
    private RecyclerView contatosRecy;
    private ContatoAdapter adapter;

    private List<ContatoInfo>listaContatos;

    private final int REQUEST_NEW = 1;
    private final int REQUEST_ALTER = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda_de_contatos);

        helper = new ContatoDAO(this);
        listaContatos = helper.getList("ASC");

        contatosRecy = findViewById(R.id.contatosRecy);
        contatosRecy.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        contatosRecy.setLayoutManager(llm);

        adapter = new ContatoAdapter(listaContatos);
        contatosRecy.setAdapter(adapter);

        //criar o click Listener

        contatosRecy.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(),
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        abrirOpcoes(listaContatos.get(position));
                    }
                }));

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

            ContatoInfo contatoInfo = data.getParcelableExtra("contato");
            helper.inserirContato(contatoInfo);
            listaContatos = helper.getList("ASC");
            adapter = new ContatoAdapter(listaContatos);
            contatosRecy.setAdapter(adapter);

        } else if (requestCode == REQUEST_ALTER && resultCode == RESULT_OK) {

            ContatoInfo contatoInfo = data.getParcelableExtra("contato");
            helper.alteraContato(contatoInfo);
            listaContatos = helper.getList("ASC");
            adapter = new ContatoAdapter(listaContatos);
            contatosRecy.setAdapter(adapter);
        }

    }

    private void abrirOpcoes(ContatoInfo contato){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(contato.getNome());
        builder.setItems(new CharSequence[]{"Ligar", "Editar", "Excluir"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + contato.getFone()));
                                startActivity(intent);
                                break;
                            case 1:
                                Intent intent1 = new Intent(AgendaDeContatosActivity.this, ContatosActivity.class);
                                intent1.putExtra("contato", contato);
                                startActivityForResult(intent1, REQUEST_ALTER);
                                break;
                            case 2:
                                listaContatos.remove(contato);
                                helper.apagarContato(contato);
                                adapter.notifyDataSetChanged();
                                break;
                        }
                    }
                });
        builder.create().show();
    }

}