package com.gustavosoares.app_hunting_ti;

import android.os.Parcel;
import android.os.Parcelable;

public class ContatoInfo implements Parcelable {

    private String nome = "";
    private String cargo = "";
    private String fone = "";
    private String senioridade = "";
    private String email = "";
    private String foto = "";

    public ContatoInfo(){

    }

    private ContatoInfo(Parcel in){

        String[] data = new String[6];
        in.readStringArray(data);
        setNome(data[0]);
        setCargo(data[1]);
        setFone(data[2]);
        setFoto(data[3]);
        setCargo(data[4]);
        setSenioridade(data[5]);

    }


    public String getSenioridade() {
        return senioridade;
    }

    public void setSenioridade(String senirioridade) {
        this.senioridade = senirioridade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeStringArray(new String[]{
                getNome(), getCargo(), getFone(), getFoto(), getCargo(), getSenioridade()

        });

    }

    public static  final Parcelable.Creator<ContatoInfo> CREATOR = new Parcelable.Creator<ContatoInfo>(){

        @Override
        public ContatoInfo createFromParcel(Parcel parcel) {
            return new ContatoInfo(parcel);
        }

        @Override
        public ContatoInfo[] newArray(int i) {
            return new ContatoInfo[i];
        }
    };
}
