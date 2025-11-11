using System;
using System.Globalization;
using System.Windows.Forms;
using CLIESC_EUREKA_GR08.ec.edu.monster.service;
using CLIESC_EUREKA_GR08.ec.edu.monster.view;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class DepositoController
    {
        private readonly FrmDeposito _vista;
        private readonly Servicio _servicio;

        public DepositoController(FrmDeposito vista)
        {
            _vista = vista ?? throw new ArgumentNullException(nameof(vista));
            _servicio = new Servicio();

            _vista.DepositarClicked += OnDepositarClicked;
            _vista.VolverMenuClicked += OnVolverMenuClicked;
            _vista.SalirClicked += OnSalirClicked;
        }

        private void OnDepositarClicked(object sender, EventArgs e)
        {
            string cuenta = _vista.NumeroCuenta;
            string montoTexto = _vista.Monto;

            // Validaciones básicas
            if (string.IsNullOrWhiteSpace(cuenta) || string.IsNullOrWhiteSpace(montoTexto))
            {
                _vista.MostrarMensajeError("Por favor, ingrese número de cuenta y monto.");
                return;
            }

            if (!decimal.TryParse(
        montoTexto.Replace(",", "."),
        NumberStyles.Number,
        CultureInfo.InvariantCulture,
        out decimal monto) || monto <= 0)
            {
                _vista.MostrarMensajeError("Ingrese un monto válido mayor a 0.");
                return;
            }

            // Formatear con punto decimal para el servicio
            string montoFormateado = monto.ToString("0.00", CultureInfo.InvariantCulture);

            bool ok;
            try
            {
                ok = _servicio.PerformOperation(
                    accountNumber: cuenta,
                    amount: montoFormateado,   // <-- ahora "1.00", "10.00", etc.
                    type: "DEP",
                    destinationAccount: ""
                );
            }
            catch (Exception ex)
            {
                _vista.MostrarMensajeError($"Error en el servicio: {ex.Message}");
                return;
            }

            if (ok)
            {
                _vista.MostrarMensajeOk("Depósito realizado con éxito.");
                _vista.Monto = string.Empty;
            }
            else
            {
                _vista.MostrarMensajeError("No se pudo realizar el depósito. Verifique los datos.");
            }
        }

        private void OnVolverMenuClicked(object sender, EventArgs e)
        {
            _vista.Close(); // vuelve al menú que abrió este form
        }

        private void OnSalirClicked(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
