using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.model
{
    internal class CuentaModel
    {
        public string CodigoCuenta { get; set; }
        public double Saldo { get; set; }
        public DateTime FechaCreacion { get; set; }
        public string Estado { get; set; }
    }
}
