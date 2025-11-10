using CLIESC_EUREKA_GR08.ec.edu.monster.service;
using CLIESC_EUREKA_GR08.ec.edu.monster.view;
using CLIESC_EUREKA_GR08.ec.edu.monster.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class MovimientosController
    {
        private FrmMovimientos _vista;
        private Servicio _servicio;

        public MovimientosController(FrmMovimientos vista)
        {
            _vista = vista;
            _servicio = new Servicio();
            _vista.BuscarClicked += new EventHandler(OnBuscarClicked);
        }

        private void OnBuscarClicked(object sender, EventArgs e)
        {
            string accountNumber = _vista.NumeroCuenta;

            if (string.IsNullOrEmpty(accountNumber))
            {
                _vista.MostrarMensaje("Por favor, ingrese un número de cuenta.");
                return;
            }

            List<MovimientoModel> movimientos = _servicio.GetMovements(accountNumber);
            CuentaModel cuenta = _servicio.GetAccount(accountNumber);

            if (cuenta == null)
            {
                _vista.MostrarMensaje("Cuenta no encontrada.");
                return;
            }

            _vista.Saldo = $"${cuenta.Saldo:F2}";
            _vista.TotalMovimientos = movimientos.Count.ToString();

            List<MovimientoItem> movimientoItems = new List<MovimientoItem>();
            foreach (var movimiento in movimientos)
            {
                MovimientoItem item = new MovimientoItem
                {
                    Cuenta = accountNumber,
                    Fecha = movimiento.FechaMovimiento,
                    Movimiento = movimiento.NumeroMovimiento.ToString(),
                    Tipo = movimiento.TipoDescripcion,
                    Accion = "Crédito", // Ajusta según la lógica del servicio
                    Importe = $"${movimiento.ImporteMovimiento:F2}",
                    SaldoActual = $"${movimiento.Saldo:F2}"
                };
                movimientoItems.Add(item);
            }

            _vista.AgregarMovimientos(movimientoItems);
        }
    }
}
