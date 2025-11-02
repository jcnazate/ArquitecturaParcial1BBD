using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.contracts
{
    // NOTA: puede usar el comando "Cambiar nombre" del menú "Refactorizar" para cambiar el nombre de interfaz "ICuentaServicio" en el código y en el archivo de configuración a la vez.
    [ServiceContract(Namespace = "http://tempuri.org/")]
    public interface ICuentaServicio
    {
        [OperationContract]
        bool Deposito(string cuenta, string monto, string tipo, string cuentaDestino);

        [OperationContract]
        CuentaModel ObtenerCuentaPorNumero(string cuenta);
    }
}
