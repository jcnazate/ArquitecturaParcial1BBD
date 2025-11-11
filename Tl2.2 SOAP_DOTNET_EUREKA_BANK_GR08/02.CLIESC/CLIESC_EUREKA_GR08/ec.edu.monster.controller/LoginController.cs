using CLIESC_EUREKA_GR08.ec.edu.monster.service;
using CLIESC_EUREKA_GR08.ec.edu.monster.view;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.controller
{
    internal class LoginController
    {
        private Form1 _vista;
        private Servicio _servicio;

        public LoginController(Form1 vista)
        {
            _vista = vista;
            _servicio = new Servicio();

            // Suscribirse al evento del botón usando la propiedad LoginButton
            _vista.LoginButton.Click += new EventHandler(BtnLogin_Click);
        }

        private void BtnLogin_Click(object sender, EventArgs e)
        {
            // Obtener datos de la Vista
            string username = _vista.Usuario;
            string password = _vista.Contrasena;

            // Validar que los campos no estén vacíos
            if (string.IsNullOrEmpty(username) || string.IsNullOrEmpty(password))
            {
                _vista.Mensaje = "Por favor, complete todos los campos.";
                return;
            }

            // Autenticar usando el Servicio
            bool isAuthenticated = _servicio.Authenticate(username, password);

            // Actualizar el mensaje según el resultado
            if (isAuthenticated)
            {
                _vista.Mensaje = "Login exitoso!";
                // Opcional: Redirigir o habilitar funcionalidades

                // Abrir el formulario de movimientos
                FrmMovimientos formMovimientos = new FrmMovimientos();
                formMovimientos.Show();

                // Opcional: Ocultar el formulario de login
                _vista.Hide();
            }
            else
            {
                _vista.Mensaje = "Usuario o contraseña incorrectos.";
            }
        }
    }
}
