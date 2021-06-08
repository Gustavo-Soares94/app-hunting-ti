package com.gustavosoares.app_hunting_ti;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class ContatoAdapter extends RecyclerView.Adapter<ContatoAdapter.ContactViewHolder> {

    private List<ContatoInfo> listaContatos;

    ContatoAdapter(List<ContatoInfo> lista){
        listaContatos = lista;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.celula_contato, parent, false);
        return new ContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ContatoAdapter.ContactViewHolder holder, int position) {
        ContatoInfo c = listaContatos.get(position);
        holder.nome.setText(c.getNome());
        holder.cargo.setText(c.getCargo());
        holder.fone.setText(c.getFone());

        File imgFile = new File(c.getFoto());
        if(imgFile.exists()){
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.foto.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    static class ContactViewHolder extends RecyclerView.ViewHolder{

        ImageView foto;
        TextView nome;
        TextView cargo;
        TextView fone;

        ContactViewHolder(View v){
            super(v);
            foto = v.findViewById(R.id.imageFoto);
            nome = v.findViewById(R.id.textoNome);
            cargo = v.findViewById(R.id.textoCargo);
            fone = v.findViewById(R.id.textoFone);
        }



    }
}
