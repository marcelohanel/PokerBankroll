package mmmobile.com.br.pokerbankroll.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mmmobile.com.br.pokerbankroll.Adapters.adpDrawerPrincipal;
import mmmobile.com.br.pokerbankroll.DataBase.tblBankroll;
import mmmobile.com.br.pokerbankroll.DataBase.tblMovimentos;
import mmmobile.com.br.pokerbankroll.DataBase.tblTorneios;
import mmmobile.com.br.pokerbankroll.Diversos.DrawerPrincipal;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.R;

public class Principal extends AppCompatActivity {

    private AdView adView;
    private DrawerLayout mDrawerLayout;

    private TextView edtMes;
    private TextView edtAno;

    private TextView txtInvestido;
    private TextView txtGanho;
    private TextView txtSaldo;
    private TextView txtROI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        setTitle(getString(R.string.p_001));

        Funcoes.adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        adView = new AdView(Principal.this);
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

        Funcoes.openDataBase(Principal.this);

        List<DrawerPrincipal> mList = new ArrayList<>();
        mList.add(new DrawerPrincipal(getString(R.string.t_001), R.drawable.ic_castle_light));
        mList.add(new DrawerPrincipal(getString(R.string.m_003), R.drawable.ic_square_inc_cash_light));
        // mList.add(new DrawerPrincipal(getString(R.string.a_002), R.drawable.ic_chart_line_light));
        mList.add(new DrawerPrincipal(getString(R.string.c_003), R.drawable.ic_settings_light));
        mList.add(new DrawerPrincipal(getString(R.string.s_001), R.drawable.ic_information_outline_light));
        mList.add(new DrawerPrincipal(getString(R.string.m_013), R.drawable.ic_google_play_light));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView mDrawerList = (ListView) findViewById(R.id.left_drawer);

