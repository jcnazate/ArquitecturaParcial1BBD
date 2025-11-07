using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.DAO;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.service
{
    public class CuentaService
    {
        private readonly MovimientoDAO _movimientoDAO = new MovimientoDAO();
        private readonly CuentaDAO _cuentaDAO = new CuentaDAO();

        public bool ActualizarSaldoYRegistrarMovimiento(string codigoCuenta, string valorMovimiento, string tipo, string cuentaDest)
        {
            try
            {
                double importe = double.Parse(valorMovimiento);
                string codTipo = "";

                if (tipo.Equals("RET", StringComparison.OrdinalIgnoreCase))
                {
                    codTipo = "004";
                    var cuenta = _cuentaDAO.ObtenerCuentaPorCodigo(codigoCuenta);
                    if (cuenta == null || cuenta.DecCuenSaldo < importe)
                        return false;

                    if (!_cuentaDAO.ActualizarSaldoCuenta(codigoCuenta, -importe))
                        return false;
                }
                else if (tipo.Equals("DEP", StringComparison.OrdinalIgnoreCase))
                {
                    codTipo = "003";
                    if (!_cuentaDAO.ActualizarSaldoCuenta(codigoCuenta, importe))
                        return false;
                }
                else if (tipo.Equals("TRA", StringComparison.OrdinalIgnoreCase))
                {
                    codTipo = "009";
                    if (string.IsNullOrEmpty(cuentaDest))
                        throw new ArgumentException("La cuenta destino es obligatoria para transferencias.");

                    var cuentaOrigen = _cuentaDAO.ObtenerCuentaPorCodigo(codigoCuenta);
                    if (cuentaOrigen == null || cuentaOrigen.DecCuenSaldo < importe)
                        return false;

                    if (!_cuentaDAO.ActualizarSaldoCuenta(codigoCuenta, -importe))
                        return false;

                    if (!_cuentaDAO.ActualizarSaldoCuenta(cuentaDest, importe))
                    {
                        _cuentaDAO.ActualizarSaldoCuenta(codigoCuenta, importe); // Rollback
                        return false;
                    }
                }
                else
                {
                    throw new ArgumentException($"Tipo de movimiento no soportado: {tipo}");
                }

                int numeroMovimiento = ObtenerSiguienteNumeroMovimiento(codigoCuenta);

                var movimiento = new MovimientoModel
                {
                    CodigoCuenta = codigoCuenta,
                    NumeroMovimiento = numeroMovimiento,
                    FechaMovimiento = DateTime.Now.ToString("yyyy-MM-dd"),
                    CodigoEmpleado = "0001",
                    CodigoTipoMovimiento = codTipo,
                    ImporteMovimiento = importe,
                    CuentaReferencia = cuentaDest
                };

                _movimientoDAO.RegistrarMovimiento(movimiento);

                if (tipo.Equals("TRA", StringComparison.OrdinalIgnoreCase))
                {
                    int numeroMovimientoDest = ObtenerSiguienteNumeroMovimiento(cuentaDest);
                    var movimientoDest = new MovimientoModel
                    {
                        CodigoCuenta = cuentaDest,
                        NumeroMovimiento = numeroMovimientoDest,
                        FechaMovimiento = DateTime.Now.ToString("yyyy-MM-dd"),
                        CodigoEmpleado = "0001",
                        CodigoTipoMovimiento = "008",
                        ImporteMovimiento = importe
                    };
                    _movimientoDAO.RegistrarMovimiento(movimientoDest);
                }

                return true;
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return false;
            }
        }

        private int ObtenerSiguienteNumeroMovimiento(string codigoCuenta)
        {
            var movimientos = _movimientoDAO.ObtenerMovimientosPorCuenta(codigoCuenta);
            int numeroMovimiento = 1;

            foreach (var movimiento in movimientos)
            {
                if (movimiento.NumeroMovimiento >= numeroMovimiento)
                    numeroMovimiento = movimiento.NumeroMovimiento + 1;
            }
            return numeroMovimiento;
        }

        public CuentaModel ObtenerCuenta(string codigoCuenta)
        {
            return _cuentaDAO.ObtenerCuentaPorCodigo(codigoCuenta);
        }
    }
}
