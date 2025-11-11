using System;
using System.Globalization;
using System.Windows.Forms;
using CLIESCR_EUREKA_GR08.ec.edu.monster.service;
using CLIESCR_EUREKA_GR08.ec.edu.monster.view;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class TransferenciaController
    {
        private readonly FrmTransferencia _vista;
        private readonly Servicio _servicio;

        public TransferenciaController(FrmTransferencia vista)
        {
            _vista = vista ?? throw new ArgumentNullException(nameof(vista));
            _servicio = new Servicio();

            _vista.TransferirClicked += OnTransferirClicked;
            _vista.VolverMenuClicked += OnVolverMenuClicked;
            _vista.SalirClicked += OnSalirClicked;
        }

        private void OnTransferirClicked(object sender, EventArgs e)
        {
            string cuentaOrigen = _vista.CuentaOrigen;
            string cuentaDestino = _vista.CuentaDestino;
            string montoTexto = _vista.Monto;

            _vista.OcultarMensaje();

            // Validaciones básicas
            if (string.IsNullOrWhiteSpace(cuentaOrigen) ||
                string.IsNullOrWhiteSpace(cuentaDestino) ||
                string.IsNullOrWhiteSpace(montoTexto))
            {
                _vista.MostrarMensajeError("Por favor, complete todos los campos.");
                return;
            }

            if (cuentaOrigen == cuentaDestino)
            {
                _vista.MostrarMensajeError("La cuenta de origen y destino no pueden ser la misma.");
                return;
            }

            // Aceptar "10", "10.5" o "10,5"
            if (!decimal.TryParse(
                    montoTexto.Replace(",", "."),
                    NumberStyles.Number,
                    CultureInfo.InvariantCulture,
                    out decimal monto) || monto <= 0)
            {
                _vista.MostrarMensajeError("Ingrese un monto válido mayor a 0.");
                return;
            }

            // Formato correcto para el servicio (punto decimal)
            string montoFormateado = monto.ToString("0.00", CultureInfo.CreateSpecificCulture("es-EC"));

            bool ok;
            try
            {
                // Transferencia:
                // - type: "TRA"
                // - accountNumber: cuentaOrigen
                // - destinationAccount: cuentaDestino
                ok = _servicio.PerformOperation(
                    accountNumber: cuentaOrigen,
                    amount: montoFormateado,
                    type: "TRA",
                    destinationAccount: cuentaDestino
                );
            }
            catch (Exception ex)
            {
                _vista.MostrarMensajeError($"Error en el servicio: {ex.Message}");
                return;
            }

            if (ok)
            {
                _vista.MostrarMensajeOk("Transferencia realizada con éxito.");
                _vista.Monto = string.Empty;
            }
            else
            {
                _vista.MostrarMensajeError("No se pudo realizar la transferencia. Verifique los datos o el saldo.");
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
