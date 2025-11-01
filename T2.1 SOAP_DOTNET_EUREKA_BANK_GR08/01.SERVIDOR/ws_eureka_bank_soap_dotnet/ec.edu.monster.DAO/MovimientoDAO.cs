using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.DAO

{
    public class MovimientoDAO
    {
        private readonly string connectionString = ConfigurationManager.ConnectionStrings["BankingDB"].ConnectionString;
        public List<MovimientoModel> ObtenerMovimientosPorCuenta(string codigoCuenta, double saldoInicial = 0)
        {
                    string sql = @"
                SELECT 
                    m.chr_cuencodigo,
                    m.int_movinumero,
                    m.dtt_movifecha,
                    m.chr_emplcodigo,
                    m.chr_tipocodigo,
                    m.dec_moviimporte,
                    m.chr_cuenreferencia,
                    t.vch_tipodescripcion AS tipoDescripcion,
                    t.vch_tipoaccion AS tipoAccion   -- <- Asegúrate que exista; si no, deriva con CASE
                FROM movimiento AS m
                INNER JOIN tipomovimiento AS t ON t.chr_tipocodigo = m.chr_tipocodigo
                WHERE m.chr_cuencodigo = @codigoCuenta
                ORDER BY m.int_movinumero ASC";  // IMPORTANTE: ascendente para acumular

                    var movimientos = new List<MovimientoModel>();

                    using (var connection = new SqlConnection(connectionString))
                    using (var command = new SqlCommand(sql, connection))
                    {
                        command.Parameters.AddWithValue("@codigoCuenta", codigoCuenta);
                        connection.Open();

                        double saldoAcumulado = saldoInicial;

                        using (var reader = command.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                            var mov = new MovimientoModel
                            {
                                CodigoCuenta = reader["chr_cuencodigo"].ToString(),
                                NumeroMovimiento = Convert.ToInt32(reader["int_movinumero"]),
                                FechaMovimientoDt = reader["dtt_movifecha"] != DBNull.Value
                                 ? (DateTime?)Convert.ToDateTime(reader["dtt_movifecha"])
                                 : null,
                                            FechaMovimiento = reader["dtt_movifecha"] != DBNull.Value
                                 ? Convert.ToDateTime(reader["dtt_movifecha"]).ToString("yyyy-MM-dd")
                                 : null,
                                CodigoEmpleado = reader["chr_emplcodigo"].ToString(),
                                CodigoTipoMovimiento = reader["chr_tipocodigo"].ToString(),
                                TipoDescripcion = reader["tipoDescripcion"].ToString(),
                                ImporteMovimiento = Convert.ToDouble(reader["dec_moviimporte"]),
                                CuentaReferencia = reader["chr_cuenreferencia"]?.ToString()
                            };

                        // === SALDO ACUMULATIVO ===
                        string tipoAccion = reader["tipoAccion"]?.ToString(); // "INGRESO" o "SALIDA"
                                double importe = mov.ImporteMovimiento;

                                if (string.Equals(tipoAccion, "INGRESO", StringComparison.OrdinalIgnoreCase))
                                    saldoAcumulado += importe;
                                else if (string.Equals(tipoAccion, "SALIDA", StringComparison.OrdinalIgnoreCase))
                                    saldoAcumulado -= importe;

                                mov.Saldo = saldoAcumulado;

                                movimientos.Add(mov);
                            }
                        }
                    }

                    // Si quieres devolver en DESC como antes:
                    movimientos.Reverse();
                    return movimientos;
                }


        public void RegistrarMovimiento(MovimientoModel movimiento)
        {
            string sql = @"INSERT INTO movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, 
                          chr_tipocodigo, dec_moviimporte, chr_cuenreferencia) 
                          VALUES (@codigoCuenta, @numeroMovimiento, @fechaMovimiento, @codigoEmpleado, 
                          @codigoTipoMovimiento, @importeMovimiento, @cuentaReferencia)";

            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                using (SqlCommand command = new SqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@codigoCuenta", movimiento.CodigoCuenta);
                    command.Parameters.AddWithValue("@numeroMovimiento", movimiento.NumeroMovimiento);
                    command.Parameters.AddWithValue("@fechaMovimiento", DateTime.Parse(movimiento.FechaMovimiento));
                    command.Parameters.AddWithValue("@codigoEmpleado", movimiento.CodigoEmpleado);
                    command.Parameters.AddWithValue("@codigoTipoMovimiento", movimiento.CodigoTipoMovimiento);
                    command.Parameters.AddWithValue("@importeMovimiento", movimiento.ImporteMovimiento);
                    command.Parameters.AddWithValue("@cuentaReferencia", (object)movimiento.CuentaReferencia ?? DBNull.Value);

                    connection.Open();
                    command.ExecuteNonQuery();
                }
            }
        }
    }
}
