using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.service
{
    public class LoginService
    {
        public bool Login(string username, string password)
        {
            return (username == "MONSTER" && password == "MONSTER9") ||
                   (username == "admin" && password == "admin");
        }
    }
}
