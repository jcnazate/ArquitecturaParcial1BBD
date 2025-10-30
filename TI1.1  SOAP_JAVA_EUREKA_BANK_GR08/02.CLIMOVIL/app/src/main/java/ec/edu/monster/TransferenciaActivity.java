package ec.edu.monster;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ec.edu.monster.service.CuentaService;

public class TransferenciaActivity extends AppCompatActivity {

    private EditText etCuentaOrigen, etCuentaDestino, etMonto;
    private Button btnRealizar;
    private ProgressBar progressBar;
    private CuentaService cuentaService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);

        cuentaService = new CuentaService();

        etCuentaOrigen = findViewById(R.id.etCuentaOrigen);
        etCuentaDestino = findViewById(R.id.etCuentaDestino);
        etMonto = findViewById(R.id.etMonto);
        btnRealizar = findViewById(R.id.btnRealizar);
        progressBar = findViewById(R.id.progressBar);

        btnRealizar.setOnClickListener(v -> {
            String cuentaOrigen = etCuentaOrigen.getText().toString().trim();
            String cuentaDestino = etCuentaDestino.getText().toString().trim();
            String monto = etMonto.getText().toString().trim();

            if (cuentaOrigen.isEmpty() || cuentaDestino.isEmpty() || monto.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            new TransferenciaTask().execute(cuentaOrigen, cuentaDestino, monto);
        });
    }

    private class TransferenciaTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            btnRealizar.setEnabled(false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String cuentaOrigen = params[0];
            String cuentaDestino = params[1];
            String monto = params[2];

            // Primero retiro de la cuenta origen
            boolean retiroExitoso = cuentaService.realizarDeposito(cuentaOrigen, monto, "RET", null);
            if (retiroExitoso) {
                // Luego depósito en la cuenta destino
                return cuentaService.realizarDeposito(cuentaDestino, monto, "TRA", cuentaOrigen);
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setVisibility(View.GONE);
            btnRealizar.setEnabled(true);

            if (success) {
                Toast.makeText(TransferenciaActivity.this, "Transferencia realizada con éxito", Toast.LENGTH_SHORT).show();
                etCuentaOrigen.setText("");
                etCuentaDestino.setText("");
                etMonto.setText("");
            } else {
                Toast.makeText(TransferenciaActivity.this, "Error al realizar la transferencia", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

