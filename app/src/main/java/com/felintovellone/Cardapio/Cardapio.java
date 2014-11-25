package com.felintovellone.Cardapio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.felintovellone.ru.uel.R;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import Dialogs.SemInternetDialog;
import Dialogs.Sobre;
import Dialogs.TabelaPrecos;
import Model.Dia;
import Utils.JSONParser;
import Utils.PainelDeControle;

public class Cardapio extends ActionBarActivity implements
        ActionBar.TabListener {
    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    //Para tratar o JSON do cardápio
    private final String url_cardapio = "http://ruuel.zz.vc/Cardapio/android.json";
    private final String TAG_SUCCESS = "success";
    private static final String NOME_JSON_CARDAPIO_OFFLINE = "cardapioOffline.json";
    JSONArray refeicoes = null;
    private Dia[] cardapio;
    private String[] dias;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    depoisCarregarCardapio();
                    break;
                default:
                    break;
            }
        }
    };

    private Handler toast = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String s = "ERRO";
            switch (msg.what) {
                case 0:
                    s = "ONLINE";
                    break;
                case 1:
                    s = "OFFLINE";
                    break;
            }
            Toast.makeText(getApplicationContext(), "Cardápio carregado "+s, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        //carrega o cardapio atraves da url_cardapio
        CarregaCardapio cc = new CarregaCardapio();
        cc.execute();
    }

    private void depoisCarregarCardapio() {
        dias = getResources().getStringArray(R.array.string_array_dias);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(
                getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager
                .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }
    }

    public class CarregaCardapio extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;

        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Cardapio.this);
            pDialog.setMessage("Carregando cardápio. Por favor espere...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                atualizaCardapio();
            } catch (IOException e) {
                e.printStackTrace();
                return "internet";
            } catch (JSONException e) {
                e.printStackTrace();
                return "erro";
            }
            return "";
        }

        protected void onPostExecute(String resultado) {
            pDialog.dismiss();
            //Se nao deu erro nenhum
            if(resultado.equals("")){
                myHandler.sendEmptyMessage(0);
                return;
            }
            if(resultado.equals("internet")){
                new SemInternetDialog(Cardapio.this);
                return;
            }
            if(resultado.equals("erro")){
                return;
            }
        }

        private void atualizaCardapio() throws IOException, JSONException {
            JSONObject json = null;
            if (PainelDeControle.isNetworkAvailable()) {
                Log.d("Pegando ONLINE", "ONLINE");
                // getting JSON string from URL
                JSONParser jParser = new JSONParser();
                json = jParser.makeHttpRequest(url_cardapio, "GET", new ArrayList<NameValuePair>());
                String jsonCardapio = json.toString();
                toast.sendEmptyMessage(0);
                PainelDeControle.writeToFile(NOME_JSON_CARDAPIO_OFFLINE, jsonCardapio);
            } else{
                toast.sendEmptyMessage(1);
                String jsonOffline = PainelDeControle.readFromFile(NOME_JSON_CARDAPIO_OFFLINE);
                json = new JSONObject(jsonOffline);
            }
            parseJSON(json);
        }

        private void parseJSON(JSONObject json) throws JSONException {
            // products found
            // Getting Array of Products
            refeicoes = json.getJSONArray("cardapio");
            // looping through All Products
            cardapio = new Dia[5];
            for (int i = 0; i < refeicoes.length(); i++) {
                JSONObject c = refeicoes.getJSONObject(i).getJSONObject("Cardapio");

                //Atualiza o array de dias com as informacoes do JSON
                cardapio[i] = new Dia(
                        c.getBoolean("funcionamento"),
                        c.getString("dia_mes"),
                        c.getString("dia_semana"),
                        c.getString("arroz"),
                        c.getString("feijao"),
                        c.getString("mistura1"),
                        c.getString("mistura2"),
                        c.getString("sobremesa"),
                        c.getString("salada"),
                        c.getString("suco"),
                        c.getBoolean("especial")
                );
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cardapio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                new Sobre(this);
                return true;
//            case R.id.preco:
//                new TabelaPrecos(this);
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab,
                              FragmentTransaction fragmentTransaction) {
        int abaSelecionada = tab.getPosition();
        mViewPager.setCurrentItem(abaSelecionada);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab,
                                FragmentTransaction fragmentTransaction) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            DiaFragment frag = new DiaFragment();
            frag.setDia(cardapio[position]);
            return frag;
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return dias[position].toUpperCase(l);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DiaFragment extends Fragment {
        private Dia dia;

        public DiaFragment() {
        }

        public void setDia(Dia dia){
            this.dia = dia;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_cardapio,
                    container, false);
            SimpleDateFormat destino = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat origem= new SimpleDateFormat("yyyy-MM-dd");
            String dia_mes = "";
            try {
                dia_mes = destino.format(origem.parse(dia.getDia_mes()));
            } catch (ParseException e) {
                dia_mes = "Erro ao recuperar data";
                e.printStackTrace();
            }
            ((TextView) rootView.findViewById(R.id.dia_mes)).setText(dia_mes);
            ((TextView) rootView.findViewById(R.id.arroz)).setText(dia.getArroz() + " / " + dia.getFeijao());
            ((TextView) rootView.findViewById(R.id.mistura1)).setText(dia.getMistura1());
            if (!dia.getMistura2().equals("null")) {
                ((TextView) rootView.findViewById(R.id.mistura2)).setText(dia.getMistura2());
            }
            ((TextView) rootView.findViewById(R.id.salada)).setText(dia.getSalada());
            ((TextView) rootView.findViewById(R.id.sobremesa)).setText(dia.getSobremesa());
            ((TextView) rootView.findViewById(R.id.suco)).setText(dia.getSuco());
            return rootView;
        }
    }
}
