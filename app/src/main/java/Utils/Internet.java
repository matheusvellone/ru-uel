package Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Matheus on 21/11/2014.
 */
public class Internet {
    public static boolean existeConexaoInternet(Activity act) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) act.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean conexao = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        if(!conexao){
            ProgressDialog dialog = new ProgressDialog(act.getApplicationContext());
            dialog.setMessage("Não existe uma conexão com a internet.");
            dialog.show();
            return false;
        }
        return true;
    }
}
