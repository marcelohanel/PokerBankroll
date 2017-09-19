package mmmobile.com.br.pokerbankroll.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import mmmobile.com.br.pokerbankroll.Adapters.adpMovimentos;
import mmmobile.com.br.pokerbankroll.DataBase.tblMovimentos;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.Diversos.SpacesItemDecoration;
import mmmobile.com.br.pokerbankroll.R;

public class Movimentos extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static adpMovimentos mAdapter;

    private TextView edtDia;
    private TextView edtMes;
    private TextView edtAno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movimentos);

        edtDia = (TextView) findViewById(R.id.edtDia);
        edtMes = (TextView) findViewById(R.id.edtMes);
        edtAno = (TextView) findViewById(R.id.edtAno);

        String[] vData = Funcoes.getData(Funcoes.getDate());
        //edtDia.setText(getString(R.string.t_009));
        edtDia.setText(vData[6]);
        edtMes.setText(vData[7]);
        edtAno.setText(vData[8]);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rvMovimentos);
        if (mRecyclerView != null) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(Movimentos.this));
            mRecyclerView.addItemDecoration(new SpacesItemDecoration());
        }

        mAdapter = new adpMovimentos(
                Movimentos.this,
                mRecyclerView,
                edtDia.getText().toString(),
                edtMes.getText().toString(),
                edtAno.getText().toString());

        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(mAdapter);
        }

        ImageButton btnAdd = (ImageButton) findViewById(R.id.btnAdd);

        if (btnAdd != null) {
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Movimentos.this, ManutencaoMovimentos.class);
                    intent.putExtra("Operacao", "I");
                    startActivity(intent);
                }
            });
        }

        edtDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboDia();
            }
        });

        edtMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboMes();
            }
        });

        edtAno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboAno();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        if (mAdapter.mItemSelected != -1)
            mAdapter.notifyItemChanged(mAdapter.mItemSelected);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_movimentos, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.mnuPesquisar).getActionView();
        searchView.setQueryHint(getString(R.string.p_002));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                invalidateOptionsMenu();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mnuOrdem) {
            montaComboOrdem();
        }

        if (id == R.id.mnuAlterar) {

            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(Movimentos.this, getString(R.string.a_003), getString(R.string.e_003), getString(R.string.o_001));
            } else {
                Intent intent = new Intent(Movimentos.this, ManutencaoMovimentos.class);
                intent.putExtra("Operacao", "A");
                startActivity(intent);
            }
        }

        if (id == R.id.mnuExcluir) {

            if (mAdapter.mItemSelected == -1) {
                Funcoes.showMessage(Movimentos.this, getString(R.string.a_003), getString(R.string.e_003), getString(R.string.o_001));
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(Movimentos.this);

                builder.setTitle(getString(R.string.c_004));

                String sMessage = getString(R.string.d_006);
                sMessage = sMessage.replace("@1", mAdapter.mListMovimentos.get(mAdapter.mItemSelected).getDtMovimento());

                builder.setMessage(sMessage);
                builder.setCancelable(false);
                builder.setPositiveButton(getString(R.string.c_004),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAdapter.remove();
                                dialog.dismiss();
                            }
                        }
                );
                builder.setNegativeButton(getString(R.string.c_005),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );

                builder.create();
                builder.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void montaComboOrdem() {

        String[] vOrdem = {
                getString(R.string.d_005),
                getString(R.string.d_004),
                getString(R.string.v_002),
                getString(R.string.t_002)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Movimentos.this);
        builder.setTitle(R.string.o_003);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vOrdem, mAdapter.mOrdem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.mOrdem = which;
                mAdapter.order();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @SuppressLint("DefaultLocale")
    private void montaComboDia() {

        int iPosition = 0;

        List<String> mList;
        tblMovimentos record = new tblMovimentos();
        mList = record.getListDia();

        Collections.sort(mList);

        if (mList.size() == 0)
            return;

        mList.add(getString(R.string.t_009));

        final String[] vNome = new String[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            if (!mList.get(i).matches(getString(R.string.t_009))) {
                vNome[i] = String.format("%02d", Integer.valueOf(mList.get(i)));
            } else {
                vNome[i] = mList.get(i);
            }

            if (vNome[i].matches(edtDia.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Movimentos.this);
        builder.setTitle(R.string.d_009);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNome, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtDia.setText(vNome[which]);

                mAdapter.setDiaMesAno(
                        edtDia.getText().toString(),
                        edtMes.getText().toString(),
                        edtAno.getText().toString());

                dialog.dismiss();
            }
        });

        builder.show();
    }

    @SuppressLint("DefaultLocale")
    private void montaComboMes() {

        int iPosition = 0;

        List<String> mList;
        tblMovimentos record = new tblMovimentos();
        mList = record.getListMes();
        Collections.sort(mList);

        if (mList.size() == 0)
            return;

        mList.add(getString(R.string.t_009));

        final String[] vNome = new String[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            if (!mList.get(i).matches(getString(R.string.t_009))) {
                vNome[i] = String.format("%02d", Integer.valueOf(mList.get(i)));
            } else {
                vNome[i] = mList.get(i);
            }

            if (vNome[i].matches(edtMes.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Movimentos.this);
        builder.setTitle(R.string.m_008);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNome, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtMes.setText(vNome[which]);

                mAdapter.setDiaMesAno(
                        edtDia.getText().toString(),
                        edtMes.getText().toString(),
                        edtAno.getText().toString());

                dialog.dismiss();
            }
        });

        builder.show();
    }

    @SuppressLint("DefaultLocale")
    private void montaComboAno() {

        int iPosition = 0;

        List<String> mList;
        tblMovimentos record = new tblMovimentos();
        mList = record.getListAno();
        Collections.sort(mList);

        if (mList.size() == 0)
            return;

        mList.add(getString(R.string.t_009));

        final String[] vNome = new String[mList.size()];

        for (int i = 0; i <= mList.size() - 1; i++) {

            if (!mList.get(i).matches(getString(R.string.t_009))) {
                vNome[i] = String.format("%04d", Integer.valueOf(mList.get(i)));
            } else {
                vNome[i] = mList.get(i);
            }

            if (vNome[i].matches(edtAno.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Movimentos.this);
        builder.setTitle(R.string.a_008);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vNome, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                edtAno.setText(vNome[which]);

                mAdapter.setDiaMesAno(
                        edtDia.getText().toString(),
                        edtMes.getText().toString(),
                        edtAno.getText().toString());

                dialog.dismiss();
            }
        });

        builder.show();
    }
}
