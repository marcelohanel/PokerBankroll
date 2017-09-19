package mmmobile.com.br.pokerbankroll.Interfaces;

import java.io.File;

public interface InterfaceBackup {
    void onFinishBackup(File s);

    void onFinishRestore(String s);
}
