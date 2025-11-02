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
    // NOTA: puede usar el comando "Rename" del menú "Refactorizar" para cambiar el nombre de clase "CuentaServicio" en el código y en el archivo de configuración a la vez.
    public class CuentaServicio : ICuentaServicio
    {
        private readonly CuentaService _servicio = new CuentaService();

        public bool Deposito(string cuenta, string monto, string tipo, string cuentaDestino)
        {
            return _servicio.ActualizarSaldoYRegistrarMovimiento(cuenta, monto, tipo, cuentaDestino);
        }

        public CuentaModel ObtenerCuentaPorNumero(string cuenta)
        {
            return _servicio.ObtenerCuenta(cuenta);
        }
    }
}
