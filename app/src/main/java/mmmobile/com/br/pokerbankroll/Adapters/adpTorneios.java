package mmmobile.com.br.pokerbankroll.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mmmobile.com.br.pokerbankroll.Comparators.comTorneios;
import mmmobile.com.br.pokerbankroll.DataBase.tblTorneios;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

@SuppressWarnings("NumberEquality")
public class adpTorneios extends RecyclerView.Adapter<adpTorneios.MyViewHolder> implements Filterable {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final RecyclerView mRecyclerView;
    public List<tblTorneios> mListTorneios;
    public int mItemSelected;
    public int mOrdem;
    private String mDia;
    private String mMes;
    private String mAno;
    private List<tblTorneios> mListTorneiosFull;
    private Filter mFilter;

    public adpTorneios(Context c, RecyclerView r, String dia, String mes, String ano) {

        this.mContext = c;
        this.mDia = dia;
        this.mMes = mes;
        this.mAno = ano;
        this.mItemSelected = -1;
        this.mOrdem = 1;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListTorneios = getDados();
        this.mListTorneiosFull = getDados();

        order();
    }

    public void setDiaMesAno(String dia, String mes, String ano) {
        this.mDia = dia;
        this.mMes = mes;
        this.mAno = ano;

        this.mListTorneios = getDados();
        this.mListTorneiosFull = getDados();

        order();
    }

    private List<tblTorneios> getDados() {

        List<tblTorneios> mList;

        tblTorneios record = new tblTorneios();
        mList = record.getList(mContext, mDia, mMes, mAno);

        return mList;
    }

    public void order() {

        comTorneios mComparator = new comTorneios(mOrdem);

        Collections.sort(mListTorneios, mComparator);
        Collections.sort(mListTorneiosFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblTorneios record) {

        int iID;
        boolean lPode = true;

        if (record.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_001),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_001)
            );

