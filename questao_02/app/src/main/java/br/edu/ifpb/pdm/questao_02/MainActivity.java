package br.edu.ifpb.pdm.questao_02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText adultos, criancas;
    private TextView bolo, doces, salgados, refri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button processButton = (Button) findViewById(R.id.button);
        processButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        adultos = (EditText) findViewById(R.id.editText3);
        criancas = (EditText) findViewById(R.id.editText4);

        long numAdultos = 0;
        long numCriancas = 0;

        try {
            numAdultos = Long.parseLong(adultos.getText().toString());
            numCriancas = Long.parseLong(criancas.getText().toString());

            long resultDoces, resultSalgados;
            float resultBolo, resultRefri;

            if (numAdultos == 0 && numCriancas == 0)
                Toast.makeText(this.getApplicationContext(), "Por favor, insira um valor acima de 0 (zero) para adultos e/ou crianças", Toast.LENGTH_SHORT).show();
            else if (numAdultos > 100000000 || numCriancas > 100000000 ) {
                Toast.makeText(this.getApplicationContext(), "É possível que você queria fazer uma festança, mas esse humilde app só aceita valores de até 100 milhões de adultos/crianças :D", Toast.LENGTH_SHORT).show();
            } else {

                resultBolo = ((float) 0.6 * numAdultos) + ((float) 0.4 * numCriancas);
                resultSalgados = (6 * numAdultos) + (4 * numCriancas);
                resultDoces = (8 * numAdultos) + (6 * numCriancas);
                resultRefri = ((float)0.6 * numAdultos) + ((float)0.5 * numCriancas);


                bolo = (TextView) findViewById(R.id.textViewBolo);
                bolo.setText(String.format("%.2f", resultBolo));

                doces = (TextView) findViewById(R.id.textViewDoces);
                doces.setText(Long.toString(resultDoces));

                salgados = (TextView) findViewById(R.id.textViewSalgados);
                salgados.setText(Long.toString(resultSalgados));

                refri = (TextView) findViewById(R.id.textViewRegri);
                refri.setText(String.format("%.2f", resultRefri));

                findViewById(R.id.resultPanel).setVisibility(View.VISIBLE);

            }
        } catch (NumberFormatException e) {
            Toast.makeText(this.getApplicationContext(), "Valor inválido. O número de crianças ou ", Toast.LENGTH_SHORT).show();
        }



    }
}
