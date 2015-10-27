package com.hofideas.cardapioruuel.Manager;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.hofideas.cardapioruuel.utils.FileHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CardapioRequestManager {
    private final static String JSON_URL = "http://cardapio-uel.herokuapp.com/";
    private final static String offlineFilename = "cardapioOffline.json";
    private final static String TAG_SUCCESS = "SUCCESS";
    private final static String REQUEST_METHOD = "GET";

    private final static String JSON_TESTE = "{\"success\" : 1,\"cardapio\":[{\"arroz\": \"Arroz\",\"feijao\": \"Feijão\",\"dia_mes\": \"23/10\",\"dia_semana\": \"Segunda\",\"funcionamento\": true,\"mistura2\": \"Mistura1\",\"mistura1\": \"Mistura2\",\"sobremesa\": \"Banana\",\"salada\": \"Salada de Alface\",\"suco\": \"Suco Verde\",\"especial\": false},{\"arroz\": \"Arroz\",\"feijao\": \"Feijão\",\"dia_mes\": \"23/10\",\"dia_semana\": \"Segunda\",\"funcionamento\": true,\"mistura2\": \"Mistura1\",\"mistura1\": \"Mistura2\",\"sobremesa\": \"Banana\",\"salada\": \"Salada de Alface\",\"suco\": \"Suco Verde\",\"especial\": false},{\"arroz\": \"Arroz\",\"feijao\": \"Feijão\",\"dia_mes\": \"23/10\",\"dia_semana\": \"Segunda\",\"funcionamento\": true,\"mistura2\": \"Mistura1\",\"mistura1\": \"Mistura2\",\"sobremesa\": \"Banana\",\"salada\": \"Salada de Alface\",\"suco\": \"Suco Verde\",\"especial\": false},{\"arroz\": \"Arroz\",\"feijao\": \"Feijão\",\"dia_mes\": \"23/10\",\"dia_semana\": \"Segunda\",\"funcionamento\": true,\"mistura2\": \"Mistura1\",\"mistura1\": \"Mistura2\",\"sobremesa\": \"Banana\",\"salada\": \"Salada de Alface\",\"suco\": \"Suco Verde\",\"especial\": false},{\"arroz\": \"Arroz\",\"feijao\": \"Feijão\",\"dia_mes\": \"23/10\",\"dia_semana\": \"Segunda\",\"funcionamento\": true,\"mistura2\": \"Mistura1\",\"mistura1\": \"Mistura2\",\"sobremesa\": \"Banana\",\"salada\": \"Salada de Alface\",\"suco\": \"Suco Verde\",\"especial\": false}]}";

    private JSONObject cardapio;

    /**
     * Retorna a cópia do cardápio que está mantida no celular
     */
    private JSONObject getCardapioOffline(Context context) throws JSONException {
        try {
            cardapio = new JSONObject(FileHandler.readFromFile(offlineFilename, context));
        } catch (IOException e) {
            Toast.makeText(context, "Não existe uma versão offline do cardápio. Por favor conecte-se à Internet para utilizar o aplicativo", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(context, "Tá com o TESTE!!", Toast.LENGTH_SHORT).show();
        cardapio = new JSONObject(JSON_TESTE);

        return cardapio;
    }

    public JSONObject getCardapio() {
        //TODO - Verificar TAG do cardápio. Se etiver com erro, pegar de novo
//        try {
//            if (cardapio.getInt(TAG_SUCCESS) == 1) {
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return cardapio;
    }

    /**
     * Tenta pegar o cardápio da Internet, caso não consiga, retorna o cardápio mantido no celular
     *
     * @return
     */

    public void updateCardapio(final Context context, final Handler handler) {
        RequestManager.getInstance(context).httpRequest(JSON_URL, REQUEST_METHOD, new RequestManager.VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                cardapio = (JSONObject) result;
                FileHandler.writeToFile(offlineFilename, cardapio.toString(), context);
                handler.sendEmptyMessage(0);

                try {
                    cardapio = getCardapioOffline(context);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(VolleyError error) {
//                if (error instanceof NoConnectionError) {
//                    Toast.makeText(context, "Utilizando versão offline do cardápio.", Toast.LENGTH_LONG).show();
//                }
                try {
                    cardapio = getCardapioOffline(context);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        });
    }
}
