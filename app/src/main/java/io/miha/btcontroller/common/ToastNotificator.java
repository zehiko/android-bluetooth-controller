package io.miha.btcontroller.common;


import android.content.Context;
import android.widget.Toast;

public class ToastNotificator {

    public static void toastNotify(Context context, String toastMessage) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, toastMessage, duration);

        toast.show();
    }
}
