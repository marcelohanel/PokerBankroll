package mmmobile.com.br.pokerbankroll.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.io.File;

import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.Interfaces.InterfaceBackup;
import mmmobile.com.br.pokerbankroll.R;
import mmmobile.com.br.pokerbankroll.Tasks.TaskBackup;
import mmmobile.com.br.pokerbankroll.Tasks.TaskRestore;

public class Configuracoes extends AppCompatActivity implements InterfaceBackup {

    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_configuracoes);

        adView = new AdView(Configuracoes.this);
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

        Button btnBackup = (Button) findViewById(R.id.btnBackup);
        if (btnBackup != null) {
            btnBackup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    backup();
                }
            });
        }

        Button btnRestore = (Button) findViewById(R.id.btnRestore);
        if (btnRestore != null) {
            btnRestore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restore();
                }
            });
        }
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

    private void backup() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Configuracoes.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Configuracoes.this);
        builder.setTitle(R.string.c_004);
        builder.setMessage(R.string.d_001);
        builder.setCancelable(false);
        builder.create();

        builder.setPositiveButton(R.string.c_004, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TaskBackup taskBackup = new TaskBackup(Configuracoes.this, Configuracoes.this);
                taskBackup.execute();

            }
        });
        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void restore() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(Configuracoes.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return;
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(Configuracoes.this);
        builder.setTitle(R.string.c_004);
        builder.setMessage(R.string.d_003);
        builder.setCancelable(false);
        builder.create();

        builder.setPositiveButton(R.string.c_004, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                TaskRestore taskRestore = new TaskRestore(Configuracoes.this, Configuracoes.this);
                taskRestore.execute();

            }
        });
        builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    @Override
    public void onFinishBackup(final File s) {

        if (s != null) {
            String sMessage = getString(R.string.b_002);
            sMessage += System.getProperty("line.separator");
            sMessage += System.getProperty("line.separator") + s.getAbsolutePath();
            sMessage += System.getProperty("line.separator");
            sMessage += System.getProperty("line.separator") + getString(R.string.d_002);

            AlertDialog.Builder builder = new AlertDialog.Builder(Configuracoes.this);
            builder.setTitle(R.string.a_004);
            builder.setMessage(sMessage);
            builder.setCancelable(false);
            builder.create();

            builder.setPositiveButton(R.string.c_004, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.b_003));
                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.s_003));
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(s));

                    startActivity(Intent.createChooser(intent, getString(R.string.c_006)));

                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(R.string.c_005, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } else {
            Funcoes.showMessage(
                    Configuracoes.this,
                    getResources().getString(R.string.e_001),
                    getResources().getString(R.string.n_002),
                    getResources().getString(R.string.o_001));
        }
    }

    @Override
    public void onFinishRestore(String s) {
        Funcoes.showMessage(
                Configuracoes.this,
                getResources().getString(R.string.a_004),
                s,
                getResources().getString(R.string.o_001));
    }

}
