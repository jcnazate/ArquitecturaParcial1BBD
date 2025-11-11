using CLIESCR_EUREKA_GR08;                     // Para Form1
using CLIESCR_EUREKA_GR08.ec.edu.monster.service;
using CLIESCR_EUREKA_GR08.ec.edu.monster.view; // Para FrmMovimiento (ajusta si es FrmMovimientos)
using System;
using System.Drawing;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CLIESCR_EUREKA_GR08.ec.edu.monster.controller
{
    internal class LoginController
    {
        private readonly Form1 _vista;
        private readonly Servicio _servicio;

        public LoginController(Form1 vista)
        {
            _vista = vista ?? throw new ArgumentNullException(nameof(vista));
            _servicio = new Servicio();
            // Ya NO nos suscribimos a ningún click aquí.
            // El Form1 ya hace: btnLogin.Click += async (...) => await BtnLogin_ClickAsync();
        }

        public async Task IniciarSesionAsync()
        {
            // Podrías quitar "async" si no vas a usar await; lo dejo así para encajar con tu Form1.
            string username = _vista.Usuario;
            string password = _vista.Contrasena;

            _vista.Mensaje = "";
            _vista.MensajeColor = Color.Red;

            // Validar campos
            if (string.IsNullOrWhiteSpace(username) || string.IsNullOrWhiteSpace(password))
            {
                _vista.Mensaje = "Por favor, complete todos los campos.";
                return;
            }

            bool isAuthenticated;
            try
            {
                // Si tu Servicio es síncrono:
                isAuthenticated = _servicio.Authenticate(username, password);

                // Si más adelante tienes versión async:
                // isAuthenticated = await _servicio.AuthenticateAsync(username, password);
            }
            catch (Exception)
            {
                _vista.Mensaje = "Error al validar credenciales.";
                return;
            }

            if (!isAuthenticated)
            {
                _vista.Mensaje = "Usuario o contraseña incorrectos.";
                return;
            }

            // Login OK
            _vista.MensajeColor = Color.FromArgb(0, 128, 0);
            _vista.Mensaje = "Login exitoso!";

            // Abrir formulario siguiente (ajusta el nombre si tu form se llama distinto)
            var formMenu = new FrmMenu(username);
            formMenu.StartPosition = FormStartPosition.CenterScreen;
            formMenu.Show();

            _vista.Hide();
        }
    }
}
