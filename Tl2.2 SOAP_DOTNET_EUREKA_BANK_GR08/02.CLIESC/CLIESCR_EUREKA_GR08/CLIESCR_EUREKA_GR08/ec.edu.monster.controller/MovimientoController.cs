using CLIESCR_EUREKA_GR08.ec.edu.monster.service;
using CLIESCR_EUREKA_GR08.ec.edu.monster.view;
using CLIESCR_EUREKA_GR08.ec.edu.monster.model;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Globalization;

namespace CLIESCR_EUREKA_GR08.ec.edu.monster.controller
{
    internal class MovimientoController
    {
        private readonly FrmMovimiento _vista;
        private readonly Servicio _servicio;

        public MovimientoController(FrmMovimiento vista)
        {
            _vista = vista ?? throw new ArgumentNullException(nameof(vista));
            _servicio = new Servicio();
            _vista.BuscarClicked += OnBuscarClicked;
        }

        private void OnBuscarClicked(object sender, EventArgs e)
        {
            string accountNumber = _vista.NumeroCuenta;

            if (string.IsNullOrWhiteSpace(accountNumber))
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
            _vista.TotalMovimientos = movimientos.Count.ToString(CultureInfo.InvariantCulture);

            var items = new List<MovimientoItem>();

            foreach (var mov in movimientos)
            {
                string accion = CalcularAccionDesdeDescripcion(mov);

                // Definir color según Crédito/Débito
                Color colorAccion;
                if (accion == "Crédito")
                {
                    colorAccion = Color.FromArgb(0, 160, 60);      // verde
                }
                else if (accion == "Débito")
                {
                    colorAccion = Color.FromArgb(210, 50, 45);     // rojo
                }
                else
                {
                    colorAccion = Color.FromArgb(90, 90, 90);      // gris neutro
                }

                var item = new MovimientoItem
                {
                    Cuenta = accountNumber,
                    Fecha = mov.FechaMovimiento,
                    Movimiento = mov.NumeroMovimiento.ToString(CultureInfo.InvariantCulture),
                    Tipo = mov.TipoDescripcion,
                    Accion = accion,
                    Importe = $"${mov.ImporteMovimiento:F2}",
                    SaldoActual = $"${mov.Saldo:F2}",

                    // Estas propiedades deben existir en MovimientoItem
                    AccionColor = colorAccion,
                    ImporteColor = colorAccion
                };

                items.Add(item);
            }

            _vista.AgregarMovimientos(items);
        }

        private string CalcularAccionDesdeDescripcion(MovimientoModel mov)
        {
            if (mov == null || string.IsNullOrWhiteSpace(mov.TipoDescripcion))
                return mov.ImporteMovimiento >= 0 ? "Crédito" : "Débito";

            string tipo = mov.TipoDescripcion.Trim().ToUpperInvariant();

            // Si viene como código (ej: 003, 004, 008, 009)
            bool esCodigo = tipo.Length == 3 && int.TryParse(tipo, out _);
            if (esCodigo)
            {
                switch (tipo)
                {
                    case "001": // Apertura
                    case "003": // Depósito
                    case "009": // Transferencia recibida
                        return "Crédito";

                    case "004": // Retiro
                    case "008": // Transferencia enviada
                        return "Débito";
                }
            }

            // Texto descriptivo
            if (tipo.Contains("APERTURA"))
                return "Crédito";

            if (tipo.Contains("DEPOSITO") || tipo.Contains("DEPÓSITO"))
                return "Crédito";

            if (tipo.Contains("RETIRO"))
                return "Débito";

            if (tipo.Contains("CREDITO") || tipo.Contains("CRÉDITO") || tipo.Contains("ABONO"))
                return "Crédito";

            if (tipo.Contains("DEBITO") || tipo.Contains("DÉBITO"))
                return "Débito";

            if (tipo.Contains("TRANSFER"))
            {
                if (tipo.Contains("RECIB") || tipo.Contains("ENTR") || tipo.Contains("A FAVOR"))
                    return "Crédito";   // transferencia recibida
                else
                    return "Débito";    // transferencia enviada
            }

            // Fallback
            return mov.ImporteMovimiento >= 0 ? "Crédito" : "Débito";
        }
    }
}
