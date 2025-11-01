using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.model
{
    [Serializable]
    public class MovimientoModel
    {
        public string CodigoCuenta { get; set; }
        public int NumeroMovimiento { get; set; }
        public DateTime? FechaMovimientoDt { get; set; }
        public string FechaMovimiento { get; set; } // String para coincidir con Java
        public string CodigoEmpleado { get; set; }
        public string CodigoTipoMovimiento { get; set; }
        public string TipoDescripcion { get; set; }
        public double ImporteMovimiento { get; set; }
        public string CuentaReferencia { get; set; }
        public double Saldo { get; set; }
    }
}
