package mmmobile.com.br.pokerbankroll.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

public class tblTorneios {

    private final String mTable = "torneio";

    private int idTorneio = -1;
    private String dtTorneio = "";
    private String modo = "";
    private String tipo = "";
    private String descricao = "";
    private Double vlrBuy = 0.0;
    private int qtReBuy = 0;
    private Double vlrReBuy = 0.0;
    private Double vlrAddOn = 0.0;
    private int posicao = 0;
    private Double vlrPremiacao = 0.0;
    private int dtTorneioInv = -1;
    private int mes = -1;
    private int ano = -1;

    public int getIdTorneio() {
        return idTorneio;
    }

    public void setIdTorneio(int idTorneio) {
        this.idTorneio = idTorneio;
    }

    private void setIdTorneio() {
        SQLiteStatement sStatement = Funcoes.mDataBase.compileStatement("SELECT MAX(id_torneio) FROM " + mTable);
        this.idTorneio = (int) sStatement.simpleQueryForLong() + 1;
    }

    public String getDtTorneio() {
        return dtTorneio;
    }

    public void setDtTorneio(String dtTorneio) {

        String[] vData = Funcoes.getData(dtTorneio);

        this.dtTorneio = dtTorneio;
        this.ano = Integer.valueOf(vData[3]);
        this.mes = Integer.valueOf(vData[2]);
        this.dtTorneioInv = Integer.valueOf(vData[4]);
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getVlrBuy() {
        return vlrBuy;
    }

    public void setVlrBuy(Double vlrBuy) {
        this.vlrBuy = vlrBuy;
    }

    public int getQtReBuy() {
        return qtReBuy;
    }

    public void setQtReBuy(int qtReBuy) {
        this.qtReBuy = qtReBuy;
    }

    public Double getVlrReBuy() {
        return vlrReBuy;
    }

    public void setVlrReBuy(Double vlrReBuy) {
        this.vlrReBuy = vlrReBuy;
    }

    public Double getVlrAddOn() {
        return vlrAddOn;
    }

    public void setVlrAddOn(Double vlrAddOn) {
        this.vlrAddOn = vlrAddOn;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public Double getVlrPremiacao() {
        return vlrPremiacao;
    }

    public void setVlrPremiacao(Double vlrPremiacao) {
        this.vlrPremiacao = vlrPremiacao;
    }

    public int getDtTorneioInv() {
        return dtTorneioInv;
    }

    public String getDia() {
        String[] vData = Funcoes.getData(getDtTorneio());
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

        values.put("id_torneio", getIdTorneio());
        values.put("dt_torneio", getDtTorneio());
        values.put("modo", getModo());
        values.put("tipo", getTipo());
        values.put("descricao", getDescricao());
        values.put("vlr_buy", getVlrBuy());
        values.put("qt_rebuy", getQtReBuy());
        values.put("vlr_rebuy", getVlrReBuy());
        values.put("vlr_addon", getVlrAddOn());
        values.put("posicao", getPosicao());
        values.put("vlr_premiacao", getVlrPremiacao());
        values.put("dt_torneio_inv", getDtTorneioInv());
        values.put("ano", getAno());
        values.put("mes", getMes());

        return values;
    }

    public long insert() {

        setIdTorneio();

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
            return Funcoes.mDataBase.update(mTable, getValues(), "id_torneio = ?", new String[]{String.valueOf(getIdTorneio())});
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete() {

        try {
            return Funcoes.mDataBase.delete(mTable, "id_torneio = ?", new String[]{String.valueOf(getIdTorneio())});
        } catch (Exception e) {
            return -1;
        }
    }

    public Double getVlrInvestido(Context c, String mes, String ano) {

        try {
            Double dVlrInvestido = 0.0;

            Cursor cursor;

            if (mes.matches(c.getString(R.string.t_009)) && ano.matches(c.getString(R.string.t_009))) {
                cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);
            } else if (mes.matches(c.getString(R.string.t_009)) && !ano.matches(c.getString(R.string.t_009))) {
                cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "ano = ?", new String[]{String.valueOf(ano)}, null, null, null);
            } else if (!mes.matches(c.getString(R.string.t_009)) && ano.matches(c.getString(R.string.t_009))) {
                cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "mes = ?", new String[]{String.valueOf(mes)}, null, null, null);
            } else {
                cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, "mes = ? AND ano = ?", new String[]{String.valueOf(mes), String.valueOf(ano)}, null, null, null);
            }

            while (cursor.moveToNext()) {
                dVlrInvestido += cursor.getDouble(cursor.getColumnIndex("vlr_buy"));
                dVlrInvestido += (cursor.getInt(cursor.getColumnIndex("qt_rebuy")) * cursor.getDouble(cursor.getColumnIndex("vlr_rebuy")));
                dVlrInvestido += cursor.getDouble(cursor.getColumnIndex("vlr_addon"));
            }
            cursor.close();

            return dVlrInvestido;

        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getVlrPremiacaoTotal(Context c, String mes, String ano) {

        try {
            Double dAux = 0.0;
            Cursor cursorSum;

            if (mes.matches(c.getString(R.string.t_009)) && ano.matches(c.getString(R.string.t_009))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_premiacao) FROM torneio", null);
            } else if (mes.matches(c.getString(R.string.t_009)) && !ano.matches(c.getString(R.string.t_009))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_premiacao) FROM torneio WHERE ano = ?", new String[]{String.valueOf(ano)});
            } else if (!mes.matches(c.getString(R.string.t_009)) && ano.matches(c.getString(R.string.t_009))) {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_premiacao) FROM torneio WHERE mes = ?", new String[]{String.valueOf(mes)});
            } else {
                cursorSum = Funcoes.mDataBase.rawQuery("SELECT SUM(vlr_premiacao) FROM torneio WHERE mes = ? AND ano = ?", new String[]{String.valueOf(mes), String.valueOf(ano)});
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

    public List<tblTorneios> getList(Context c, String dia, String mes, String ano) {

        boolean lPode;

        List<tblTorneios> mList = new ArrayList<>();

        Cursor cursor;
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"*"}, null, null, null, null, null);

        while (cursor.moveToNext()) {

            lPode = true;

            tblTorneios record = new tblTorneios();

            record.setIdTorneio(cursor.getInt(cursor.getColumnIndex("id_torneio")));
            record.setDtTorneio(cursor.getString(cursor.getColumnIndex("dt_torneio")));
            record.setModo(cursor.getString(cursor.getColumnIndex("modo")));
            record.setTipo(cursor.getString(cursor.getColumnIndex("tipo")));
            record.setDescricao(cursor.getString(cursor.getColumnIndex("descricao")));
            record.setVlrBuy(cursor.getDouble(cursor.getColumnIndex("vlr_buy")));
            record.setQtReBuy(cursor.getInt(cursor.getColumnIndex("qt_rebuy")));
            record.setVlrReBuy(cursor.getDouble(cursor.getColumnIndex("vlr_rebuy")));
            record.setVlrAddOn(cursor.getDouble(cursor.getColumnIndex("vlr_addon")));
            record.setPosicao(cursor.getInt(cursor.getColumnIndex("posicao")));
            record.setVlrPremiacao(cursor.getDouble(cursor.getColumnIndex("vlr_premiacao")));

            String[] vData = Funcoes.getData(cursor.getString(cursor.getColumnIndex("dt_torneio")));

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
        cursor = Funcoes.mDataBase.query(mTable, new String[]{"dt_torneio"}, null, null, "dt_torneio", null, null);
        while (cursor.moveToNext()) {
            String[] vData = Funcoes.getData(cursor.getString(cursor.getColumnIndex("dt_torneio")));

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
