package ec.edu.monster.controller;

import ec.edu.monster.view.MovimientoView;
import ec.edu.monster.model.MovimientoModel;
import ec.edu.monster.service.MovimientoService;
import ec.edu.monster.service.CuentaService;
import ec.edu.monster.model.CuentaModel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MovimientoController {

    private MovimientoService movimientoService;
    private CuentaService cuentaService;

    public MovimientoController() {
        this.movimientoService = new MovimientoService();
        this.cuentaService = new CuentaService();
    }

    /**
     * Método para cargar y mostrar los movimientos en la vista proporcionada.
     *
     * @param cuenta          Número de cuenta a buscar.
     * @param movimientoView  Instancia de la vista donde se mostrarán los resultados.
     */
    public void cargarMovimientos(String cuenta, MovimientoView movimientoView) {
        try {
            List<MovimientoModel> movimientos = movimientoService.obtenerMovimientos(cuenta);
            CuentaModel cuentaModel = cuentaService.obtenerCuentaPorNumero(cuenta);

            JPanel panelResultados = new JPanel();
            panelResultados.setLayout(new BoxLayout(panelResultados, BoxLayout.Y_AXIS));
            panelResultados.setBackground(Color.WHITE);

            if (movimientos == null || movimientos.isEmpty()) {
                JLabel noResultados = new JLabel("No se encontraron movimientos para la cuenta: " + cuenta);
                noResultados.setFont(new Font("Arial", Font.BOLD, 16));
                noResultados.setForeground(Color.RED);
                panelResultados.add(noResultados);
            } else {
                // Crear celdas para cada movimiento
                for (MovimientoModel mov : movimientos) {

                String saldoTexto;
                if (mov.getSaldo() != 0) {
                    // Asumimos que el backend ya envía el saldo acumulativo en cada movimiento
                    saldoTexto = "Saldo después: $" + String.format("%.2f", mov.getSaldo());
                } else {
                    // Si por alguna razón viene 0, lo mostramos como "no disponible"
                    saldoTexto = "Saldo después: N/D";
                }

                JPanel celda = movimientoView.crearCelda(
                        "Cuenta: " + mov.getCodigoCuenta(),
                        "Fecha: " + mov.getFechaMovimiento(),
                        "Movimiento: " + mov.getNumeroMovimiento(),
                        "Descripción: " + mov.getTipoDescripcion(),
                        "Tipo Mov.: " + mov.getCodigoTipoMovimiento(),
                        "Importe: $" + String.format("%.2f", mov.getImporteMovimiento()),
                        saldoTexto
                );
                panelResultados.add(celda);
            }
                // Resumen de totales
                double totalIngresos = movimientos.stream()
                        .filter(m -> !m.getTipoDescripcion().equals("Retiro"))
                        .mapToDouble(MovimientoModel::getImporteMovimiento)
                        .sum();

                double totalEgresos = movimientos.stream()
                        .filter(m -> m.getTipoDescripcion().equals("Retiro"))
                        .mapToDouble(MovimientoModel::getImporteMovimiento)
                        .sum();

                JPanel resumenPanel = new JPanel(new GridLayout(5, 1));
                resumenPanel.setBackground(new Color(255, 255, 255));
                resumenPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
           
                JLabel saldoCuentaLabel = new JLabel("Saldo de la Cuenta: $" + (cuentaModel != null ? String.format("%.2f", cuentaModel.getDecCuenSaldo()) : "N/A"));
                JLabel totalMovimientosLabel = new JLabel("Total Movimientos: " + (cuentaModel != null ? cuentaModel.getIntCuenContMov() : "N/A"));
           
                resumenPanel.add(saldoCuentaLabel);
                resumenPanel.add(totalMovimientosLabel);

                panelResultados.add(resumenPanel);
            }

            // Reemplazar el contenido del JScrollPane con los nuevos resultados
            movimientoView.getjScrollPane1().setViewportView(panelResultados);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(movimientoView,
                    "Error al obtener los movimientos o datos de la cuenta: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}