package br.edu.ifpb.pdm.questao_04;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

/**
 * Classe principal que recebe o link a ser feito o download e mostra quando o mesmo é finalizado
 */
public class MainActivity extends AppCompatActivity {

    private EditText urlEditText;       // caixa de texto para inserir o link
    private Button downloadButton;      // botão para inicar o download
    private Button eraseButton;         // botão para apagar os downloads realizados

    private ListView listView;          // list view para mostrar a lista de downloads
    ProgressBar progressBar;            // barra de progresso de download

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // linkando com widgets da activity
        urlEditText = (EditText) findViewById(R.id.urlEditText);
        downloadButton = (Button) findViewById(R.id.downloadButton);
        eraseButton = (Button) findViewById(R.id.button2);

        // setando a progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);


        // colocando o arquivos da pasta de downloads no listview
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<File> adapter = new ArrayAdapter<File>(this,
                android.R.layout.simple_list_item_1);

        File directory = new File(Environment.getExternalStorageDirectory()
                + "/questao_04/");

        File[] files = directory.listFiles();
        if (files != null)
            adapter.addAll(files);

        listView.setAdapter(adapter);

        // setando ação de iniciar o serviço de download quando o botão é acionado
        downloadButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("[MyDebug]", "Chamando serviço...");
                MainActivity.this.startDownload();
            }
        });

        // setando ação para apagar os downloads da pasta
        eraseButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("[MyDebug]", "Chamando serviço...");
                MainActivity.this.eraseDownloads();
            }
        });

    }

    // método que atualiza o listview com os arquivos da pasta de download
    private void updateDownloadList() {

        ArrayAdapter<File> adapter = (ArrayAdapter<File>) listView.getAdapter();

        File directory = new File(Environment.getExternalStorageDirectory()
                + "/questao_04/");

        adapter.clear();

        File[] files = directory.listFiles();
        if (files != null)
            adapter.addAll(files);

        adapter.notifyDataSetChanged();

    }

    // método que chama o serviço de download
    private void startDownload() {
        //preparing intent
        String url = urlEditText.getText().toString();
        Intent intent = new Intent(this, MyCustomDownloadService.class);
        intent.putExtra("resultReceiver", new MyResultReceiver(new Handler()));
        intent.putExtra("url", url);

        //disabling buttons
        eraseButton.setEnabled(false);
        downloadButton.setEnabled(false);

        //starting download service
        startService(intent);
    }

    // método que apaga os downloads já realizados
    private void eraseDownloads() {

        ArrayAdapter<File> adapter = (ArrayAdapter<File>) listView.getAdapter();

        File dir = new File(Environment.getExternalStorageDirectory()
                + "/questao_04/");

        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
        adapter.clear();
        adapter.notifyDataSetChanged();

    }

    /**
     * Classe interna utilizada para receber os resultados dos downloads em andamento / finalizados
     * É responsável dentre outras coisa por atualizar a barra de progresso enquanto um download é realizdo.
     */
    private class MyResultReceiver extends ResultReceiver {

        public MyResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode) {
                // atualiza a tela para mostrar mensagem sobre erro na execução do download
                case MyCustomDownloadService.DOWNLOAD_ERROR:
                    String message = resultData.getString("message");   // pega a mensagem de erro enviada pelo serviço
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();    //mostra mensagem de erro
                    progressBar.setVisibility(View.INVISIBLE);  // desabilita a barra de progresso
                    // habilita os botões de downlaod e de apagar arquivos
                    eraseButton.setEnabled(true);
                    downloadButton.setEnabled(true);
                    break;

                // atualiza a tela para mostrar que um download foi finalizado
                case MyCustomDownloadService.DOWNLOAD_SUCCESS:
                    // mostra mensagem de download concluído
                    Toast.makeText(getApplicationContext(),
                            "Download completo",
                            Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);      // desabilita a barra de progresso
                    updateDownloadList();                           // atualiza a lista de arquivos baixados
                    eraseButton.setEnabled(true);                   // habilita o botão de apagar
                    downloadButton.setEnabled(true);                // habilita o botão de download para poder realizar um novo
                    break;

                // atualiza status da barra de progresso enquanto o download está em andamento
                case MyCustomDownloadService.DOWNLOAD_ONGOING:
                    // atualiza a barra de progresso. Recebe do serviço a porcentagem que o download já concluiu.
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(resultData.getInt("progress"));

                    break;

            }

            super.onReceiveResult(resultCode, resultData);  // chama o método da superclasse ResultReceiver
        }

    }


}
