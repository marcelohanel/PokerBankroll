package mmmobile.com.br.pokerbankroll.Adapters;

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

import mmmobile.com.br.pokerbankroll.Comparators.comMovimentos;
import mmmobile.com.br.pokerbankroll.DataBase.tblMovimentos;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

@SuppressWarnings("NumberEquality")
public class adpMovimentos extends RecyclerView.Adapter<adpMovimentos.MyViewHolder> implements Filterable {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final RecyclerView mRecyclerView;
    public List<tblMovimentos> mListMovimentos;
    public int mItemSelected;
    public int mOrdem;
    private String mDia;
    private String mMes;
    private String mAno;
    private List<tblMovimentos> mListMovimentosFull;
    private Filter mFilter;

    public adpMovimentos(Context c, RecyclerView r, String dia, String mes, String ano) {

        this.mContext = c;
        this.mDia = dia;
        this.mMes = mes;
        this.mAno = ano;
        this.mItemSelected = -1;
        this.mOrdem = 1;
        this.mRecyclerView = r;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mListMovimentos = getDados();
        this.mListMovimentosFull = getDados();

        order();
    }

    public void setDiaMesAno(String dia, String mes, String ano) {
        this.mDia = dia;
        this.mMes = mes;
        this.mAno = ano;

        this.mListMovimentos = getDados();
        this.mListMovimentosFull = getDados();

        order();
    }

    private List<tblMovimentos> getDados() {

        List<tblMovimentos> mList;

        tblMovimentos record = new tblMovimentos();
        mList = record.getList(mContext, mDia, mMes, mAno);

        return mList;
    }

    public void order() {

        comMovimentos mComparator = new comMovimentos(mOrdem);

        Collections.sort(mListMovimentos, mComparator);
        Collections.sort(mListMovimentosFull, mComparator);

        notifyDataSetChanged();

        setSelected(-1);
    }

    private void setSelected(int position) {

        notifyItemChanged(mItemSelected);
        this.mItemSelected = position;
        notifyItemChanged(mItemSelected);

        mRecyclerView.scrollToPosition(mItemSelected);
    }

