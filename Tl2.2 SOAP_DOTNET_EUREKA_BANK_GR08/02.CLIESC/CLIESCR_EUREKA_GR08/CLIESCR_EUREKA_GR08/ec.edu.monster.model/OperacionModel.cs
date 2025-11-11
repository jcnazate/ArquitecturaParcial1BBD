using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESCR_EUREKA_GR08.ec.edu.monster.model
{
    internal class OperacionModel
    {
        public string AccountNumber { get; set; }
        public string Amount { get; set; }
        public string Type { get; set; } // DEP, RET, TRA
        public string DestinationAccount { get; set; }
    }
}
