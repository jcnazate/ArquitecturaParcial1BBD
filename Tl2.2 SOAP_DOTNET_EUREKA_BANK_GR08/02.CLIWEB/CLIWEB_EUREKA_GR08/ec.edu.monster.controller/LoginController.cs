using System;
using System.Web.Mvc;
using CLIWEB_EUREKA_GR08.LoginServicio;

namespace CLIWEB_EUREKA_GR08.ec.edu.monster.controller
{
    public class LoginController : Controller
    {
        // GET: Login
        public ActionResult Index()
        {
            // Si ya está autenticado, redirigir al menú
            if (Session["Username"] != null)
            {
                return RedirectToAction("Index", "Home");
            }
            return View();
        }

        // POST: Login
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Index(string username, string password)
        {
            if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
            {
                ViewBag.Error = "Por favor complete todos los campos";
                return View();
            }

            try
            {
                using (var client = new LoginServicioClient())
                {
                    bool isAuthenticated = client.Auth(username.Trim(), password.Trim());

                    if (isAuthenticated)
                    {
                        Session["Username"] = username.Trim();
                        return RedirectToAction("Index", "Home");
                    }
                    else
                    {
                        ViewBag.Error = "Credenciales incorrectas";
                        return View();
                    }
                }
            }
            catch (Exception ex)
            {
                ViewBag.Error = "Error al conectar con el servidor: " + ex.Message;
                return View();
            }
        }

        // GET: Logout
        public ActionResult Logout()
        {
            Session.Clear();
            Session.Abandon();
            return RedirectToAction("Index", "Login");
        }
    }
}

