package mmmobile.com.br.pokerbankroll.Comparators;

import java.util.Comparator;

import mmmobile.com.br.pokerbankroll.DataBase.tblTorneios;

public class comTorneios implements Comparator<tblTorneios> {

    private final int mOrdem;

    public comTorneios(int ordem) {
        this.mOrdem = ordem;
    }

    @Override
    public int compare(tblTorneios lhs, tblTorneios rhs) {

        int iRetorno = 0;

        if (mOrdem == 0) {
            iRetorno = lhs.getDescricao().compareToIgnoreCase(rhs.getDescricao());
        }

        if (mOrdem == 1) {
            if ((lhs.getDtTorneioInv() < rhs.getDtTorneioInv())) iRetorno = -1;
            if ((lhs.getDtTorneioInv() > rhs.getDtTorneioInv())) iRetorno = 1;
            if ((lhs.getDtTorneioInv() == rhs.getDtTorneioInv())) iRetorno = 0;
        }

        if (mOrdem == 2) {
            iRetorno = lhs.getTipo().compareToIgnoreCase(rhs.getTipo());
        }

        if (mOrdem == 3) {
            iRetorno = lhs.getModo().compareToIgnoreCase(rhs.getModo());
        }

        if (mOrdem == 4) {
            if ((lhs.getPosicao() < rhs.getPosicao())) iRetorno = -1;
            if ((lhs.getPosicao() > rhs.getPosicao())) iRetorno = 1;
            if ((lhs.getPosicao() == rhs.getPosicao())) iRetorno = 0;
        }

        return iRetorno;
    }
}

