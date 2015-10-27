package com.hofideas.cardapioruuel;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hofideas.cardapioruuel.Manager.CardapioRequestManager;
import com.hofideas.cardapioruuel.Model.Dia;
import com.hofideas.cardapioruuel.utils.ConvertUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class CardapioActivity extends AppCompatActivity {
    private static final String[] dias = {
            "Segunda-Feira", "Terça-Feira", "Quarta-Feira", "Quinta-Feira", "Sexta-Feira"
    };
    private Dia[] cardapioDias;
    private ProgressDialog loadingCardapioDialog;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private CardapioRequestManager cardapioRequestManager = new CardapioRequestManager();
    private Handler cardapioHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    parseCardapio();
                default:
                    loadingCardapioDialog.dismiss();
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

                    // Set up the ViewPager with the sections adapter.
                    mViewPager = (ViewPager) findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadingCardapioDialog = new ProgressDialog(this);
        loadingCardapioDialog.setMessage("Carregando cardápio. Por favor espere...");
        loadingCardapioDialog.setIndeterminate(false);
        loadingCardapioDialog.setCancelable(false);
        loadingCardapioDialog.show();

        cardapioDias = new Dia[5];
        cardapioRequestManager.updateCardapio(this, cardapioHandler);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cardapio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(cardapioDias[position]);
        }

        @Override
        public int getCount() {
            return dias.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position < dias.length) {
                return ConvertUtils.sigla(dias[position], 3);
            }
            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private static Dia diaSelecionado;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(Dia dia) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            diaSelecionado = dia;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.d("NULL", (diaSelecionado == null) + "");
            View rootView = inflater.inflate(R.layout.fragment_cardapio, container, false);
            ((TextView) rootView.findViewById(R.id.dia)).setText(diaSelecionado.getDia_mes() + " <> " + diaSelecionado.getDia_semana());
            ((TextView) rootView.findViewById(R.id.arrozfeijao)).setText(diaSelecionado.getArroz() + " e " + diaSelecionado.getFeijao());
            ((TextView) rootView.findViewById(R.id.mistura1)).setText(diaSelecionado.getMistura1());
            ((TextView) rootView.findViewById(R.id.mistura2)).setText(diaSelecionado.getMistura2());
            ((TextView) rootView.findViewById(R.id.salada)).setText(diaSelecionado.getSalada());
            ((TextView) rootView.findViewById(R.id.sobremesa)).setText(diaSelecionado.getSobremesa());
            ((TextView) rootView.findViewById(R.id.suco)).setText(diaSelecionado.getSuco());
            return rootView;
        }
    }

    private void parseCardapio() {
        JSONObject json = cardapioRequestManager.getCardapio();
        try {
            JSONArray refeicoes = json.getJSONArray("cardapio");
            for (int i = 0; i < refeicoes.length(); i++) {
                JSONObject c = refeicoes.getJSONObject(i);

                //Atualiza o array de dias com as informacoes do JSON
                cardapioDias[i] = new Dia();
                cardapioDias[i].setArroz(c.getString("arroz"));
                cardapioDias[i].setFeijao(c.getString("feijao"));
                cardapioDias[i].setDia_mes(c.getString("dia_mes"));
                cardapioDias[i].setDia_semana(dias[i]);
//                cardapioDias[i].setDia_semana(c.getString("dia_semana"));
                cardapioDias[i].setFuncionamento(c.getBoolean("funcionamento"));
                cardapioDias[i].setMistura1(c.getString("mistura1"));
                cardapioDias[i].setMistura2(c.getString("mistura2"));
                cardapioDias[i].setSobremesa(c.getString("sobremesa"));
                cardapioDias[i].setSalada(c.getString("salada"));
                cardapioDias[i].setSuco(c.getString("suco"));
                cardapioDias[i].setEspecial(c.getBoolean("especial"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
