using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.contracts
{
    [ServiceContract]
    public interface IMovimientoServicio
    {
        [OperationContract]
        List<MovimientoModel> Movimientos(string numcuenta);
    }
}
