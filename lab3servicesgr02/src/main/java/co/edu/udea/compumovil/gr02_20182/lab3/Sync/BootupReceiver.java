package co.edu.udea.compumovil.gr02_20182.lab3.Sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import co.edu.udea.compumovil.gr02_20182.lab3.MainActivity;

public class BootupReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"App RestMobil", Toast.LENGTH_LONG).show();
        Intent i= new Intent(context,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