    public void insert(tblMovimentos movimento) {

        int iID;
        boolean lPode = true;

        if (movimento.insert() == -1) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_001),
                    mContext.getResources().getString(R.string.n_004),
                    mContext.getResources().getString(R.string.o_001)
            );

            return;
        }

        if (!mDia.matches(mContext.getString(R.string.t_009))) {
            if (Integer.valueOf(mDia) != Integer.valueOf(movimento.getDia())) {
                lPode = false;
            }
        }

        if (lPode) {
            if (!mMes.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mMes) != movimento.getMes()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            if (!mAno.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mAno) != movimento.getAno()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            iID = movimento.getIdMovimento();

            mListMovimentos.add(movimento);
            mListMovimentosFull.add(movimento);

            order();

            setSelectedByID(iID);
        }

        String sMessage = mContext.getString(R.string.m_004);
        sMessage = sMessage.replace("@1", movimento.getDtMovimento());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    public void update(tblMovimentos movimento) {

        int iID = movimento.getIdMovimento();
        boolean lPode = true;

        if (movimento.update() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_001),
                    mContext.getResources().getString(R.string.n_005),
                    mContext.getResources().getString(R.string.o_001)
            );

            return;
        }

        for (int i = 0; i <= mListMovimentosFull.size() - 1; i++) {
            if (mListMovimentosFull.get(i).getIdMovimento() == mListMovimentos.get(mItemSelected).getIdMovimento()) {
                mListMovimentosFull.remove(i);
                break;
            }
        }

        mListMovimentos.remove(mItemSelected);

        if (!mDia.matches(mContext.getString(R.string.t_009))) {
            if (Integer.valueOf(mDia) != Integer.valueOf(movimento.getDia())) {
                lPode = false;
            }
        }

        if (lPode) {
            if (!mMes.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mMes) != movimento.getMes()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            if (!mAno.matches(mContext.getString(R.string.t_009))) {
                if (Integer.valueOf(mAno) != movimento.getAno()) {
                    lPode = false;
                }
            }
        }

        if (lPode) {
            mListMovimentos.add(movimento);
            mListMovimentosFull.add(movimento);

            order();

            setSelectedByID(iID);
        } else {
            notifyItemRemoved(mItemSelected);
            setSelected(-1);
        }

        String sMessage = mContext.getString(R.string.m_005);
        sMessage = sMessage.replace("@1", movimento.getDtMovimento());

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    private void setSelectedByID(int id_movimento) {

        int iPosition = -1;

        for (int i = 0; i <= mListMovimentos.size() - 1; i++) {
            if (mListMovimentos.get(i).getIdMovimento() == id_movimento) {
                iPosition = i;
                break;
            }
        }

        setSelected(iPosition);
    }

    public void remove() {

        if (mItemSelected == -1)
            return;

        tblMovimentos record = new tblMovimentos();
        record.setIdMovimento(mListMovimentos.get(mItemSelected).getIdMovimento());

        if (record.delete() <= 0) {

            Funcoes.showMessage(
                    mContext,
                    mContext.getResources().getString(R.string.e_001),
                    mContext.getResources().getString(R.string.n_006),
                    mContext.getResources().getString(R.string.o_001)
            );

            return;
        }

        for (int i = 0; i <= mListMovimentosFull.size() - 1; i++) {
            if (mListMovimentosFull.get(i).getIdMovimento() == mListMovimentos.get(mItemSelected).getIdMovimento()) {
                mListMovimentosFull.remove(i);
                break;
            }
        }

        String sMessage = mContext.getString(R.string.m_006);
        sMessage = sMessage.replace("@1", mListMovimentos.get(mItemSelected).getDtMovimento());

        mListMovimentos.remove(mItemSelected);
        notifyItemRemoved(mItemSelected);

        setSelected(-1);

        Toast.makeText(mContext, sMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = mLayoutInflater.inflate(R.layout.list_movimentos, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

        myViewHolder.txtID.setText(String.valueOf(mListMovimentos.get(i).getIdMovimento()));

        myViewHolder.txtData.setText(mContext.getString(R.string.d_004) +
                mContext.getString(R.string._001) + " " +
                mListMovimentos.get(i).getDtMovimento());

        myViewHolder.txtTipo.setText(mContext.getString(R.string.t_002) +
                mContext.getString(R.string._001) + " " +
                mListMovimentos.get(i).getTipo());

        if (mListMovimentos.get(i).getVlrMovimento() == 0.0) {
            myViewHolder.txtValor.setVisibility(View.GONE);
        } else {
            myViewHolder.txtValor.setVisibility(View.VISIBLE);
            myViewHolder.txtValor.setText(mContext.getString(R.string.v_002) +
                    mContext.getString(R.string._001) + " " +
                    Funcoes.decimalFormat.format(mListMovimentos.get(i).getVlrMovimento()));
        }

        if (mListMovimentos.get(i).getDescricao().trim().length() == 0) {
            myViewHolder.txtDescricao.setVisibility(View.GONE);
        } else {
            myViewHolder.txtDescricao.setVisibility(View.VISIBLE);
            myViewHolder.txtDescricao.setText(mContext.getString(R.string.d_005) +
                    mContext.getString(R.string._001) + " " +
                    mListMovimentos.get(i).getDescricao());
        }

        if (mItemSelected == i)
            myViewHolder.itemView.setBackgroundColor(Funcoes.getColorWrapper(mContext, R.color.primary_light));
        else
            myViewHolder.itemView.setBackgroundColor(Funcoes.getColorWrapper(mContext, R.color.icons));
    }

    @Override
    public int getItemCount() {
        return mListMovimentos.size();
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
                List<tblMovimentos> list = new ArrayList<>(mListMovimentosFull);
                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<tblMovimentos> newValues = new ArrayList<>(mListMovimentosFull.size());

                for (int i = 0; i < mListMovimentosFull.size(); i++) {
                    if (mListMovimentosFull.get(i).getDescricao().toLowerCase().contains(constraintString.toLowerCase())) {
                        newValues.add(mListMovimentosFull.get(i));
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
                mListMovimentos = (ArrayList<tblMovimentos>) results.values;
            } else {
                mListMovimentos = new ArrayList<>();
            }

            notifyDataSetChanged();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public final TextView txtID;
        public final TextView txtData;
        public final TextView txtTipo;
        public final TextView txtValor;
        public final TextView txtDescricao;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtID = (TextView) itemView.findViewById(R.id.txtID);
            txtData = (TextView) itemView.findViewById(R.id.txtData);
            txtTipo = (TextView) itemView.findViewById(R.id.txtTipo);
            txtValor = (TextView) itemView.findViewById(R.id.txtValor);
            txtDescricao = (TextView) itemView.findViewById(R.id.txtDescricao);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(getAdapterPosition());
                }
            });
        }
    }
}
