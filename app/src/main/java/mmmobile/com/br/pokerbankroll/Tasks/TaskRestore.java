package mmmobile.com.br.pokerbankroll.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import mmmobile.com.br.pokerbankroll.DataBase.tblConfig;
import mmmobile.com.br.pokerbankroll.DataBase.tblMovimentos;
import mmmobile.com.br.pokerbankroll.DataBase.tblTorneios;
import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.Interfaces.InterfaceBackup;
import mmmobile.com.br.pokerbankroll.R;

public class TaskRestore extends AsyncTask<String, String, String> {

    private final Context mContext;
    private final InterfaceBackup mInterface;
    private ProgressDialog mProgressDialog;

    public TaskRestore(Context c, InterfaceBackup i) {
        this.mContext = c;
        this.mInterface = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle(mContext.getString(R.string.a_003));
        mProgressDialog.setMessage(mContext.getString(R.string.r_003));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            String sAux;
            String sFile;

            sFile = existeBackup();

            if (sFile.trim().length() == 0) {
                sAux = mContext.getString(R.string.a_005);
                sAux = sAux.replace("@1", sFile);
                return sAux;
            } else {
                if (restore(sFile)) {
                    return mContext.getString(R.string.b_004);
                } else {
                    return mContext.getString(R.string.n_003);
                }
            }

        } catch (Exception e) {
            return mContext.getString(R.string.n_003);
        }
    }

    /*
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
*/

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mProgressDialog.dismiss();
        mInterface.onFinishRestore(s);
    }

    private Boolean restore(String s) {

        try {
            File file = new File(s);

            StringBuilder stringBuilder = new StringBuilder();

            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String sLine;

            while ((sLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(sLine);
                stringBuilder.append('\n');
            }
            bufferedReader.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray;
            JSONObject jsonObjectData;

            // Testa Versão base de dados
            jsonArray = jsonObject.getJSONArray("DataBase");
            jsonObjectData = jsonArray.getJSONObject(0);
            if (Integer.valueOf(jsonObjectData.getString("Version")) != Funcoes.mDataBase.getVersion())
                return false;

            Funcoes.mDataBase.beginTransaction();

            // Apaga dados antigos
            Funcoes.mDataBase.delete("torneio", null, null);
            Funcoes.mDataBase.delete("movimento", null, null);
            Funcoes.mDataBase.delete("config", null, null);

            // Tabela config
            jsonArray = jsonObject.getJSONArray("config");
            tblConfig recConfig = new tblConfig();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recConfig.setChave(jsonObjectData.getString("chave"));
                recConfig.setValor(jsonObjectData.getString("valor"));
                recConfig.copy();
            }

            // Tabela movimento
            jsonArray = jsonObject.getJSONArray("movimento");
            tblMovimentos recMovimento = new tblMovimentos();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recMovimento.setIdMovimento(jsonObjectData.getInt("id_movimento"));
                recMovimento.setDtMovimento(jsonObjectData.getString("dt_movimento"));
                recMovimento.setTipo(jsonObjectData.getString("tipo"));
                recMovimento.setVlrMovimento(jsonObjectData.getDouble("vlr_movimento"));
                recMovimento.setDescricao(jsonObjectData.getString("descricao"));
                recMovimento.copy();
            }

            // Tabela torneio
            jsonArray = jsonObject.getJSONArray("torneio");
            tblTorneios recTorneio = new tblTorneios();
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObjectData = jsonArray.getJSONObject(i);
                recTorneio.setIdTorneio(jsonObjectData.getInt("id_torneio"));
                recTorneio.setDtTorneio(jsonObjectData.getString("dt_torneio"));
                recTorneio.setModo(jsonObjectData.getString("modo"));
                recTorneio.setTipo(jsonObjectData.getString("tipo"));
                recTorneio.setDescricao(jsonObjectData.getString("descricao"));
                recTorneio.setVlrBuy(jsonObjectData.getDouble("vlr_buy"));
                recTorneio.setQtReBuy(jsonObjectData.getInt("qt_rebuy"));
                recTorneio.setVlrReBuy(jsonObjectData.getDouble("vlr_rebuy"));
                recTorneio.setVlrAddOn(jsonObjectData.getDouble("vlr_addon"));
                recTorneio.setPosicao(jsonObjectData.getInt("posicao"));
                recTorneio.setVlrPremiacao(jsonObjectData.getDouble("vlr_premiacao"));
                recTorneio.copy();
            }

            Funcoes.mDataBase.setTransactionSuccessful();

            return true;

        } catch (Exception e) {
            return false;
        } finally {
            Funcoes.mDataBase.endTransaction();
        }
    }

    private String existeBackup() {

        if (Funcoes.isExternalStorageReadable()) {
            return "";
        }

        try {
            File sRoot = new File(Environment.getExternalStorageDirectory(), mContext.getString(mContext.getApplicationInfo().labelRes));
            if (!sRoot.exists()) {
                return "";
            }

            File sFile = new File(sRoot, mContext.getString(R.string.b_001));
            if (!sFile.exists()) {
                return "";
            } else {
                return sFile.getAbsolutePath();
            }
        } catch (Exception e) {
            return "";
        }
    }
}
