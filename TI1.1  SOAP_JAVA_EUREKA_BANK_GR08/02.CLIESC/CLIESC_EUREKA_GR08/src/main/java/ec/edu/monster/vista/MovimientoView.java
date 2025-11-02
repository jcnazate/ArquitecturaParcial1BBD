/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.monster.vista;

import ec.edu.monster.service.MovimientoService;
import ec.edu.monster.ws.MovimientoModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author JOHAN
 */
public class MovimientoView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MovimientoView.class.getName());
    private final MovimientoService movimientoService = new MovimientoService();
    private javax.swing.JTable tblMovimientos;
    private javax.swing.JScrollPane scrollPane;

    /**
     * Creates new form MovimientoView
     */
    public MovimientoView() {
        initComponents();
        crearTablaMovimientos();
        // Agregar listener fuera de la sección generada para que no se elimine al modificar el diseño
        btnBuscarCuenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarCuentaActionPerformed(evt);
            }
        });
    }
    
    private void crearTablaMovimientos() {
        // Crear modelo de tabla con las columnas igual que en consola
        String[] columnas = {"Nro", "Fecha", "Tipo Mov.", "Descripción", "Importe", "Cta Ref.", "Saldo"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        // Crear la tabla
        tblMovimientos = new javax.swing.JTable(model);
        tblMovimientos.setFont(new java.awt.Font("Segoe UI", 0, 12));
        tblMovimientos.setRowHeight(20);
        tblMovimientos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        // Configurar anchos de columnas
        tblMovimientos.getColumnModel().getColumn(0).setPreferredWidth(60);   // Nro
        tblMovimientos.getColumnModel().getColumn(1).setPreferredWidth(120);  // Fecha
        tblMovimientos.getColumnModel().getColumn(2).setPreferredWidth(80);    // Tipo Mov.
        tblMovimientos.getColumnModel().getColumn(3).setPreferredWidth(150);  // Descripción
        tblMovimientos.getColumnModel().getColumn(4).setPreferredWidth(100); // Importe
        tblMovimientos.getColumnModel().getColumn(5).setPreferredWidth(100); // Cta Ref.
        tblMovimientos.getColumnModel().getColumn(6).setPreferredWidth(100);  // Saldo
        
        // Crear scroll pane
        scrollPane = new JScrollPane(tblMovimientos);
        scrollPane.setBorder(javax.swing.BorderFactory.createTitledBorder("Movimientos de la Cuenta"));
        scrollPane.setPreferredSize(new java.awt.Dimension(650, 280));
        
        // Obtener el layout existente y recrearlo incluyendo la tabla
        javax.swing.GroupLayout layout = (javax.swing.GroupLayout) getContentPane().getLayout();
        
        // Recrear el layout horizontal incluyendo todos los elementos existentes + la tabla
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnBuscarCuenta)))
                .addContainerGap(127, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addGap(50, 50, 50))
        );
        
        // Recrear el layout vertical incluyendo todos los elementos existentes + la tabla
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCuenta))
                .addGap(18, 18, 18)
                .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );
        
        // Agregar el componente al content pane si no está ya
        if (!java.util.Arrays.asList(getContentPane().getComponents()).contains(scrollPane)) {
            getContentPane().add(scrollPane);
        }
        
        // Revalidar el layout
        getContentPane().revalidate();
        getContentPane().repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtNumeroCuenta = new javax.swing.JTextField();
        btnBuscarCuenta = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel1.setText("Ingrese el número de Cuenta");

        btnBuscarCuenta.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnBuscarCuenta.setText("Buscar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnBuscarCuenta)))
                .addContainerGap(127, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCuenta))
                .addContainerGap(329, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCuentaActionPerformed
        String numeroCuenta = txtNumeroCuenta.getText() != null ? txtNumeroCuenta.getText().trim() : "";
        
        if (numeroCuenta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese un número de cuenta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            List<MovimientoModel> movimientos = movimientoService.obtenerMovimientos(numeroCuenta);
            
            // Obtener el modelo de la tabla
            DefaultTableModel model = (DefaultTableModel) tblMovimientos.getModel();
            
            // Limpiar la tabla
            model.setRowCount(0);
            
            if (movimientos == null || movimientos.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No se encontraron movimientos para la cuenta: " + numeroCuenta, "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Llenar la tabla con los movimientos (igual que en consola)
            for (MovimientoModel mov : movimientos) {
                Object[] row = {
                    mov.getNumeroMovimiento(),
                    mov.getFechaMovimiento(),
                    mov.getCodigoTipoMovimiento(),
                    mov.getTipoDescripcion(),
                    String.format("%.2f", mov.getImporteMovimiento()),
                    mov.getCuentaReferencia() != null ? mov.getCuentaReferencia() : "N/A",
                    String.format("%.2f", mov.getSaldo())
                };
                model.addRow(row);
            }
            
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al obtener los movimientos", e);
            JOptionPane.showMessageDialog(this, "Error al obtener los movimientos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // Limpiar la tabla en caso de error
            DefaultTableModel model = (DefaultTableModel) tblMovimientos.getModel();
            model.setRowCount(0);
        }
    }//GEN-LAST:event_btnBuscarCuentaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MovimientoView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField txtNumeroCuenta;
    // End of variables declaration//GEN-END:variables
}
