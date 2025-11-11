using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Configuration;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.DAO
{
    public class CuentaDAO
    {
        private readonly string connectionString = ConfigurationManager.ConnectionStrings["BankingDB"].ConnectionString;

        public bool ActualizarSaldoCuenta(string codigoCuenta, double valorMovimiento)
        {
            string sql = "UPDATE cuenta SET dec_cuensaldo = dec_cuensaldo + @valor, int_cuencontmov = int_cuencontmov + 1 WHERE chr_cuencodigo = @codigoCuenta";

            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                using (SqlCommand command = new SqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@valor", valorMovimiento);
                    command.Parameters.AddWithValue("@codigoCuenta", codigoCuenta);

                    connection.Open();
                    int rowsAffected = command.ExecuteNonQuery();
                    return rowsAffected > 0;
                }
            }
        }

        public CuentaModel ObtenerCuentaPorCodigo(string codigoCuenta)
        {
            string sql = "SELECT * FROM cuenta WHERE chr_cuencodigo = @codigoCuenta";

            using (SqlConnection connection = new SqlConnection(connectionString))
            {
                using (SqlCommand command = new SqlCommand(sql, connection))
                {
                    command.Parameters.AddWithValue("@codigoCuenta", codigoCuenta);

                    connection.Open();
                    using (SqlDataReader reader = command.ExecuteReader())
                    {
                        if (reader.Read())
                        {
                            return new CuentaModel
                            {
                                ChrCuenCodigo = reader["chr_cuencodigo"].ToString(),
                                ChrMoneCodigo = reader["chr_monecodigo"].ToString(),
                                ChrSucucodigo = reader["chr_sucucodigo"].ToString(),
                                ChrEmplCreaCuenta = reader["chr_emplcreacuenta"].ToString(),
                                ChrClieCodigo = reader["chr_cliecodigo"].ToString(),
                                DecCuenSaldo = Convert.ToDouble(reader["dec_cuensaldo"]),
                                DttCuenFechaCreacion = Convert.ToDateTime(reader["dtt_cuenfechacreacion"]),
                                VchCuenEstado = reader["vch_cuenestado"].ToString(),
                                IntCuenContMov = Convert.ToInt32(reader["int_cuencontmov"]),
                                ChrCuenClave = reader["chr_cuenclave"].ToString()
                            };
                        }
                    }
                }
            }
            return null;
        }
    }
}
