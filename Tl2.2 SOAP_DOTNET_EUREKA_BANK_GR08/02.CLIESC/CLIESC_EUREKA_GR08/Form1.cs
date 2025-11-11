using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using CLIESC_EUREKA_GR08.ec.edu.monster.controller;

namespace CLIESC_EUREKA_GR08
{
    public partial class Form1 : Form
    {
        private LoginController _controller;

        public Form1()
        {
            InitializeComponent();
            _controller = new LoginController(this); // Pasar la instancia actual de Form1
        }

        // Propiedades existentes
        public string Usuario
        {
            get { return txtUsuario.Text; }
            set { txtUsuario.Text = value; }
        }

        public string Contrasena
        {
            get { return txtContrasena.Text; }
            set { txtContrasena.Text = value; }
        }

        public string Mensaje
        {
            get { return lblMensaje.Text; }
            set { lblMensaje.Text = value; }
        }

        // Propiedad para el botón corregida
        public Button LoginButton
        {
            get { return btnLogin; }
        }

        private void btnLogin_Click(object sender, EventArgs e)
        {

        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }
    }
}
