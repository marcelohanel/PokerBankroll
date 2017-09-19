package mmmobile.com.br.pokerbankroll.DataBase;

import android.content.Context;

import mmmobile.com.br.pokerbankroll.R;

public class tblBankroll {

    private final Context context;

    public tblBankroll(Context c) {
        this.context = c;
    }

    public Double getVlrEntrada(String mes, String ano) {

        try {
            Double dAux;

            tblMovimentos recMovimento = new tblMovimentos();

            dAux = recMovimento.getVlrMovimentoByTipo(context, mes, ano, context.getString(R.string.e_004));

            return dAux;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getVlrSaida(String mes, String ano) {

        try {
            Double dAux;

            tblMovimentos recMovimento = new tblMovimentos();

            dAux = recMovimento.getVlrMovimentoByTipo(context, mes, ano, context.getString(R.string.s_005));

            return dAux;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getVlrInvestido(String mes, String ano) {

        try {
            Double dAux = 0.0;

            //tblMovimentos recMovimento = new tblMovimentos();
            tblTorneios recTorneio = new tblTorneios();

            //dAux = dAux + recMovimento.getVlrMovimentoByTipo(context, mes, ano, context.getString(R.string.e_004));
            //dAux = dAux - recMovimento.getVlrMovimentoByTipo(context, mes, ano, context.getString(R.string.s_005));
            dAux = dAux + recTorneio.getVlrInvestido(context, mes, ano);

            return dAux;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Double getVlrGanho(String mes, String ano) {

        try {
            tblTorneios recTorneio = new tblTorneios();
            return recTorneio.getVlrPremiacaoTotal(context, mes, ano);
        } catch (Exception e) {
            return 0.0;
        }
    }

}
