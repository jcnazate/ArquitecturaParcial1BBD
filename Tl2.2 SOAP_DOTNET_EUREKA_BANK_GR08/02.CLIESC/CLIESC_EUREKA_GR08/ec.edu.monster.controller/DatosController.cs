using System;
using System.Windows.Forms;
using CLIESC_EUREKA_GR08.ec.edu.monster.service;
using CLIESC_EUREKA_GR08.ec.edu.monster.view;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class DatosController
    {
        private readonly FrmDatos _vista;
        private readonly Servicio _servicio;

        public DatosController(FrmDatos vista)
        {
            _vista = vista ?? throw new ArgumentNullException(nameof(vista));
            _servicio = new Servicio();

            // Suscribir eventos del formulario
            _vista.ConsultarClicked += OnConsultarClicked;
            _vista.VolverMenuClicked += OnVolverMenuClicked;
            _vista.SalirClicked += OnSalirClicked;

            // Ocultar panel de información al inicio
            _vista.MostrarPanelInfo(false);
        }

        private void OnConsultarClicked(object sender, EventArgs e)
        {
            string nroCuenta = _vista.NumeroCuenta;

            if (string.IsNullOrWhiteSpace(nroCuenta))
            {
                _vista.MostrarMensajeError("Por favor, ingrese un número de cuenta.");
                _vista.MostrarPanelInfo(false);
                return;
            }

            // Obtener cuenta
            var cuenta = _servicio.GetAccount(nroCuenta);
            if (cuenta == null)
            {
                _vista.MostrarMensajeError("Cuenta no encontrada.");
                _vista.MostrarPanelInfo(false);
                return;
            }

            // Obtener movimientos solo para contar
            var movimientos = _servicio.GetMovements(nroCuenta);

            // Llenar datos en la vista
            _vista.CodigoCuenta = cuenta.CodigoCuenta;
            _vista.Saldo = $"${cuenta.Saldo:F2}";
            _vista.ContadorMovimientos = (movimientos?.Count ?? 0).ToString();
            _vista.FechaCreacion = cuenta.FechaCreacion.ToString("dd/MM/yyyy");
            _vista.Estado = cuenta.Estado;

            // Si no tienes sucursal en el servicio, muestra algo fijo o vacío
            _vista.Sucursal = "No disponible";

            _vista.MostrarPanelInfo(true);
        }

        private void OnVolverMenuClicked(object sender, EventArgs e)
        {
            // Volver al menú: simplemente cerramos FrmDatos.
            _vista.Close();
        }

        private void OnSalirClicked(object sender, EventArgs e)
        {
            Application.Exit();
        }
    }
}
