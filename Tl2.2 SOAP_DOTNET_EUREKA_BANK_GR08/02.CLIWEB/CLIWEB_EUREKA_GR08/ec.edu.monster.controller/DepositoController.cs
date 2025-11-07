using System;
using System.Web.Mvc;
using CLIWEB_EUREKA_GR08.CuentaServicio;

namespace CLIWEB_EUREKA_GR08.ec.edu.monster.controller
{
    public class DepositoController : Controller
    {
        // GET: Deposito
        public ActionResult Index()
        {
            if (Session["Username"] == null)
            {
                return RedirectToAction("Index", "Login");
            }
            return View();
        }

        // POST: Deposito
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Index(string cuenta, string monto)
        {
            if (Session["Username"] == null)
            {
                return RedirectToAction("Index", "Login");
            }

            if (string.IsNullOrWhiteSpace(cuenta) || string.IsNullOrWhiteSpace(monto))
            {
                ViewBag.Error = "Por favor complete todos los campos";
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
                    // DEP = Depósito
                    bool resultado = client.Deposito(cuenta.Trim(), monto.Trim(), "DEP", null);

                    if (resultado)
                    {
                        ViewBag.Success = "Depósito realizado con éxito";
                        ModelState.Clear();
                    }
                    else
                    {
                        ViewBag.Error = "Error al realizar el depósito. Verifique los datos de la cuenta";
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

