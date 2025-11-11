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

namespace CLIESC_EUREKA_GR08.ec.edu.monster.view
{
    public partial class FrmMovimientos : Form
    {
        public event EventHandler BuscarClicked;

        public FrmMovimientos()
        {
            InitializeComponent();
            new MovimientosController(this);
        }

        private void btnBuscar_Click(object sender, EventArgs e)
        {
            BuscarClicked?.Invoke(this, EventArgs.Empty);
        }

        public string NumeroCuenta
        {
            get { return txtCuenta.Text; }
            set { txtCuenta.Text = value; }
        }

        public string Saldo
        {
            set { lblSaldo.Text = $"Saldo de la Cuenta: {value}"; }
        }

        public string TotalMovimientos
        {
            set { lblTotalMovimientos.Text = $"Total Movimientos: {value}"; }
        }

        public void AgregarMovimientos(List<MovimientoItem> movimientos)
        {
            flowLayoutPanel.Controls.Clear();
            foreach (var movimiento in movimientos)
            {
                flowLayoutPanel.Controls.Add(movimiento);
            }
        }

        public void MostrarMensaje(string mensaje)
        {
            MessageBox.Show(mensaje);
        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        private void txtCuenta_TextChanged(object sender, EventArgs e)
        {

        }

        private void FrmMovimientos_FormClosing(object sender, FormClosingEventArgs e)
        {
            Application.Exit(); // Termina la aplicación completamente
        }

        private void button1_Click(object sender, EventArgs e)
        {
            
        }

        private void btnNuevoMovimiento_Click(object sender, EventArgs e)
        {
            FrmOperaciones formOperaciones = new FrmOperaciones();
            formOperaciones.ShowDialog(); // Muestra como diálogo
        }

        private void FrmMovimientos_Load(object sender, EventArgs e)
        {

        }
    }
}
