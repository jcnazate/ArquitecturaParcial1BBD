package ec.edu.monster;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import ec.edu.monster.R;

public class MainActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = getIntent().getStringExtra("username");

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        CardView cardDeposito = findViewById(R.id.cardDeposito);
        CardView cardRetiro = findViewById(R.id.cardRetiro);
        CardView cardTransferencia = findViewById(R.id.cardTransferencia);
        CardView cardMovimientos = findViewById(R.id.cardMovimientos);
        CardView cardDatosCuenta = findViewById(R.id.cardDatosCuenta);
        ImageButton btnLogout = findViewById(R.id.btnLogout);

        tvWelcome.setText("Bienvenido, " + username);

        cardDeposito.setOnClickListener(v -> {
            Intent intent = new Intent(this, DepositoActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        cardRetiro.setOnClickListener(v -> {
            Intent intent = new Intent(this, RetiroActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        cardTransferencia.setOnClickListener(v -> {
            Intent intent = new Intent(this, TransferenciaActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        cardMovimientos.setOnClickListener(v -> {
            Intent intent = new Intent(this, MovimientosActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        cardDatosCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(this, DatosCuentaActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}