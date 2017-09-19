package mmmobile.com.br.pokerbankroll.Tasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import mmmobile.com.br.pokerbankroll.Diversos.Funcoes;
import mmmobile.com.br.pokerbankroll.Interfaces.InterfaceBackup;
import mmmobile.com.br.pokerbankroll.R;

public class TaskBackup extends AsyncTask<String, String, File> {

    private final Context mContext;
    private final InterfaceBackup mInterface;
    private ProgressDialog mProgressDialog;

    public TaskBackup(Context c, InterfaceBackup i) {
        this.mContext = c;
        this.mInterface = i;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setTitle(mContext.getString(R.string.a_003));
        mProgressDialog.setMessage(mContext.getString(R.string.f_001));
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected File doInBackground(String... params) {

        try {
            JSONObject jBackup = new JSONObject();

            jBackup.put("DataBase", geraBackupItem("DataBase"));
            jBackup.put("config", geraBackupItem("config"));
            jBackup.put("movimento", geraBackupItem("movimento"));
            jBackup.put("torneio", geraBackupItem("torneio"));

            return Funcoes.saveFile(mContext, mContext.getString(R.string.b_001), jBackup.toString());

        } catch (Exception e) {
            return null;
        }
    }

    /*
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
*/

    @Override
    protected void onPostExecute(File s) {
        super.onPostExecute(s);

        mProgressDialog.dismiss();
        mInterface.onFinishBackup(s);
    }

    private JSONArray geraBackupItem(String tabela) {

        JSONArray resultSet = new JSONArray();

        try {
            Cursor cursor;

            if (tabela.matches("DataBase")) {
                JSONObject rowObject = new JSONObject();
                rowObject.put("Version", Funcoes.mDataBase.getVersion());
                resultSet.put(rowObject);
            } else {
                cursor = Funcoes.mDataBase.query(tabela, new String[]{"*"}, null, null, null, null, null);
                while (cursor.moveToNext()) {

                    JSONObject rowObject = new JSONObject();

                    for (int i = 0; i < cursor.getColumnCount(); i++) {

                        if (cursor.getString(i) != null)
                            rowObject.put(cursor.getColumnName(i), cursor.getString(i));
                        else
                            rowObject.put(cursor.getColumnName(i), "");

                    }
                    resultSet.put(rowObject);
                }
                cursor.close();
            }

        } catch (Exception ignored) {
        }

        return resultSet;
    }
}
