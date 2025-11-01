using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.model
{
    [Serializable]
    public class CuentaModel
    {
        public string ChrCuenCodigo { get; set; }       // Código de la cuenta
    public string ChrMoneCodigo { get; set; }       // Código de moneda
    public string ChrSucucodigo { get; set; }       // Código de sucursal
    public string ChrEmplCreaCuenta { get; set; }   // Código de empleado que crea la cuenta
    public string ChrClieCodigo { get; set; }       // Código de cliente
    public double DecCuenSaldo { get; set; }        // Saldo de la cuenta
    public DateTime DttCuenFechaCreacion { get; set; } // Fecha de creación
    public string VchCuenEstado { get; set; }       // Estado (ACTIVO, ANULADO, CANCELADO)
    public int IntCuenContMov { get; set; }         // Contador de movimientos
    public string ChrCuenClave { get; set; }        // Clave de la cuenta
 }
}

