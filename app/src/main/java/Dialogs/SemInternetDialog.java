package Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

/**
 * Created by Matheus on 21/11/2014.
 */
public class SemInternetDialog extends AlertDialog {
    public SemInternetDialog(final Context context) {
        super(context);
        setTitle("Sem conexão com a internet");
        setMessage("Não foi possível estabelecer uma conexão com a Internet para esta operação");
        setCancelable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                System.exit(0);
            }
        });
        show();
    }


}