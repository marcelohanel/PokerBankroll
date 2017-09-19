package mmmobile.com.br.pokerbankroll.Diversos;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import mmmobile.com.br.pokerbankroll.DataBase.DBHelper;
import mmmobile.com.br.pokerbankroll.R;

public class Funcoes {

    public static final int SPACE_BETWEEN_ITEMS = 10;
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    public static final DecimalFormat decimalFormat = new DecimalFormat("###,###,##0.00");
    public static final DecimalFormat integerFormat = new DecimalFormat("###,###,##0");
    public static AdRequest adRequest;
    public static SQLiteDatabase mDataBase;

    public static int getColorWrapper(Context context, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getColor(color);
        } else {
            //noinspection deprecation
            return context.getResources().getColor(color);
        }
    }


    public static void openDataBase(Context c) {
        DBHelper dataBase = new DBHelper(c);
        mDataBase = dataBase.getWritableDatabase();
    }

    public static void closeDataBase() {
        if (mDataBase != null && mDataBase.isOpen())
            mDataBase.close();
    }

    public static File saveFile(Context context, String filename, String dados) {

        if (!isExternalStorageWritable())
            Toast.makeText(context, context.getString(R.string.n_001), Toast.LENGTH_LONG).show();

        if (isExternalStorageReadable()) {
            Toast.makeText(context, context.getString(R.string.n_001), Toast.LENGTH_LONG).show();
        }

        try {
            File root = new File(Environment.getExternalStorageDirectory(), context.getString(context.getApplicationInfo().labelRes));
            if (!root.exists())
                //noinspection ResultOfMethodCallIgnored
                root.mkdirs();

            File sFile = new File(root, filename);
            if (sFile.exists())
                //noinspection ResultOfMethodCallIgnored
                sFile.delete();

            FileWriter writer = new FileWriter(sFile);
            writer.append(dados);
            writer.flush();
            writer.close();

            return sFile;
        } catch (IOException e) {
            Toast.makeText(context, context.getString(R.string.n_001), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    public static String getDate() {
        return Funcoes.dateFormat.format(Calendar.getInstance().getTime());
    }

    public static String[] getData(String data) {

        String[] vRetorno = new String[9];
        int iDia = 0;
        int iMes = 0;
        int iAno = 0;
        String sAno = "";
        String sMes = "";
        String sDia = "";

        if (data.trim().length() != 0) {
            iDia = Integer.valueOf(data.split("/")[0]);
            iMes = Integer.valueOf(data.split("/")[1]);
            iAno = Integer.valueOf(data.split("/")[2]);

            sDia = data.split("/")[0];
            sMes = data.split("/")[1];
            sAno = data.split("/")[2];
        }

        vRetorno[0] = data;
        vRetorno[1] = String.valueOf(iDia);
        vRetorno[2] = String.valueOf(iMes);
        vRetorno[3] = String.valueOf(iAno);
        vRetorno[4] = sAno + sMes + sDia;
        vRetorno[5] = sMes + "/" + sAno;
        vRetorno[6] = sDia;
        vRetorno[7] = sMes;
        vRetorno[8] = sAno;

        return vRetorno;
    }

    public static void showMessage(Context context, String title, String message, String textButton) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(textButton,
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

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return !Environment.MEDIA_MOUNTED.equals(state) && !Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static String getVersionName(Context context) {
        try {
            ComponentName comp = new ComponentName(context, context.getClass());
            PackageInfo pinfo = context.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
            return pinfo.versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }
}
