using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.model;
using ws_eureka_bank_soap_dotnet.ec.edu.monster.service;

namespace ws_eureka_bank_soap_dotnet.ec.edu.monster.contracts
{
    public class MovimientoServicio : IMovimientoServicio
    {
        private readonly MovimientoService _servicio = new MovimientoService();

        public List<MovimientoModel> Movimientos(string numcuenta)
        {
            return _servicio.ObtenerMovimiento(numcuenta);
        }
    }
}
