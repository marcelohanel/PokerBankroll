package mmmobile.com.br.pokerbankroll.Comparators;

import java.util.Comparator;

import mmmobile.com.br.pokerbankroll.DataBase.tblMovimentos;

@SuppressWarnings("NumberEquality")
public class comMovimentos implements Comparator<tblMovimentos> {

    private final int mOrdem;

    public comMovimentos(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblMovimentos lhs, tblMovimentos rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getDescricao().compareToIgnoreCase(rhs.getDescricao());
        }

        if (mOrdem == 1) {
            if ((lhs.getDtMovimentoInv() < rhs.getDtMovimentoInv())) iRetorno = -1;
            if ((lhs.getDtMovimentoInv() > rhs.getDtMovimentoInv())) iRetorno = 1;
            if ((lhs.getDtMovimentoInv() == rhs.getDtMovimentoInv())) iRetorno = 0;
        }

        if (mOrdem == 2) {
            if ((lhs.getVlrMovimento() < rhs.getVlrMovimento())) iRetorno = -1;
            if ((lhs.getVlrMovimento() > rhs.getVlrMovimento())) iRetorno = 1;
            if ((lhs.getVlrMovimento() == rhs.getVlrMovimento())) iRetorno = 0;
        }

        if (mOrdem == 3) {
            iRetorno = lhs.getTipo().compareToIgnoreCase(rhs.getTipo());
        }

        return iRetorno;
    }
}

