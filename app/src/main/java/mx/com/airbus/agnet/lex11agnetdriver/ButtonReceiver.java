// ButtonReceiver.java
// Autor: Giancarlo Santini
// Soporte y contacto: giancarlo.santini@airbus.com
// Última actualización: 24/06/2022

/***

 Esta clase implementa un broadcast receiver para detectar eventos cuando la pantalla se apaga y se va a idle.
 Particularmente detecta el apagado de la pantalla, lanzando asì un wake lock para permitir que los eventos de las teclas
 o botones sigan siendo detectados por el Accessibility Service.

 ***/

package mx.com.airbus.agnet.lex11agnetdriver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;

public class ButtonReceiver extends BroadcastReceiver {
    /*
        Detecta eventos enviados vía broadcast
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("INFO", "EVENTO CAPTURADO: " + intent.getAction());

        /*
            En caso de detectar que la pantalla se apaga, se envìa un wake lock para dejar la pantalla encencidida con bajo brillo
            para permitir que los eventos de botones sigan siendo detectados por el Accessibility Service. Esto es debido a que
            cuando se apaga la pantalla, el accessibility service ya no es capaz de detectar la presión de los botones en algunos
            modelos de terminal.
         */
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
            PowerManager powerManager = (PowerManager) context.getSystemService(context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK|
                            PowerManager.ACQUIRE_CAUSES_WAKEUP,
                    "mx.com.airbus.agnet.lex11agnetdriver.Lex11PTTMapper::PTTMapperWakeLock");

            wakeLock.acquire();
        }

        //KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        //Log.i("INFO", "Key: " + keyEvent.getKeyCode() + " -> ScanCode: " + keyEvent.getScanCode());

    }
}
