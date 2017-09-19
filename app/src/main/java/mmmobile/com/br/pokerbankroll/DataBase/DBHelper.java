package mmmobile.com.br.pokerbankroll.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Bankroll.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONFIG = "CREATE TABLE " +
            "IF NOT EXISTS config (" +
            " chave TEXT PRIMARY KEY, " +
            " valor TEXT " +
            ")";

    private static final String TABLE_MOVIMENTO = "CREATE TABLE " +
            "IF NOT EXISTS movimento (" +
            " id_movimento INTEGER NOT NULL PRIMARY KEY, " +
            " dt_movimento TEXT NOT NULL, " +
            " tipo TEXT NOT NULL, " +
            " vlr_movimento NUMERIC NOT NULL, " +
            " descricao TEXT NOT NULL, " +
            " dt_movimento_inv INTEGER NOT NULL, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL " +
            ")";

    private static final String TABLE_TORNEIO = "CREATE TABLE " +
            "IF NOT EXISTS torneio (" +
            " id_torneio INTEGER NOT NULL PRIMARY KEY, " +
            " dt_torneio TEXT NOT NULL, " +
            " modo TEXT NOT NULL, " +
            " tipo TEXT NOT NULL, " +
            " descricao TEXT NOT NULL, " +
            " vlr_buy NUMERIC NOT NULL, " +
            " qt_rebuy INTEGER NOT NULL, " +
            " vlr_rebuy NUMERIC NOT NULL, " +
            " vlr_addon NUMERIC NOT NULL, " +
            " posicao INTEGER NOT NULL, " +
            " vlr_premiacao NUMERIC NOT NULL, " +
            " dt_torneio_inv INTEGER NOT NULL, " +
            " mes INTEGER NOT NULL, " +
            " ano INTEGER NOT NULL " +
            ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CONFIG);
        db.execSQL(TABLE_MOVIMENTO);
        db.execSQL(TABLE_TORNEIO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*
        if (oldVersion < 2){
            db.execSQL(TABLE_QUESTIONARIO_1);
        }
*/
    }
}
