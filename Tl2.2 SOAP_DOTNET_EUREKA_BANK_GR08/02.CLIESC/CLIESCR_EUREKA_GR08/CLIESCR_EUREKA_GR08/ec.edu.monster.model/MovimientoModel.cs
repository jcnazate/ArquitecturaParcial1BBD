using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESCR_EUREKA_GR08.ec.edu.monster.model
{
    internal class MovimientoModel
    {
        public int NumeroMovimiento { get; set; }
        public string FechaMovimiento { get; set; }
        public string TipoDescripcion { get; set; }
        public double ImporteMovimiento { get; set; }
        public double Saldo { get; set; }
    }
}
