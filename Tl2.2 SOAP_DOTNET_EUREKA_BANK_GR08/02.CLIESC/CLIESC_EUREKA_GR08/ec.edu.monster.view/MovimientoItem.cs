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
    public partial class MovimientoItem : UserControl
    {
        public MovimientoItem()
        {
            InitializeComponent();
            this.BorderStyle = BorderStyle.FixedSingle; // Borde para cada item
            this.Width = 400; // Ajusta según tu diseño
            this.Height = 100; // Altura fija para cada movimiento
        }

        // Propiedades para configurar los datos del movimiento
        public string Cuenta
        {
            get { return lblCuenta.Text; }
            set { lblCuenta.Text = $"Cuenta: {value}"; }
        }

        public string Fecha
        {
            get { return lblFecha.Text; }
            set { lblFecha.Text = $"Fecha: {value}"; }
        }

        public string Movimiento
        {
            get { return lblMovimiento.Text; }
            set { lblMovimiento.Text = $"Movimiento: {value}"; }
        }

        public string Tipo
        {
            get { return lblTipo.Text; }
            set { lblTipo.Text = $"Tipo: {value}"; }
        }

        public string Accion
        {
            get { return lblAccion.Text; }
            set { lblAccion.Text = $"Acción: {value}"; }
        }

        public string Importe
        {
            get { return lblImporte.Text; }
            set { lblImporte.Text = $"Importe: {value}"; }
        }

        public string SaldoActual
        {
            get { return lblSaldoActual.Text; }
            set { lblSaldoActual.Text = $"Saldo Actual: {value}"; }
        }

        private void lblTipo_Click(object sender, EventArgs e)
        {

        }

        private void lblAccion_Click(object sender, EventArgs e)
        {

        }

        private void lblImporte_Click(object sender, EventArgs e)
        {

        }

        private void MovimientoItem_Load(object sender, EventArgs e)
        {

        }

        private void lblSaldo_Click(object sender, EventArgs e)
        {

        }
    }
}
