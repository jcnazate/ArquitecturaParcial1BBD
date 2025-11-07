using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.service;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.contracts
{
    // NOTA: puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de clase "LoginServicio" en el código y en el archivo de configuración a la vez.
    public class LoginServicio : ILoginServicio
    {
        private readonly LoginService _service = new LoginService();

        public bool Auth(string username, string password)
        {
            return _service.Login(username, password);
        }
    }
}
