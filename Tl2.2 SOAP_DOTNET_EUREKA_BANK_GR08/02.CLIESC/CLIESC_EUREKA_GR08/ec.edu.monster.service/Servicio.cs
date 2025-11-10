using CLIESC_EUREKA_GR08.WSCuentaReference;
using CLIESC_EUREKA_GR08.WSLoginReference;
using CLIESC_EUREKA_GR08.WSMovimientoReference;
using CLIESC_EUREKA_GR08.ec.edu.monster.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.service
{
    internal class Servicio
    {
        public bool Authenticate(string username, string password)
        {
            using (var loginClient = new WSLoginReference.LoginServicioClient())
            {
                try
                {
                    return loginClient.Auth(username, password);
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error en autenticación: {ex.Message}");
                    return false;
                }
            }
        }

        public ec.edu.monster.model.CuentaModel GetAccount(string accountNumber)
        {
            using (var cuentaClient = new WSCuentaReference.CuentaServicioClient())
            {
                try
                {
                    WSCuentaReference.CuentaModel account = cuentaClient.ObtenerCuentaPorNumero(accountNumber);
                    if (account == null) return null;
                    return new ec.edu.monster.model.CuentaModel
                    {
                        CodigoCuenta = account.ChrCuenCodigo,
                        Saldo = account.DecCuenSaldo,
                        FechaCreacion = account.DttCuenFechaCreacion,
                        Estado = account.VchCuenEstado
                    };
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error al obtener cuenta: {ex.Message}");
                    return null;
                }
            }
        }

        public List<ec.edu.monster.model.MovimientoModel> GetMovements(string accountNumber)
        {
            using (var movimientoClient = new WSMovimientoReference.MovimientoServicioClient())
            {
                try
                {
                    WSMovimientoReference.MovimientoModel[] movements = movimientoClient.Movimientos(accountNumber);
                    var result = movements?.Select(m => new ec.edu.monster.model.MovimientoModel
                    {
                        NumeroMovimiento = m.NumeroMovimiento,
                        FechaMovimiento = DateTime.Parse(m.FechaMovimiento).ToString("dd/MM/yyyy"),
                        TipoDescripcion = m.TipoDescripcion,
                        ImporteMovimiento = m.ImporteMovimiento,
                        Saldo = m.Saldo
                    }).ToList() ?? new List<ec.edu.monster.model.MovimientoModel>();
                    Console.WriteLine($"[DEBUG] Obtenidos {result.Count} movimientos para cuenta {accountNumber}");
                    return result;
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error al obtener movimientos: {ex.Message}");
                    return new List<ec.edu.monster.model.MovimientoModel>();
                }
            }
        }

        public bool PerformOperation(string accountNumber, string amount, string type, string destinationAccount)
        {
            using (var cuentaClient = new WSCuentaReference.CuentaServicioClient())
            {
                try
                {
                    bool result = cuentaClient.Deposito(accountNumber, amount, type, destinationAccount);
                    Console.WriteLine($"[DEBUG] Operación {type} para cuenta {accountNumber} con monto {amount}: {(result ? "Éxito" : "Fallido")}");
                    return result;
                }
                catch (Exception ex)
                {
                    Console.WriteLine($"Error en operación: {ex.Message}");
                    return false;
                }
            }
        }
    }
}
