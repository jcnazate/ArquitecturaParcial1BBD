using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.contracts
{
    [ServiceContract]
    public interface ILoginServicio
    {
        [OperationContract]
        bool Auth(string username, string password);
    }
}
