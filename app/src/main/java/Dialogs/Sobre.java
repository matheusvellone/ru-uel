package Dialogs;

import android.app.Dialog;
import android.content.Context;

import com.felintovellone.ru.uel.R;

/**
 * Created by Matheus on 22/11/2014.
 */
public class Sobre extends Dialog{
    public Sobre(Context context) {
        super(context);
        setTitle(R.string.sobre);
        setCancelable(true);
        setContentView(R.layout.sobre);
        show();
    }
}
