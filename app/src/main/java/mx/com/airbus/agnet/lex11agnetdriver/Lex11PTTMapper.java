// Lex11PTTMapper.java
// Autor: Giancarlo Santini
// Soporte y contacto: giancarlo.santini@airbus.com
// Última actualización: 24/06/2022

/***

Esta clase implementa el Accessibility service que es el driver que detecta los cambios de estado en
 los botones en Hardware para lanzar los eventos de inicio y fin de PTT, cambios de grupo y lanzan las
 llamadas de emergencia.

 El ID de los botones (KeyCode) se mapean de forma manual en código

 ***/

package mx.com.airbus.agnet.lex11agnetdriver;

import android.accessibilityservice.AccessibilityService;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;

public class Lex11PTTMapper extends AccessibilityService {
    public Lex11PTTMapper() {
    }

    @Override
    public void onServiceConnected() {
        Log.i("INFO", "Driver AGNET PTT iniciado");

        // Instancia de un broadcast receiver el cual detecta los eventos de presiòn de botones y apagado
        // de pantalla, esto cuando la terminal se va a modo idle
        BroadcastReceiver bcr = new ButtonReceiver();

        IntentFilter filter = new IntentFilter(Intent.ACTION_CAMERA_BUTTON);
        filter.addAction(Intent.ACTION_MEDIA_BUTTON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(bcr, filter);

        // Instancia de Power Manager y un Wake lock de manera que la terminal siga activa y detecte
        // los eventos de PTT.
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "mx.com.airbus.agnet.lex11agnetdriver.Lex11PTTMapper::PTTMapperWakeLock");

        wakeLock.acquire();

        Log.i("INFO", "WakeLock acquired by the PTT Mapper Accessibility Service");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.i("INFO", accessibilityEvent.toString());
    }

    /*
        Este método se manejan los eventos relacionados a acciones sobre las teclas o botones externos.
        En este método se detecta cuando los botones en hardware se presionan y liberan. Basados en el código de botón
        (KeyCode y ScanCode) se determina la función o acción a ejecutar.
     */
    @Override
    public boolean onKeyEvent(KeyEvent event) {
        Log.i("INFO", "Key: " + event.getKeyCode() + " -> ScanCode: " + event.getScanCode());

        // Cuando se detecta que una tecla o botón ha sido presionado
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            // Botón Push-to-Talk - Inicia llamada PTT
                  // UMIDIGI BISON             // Cyrus CM17               // SONIM XP8 A10             // MOTOROLA LEX 11
            if(event.getKeyCode() == 302 || event.getKeyCode() == 289 || event.getKeyCode() == 228 || (event.getKeyCode() == 0 && event.getScanCode() == 744)){
                Log.i("INFO", "Boton de PTT Presionado!!");

                // Lanza intent (basado en el API Intent del cliente de AGNET) el cual inicia la toma de canal en PTT
                Intent pushPtt = new Intent("com.airbus.pmr.action.PTT_START");
                sendBroadcast(pushPtt);
            }

            // Botón Group Next - Selección de siguiente grupode comunicación
            else if(event.getKeyCode() == 0 && event.getScanCode() == 749){
                Intent nextGroup = new Intent("com.airbus.pmr.action.GROUP_SELECT_NEXT");
                sendBroadcast(nextGroup);
            }

            // Botón Group Previous - Selección de grupo previo de comunicación
            else if(event.getKeyCode() == 0 && event.getScanCode() == 748){
                Intent prevGroup = new Intent("com.airbus.pmr.action.GROUP_SELECT_PREVIOUS");
                sendBroadcast(prevGroup);
            }

            // Botón SOS - Lanza llamada de emergencia
            else if(event.getKeyCode() == 227 || (event.getKeyCode() == 0 && event.getScanCode() == 745)){
                Intent startEmergency = new Intent("com.airbus.pmr.action.EMERGENCY_START");
                sendBroadcast(startEmergency);
            }
        }

        // Cuando se detecta que el botón o tecla ha sido liberado
        else if(event.getAction() == KeyEvent.ACTION_UP) {
            if(event.getKeyCode() == 302 || event.getKeyCode() == 289 ||event.getKeyCode() == 228 || (event.getKeyCode() == 0 && event.getScanCode() == 744)){
                Log.i("INFO", "Boton de PTT Liberado!!");

                // Lanza intent (basado en el API Intent del cliente de AGNET) el cual termina la toma de canal en PTT
                Intent releasePtt = new Intent("com.airbus.pmr.action.PTT_STOP");
                sendBroadcast(releasePtt);
            }
        }

        return super.onKeyEvent(event);
    }

    @Override
    public void onInterrupt() {
        Log.i("INFO", "Interrupted");
    }

}
