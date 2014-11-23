package Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;

import Utils.PainelDeControle;

import com.felintovellone.ru.uel.R;

import java.util.concurrent.RunnableFuture;


/**
 * Created by Matheus on 22/11/2014.
 */
public class TabelaPrecos extends Dialog{
    private enum valorRefeicao{
        VISITANTE(6),
        ESTUDANTE(2.7);

        valorRefeicao(double v) {

        }
    }

    public TabelaPrecos(Context context) {
        super(context);
        setContentView(R.layout.tabela_precos);
        setTitle(R.string.tabela_precos);
        setCancelable(true);
        Log.d("Versão Android Rodando", "= "+PainelDeControle.VERSAO_ANDROID);
        if(PainelDeControle.VERSAO_ANDROID >= 11){
        } else{
            Button btnCalcular = (Button) findViewById(R.id.btn_calcular);
            if(btnCalcular != null) {
                btnCalcular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText quantidadeText = (EditText) findViewById(R.id.quantidade);
                        int quantidade = Integer.parseInt(quantidadeText.getText().toString());
                        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                        double resultado = 0;
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.visitante:
                                resultado = 6 * quantidade;
                                break;
                            case R.id.estudante:
                                resultado = 2.7 * quantidade;
                                break;
                        }
                        TextView resultadoTextView = (TextView) findViewById(R.id.resultado);
                        resultadoTextView.setText("Total = " + resultado);
                    }
                });
            }
        }
        show();
    }
}