            return;
        }

        if (!mDia.matches(mContext.getString(R.string.t_009))) {
            if (Integer.valueOf(mDia) != Integer.valueOf(record.getDia())) {
                lPode = false;
            }
        }

        if (lPode) {
            if (!mMes.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mMes) != record.getMes()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            if (!mAno.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mAno) != record.getAno()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            iID = record.getIdTorneio();

            mListTorneios.add(record);
            mListTorneiosFull.add(record);

            order();

            setSelectedByID(iID);
        }

        String sMessage = mContext.getString(R.string.t_004);
        sMessage = sMessage.replace("@1", record.getDtTorneio());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblTorneios record) {

        int iID = record.getIdTorneio();
        boolean lPode = true;

        if (record.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_001),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_001)
            );

            return;
        }

        for (int i = 0; i <= mListTorneiosFull.size() - 1; i++) {
            if (mListTorneiosFull.get(i).getIdTorneio() == mListTorneios.get(mItemSelected).getIdTorneio()) {
                mListTorneiosFull.remove(i);
                break;
            }
        }

        mListTorneios.remove(mItemSelected);

        if (!mDia.matches(mContext.getString(R.string.t_009))) {
            if (Integer.valueOf(mDia) != Integer.valueOf(record.getDia())) {
                lPode = false;
            }
        }

        if (lPode) {
            if (!mMes.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mMes) != record.getMes()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            if (!mAno.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mAno) != record.getAno()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            mListTorneios.add(record);
            mListTorneiosFull.add(record);

            order();

            setSelectedByID(iID);
        } else {
            notifyItemRemoved(mItemSelected);
            setSelected(-1);
        }

        String sMessage = mContext.getString(R.string.t_005);
        sMessage = sMessage.replace("@1", record.getDtTorneio());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_torneio) {

        int iPosition = -1;

        for (int i = 0; i <= mListTorneios.size() - 1; i++) {
            if (mListTorneios.get(i).getIdTorneio() == id_torneio) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblTorneios record = new tblTorneios();
        record.setIdTorneio(mListTorneios.get(mItemSelected).getIdTorneio());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_001),
                    mContext.getResources().getString(R.string.n_006),
                    mContext.getResources().getString(R.string.o_001)
            );

            return;
        }

        for (int i = 0; i <= mListTorneiosFull.size() - 1; i++) {
            if (mListTorneiosFull.get(i).getIdTorneio() == mListTorneios.get(mItemSelected).getIdTorneio()) {
                mListTorneiosFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.t_006);
        sMessage = sMessage.replace("@1", mListTorneios.get(mItemSelected).getDtTorneio());

        mListTorneios.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_torneios, viewGroup, false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        Double dInvestido;
        Double dGanho;
        Double dSaldo;
        Double dROI;

        dInvestido = (mListTorneios.get(i).getVlrBuy() + mListTorneios.get(i).getVlrAddOn()) + (mListTorneios.get(i).getQtReBuy() * mListTorneios.get(i).getVlrReBuy());
        dGanho = mListTorneios.get(i).getVlrPremiacao();
        dSaldo = dGanho - dInvestido;
        dROI = dInvestido > 0 ? dSaldo * 100 / dInvestido : 0.0;

        myViewHolder.txtID.setText(String.valueOf(mListTorneios.get(i).getIdTorneio()));

        myViewHolder.txtData.setText(mContext.getString(R.string.d_004) +
                mContext.getString(R.string._001) + " " +
                mListTorneios.get(i).getDtTorneio());

        myViewHolder.txtModo.setText(mContext.getString(R.string.m_010) +
                mContext.getString(R.string._001) + " " +
                mListTorneios.get(i).getModo());

        myViewHolder.txtTipo.setText(mContext.getString(R.string.t_002) +
                mContext.getString(R.string._001) + " " +
                mListTorneios.get(i).getTipo());

        if (mListTorneios.get(i).getDescricao().trim().length() == 0) {
            myViewHolder.txtDescricao.setVisibility(View.GONE);
        } else {
            myViewHolder.txtDescricao.setVisibility(View.VISIBLE);
            myViewHolder.txtDescricao.setText(mContext.getString(R.string.d_005) +
                    mContext.getString(R.string._001) + " " +
                    mListTorneios.get(i).getDescricao());
        }

        if (mListTorneios.get(i).getVlrBuy() == 0.0) {
            myViewHolder.txtVlrBuy.setVisibility(View.GONE);
        } else {
            myViewHolder.txtVlrBuy.setVisibility(View.VISIBLE);
            myViewHolder.txtVlrBuy.setText(mContext.getString(R.string.v_003) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.decimalFormat.format(mListTorneios.get(i).getVlrBuy()));
        }

        if (mListTorneios.get(i).getQtReBuy() == 0) {
            myViewHolder.txtQtReBuy.setVisibility(View.GONE);
        } else {
            myViewHolder.txtQtReBuy.setVisibility(View.VISIBLE);
            myViewHolder.txtQtReBuy.setText(mContext.getString(R.string.q_001) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.integerFormat.format(mListTorneios.get(i).getQtReBuy()));
        }

        if (mListTorneios.get(i).getVlrReBuy() == 0.0) {
            myViewHolder.txtVlrReBuy.setVisibility(View.GONE);
        } else {
            myViewHolder.txtVlrReBuy.setVisibility(View.VISIBLE);
            myViewHolder.txtVlrReBuy.setText(mContext.getString(R.string.v_004) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.decimalFormat.format(mListTorneios.get(i).getVlrReBuy()));
        }

        if (mListTorneios.get(i).getVlrAddOn() == 0.0) {
            myViewHolder.txtAddOn.setVisibility(View.GONE);
        } else {
            myViewHolder.txtAddOn.setVisibility(View.VISIBLE);
            myViewHolder.txtAddOn.setText(mContext.getString(R.string.v_005) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.decimalFormat.format(mListTorneios.get(i).getVlrAddOn()));
        }

        if (mListTorneios.get(i).getPosicao() == 0) {
            myViewHolder.txtPosicao.setVisibility(View.GONE);
        } else {
            myViewHolder.txtPosicao.setVisibility(View.VISIBLE);
            myViewHolder.txtPosicao.setText(mContext.getString(R.string.p_003) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.integerFormat.format(mListTorneios.get(i).getPosicao()));
        }

        if (mListTorneios.get(i).getVlrPremiacao() == 0.0) {
            myViewHolder.txtVlrPremiacao.setVisibility(View.GONE);
        } else {
            myViewHolder.txtVlrPremiacao.setVisibility(View.VISIBLE);
            myViewHolder.txtVlrPremiacao.setText(mContext.getString(R.string.v_006) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.decimalFormat.format(mListTorneios.get(i).getVlrPremiacao()));
        }

        if (dSaldo == 0.0) {
            myViewHolder.txtVlrSaldo.setVisibility(View.GONE);
        } else {
            myViewHolder.txtVlrSaldo.setVisibility(View.VISIBLE);
            myViewHolder.txtVlrSaldo.setText(Funcoes.decimalFormat.format(dSaldo));
        }

        if (dROI == 0.0) {
            myViewHolder.txtROI.setVisibility(View.GONE);
        } else {
            myViewHolder.txtROI.setVisibility(View.VISIBLE);
            myViewHolder.txtROI.setText(Funcoes.decimalFormat.format(dROI) + mContext.getString(R.string._002));
        }

        if (dSaldo < 0)
            myViewHolder.txtVlrSaldo.setTextColor(Funcoes.getColorWrapper(mContext, R.color.red));
        else
            myViewHolder.txtVlrSaldo.setTextColor(Funcoes.getColorWrapper(mContext, R.color.green));

        if (dROI < 0)
            myViewHolder.txtROI.setTextColor(Funcoes.getColorWrapper(mContext, R.color.red));
        else
            myViewHolder.txtROI.setTextColor(Funcoes.getColorWrapper(mContext, R.color.green));

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(Funcoes.getColorWrapper(mContext, R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(Funcoes.getColorWrapper(mContext, R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListTorneios.size();
    }

    @Override
    public Filter getFilter() {

        if (mFilter == null)
            mFilter = new DomainFilter();

        setSelected(-1);

        return mFilter;
    }

    @SuppressWarnings("unchecked")
    private class DomainFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();
            String constraintString = (constraint + "").toLowerCase();

            if (constraint == null || constraint.length() == 0) {
                List<tblTorneios> list = new ArrayList<>(mListTorneiosFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblTorneios> newValues = new ArrayList<>(mListTorneiosFull.size());

                for (int i = 0; i < mListTorneiosFull.size(); i++) {
                    if (mListTorneiosFull.get(i).getDescricao().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListTorneiosFull.get(i));
                    }
                }

                results.count = newValues.size();
                results.values = newValues;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if (results.values != null) {
                mListTorneios = (ArrayList<tblTorneios>) results.values;
            } else {
                mListTorneios = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtID;
        public final TextView txtData;
        public final TextView txtModo;
        public final TextView txtTipo;
        public final TextView txtDescricao;
        public final TextView txtVlrBuy;
        public final TextView txtQtReBuy;
        public final TextView txtVlrReBuy;
        public final TextView txtAddOn;
        public final TextView txtPosicao;
        public final TextView txtVlrPremiacao;
        public final TextView txtVlrSaldo;
        public final TextView txtROI;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtModo = (TextView) itemView.findViewById(R.id.txtModo);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
            txtDescricao = (TextView) itemView.findViewById(R.id.txtDescricao);
            txtVlrBuy = (TextView) itemView.findViewById(R.id.txtVlrBuy);
            txtQtReBuy = (TextView) itemView.findViewById(R.id.txtQtReBuy);
            txtVlrReBuy = (TextView) itemView.findViewById(R.id.txtVlrReBuy);
            txtAddOn = (TextView) itemView.findViewById(R.id.txtAddOn);
            txtPosicao = (TextView) itemView.findViewById(R.id.txtPosicao);
            txtVlrPremiacao = (TextView) itemView.findViewById(R.id.txtVlrPremiacao);
            txtVlrSaldo = (TextView) itemView.findViewById(R.id.txtVlrSaldo);
            txtROI = (TextView) itemView.findViewById(R.id.txtROI);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
