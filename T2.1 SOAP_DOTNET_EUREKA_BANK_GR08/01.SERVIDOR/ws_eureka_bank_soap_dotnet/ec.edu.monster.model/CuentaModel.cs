using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.model
{
    [DataContract]
    public class CuentaModel
    {
        [DataMember]
        public string ChrCuenCodigo { get; set; }       // Código de la cuenta
        [DataMember]
        public string ChrMoneCodigo { get; set; }       // Código de moneda
        [DataMember]
        public string ChrSucucodigo { get; set; }       // Código de sucursal
        [DataMember]
        public string ChrEmplCreaCuenta { get; set; }   // Código de empleado que crea la cuenta
        [DataMember]
        public string ChrClieCodigo { get; set; }       // Código de cliente
        [DataMember]
        public double DecCuenSaldo { get; set; }        // Saldo de la cuenta
        [DataMember]
        public DateTime DttCuenFechaCreacion { get; set; } // Fecha de creación
        [DataMember]
        public string VchCuenEstado { get; set; }       // Estado (ACTIVO, ANULADO, CANCELADO)
        [DataMember]
        public int IntCuenContMov { get; set; }         // Contador de movimientos
        [DataMember]
        public string ChrCuenClave { get; set; }        // Clave de la cuenta
    }
}

