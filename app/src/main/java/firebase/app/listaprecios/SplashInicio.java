package firebase.app.listaprecios;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashInicio extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_inicio);
        //(11)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_izquierda);
        ImageView img = findViewById(R.id.imgGif);
        img.setAnimation(animacion1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashInicio.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }
}