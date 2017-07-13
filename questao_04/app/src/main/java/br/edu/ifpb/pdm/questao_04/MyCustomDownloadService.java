package br.edu.ifpb.pdm.questao_04;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

/**
 * Created by natarajan on 29/06/17.
 */

/**
 * Classe que extende IntentService responsável por realizar os downloados dos arquivos
 */
public class MyCustomDownloadService extends IntentService {

    // código usados para enviar Resultados parciais ou finaais da execução do serviço
    public static final int DOWNLOAD_ERROR = 10;
    public static final int DOWNLOAD_SUCCESS = 11;
    public static final int DOWNLOAD_ONGOING= 12;


    private ResultReceiver resultReceiver;  // ResultReceiver para onde são enviados os resultados da execução


    public MyCustomDownloadService() {
        super("MyCustomDownloadService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyCustomDownloadService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("[MyDebug]", "Iniciando download:");

        // inicializando variáveis necessárias
        String urlString = intent.getStringExtra("url");
        resultReceiver = intent.getParcelableExtra("resultReceiver");
        Bundle bundle = new Bundle();
        bundle.putInt("progress", 0);

        Log.d("[MyDebug]", "Url recebida: " + urlString + ". Iniciando download");

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();    // abre conexão para iniciar download

            // se a conexão responde com código 200, processa o download
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                String fileName = "";
                String disposition = connection.getHeaderField("Content-Disposition");
                String contentType = connection.getContentType();
                int contentLength = connection.getContentLength();

                // o campo "Content-Disposition" pode conter o nome do arquivo a ser baixado.

                // se este campo estiver disponível no header da conexão aberta:
                if (disposition != null) {
                    // extrai o nome do arquivo do header
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10,
                                disposition.length() - 1);
                    }
                } else {
                    // caso não haja o campo no header, extraimos o nome do arquivo pela própria url
                    fileName = urlString.substring(urlString.lastIndexOf("/") + 1,
                            urlString.length());
                }

                // imprimindo para efeito de log.
                System.out.println("Content-Type = " + contentType);
                System.out.println("Content-Disposition = " + disposition);
                System.out.println("Content-Length = " + contentLength);
                System.out.println("fileName = " + fileName);

                // criando o arquivo na pasta de downloads

                // neste caso a raiz é pasta do aplicativo
                File root = new File(Environment.getExternalStorageDirectory()
                        + "/questao_04/");

                if (!root.exists()) {       // se ainda não existir, cria a pasta
                    root.mkdirs();
                }

                // cria o arquivo (usando o nome obtido no Content-disposition ou na URL
                File fileResult = new File(root.getPath() + "/" + URLDecoder.decode(fileName, "ISO8859_1"));

                if (!fileResult.exists()) {
                    // file does not exist, create it
//                    fileResult.mkdirs();
                    fileResult.createNewFile();
                }

                // pega o tamanho total do arquivo
                // útil para enviar a informação para o status da barra de progresso
                int fileLength = connection.getContentLength();

                resultReceiver.send(DOWNLOAD_ONGOING, bundle);

                // downloading
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(fileResult);

                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // enviando o informações para mostrar a barra
                    bundle.putInt("progress",(int) (total * 100 / fileLength));
                    resultReceiver.send(DOWNLOAD_ONGOING, bundle);
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

                resultReceiver.send(DOWNLOAD_SUCCESS, bundle);  // envia resultado para a main activity dizendo que o download terminou

            } else {
                //erro de conexao
                // informa através do resultReceiver
                bundle.putString("message", "Erro de conexão. Verifique se o link está correto e ainda é válido");
                resultReceiver.send(DOWNLOAD_ERROR, bundle);
            }

        // exceções
        } catch (MalformedURLException e) {
            bundle.putString("message", "URL mal formatada. Verifique se o link está correto");
            resultReceiver.send(DOWNLOAD_ERROR, bundle);
            e.printStackTrace();
        } catch (IOException e) {
            bundle.putString("message", "Ocorreu um erro no aplicativo. Tente novamente");
            resultReceiver.send(DOWNLOAD_ERROR, bundle);
            e.printStackTrace();
        }
    }



}
