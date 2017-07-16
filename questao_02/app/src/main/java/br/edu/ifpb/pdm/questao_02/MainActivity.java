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

/**
 * Questão 2. Classe principal
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText adultos, criancas;   // editText para informação de num. de adultos e crianças
    private TextView bolo, doces, salgados, refri;  // textViews para resultados dos cálculos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button processButton = (Button) findViewById(R.id.button);
        processButton.setOnClickListener(this); // informando ação do botão "Calcular"
    }

    /*
      Método que faz o cálculo das quantidades de itens para festa.
      O cálculo é feito aqui porque trata-se de uma execução simples e curta,
      e seus resultados são prevíveis e podem ser tratados sem interferir.
    */
    public void onClick(View view) {
        // referencias para pegar os dados de entrada
        adultos = (EditText) findViewById(R.id.editText3);
        criancas = (EditText) findViewById(R.id.editText4);

        long numAdultos = 0;
        long numCriancas = 0;
        // cálculo dos itens
        try {
            numAdultos = Long.parseLong(adultos.getText().toString());
            numCriancas = Long.parseLong(criancas.getText().toString());

            long resultDoces, resultSalgados;
            float resultBolo, resultRefri;

            // valida o número de adultos e crianças informados
            if (numAdultos == 0 && numCriancas == 0)
                Toast.makeText(this.getApplicationContext(), "Por favor, insira um valor acima de 0 (zero) para adultos e/ou crianças", Toast.LENGTH_SHORT).show();

            // Como a descrição da questão não impede,
            // arbitramos um limite para o número de adultos e crianças.
            // Útil para evitar que o usuário digite número estratosféricos, extrapolando o limite de um long.
            // O número escolhido é mais que razoável para alguém que queria fazer uma festa :D
            else if (numAdultos > 100000000 || numCriancas > 100000000 ) {
                Toast.makeText(this.getApplicationContext(), "É possível que você queria fazer uma festança, mas esse humilde app só aceita valores de até 100 milhões de adultos/crianças :D", Toast.LENGTH_SHORT).show();
            } else {

                // se a entrada é válida raeliza os cálculos

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

                // mostra a ListView com o resultado dos itens
                findViewById(R.id.resultPanel).setVisibility(View.VISIBLE);

            }
        } catch (NumberFormatException e) {
            // mensagem de erro para entradas inválidas
            Toast.makeText(this.getApplicationContext(), "Valor inválido. O número de crianças ou ", Toast.LENGTH_SHORT).show();
        }



    }
}
