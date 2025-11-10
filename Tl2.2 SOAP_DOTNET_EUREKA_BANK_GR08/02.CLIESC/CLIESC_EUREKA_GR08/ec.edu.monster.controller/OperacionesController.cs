using CLIESC_EUREKA_GR08.ec.edu.monster.service;
using CLIESC_EUREKA_GR08.ec.edu.monster.view;
using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class OperacionesController
    {
        private FrmOperaciones _vista;
        private Servicio _servicio;

        public OperacionesController(FrmOperaciones vista)
        {
            _vista = vista;
            _servicio = new Servicio();
            _vista.RealizarClicked += new EventHandler(OnRealizarClicked);
        }

        private void OnRealizarClicked(object sender, EventArgs e)
        {
            string tipoOperacion = _vista.TipoOperacion;
            string cuentaDestino = _vista.CuentaDestino;
            decimal monto = _vista.Monto;

            // Depuración: Verificar el valor de tipoOperacion  
            Console.WriteLine($"[DEBUG] TipoOperacion: '{tipoOperacion}'");

            if (string.IsNullOrEmpty(tipoOperacion) || string.IsNullOrEmpty(cuentaDestino) || monto <= 0)
            {
                _vista.MostrarMensaje("Por favor, complete todos los campos correctamente.");
                return;
            }

            string cuentaOrigen = _vista.CuentaOrigen;
            if (tipoOperacion == "Transferencia" && string.IsNullOrEmpty(cuentaOrigen))
            {
                _vista.MostrarMensaje("Por favor, ingrese la cuenta de origen para la transferencia.");
                return;
            }

            // Mapear tipo de operación a códigos del servidor  
            string tipoCodigo;
            if (tipoOperacion == "Depósito")
            {
                tipoCodigo = "DEP";
            }
            else if (tipoOperacion == "Retiro")
            {
                tipoCodigo = "RET";
            }
            else if (tipoOperacion == "Transferencia")
            {
                tipoCodigo = "TRA";
            }
            else
            {
                throw new ArgumentException($"Tipo de operación no válido: '{tipoOperacion}'");
            }

            // Formatear el monto con coma como separador decimal  
            string montoFormateado = monto.ToString("F2", new CultureInfo("es-ES"));

            // Determinar destinationAccount según el tipo de operación  
            string destinationAccount = tipoOperacion == "Transferencia" ? cuentaDestino : "";

            // Para transferencias, accountNumber debe ser la cuenta de origen  
            string accountNumber = tipoOperacion == "Transferencia" ? cuentaOrigen : cuentaDestino;

            bool resultado = false;
            try
            {
                resultado = _servicio.PerformOperation(accountNumber, montoFormateado, tipoCodigo, destinationAccount);

                if (resultado)
                    _vista.MostrarMensaje($"Operación {tipoOperacion} realizada con éxito.");
                else
                    _vista.MostrarMensaje($"Error al realizar la operación {tipoOperacion}.");
            }
            catch (Exception ex)
            {
                _vista.MostrarMensaje($"Error: {ex.Message}");
            }
        }
    }
}
