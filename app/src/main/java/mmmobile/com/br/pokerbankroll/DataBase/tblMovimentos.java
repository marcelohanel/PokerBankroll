package mmmobile.com.br.pokerbankroll.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

public class tblMovimentos {

    private final String mTable = "movimento";

    private int idMovimento = -1;
    private String dtMovimento = "";
    private String tipo = "";
    private Double vlrMovimento = 0.0;
    private String descricao = "";
    private int dtMovimentoInv = -1;
    private int mes = -1;
    private int ano = -1;

    public int getIdMovimento() {
        return idMovimento;
    }

    public void setIdMovimento(int idMovimento) {
        this.idMovimento = idMovimento;
    }

    private void setIdMovimento() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_movimento) FROM " + mTable);
        this.idMovimento = (int) sStatement.simpleQueryForLong() + 1;
    }

    public Double getVlrMovimento() {
        return vlrMovimento;
    }

    public void setVlrMovimento(Double vlrMovimento) {
        this.vlrMovimento = vlrMovimento;
    }

    public String getDtMovimento() {
        return dtMovimento;
    }

    public void setDtMovimento(String dtMovimento) {

        String[] vData = Funcoes.getData(dtMovimento);

        this.dtMovimento = dtMovimento;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
        this.dtMovimentoInv = Integer.valueOf(vData[4]);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDtMovimentoInv() {
        return dtMovimentoInv;
    }

    public String getDia() {
        String[] vData = Funcoes.getData(getDtMovimento());
        return vData[6];
    }

    public int getMes() {
        return mes;
    }

    public int getAno() {
        return ano;
    }

    private ContentValues getValues() {

        ContentValues values = new ContentValues();

        values.put("id_movimento", getIdMovimento());
        values.put("dt_movimento", getDtMovimento());
        values.put("tipo", getTipo());
        values.put("vlr_movimento", getVlrMovimento());
        values.put("descricao", getDescricao());
        values.put("dt_movimento_inv", getDtMovimentoInv());
        values.put("ano", getAno());
        values.put("mes", getMes());

        return values;
    }

    public long insert() {

        setIdMovimento();

        try {
            return Funcoes.mDataBase.insert(mTable, null, getValues());
        } catch (Exception e) {
            return -1;
        }
    }

    public void copy() {
        try {
            Funcoes.mDataBase.insert(mTable, null, getValues());
        } catch (Exception ignored) {
        }
    }

    public int update() {

        try {
            return Funcoes.mDataBase.update(mTable, getValues(), "id_movimento = ?", new String[]{String.valueOf(getIdMovimento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_movimento = ?", new String[]{String.valueOf(getIdMovimento())});
        } catch (Exception e) {
            return -1;
        }
    }

    public Double getVlrMovimentoByTipo(Context c, String mes, String ano, String tipo) {

        try {
            Double dAux = 0.0;
            Cursor cursorSum;

            if (mes.matches(c.getString(R.string.t_009)) && ano.matches(c.getString(R.string.t_009))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_movimento) FROM movimento WHERE tipo = ?", new String[]{String.valueOf(tipo)});
            } else if (mes.matches(c.getString(R.string.t_009)) && !ano.matches(c.getString(R.string.t_009))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_movimento) FROM movimento WHERE tipo = ? AND ano = ?", new String[]{String.valueOf(tipo), String.valueOf(ano)});
            } else if (!mes.matches(c.getString(R.string.t_009)) && ano.matches(c.getString(R.string.t_009))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_movimento) FROM movimento WHERE tipo = ? AND mes = ?", new String[]{String.valueOf(tipo), String.valueOf(mes)});
            } else {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_movimento) FROM movimento WHERE tipo = ? AND mes = ? AND ano = ?", new String[]{String.valueOf(tipo), String.valueOf(mes), String.valueOf(ano)});
            }

            if (cursorSum.getCount() > 0) {
                cursorSum.moveToFirst();
                dAux = cursorSum.getDouble(0);
            }
            cursorSum.close();

            return dAux;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<tblMovimentos> getList(Context c, String dia, String mes, String ano) {

        boolean lPode;

        List<tblMovimentos> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);

        while (cursor.moveToNext()) {

            lPode = true;

            tblMovimentos record = new tblMovimentos();

            record.setIdMovimento(cursor.getInt(cursor.getColumnIndex("id_movimento")));
            record.setDtMovimento(cursor.getString(cursor.getColumnIndex("dt_movimento")));
            record.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
            record.setVlrMovimento(cursor.getDouble(cursor.getColumnIndex("vlr_movimento")));
            record.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));

            String[] vData = Funcoes.getData(cursor.getString(cursor.getColumnIndex("dt_movimento")));

            if (!dia.matches(c.getString(R.string.t_009))) {
                if (!vData[6].matches(dia)) {
                    lPode = false;
                }
            }

            if (lPode) {
                if (!mes.matches(c.getString(R.string.t_009))) {
                    if (!vData[7].matches(mes)) {
                        lPode = false;
                    }
                }
            }

            if (lPode) {
                if (!ano.matches(c.getString(R.string.t_009))) {
                    if (!vData[8].matches(ano)) {
                        lPode = false;
                    }
                }
            }

            if (lPode) {
                mList.add(record);
            }
        }
        cursor.close();

        return mList;
    }

    public List<String> getListDia() {

        List<String> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"dt_movimento"}, null, null, "dt_movimento", null, null);
        while (cursor.moveToNext()) {
            String[] vData = Funcoes.getData(cursor.getString(cursor.getColumnIndex("dt_movimento")));

            Boolean lPode = true;
            for (int i = 0; i <= mList.size() - 1; i++) {
                if (mList.get(i).matches(vData[6])) {
                    lPode = false;
                    break;
                }
            }

            if (lPode) {
                mList.add(vData[6]);
            }
        }
        cursor.close();

        return mList;
    }

    public List<String> getListMes() {

        List<String> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"mes"}, null, null, "mes", null, null);
        while (cursor.moveToNext()) {
            mList.add(cursor.getString(cursor.getColumnIndex("mes")));
        }
        cursor.close();

        return mList;
    }

    public List<String> getListAno() {

        List<String> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"ano"}, null, null, "ano", null, null);
        while (cursor.moveToNext()) {
            mList.add(cursor.getString(cursor.getColumnIndex("ano")));
        }
        cursor.close();

        return mList;
    }
}
