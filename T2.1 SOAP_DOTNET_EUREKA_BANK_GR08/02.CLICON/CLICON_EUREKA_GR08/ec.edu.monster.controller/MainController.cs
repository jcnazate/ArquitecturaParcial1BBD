using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using CLICON_EUREKA_GR08.LoginServicio;
using CLICON_EUREKA_GR08.CuentaServicio;
using CLICON_EUREKA_GR08.MovimientoServicio;

namespace CLICON_EUREKA_GR08.ec.edu.monster.controller
{
    public class MainController
    {
        private string usuarioActual = null;

        public string GetUsuarioActual()
        {
            return usuarioActual;
        }

        public bool IniciarSesion(string username, string password)
        {
            try
            {
                using (var client = new LoginServicioClient())
                {
                    if (client.Auth(username, password))
                    {
                        usuarioActual = username;
                        return true;
                    }
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error en la autenticación: " + ex.Message);
            }
            return false;
        }

        public void CerrarSesion()
        {
            usuarioActual = null;
        }

        public bool RealizarDeposito(string cuenta, string monto, string tipo, string cd)
        {
            try
            {
                using (var client = new CuentaServicioClient())
                {
                    return client.Deposito(cuenta, monto, tipo, cd);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error al realizar depósito: " + ex.Message);
                return false;
            }
        }

        public bool RealizarRetiro(string cuenta, string monto)
        {
            return RealizarDeposito(cuenta, monto, "RET", null);
        }

        public bool RealizarTransferencia(string cuentaOrigen, string cuentaDestino, string monto)
        {
            // Seguir la misma lógica del cliente Java: RET + TRA
            bool retiroExitoso = RealizarRetiro(cuentaOrigen, monto);
            if (retiroExitoso)
            {
                return RealizarDeposito(cuentaDestino, monto, "TRA", cuentaOrigen);
            }
            return false;
        }

        public string VerMovimientos(string cuenta)
        {
            try
            {
                List<MovimientoModel> movimientos = null;
                CuentaModel cuentaModel = null;

                using (var movimientoClient = new MovimientoServicioClient())
                using (var cuentaClient = new CuentaServicioClient())
                {
                    movimientos = movimientoClient.Movimientos(cuenta)?.ToList() ?? new List<MovimientoModel>();
                    cuentaModel = cuentaClient.ObtenerCuentaPorNumero(cuenta);
                }

                StringBuilder resultado = new StringBuilder();
                if (movimientos == null || movimientos.Count == 0)
                {
                    resultado.Append("No se encontraron movimientos para la cuenta: ").Append(cuenta).AppendLine();
                    return EncloseInBox("MOVIMIENTOS DE LA CUENTA " + cuenta, resultado.ToString());
                }

                // Mostrar saldo al inicio
                if (cuentaModel != null)
                {
                    resultado.AppendFormat("Saldo Actual de la Cuenta: {0:F2}", cuentaModel.DecCuenSaldo).AppendLine();
                    resultado.AppendLine();
                }

                resultado.AppendFormat("{0,-10} {1,-20} {2,-12} {3,-20} {4,-15} {5,-15} {6,-15}",
                    "Nro", "Fecha", "Tipo Mov.", "Descripción", "Importe", "Cta Ref.", "Saldo").AppendLine();
                resultado.AppendLine("----------------------------------------------------------------------------------------------------");

                foreach (var mov in movimientos)
                {
                    string fecha = mov.FechaMovimientoDt?.ToString("dd/MM/yyyy HH:mm") ?? mov.FechaMovimiento ?? "N/A";
                    string cuentaRef = mov.CuentaReferencia ?? "N/A";
                    string tipoDesc = mov.TipoDescripcion ?? "N/A";

                    resultado.AppendFormat("{0,-10} {1,-20} {2,-12} {3,-20} {4,-15:F2} {5,-15} {6,-15:F2}",
                        mov.NumeroMovimiento,
                        fecha,
                        mov.CodigoTipoMovimiento ?? "N/A",
                        tipoDesc,
                        mov.ImporteMovimiento,
                        cuentaRef,
                        mov.Saldo).AppendLine();
                }

                // Calcular totales
                double totalIngresos = movimientos
                    .Where(m => m.TipoDescripcion != null && !m.TipoDescripcion.Equals("Retiro", StringComparison.OrdinalIgnoreCase))
                    .Sum(m => m.ImporteMovimiento);

                double totalEgresos = movimientos
                    .Where(m => m.TipoDescripcion != null && m.TipoDescripcion.Equals("Retiro", StringComparison.OrdinalIgnoreCase))
                    .Sum(m => m.ImporteMovimiento);

                resultado.AppendLine();
                resultado.AppendLine("RESUMEN:");
                resultado.AppendFormat("Total Ingresos: {0:F2}", totalIngresos).AppendLine();
                resultado.AppendFormat("Total Egresos (Retiros): {0:F2}", Math.Abs(totalEgresos)).AppendLine();
                resultado.AppendFormat("Saldo Neto: {0:F2}", totalIngresos + totalEgresos).AppendLine();

                // Mostrar saldo actual de la cuenta
                if (cuentaModel != null)
                {
                    resultado.AppendFormat("Saldo Actual de la Cuenta: {0:F2}", cuentaModel.DecCuenSaldo).AppendLine();
                }

                return EncloseInBox("MOVIMIENTOS DE LA CUENTA " + cuenta, resultado.ToString());
            }
            catch (Exception ex)
            {
                return "Error al obtener los movimientos: " + ex.Message;
            }
        }

        public string VerDatosCuenta(string cuenta)
        {
            try
            {
                CuentaModel cuentaModel = null;

                using (var client = new CuentaServicioClient())
                {
                    cuentaModel = client.ObtenerCuentaPorNumero(cuenta);
                }

                if (cuentaModel == null)
                {
                    return "No se encontraron datos para la cuenta: " + cuenta;
                }

                StringBuilder resultado = new StringBuilder();
                resultado.AppendFormat("{0,-20} {1,-30}", "Campo", "Valor").AppendLine();
                resultado.AppendLine("--------------------------------------------------");
                resultado.AppendFormat("{0,-20} {1,-30:F2}", "Saldo", cuentaModel.DecCuenSaldo).AppendLine();
                resultado.AppendFormat("{0,-20} {1,-30}", "Contador Movimientos", cuentaModel.IntCuenContMov).AppendLine();
                resultado.AppendLine("--------------------------------------------------");

                return EncloseInBox("DATOS DE LA CUENTA " + cuenta, resultado.ToString());
            }
            catch (Exception ex)
            {
                return "Error al obtener los datos de la cuenta: " + ex.Message;
            }
        }

        // Utilidades para crear recuadros ASCII
        private string EncloseInBox(string title, string multilineContent)
        {
            string[] lines = multilineContent.Split(new[] { "\r\n", "\r", "\n" }, StringSplitOptions.None);
            int maxLineLength = title.Length;
            foreach (string line in lines)
            {
                if (line.Length > maxLineLength)
                {
                    maxLineLength = line.Length;
                }
            }

            int innerWidth = Math.Max(maxLineLength, title.Length);
            string horizontal = Repeat('-', innerWidth + 2);

            StringBuilder box = new StringBuilder();
            box.Append('+').Append(horizontal).Append("+").AppendLine();
            box.Append('|').Append(PadCenter(" " + title + " ", innerWidth + 2)).Append('|').AppendLine();
            box.Append('+').Append(horizontal).Append("+").AppendLine();
            foreach (string line in lines)
            {
                box.Append('|').Append(PadRight(" " + line, innerWidth + 2)).Append('|').AppendLine();
            }
            box.Append('+').Append(horizontal).Append('+');
            return box.ToString();
        }

        private string Repeat(char ch, int count)
        {
            return new string(ch, count);
        }

        private string PadRight(string text, int width)
        {
            if (text.Length >= width)
            {
                return text;
            }
            return text.PadRight(width);
        }

        private string PadCenter(string text, int width)
        {
            if (text.Length >= width)
            {
                return text;
            }
            int totalPadding = width - text.Length;
            int left = totalPadding / 2;
            int right = totalPadding - left;
            return new string(' ', left) + text + new string(' ', right);
        }
    }
}

