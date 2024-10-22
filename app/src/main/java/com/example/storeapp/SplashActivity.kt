public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establecer el layout del splash screen
        setContentView(R.layout.splash_screen);

        // Simular una pausa de 2 segundos antes de iniciar la actividad principal
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Iniciar la MainActivity
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Finalizar la actividad para que no se pueda regresar
            }
        }, 2000); // 2000 ms = 2 segundos
    }
}
