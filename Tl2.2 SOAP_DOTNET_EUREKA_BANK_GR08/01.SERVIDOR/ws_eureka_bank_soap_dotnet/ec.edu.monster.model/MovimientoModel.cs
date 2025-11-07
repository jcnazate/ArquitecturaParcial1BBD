using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Threading.Tasks;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.model
{
    [DataContract]
    public class MovimientoModel
    {
        [DataMember]
        public string CodigoCuenta { get; set; }
        [DataMember]
        public int NumeroMovimiento { get; set; }
        [DataMember]
        public DateTime? FechaMovimientoDt { get; set; }
        [DataMember]
        public string FechaMovimiento { get; set; } // String para coincidir con Java
        [DataMember]
        public string CodigoEmpleado { get; set; }
        [DataMember]
        public string CodigoTipoMovimiento { get; set; }
        [DataMember]
        public string TipoDescripcion { get; set; }
        [DataMember]
        public double ImporteMovimiento { get; set; }
        [DataMember]
        public string CuentaReferencia { get; set; }
        [DataMember]
        public double Saldo { get; set; }
    }
}
