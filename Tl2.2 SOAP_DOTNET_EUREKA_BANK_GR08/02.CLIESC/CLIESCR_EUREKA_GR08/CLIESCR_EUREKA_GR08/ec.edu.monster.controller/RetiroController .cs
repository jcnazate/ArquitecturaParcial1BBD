using System;
using System.Globalization;
using System.Windows.Forms;
using CLIESCR_EUREKA_GR08.ec.edu.monster.service;
using CLIESCR_EUREKA_GR08.ec.edu.monster.view;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class RetiroController
    {
        private readonly FrmRetiro _vista;
        private readonly Servicio _servicio;

        public RetiroController(FrmRetiro vista)
        {
            _vista = vista ?? throw new ArgumentNullException(nameof(vista));
            _servicio = new Servicio();

            _vista.RetirarClicked += OnRetirarClicked;
            _vista.VolverMenuClicked += OnVolverMenuClicked;
            _vista.SalirClicked += OnSalirClicked;
        }

        private void OnRetirarClicked(object sender, EventArgs e)
        {
            string cuenta = _vista.NumeroCuenta;
            string montoTexto = _vista.Monto;

            _vista.OcultarMensaje();

            // Validaciones básicas
            if (string.IsNullOrWhiteSpace(cuenta) || string.IsNullOrWhiteSpace(montoTexto))
            {
                _vista.MostrarMensajeError("Por favor, ingrese número de cuenta y monto.");
                return;
            }

            // Aceptar tanto "10", "10.5" como "10,5"
            if (!decimal.TryParse(
                    montoTexto.Replace(",", "."),
                    NumberStyles.Number,
                    CultureInfo.InvariantCulture,
                    out decimal monto) || monto <= 0)
            {
                _vista.MostrarMensajeError("Ingrese un monto válido mayor a 0.");
                return;
            }

            // Formato para el servicio: siempre con punto decimal
            string montoFormateado = monto.ToString("0.00", CultureInfo.CreateSpecificCulture("es-EC"));

            bool ok;
            try
            {
                // Para retiro:
                // - type: "RET"
                // - accountNumber: cuenta (la que retira)
                // - destinationAccount: "" (no aplica)
                ok = _servicio.PerformOperation(
                    accountNumber: cuenta,
                    amount: montoFormateado,
                    type: "RET",
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
                _vista.MostrarMensajeOk("Retiro realizado con éxito.");
                _vista.Monto = string.Empty;
            }
            else
            {
                _vista.MostrarMensajeError("No se pudo realizar el retiro. Verifique los datos o el saldo.");
            }
        }

        private void OnVolverMenuClicked(object sender, EventArgs e)
        {
            _vista.Close(); // vuelve al menú
        }

        private void OnSalirClicked(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
