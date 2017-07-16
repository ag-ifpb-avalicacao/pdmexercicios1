package br.edu.ifpb.pdm.questao_05_app.service;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import br.edu.ifpb.pdm.questao_05_app.model.MyChatMessage;

/**
 * Created by natarajan on 16/07/17.
 */

/**
 * Classe que manipula os arquivos que salvam as mensagem em disco. Usada para evitar load
 * de todas as mensagens a cada atualização de tela
 */
public class LocalFileUtils {

    /**
     * Método para salvar em arquivo as mensagens de uma ChatActivity.
     * @param messages mensagens a serem salvas no arquivo.
     * @param isPerson1 boolean informando se as mensagens pertecem à tela de pessoa 1 ou 2.
     */
    public static void saveMessagesOnFile(List<MyChatMessage> messages, boolean isPerson1) {

        // Existe um arquivo para cada pessoa. Aqui se define o nome do arquivo
        String fileName = isPerson1 ? "person1Files" :  "person2Files";

        // cria e recupera instancia que representa a pasta de arquivos do aplicativo
        File root = new File(Environment.getExternalStorageDirectory()
                + "/questao_05/");
        if (!root.exists()) {       // se ainda não existir, cria a pasta
            root.mkdirs();
        }

        // cria o arquivo a ser salvo propriamente dito
        File fileResult = new File(root.getPath() + "/" + fileName);
        if (!fileResult.exists()) {     // se não existir, cria
            try {
                fileResult.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // salva as mensagens em arquivo
        try {
            FileOutputStream fos = new FileOutputStream(fileResult);

            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(messages);
            oos.flush();
            oos.close();

            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método usado para recuperar as mensagens salvas em arquivos.
     * @param isPerson1 boolean que informa qual arquivo de mensagens recuperar. true = arquivo de pessoa 1; false, pessoa 2.
     * @return lista das mensagens recuperadas.
     */
    public static List<MyChatMessage> readMessagesFile(boolean isPerson1) {

        String fileName = isPerson1 ? "person1Files" :  "person2Files";

        try {
            FileInputStream fi = new FileInputStream(new File(Environment.getExternalStorageDirectory().getPath() + "/questao_05/" + fileName));
            ObjectInputStream oi = null;
            oi = new ObjectInputStream(fi);

            // faz leitura do arquivo de messagens
            List<MyChatMessage> messages = (List<MyChatMessage>) oi.readObject();
            return messages;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
