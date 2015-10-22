package com.hofideas.cardapioruuel.Manager;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RequestManager {
    private static RequestManager requestManager;
    private RequestQueue requestQueue;
    private Context _context;

    private RequestManager(Context context) {
        _context = context;
        requestQueue = getRequestQueue();
    }

    public void httpRequest(String url, String method, final VolleyCallback callback) {
        int requestMethod;
        switch (method) {
            case "POST":
            case "post":
                requestMethod = Request.Method.POST;
                break;
            case "GET":
            case "get":
                requestMethod = Request.Method.GET;
                break;
            default:
                throw new IllegalArgumentException("O parâmetro String method deve ser POST/post ou GET/get");
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestMethod, url, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(_context, "Não há uma conexão com a Internet para realizar esta operação", Toast.LENGTH_LONG).show();
                        }
                        callback.onError(error);
                    }
                });

        getRequestQueue().add(jsObjRequest);
    }

    public static synchronized RequestManager getInstance(Context context) {
        if (requestManager == null) {
            requestManager = new RequestManager(context);
        }
        return requestManager;
    }

//    private void addRequestQueue(Request){
//        if()
//    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(_context.getApplicationContext());
        }
        return requestQueue;
    }

    public interface VolleyCallback {
        void onSuccess(Object result);
        void onError(VolleyError error);
    }
}
