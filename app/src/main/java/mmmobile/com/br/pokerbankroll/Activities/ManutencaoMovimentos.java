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

import mmmobile.com.br.pokerbankroll.DataBase.tblMovimentos;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

public class ManutencaoMovimentos extends AppCompatActivity {

    private String mOperacao;

    private AdView adView;

    private EditText edtDtMovimento;
    private EditText edtVlrMovimento;
    private EditText edtTipoMovimento;
    private EditText edtDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manutencao_movimentos);

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

        assert mOperacao != null;
        if (mOperacao.matches("I"))
            setTitle(getString(R.string.i_001));

        if (mOperacao.matches("A")) {
            setTitle(getString(R.string.a_009));
        }

        edtDescricao = (EditText) findViewById(R.id.edtDescricao);
        edtVlrMovimento = (EditText) findViewById(R.id.edtVlrMovimento);

        edtDtMovimento = (EditText) findViewById(R.id.edtDtMovimento);
        edtDtMovimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int iAno = Calendar.getInstance().get(Calendar.YEAR);
                int iMes = Calendar.getInstance().get(Calendar.MONTH);
                int iDia = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                if (edtDtMovimento.getText().toString().trim().length() != 0) {
                    iDia = Integer.valueOf(edtDtMovimento.getText().toString().split("/")[0]);
                    iMes = Integer.valueOf(edtDtMovimento.getText().toString().split("/")[1]) - 1;
                    iAno = Integer.valueOf(edtDtMovimento.getText().toString().split("/")[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(ManutencaoMovimentos.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        edtDtMovimento.setText(Funcoes.dateFormat.format(newDate.getTime()));
                    }
                }, iAno, iMes, iDia);

                datePickerDialog.show();
            }
        });

        edtTipoMovimento = (EditText) findViewById(R.id.edtTipo);
        edtTipoMovimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                montaComboTipo();
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
        getMenuInflater().inflate(R.menu.mnu_manutencao_movimentos, menu);
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

            edtDtMovimento.setText(Funcoes.dateFormat.format(newDate.getTime()));
            edtVlrMovimento.setText("");
            edtTipoMovimento.setText(getString(R.string.e_004));
        }

        if (mOperacao.matches("A")) {

            edtDtMovimento.setText(Movimentos.mAdapter.mListMovimentos.get(Movimentos.mAdapter.mItemSelected).getDtMovimento());
            edtDescricao.setText(Movimentos.mAdapter.mListMovimentos.get(Movimentos.mAdapter.mItemSelected).getDescricao());
            edtTipoMovimento.setText(Movimentos.mAdapter.mListMovimentos.get(Movimentos.mAdapter.mItemSelected).getTipo());

            try {
                edtVlrMovimento.setText(String.valueOf(Movimentos.mAdapter.mListMovimentos.get(Movimentos.mAdapter.mItemSelected).getVlrMovimento()));
            } catch (Exception e) {
                edtVlrMovimento.setText("");
            }
        }
    }

    private void grava() {

        tblMovimentos record = new tblMovimentos();

        record.setDescricao(edtDescricao.getText().toString());
        record.setDtMovimento(edtDtMovimento.getText().toString());
        record.setTipo(edtTipoMovimento.getText().toString());

        try {
            record.setVlrMovimento(Double.parseDouble(edtVlrMovimento.getText().toString()));
        } catch (Exception e) {
            record.setVlrMovimento(0.0);
        }

        if (mOperacao.matches("I")) {
            Movimentos.mAdapter.insert(record);
        }

        if (mOperacao.matches("A")) {
            record.setIdMovimento(Movimentos.mAdapter.mListMovimentos.get(Movimentos.mAdapter.mItemSelected).getIdMovimento());
            Movimentos.mAdapter.update(record);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtDtMovimento.getWindowToken(), 0);

        finish();
    }

    private void montaComboTipo() {

        int iPosition;

        if (edtTipoMovimento.getText().toString().matches(getString(R.string.e_004)))
            iPosition = 0;
        else
            iPosition = 1;

        final String[] vOrdem = {
                getString(R.string.e_004),
                getString(R.string.s_005)
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ManutencaoMovimentos.this);
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
                edtTipoMovimento.setText(vOrdem[which]);
                dialog.dismiss();
            }
        });

        builder.show();
    }

}
