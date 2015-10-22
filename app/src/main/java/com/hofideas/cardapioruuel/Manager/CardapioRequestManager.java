package com.hofideas.cardapioruuel.Manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public class CardapioRequestManager {
    private final static String JSON_URL = "http://cardapio-uel.herokuapp.com/";
    private final static String REQUEST_METHOD = "GET";

    public CardapioRequestManager() {

    }

    /**
     * Retorna a cópia do cardápio que está mantida no celular
     */
    private JSONObject getCardapioOffline(){
        JSONObject cardapio = null;

        return cardapio;
    }

    /**
     * Atualiza a cópia do cardápio mantida no celular
     */
    private void updateCardapio(){

    }

    /**
     * Tenta pegar o cardápio da Internet, caso não consiga, retorna o cardápio mantido no celular
     * @return
     */
    public JSONObject getCardapio(final Context context) {
        JSONObject cardapio = null;
        RequestManager.getInstance(context).httpRequest(JSON_URL, REQUEST_METHOD, new RequestManager.VolleyCallback() {
            @Override
            public void onSuccess(Object result) {
                JSONObject response = (JSONObject) result;
                Toast.makeText(context, "Cardápio carregado: "+response.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
        return cardapio;
    }
}
