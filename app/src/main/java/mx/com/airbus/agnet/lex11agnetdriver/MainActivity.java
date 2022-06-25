// MainActivity.java
// Autor: Giancarlo Santini
// Soporte y contacto: giancarlo.santini@airbus.com
// Última actualización: 24/06/2022

/***

Clase principal que implementa el único activity de la aplicación.
 Este activity no tiene función alguna, ùnicamente despliega una pantalla con el nombre e información de
 la aplicación.

 ***/

package mx.com.airbus.agnet.lex11agnetdriver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}