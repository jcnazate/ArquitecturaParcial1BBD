using System;
using System.Web.Mvc;
using CLIWEB_EUREKA_GR08.CuentaServicio;

namespace CLIWEB_EUREKA_GR08.ec.edu.monster.controller
{
    public class TransferenciaController : Controller
    {
        // GET: Transferencia
        public ActionResult Index()
        {
            if (Session["Username"] == null)
            {
                return RedirectToAction("Index", "Login");
            }
            return View();
        }

        // POST: Transferencia
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Index(string cuentaOrigen, string cuentaDestino, string monto)
        {
            if (Session["Username"] == null)
            {
                return RedirectToAction("Index", "Login");
            }

            if (string.IsNullOrWhiteSpace(cuentaOrigen) || string.IsNullOrWhiteSpace(cuentaDestino) || string.IsNullOrWhiteSpace(monto))
            {
                ViewBag.Error = "Por favor complete todos los campos";
                return View();
            }

            if (cuentaOrigen.Trim() == cuentaDestino.Trim())
            {
                ViewBag.Error = "La cuenta origen y destino no pueden ser la misma";
                return View();
            }

            // Validar que el monto sea un número válido y positivo
            if (!decimal.TryParse(monto, out decimal montoDecimal) || montoDecimal <= 0)
            {
                ViewBag.Error = "El monto debe ser un número positivo válido";
                return View();
            }

            try
            {
                using (var client = new CuentaServicioClient())
                {
                    // El servidor maneja toda la transferencia con tipo "TRA":
                    // - Resta el monto de la cuenta origen
                    // - Suma el monto a la cuenta destino
                    // - Registra movimientos en ambas cuentas
                    // - Hace rollback automático si algo falla
                    bool transferenciaExitosa = client.Deposito(cuentaOrigen.Trim(), monto.Trim(), "TRA", cuentaDestino.Trim());

                    if (transferenciaExitosa)
                    {
                        ViewBag.Success = "Transferencia realizada con éxito";
                        ModelState.Clear();
                    }
                    else
                    {
                        ViewBag.Error = "Error al realizar la transferencia. Verifique los datos de las cuentas y el saldo disponible en la cuenta origen";
                    }
                }
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error al conectar con el servidor: " + ex.Message;
            }

            return View();
        }
    }
}

