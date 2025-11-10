using CLIESC_EUREKA_GR08.ec.edu.monster.controller;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace CLIESC_EUREKA_GR08.ec.edu.monster.view
{
    public partial class FrmOperaciones : Form
    {

        public event EventHandler RealizarClicked;

        public FrmOperaciones()
        {
            InitializeComponent();
            ConfigurarFormulario();
            new OperacionesController(this);
        }

        private void ConfigurarFormulario()
        {
            // Configurar opciones del ComboBox
            if (cmbTipoOperacion.Items.Count == 0)
            {
                cmbTipoOperacion.Items.AddRange(new string[] { "Depósito", "Retiro", "Transferencia" });
            }

            // Establecer una selección predeterminada
            if (cmbTipoOperacion.SelectedIndex == -1)
            {
                cmbTipoOperacion.SelectedIndex = 0; // Selecciona "Depósito" por defecto
            }

            // Suscribir al evento de cambio de selección
            cmbTipoOperacion.SelectedIndexChanged += cmbTipoOperacion_SelectedIndexChanged;
        }

        private void cmbTipoOperacion_SelectedIndexChanged(object sender, EventArgs e)
        {
            string seleccion = cmbTipoOperacion.SelectedItem?.ToString();
            if (seleccion != null)
            {
                // Mostrar u ocultar Cuenta Origen según la selección
                bool esTransferencia = seleccion == "Transferencia";
                lblCuentaOrigen.Visible = esTransferencia;
                txtCuentaOrigen.Visible = esTransferencia;
            }
        }

        private void btnRealizar_Click(object sender, EventArgs e)
        {
            RealizarClicked?.Invoke(this, EventArgs.Empty);
        }

        public string TipoOperacion
        {
            get { return cmbTipoOperacion.SelectedItem?.ToString(); }
        }

        public string CuentaDestino
        {
            get { return txtCuentaDestino.Text; }
        }

        public string CuentaOrigen
        {
            get { return txtCuentaOrigen.Text; }
        }

        public decimal Monto
        {
            get
            {
                if (decimal.TryParse(txtMonto.Text, out decimal monto))
                    return monto;
                return 0;
            }
        }

        public void MostrarMensaje(string mensaje)
        {
            MessageBox.Show(mensaje);
        }

        private void FrmOperaciones_Load(object sender, EventArgs e)
        {

        }
    }
}
