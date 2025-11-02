using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.DAO;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.service
{
    public class MovimientoService
    {
        private readonly MovimientoDAO _dao = new MovimientoDAO();

        public List<MovimientoModel> ObtenerMovimiento(string cuenta)
        {
            return _dao.ObtenerMovimientosPorCuenta(cuenta);
        }
    }
}