        adpDrawerPrincipal adapter = new adpDrawerPrincipal(Principal.this, mList);
        if (mDrawerList != null) {
            mDrawerList.setAdapter(adapter);
        }

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_dark);

        if (mDrawerList != null) {
            mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        }

        txtInvestido = (TextView) findViewById(R.id.txtInvestido);
        txtGanho = (TextView) findViewById(R.id.txtGanho);
        txtSaldo = (TextView) findViewById(R.id.txtSaldo);
        txtROI = (TextView) findViewById(R.id.txtROI);

        edtMes = (TextView) findViewById(R.id.edtMes);
        edtAno = (TextView) findViewById(R.id.edtAno);

        edtMes.setText(getString(R.string.t_009));
        edtAno.setText(getString(R.string.t_009));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {

            if (!mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                mDrawerLayout.openDrawer(Gravity.LEFT);
            else
                mDrawerLayout.closeDrawer(Gravity.LEFT);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adView != null) {
            adView.destroy();
        }

        Funcoes.closeDataBase();
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

        leitura();
    }

    @SuppressLint("SetTextI18n")
    private void leitura() {

        Double dInvestido;
        Double dGanho;
        Double dSaldo;
        Double dROI;
        Double dEntrada;
        Double dSaida;

        txtInvestido.setText(Funcoes.decimalFormat.format(0));
        txtGanho.setText(Funcoes.decimalFormat.format(0));
        txtSaldo.setText(Funcoes.decimalFormat.format(0));
        txtROI.setText(Funcoes.decimalFormat.format(0));

        tblBankroll recBankroll = new tblBankroll(Principal.this);

        dInvestido = recBankroll.getVlrInvestido(edtMes.getText().toString(), edtAno.getText().toString());
        dGanho = recBankroll.getVlrGanho(edtMes.getText().toString(), edtAno.getText().toString());
        dEntrada = recBankroll.getVlrEntrada(edtMes.getText().toString(), edtAno.getText().toString());
        dSaida = recBankroll.getVlrSaida(edtMes.getText().toString(), edtAno.getText().toString());

        dSaldo = (dGanho + dEntrada) - (dSaida + dInvestido);

        dROI = dInvestido > 0 ? dSaldo * 100 / dInvestido : 0.0;

        txtInvestido.setText(Funcoes.decimalFormat.format(dInvestido));
        txtGanho.setText(Funcoes.decimalFormat.format(dGanho));
        txtSaldo.setText(Funcoes.decimalFormat.format(dSaldo));
        txtROI.setText(Funcoes.decimalFormat.format(dROI) + getString(R.string._002));

        if (dROI < 0)
            txtROI.setTextColor(Funcoes.getColorWrapper(this, R.color.red));
        else
            txtROI.setTextColor(Funcoes.getColorWrapper(this, R.color.green));

        if (dSaldo < 0)
            txtSaldo.setTextColor(Funcoes.getColorWrapper(this, R.color.red));
        else
            txtSaldo.setTextColor(Funcoes.getColorWrapper(this, R.color.green));
    }

    @SuppressLint("DefaultLocale")
    private void montaComboMes() {

        int iPosition = 0;

        List<String> mListFull = new ArrayList<>();
        List<String> mList;

        mList = new tblMovimentos().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblTorneios().getListMes();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        if (mListFull.size() == 0)
            return;

        mListFull.add(getString(R.string.t_009));

        Collections.sort(mListFull);

        final String[] vNome = new String[mListFull.size()];

        for (int i = 0; i <= mListFull.size() - 1; i++) {

            if (!mListFull.get(i).matches(getString(R.string.t_009))) {
                vNome[i] = String.format("%02d", Integer.valueOf(mListFull.get(i)));
            } else {
                vNome[i] = mListFull.get(i);
            }

            if (vNome[i].matches(edtMes.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
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
                dialog.dismiss();
                leitura();
            }
        });

        builder.show();
    }

    @SuppressLint("DefaultLocale")
    private void montaComboAno() {

        int iPosition = 0;

        List<String> mListFull = new ArrayList<>();
        List<String> mList;

        mList = new tblMovimentos().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        mList = new tblTorneios().getListAno();
        for (int i = 0; i <= mList.size() - 1; i++) {
            if (mListFull.indexOf(mList.get(i)) == -1) {
                mListFull.add(mList.get(i));
            }
        }

        if (mListFull.size() == 0)
            return;

        mListFull.add(getString(R.string.t_009));

        Collections.sort(mListFull);

        final String[] vNome = new String[mListFull.size()];

        for (int i = 0; i <= mListFull.size() - 1; i++) {

            if (!mListFull.get(i).matches(getString(R.string.t_009))) {
                vNome[i] = String.format("%02d", Integer.valueOf(mListFull.get(i)));
            } else {
                vNome[i] = mListFull.get(i);
            }

            if (vNome[i].matches(edtAno.getText().toString()))
                iPosition = i;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Principal.this);
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
                dialog.dismiss();
                leitura();
            }
        });

        builder.show();
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @SuppressLint("RtlHardcoded")
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mDrawerLayout.closeDrawer(Gravity.LEFT);

            switch (position) {
                case 0: // Torneios
                    startActivity(new Intent(Principal.this, Torneios.class));
                    break;
                case 1: // Movimentos
                    startActivity(new Intent(Principal.this, Movimentos.class));
                    break;
                case 2: // Configurações
                    startActivity(new Intent(Principal.this, Configuracoes.class));
                    break;
                case 3: // Sobre
                    startActivity(new Intent(Principal.this, Sobre.class));
                    break;
                case 4: // Mãos Poker Texas Holdem

                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("market://details?id=mmmobile.com.br.maospoker"));
                        startActivity(intent);
                    } catch (Exception e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=mmmobile.com.br.maospoker"));
                        startActivity(intent);
                    }

                    break;
                default:
                    Toast.makeText(Principal.this, getString(R.string.s_002), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }

}
