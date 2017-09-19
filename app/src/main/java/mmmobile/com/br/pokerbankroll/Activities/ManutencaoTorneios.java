package mmmobile.com.br.pokerbankroll.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import mmmobile.com.br.pokerbankroll.DataBase.tblTorneios;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

public class ManutencaoTorneios extends AppCompatActivity {

    private String mOperacao;

    private AdView adView;

    private EditText edtDtTorneio;
    private EditText edtModo;
    private EditText edtTipo;
    private EditText edtDescricao;
    private EditText edtVlrBuy;
    private EditText edtQtReBuy;
    private EditText edtVlrReBuy;
    private EditText edtVlrAddOn;
    private EditText edtPosicao;
    private EditText edtVlrPremiacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao_torneios);

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(getString(R.string.admob_banner));

        LinearLayout layout = (LinearLayout) findViewById(R.id.banner_layout);
        if (layout != null) {
            try {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
                adView.setLayoutParams(layoutParams);
                layout.addView(adView);
                adView.loadAd(Funcoes.adRequest);
            } catch (Exception ignored) {
            }
        }

        mOperacao = getIntent().getExtras().getString("Operacao");

        if (mOperacao != null) {
            if (mOperacao.matches("I"))
                setTitle(getString(R.string.i_002));

            if (mOperacao.matches("A")) {
                setTitle(getString(R.string.a_010));
            }
        }

        edtDtTorneio = (EditText) findViewById(R.id.edtDtTorneio);
        edtModo = (EditText) findViewById(R.id.edtModo);
        edtTipo = (EditText) findViewById(R.id.edtTipo);
        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtVlrBuy = (EditText) findViewById(R.id.edtVlrBuy);
        edtQtReBuy = (EditText) findViewById(R.id.edtQtReBuy);
        edtVlrReBuy = (EditText) findViewById(R.id.edtVlrReBuy);
        edtVlrAddOn = (EditText) findViewById(R.id.edtVlrAddOn);
        edtPosicao = (EditText) findViewById(R.id.edtPosicao);
        edtVlrPremiacao = (EditText) findViewById(R.id.edtVlrPremiacao);

        edtDtTorneio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iAno = Calendar.getInstance().get(Calendar.YEAR);
                int iMes = Calendar.getInstance().get(Calendar.MONTH);
                int iDia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (edtDtTorneio.getText().toString().trim().length() != 0) {
                    iDia = Integer.valueOf(edtDtTorneio.getText().toString().split("/")[0]);
                    iMes = Integer.valueOf(edtDtTorneio.getText().toString().split("/")[1]) - 1;
                    iAno = Integer.valueOf(edtDtTorneio.getText().toString().split("/")[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(ManutencaoTorneios.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtTorneio.setText(Funcoes.dateFormat.format(newDate.getTime()));
                    }
                }, iAno, iMes, iDia);

                datePickerDialog.show();
            }
        });

        edtTipo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboTipo();
            }
        });

        edtModo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboModo();
            }
        });

        leitura();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adView != null) {
            adView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnu_manutencao_torneios, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.mnuSalvar) {
            grava();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void leitura() {

        if (mOperacao.matches("I")) {

            Calendar newDate = Calendar.getInstance();
            newDate.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            edtDtTorneio.setText(Funcoes.dateFormat.format(newDate.getTime()));
            edtModo.setText(getString(R.string.o_004));
            edtTipo.setText(getString(R.string.s_006));
            edtDescricao.setText("");
            edtVlrBuy.setText("");
            edtQtReBuy.setText("");
            edtVlrReBuy.setText("");
            edtVlrAddOn.setText("");
            edtPosicao.setText("");
            edtVlrPremiacao.setText("");
        }

        if (mOperacao.matches("A")) {

            edtDtTorneio.setText(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getDtTorneio());
            edtModo.setText(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getModo());
            edtTipo.setText(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getTipo());
            edtDescricao.setText(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getDescricao());

            try {
                edtVlrBuy.setText(String.valueOf(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getVlrBuy()));
            } catch (Exception e) {
                edtVlrBuy.setText("");
            }

            try {
                edtQtReBuy.setText(String.valueOf(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getQtReBuy()));
            } catch (Exception e) {
                edtQtReBuy.setText("");
            }

            try {
                edtVlrReBuy.setText(String.valueOf(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getVlrReBuy()));
            } catch (Exception e) {
                edtVlrReBuy.setText("");
            }

            try {
                edtVlrAddOn.setText(String.valueOf(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getVlrAddOn()));
            } catch (Exception e) {
                edtVlrAddOn.setText("");
            }

            try {
                edtPosicao.setText(String.valueOf(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getPosicao()));
            } catch (Exception e) {
                edtPosicao.setText("");
            }

            try {
                edtVlrPremiacao.setText(String.valueOf(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getVlrPremiacao()));
            } catch (Exception e) {
                edtVlrPremiacao.setText("");
            }
        }
    }

    private void grava() {

        tblTorneios record = new tblTorneios();

        record.setDescricao(edtDescricao.getText().toString());
        record.setDtTorneio(edtDtTorneio.getText().toString());
        record.setModo(edtModo.getText().toString());
        record.setTipo(edtTipo.getText().toString());

        try {
            record.setVlrBuy(Double.parseDouble(edtVlrBuy.getText().toString()));
        } catch (Exception e) {
            record.setVlrBuy(0.0);
        }

        try {
            record.setVlrReBuy(Double.parseDouble(edtVlrReBuy.getText().toString()));
        } catch (Exception e) {
            record.setVlrReBuy(0.0);
        }

        try {
            record.setVlrAddOn(Double.parseDouble(edtVlrAddOn.getText().toString()));
        } catch (Exception e) {
            record.setVlrAddOn(0.0);
        }

        try {
            record.setVlrPremiacao(Double.parseDouble(edtVlrPremiacao.getText().toString()));
        } catch (Exception e) {
            record.setVlrPremiacao(0.0);
        }

        try {
            record.setQtReBuy(Integer.parseInt(edtQtReBuy.getText().toString()));
        } catch (Exception e) {
            record.setQtReBuy(0);
        }

        try {
            record.setPosicao(Integer.parseInt(edtPosicao.getText().toString()));
        } catch (Exception e) {
            record.setPosicao(0);
        }

        if (mOperacao.matches("I")) {
            Torneios.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdTorneio(Torneios.mAdapter.mListTorneios.get(Torneios.mAdapter.mItemSelected).getIdTorneio());
            Torneios.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtDtTorneio.getWindowToken(), 0);

        finish();
    }

    private void montaComboTipo() {

        int iPosition = 0;

        if (edtTipo.getText().toString().matches(getString(R.string.c_007)))
            iPosition = 0;
        else if (edtTipo.getText().toString().matches(getString(R.string.m_011)))
            iPosition = 1;
        else if (edtTipo.getText().toString().matches(getString(R.string.s_006)))
            iPosition = 2;
        else if (edtTipo.getText().toString().matches(getString(R.string.s_008)))
            iPosition = 3;

        final String[] vOrdem = {
                getString(R.string.c_007),
                getString(R.string.m_011),
                getString(R.string.s_006),
                getString(R.string.s_008)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ManutencaoTorneios.this);
        builder.setTitle(R.string.t_003);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vOrdem, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtTipo.setText(vOrdem[which]);
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void montaComboModo() {

        int iPosition = 0;

        if (edtModo.getText().toString().matches(getString(R.string.o_004)))
            iPosition = 0;
        else if (edtModo.getText().toString().matches(getString(R.string.a_011)))
            iPosition = 1;

        final String[] vOrdem = {
                getString(R.string.o_004),
                getString(R.string.a_011)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ManutencaoTorneios.this);
        builder.setTitle(R.string.m_010);
        builder.setCancelable(false);
        builder.create();

        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setSingleChoiceItems(vOrdem, iPosition, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                edtModo.setText(vOrdem[which]);
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
