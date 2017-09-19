package mmmobile.com.br.pokerbankroll.DataBase;

import android.content.ContentValues;

import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;

public class tblConfig {

    private String chave = "";
    private String valor = "";

    private String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    private String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("chave", getChave());
        values.put("valor", getValor());

        return values;
    }

    public void copy() {
        try {
            String mTable = "config";
            Funcoes.mDataBase.insert(mTable, null, getValues());
        } catch (Exception ignored) {
        }
    }

}
