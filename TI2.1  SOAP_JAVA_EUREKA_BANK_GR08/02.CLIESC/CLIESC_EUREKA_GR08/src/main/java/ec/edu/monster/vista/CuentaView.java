/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.monster.vista;

import ec.edu.monster.service.CuentaService;
import ec.edu.monster.ws.CuentaModel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JLabel;

/**
 *
 * @author JOHAN
 */
public class CuentaView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(CuentaView.class.getName());
    private final CuentaService cuentaService = new CuentaService();
    private Color colorFondo = new Color(118, 75, 162); // #764ba2
    private Color colorGradiente1 = new Color(102, 126, 234); // #667eea
    private Color colorGradiente2 = new Color(118, 75, 162); // #764ba2

    // Componentes del diseño web
    private JPanel navbarPanel;
    private RoundedCardPanel cardPanel;
    private RoundedCardPanel infoCardPanel;
    private JLabel lblError;
    private CuentaModel cuentaActual;
    private IconTextField txtCuentaField;

    /**
     * Creates new form CuentaView
     */
    public CuentaView() {
        initComponents();
        crearDiseñoWeb();
    }
    
    private void crearDiseñoWeb() {
        // Configurar frame
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIHelper.COLOR_FONDO);
        setBackground(UIHelper.COLOR_FONDO);
        
        // Crear navbar (simplificado, sin usuario)
        crearNavbar();
        
        // Crear card principal
        crearCardPrincipal();
        
        // Configurar tamaño
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    private void crearNavbar() {
        navbarPanel = new JPanel(new BorderLayout());
        navbarPanel.setBackground(new Color(255, 255, 255, 242));
        navbarPanel.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));
        
        JLabel brandLabel = new JLabel(UIHelper.Icons.UNIVERSITY + " Eureka Bank");
        brandLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        brandLabel.setForeground(UIHelper.COLOR_GRADIENTE_1);
        
        GradientButton btnVolver = new GradientButton(UIHelper.Icons.ARROW_LEFT + " Volver al Menú");
        btnVolver.setPreferredSize(new Dimension(150, 35));
        btnVolver.addActionListener(evt -> {
            this.dispose();
        });
        
        navbarPanel.add(brandLabel, BorderLayout.WEST);
        navbarPanel.add(btnVolver, BorderLayout.EAST);
        
        getContentPane().add(navbarPanel, BorderLayout.NORTH);
    }
    
    private void crearCardPrincipal() {
        cardPanel = new RoundedCardPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new javax.swing.BoxLayout(contentPanel, javax.swing.BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Título
        JLabel titulo = new JLabel(UIHelper.Icons.INFO + " Consultar Datos de Cuenta");
        titulo.setFont(new Font("Poppins", Font.BOLD, 28));
        titulo.setForeground(UIHelper.COLOR_TEXTO);
        titulo.setBorder(new EmptyBorder(0, 0, 24, 0));
        
        // Label de error
        lblError = new JLabel();
        lblError.setFont(new Font("Poppins", Font.PLAIN, 14));
        lblError.setForeground(UIHelper.COLOR_ERROR);
        lblError.setVisible(false);
        lblError.setBackground(UIHelper.COLOR_ALERTA_ERROR);
        lblError.setOpaque(true);
        lblError.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(245, 87, 108)),
            new EmptyBorder(16, 20, 16, 20)
        ));
        
        // Formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setBorder(new EmptyBorder(0, 0, 32, 0));
        
        JLabel lblCuenta = new JLabel(UIHelper.Icons.CREDIT_CARD + " Número de Cuenta");
        lblCuenta.setFont(new Font("Poppins", Font.BOLD, 15));
        lblCuenta.setForeground(new Color(85, 85, 85));
        lblCuenta.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        txtCuentaField = new IconTextField();
        txtCuentaField.setIcon(UIHelper.Icons.CREDIT_CARD);
        txtCuentaField.setPlaceholder("Ingrese el número de cuenta");
        txtCuentaField.setPreferredSize(new Dimension(0, 50));
        txtCuentaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        if (txtNumeroCuenta != null && !txtNumeroCuenta.getText().isEmpty()) {
            txtCuentaField.setText(txtNumeroCuenta.getText());
        }
        
        GradientButton btnBuscar = new GradientButton(UIHelper.Icons.SEARCH + " Consultar");
        btnBuscar.setPreferredSize(new Dimension(0, 55));
        btnBuscar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnBuscar.addActionListener(evt -> {
            btnBuscarCuentaActionPerformed(new java.awt.event.ActionEvent(btnBuscar, 
                java.awt.event.ActionEvent.ACTION_PERFORMED, ""));
        });
        
        formPanel.add(lblCuenta);
        formPanel.add(txtCuentaField);
        formPanel.add(btnBuscar);
        
        // Panel de información (inicialmente oculto)
        infoCardPanel = new RoundedCardPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                GradientPaint gradient = new GradientPaint(
                    0, 0, UIHelper.COLOR_GRADIENTE_1,
                    getWidth(), 0, UIHelper.COLOR_GRADIENTE_2
                );
                g2d.setPaint(gradient);
                
                java.awt.geom.RoundRectangle2D roundedRectangle = new java.awt.geom.RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 15, 15
                );
                g2d.fill(roundedRectangle);
                g2d.dispose();
            }
        };
        infoCardPanel.setOpaque(false);
        infoCardPanel.setLayout(new BorderLayout());
        infoCardPanel.setBorder(new EmptyBorder(24, 24, 24, 24));
        infoCardPanel.setVisible(false);
        
        contentPanel.add(titulo);
        contentPanel.add(lblError);
        contentPanel.add(formPanel);
        contentPanel.add(infoCardPanel);
        
        cardPanel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        containerPanel.add(cardPanel, BorderLayout.CENTER);
        
        getContentPane().add(containerPanel, BorderLayout.CENTER);
        
        // Sincronizar botón
        if (btnBuscarCuenta != null) {
            // Mantener la referencia del botón original para el evento
        }
        btnBuscarCuenta = btnBuscar;
    }
    
    private void mostrarInformacionCuenta(CuentaModel cuenta) {
        cuentaActual = cuenta;
        
        infoCardPanel.removeAll();
        infoCardPanel.setLayout(new BorderLayout());
        
        JLabel tituloInfo = new JLabel(UIHelper.Icons.CREDIT_CARD + " Información de la Cuenta");
        tituloInfo.setFont(new Font("Poppins", Font.BOLD, 24));
        tituloInfo.setForeground(Color.WHITE);
        tituloInfo.setBorder(new EmptyBorder(0, 0, 24, 0));
        
        JPanel gridPanel = new JPanel(new GridLayout(3, 2, 24, 24));
        gridPanel.setOpaque(false);
        
        // Código de Cuenta
        agregarItemInfo(gridPanel, UIHelper.Icons.CREDIT_CARD + " Código de Cuenta:", 
            cuenta.getChrCuenCodigo() != null ? cuenta.getChrCuenCodigo() : "N/A");
        
        // Saldo
        agregarItemInfo(gridPanel, UIHelper.Icons.DOLLAR + " Saldo:", 
            "$" + String.format("%.2f", cuenta.getDecCuenSaldo()));
        
        // Contador de Movimientos
        agregarItemInfo(gridPanel, UIHelper.Icons.LIST + " Contador de Movimientos:", 
            String.valueOf(cuenta.getIntCuenContMov()));
        
        // Fecha de Creación
        String fechaStr = "N/A";
        if (cuenta.getDttCuenFechaCreacion() != null) {
            try {
                // Intentar diferentes métodos según el tipo real del objeto
                Object fechaObj = cuenta.getDttCuenFechaCreacion();
                if (fechaObj instanceof javax.xml.datatype.XMLGregorianCalendar) {
                    javax.xml.datatype.XMLGregorianCalendar xmlCal = (javax.xml.datatype.XMLGregorianCalendar) fechaObj;
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaStr = sdf.format(xmlCal.toGregorianCalendar().getTime());
                } else if (fechaObj instanceof java.util.Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaStr = sdf.format((java.util.Date) fechaObj);
                } else if (fechaObj instanceof java.sql.Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    fechaStr = sdf.format((java.sql.Date) fechaObj);
                } else {
                    // Si no podemos convertir, mostrar como string
                    fechaStr = fechaObj.toString();
                }
            } catch (Exception e) {
                // Si hay error, mostrar el toString del objeto
                fechaStr = cuenta.getDttCuenFechaCreacion().toString();
            }
        }
        agregarItemInfo(gridPanel, UIHelper.Icons.CALENDAR + " Fecha de Creación:", fechaStr);
        
        // Estado
        agregarItemInfo(gridPanel, UIHelper.Icons.INFO + " Estado:", 
            cuenta.getVchCuenEstado() != null ? cuenta.getVchCuenEstado() : "N/A");
        
        // Sucursal
        agregarItemInfo(gridPanel, UIHelper.Icons.BUILDING + " Sucursal:", 
            cuenta.getChrSucucodigo() != null ? cuenta.getChrSucucodigo() : "N/A");
        
        infoCardPanel.add(tituloInfo, BorderLayout.NORTH);
        infoCardPanel.add(gridPanel, BorderLayout.CENTER);
        
        infoCardPanel.setVisible(true);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    private void agregarItemInfo(JPanel parent, String label, String value) {
        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new javax.swing.BoxLayout(itemPanel, javax.swing.BoxLayout.Y_AXIS));
        itemPanel.setOpaque(false);
        
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Poppins", Font.BOLD, 14));
        lbl.setForeground(new Color(255, 255, 255, 230));
        
        boolean isSaldo = label.contains("Saldo");
        JLabel val = new JLabel(value);
        val.setFont(new Font("Poppins", Font.PLAIN, isSaldo ? 24 : 18));
        if (isSaldo) {
            val.setFont(new Font("Poppins", Font.BOLD, 24));
        }
        val.setForeground(Color.WHITE);
        val.setBorder(new EmptyBorder(8, 0, 0, 0));
        
        itemPanel.add(lbl);
        itemPanel.add(val);
        parent.add(itemPanel);
    }
    
    private void mostrarError(String mensaje) {
        lblError.setText(UIHelper.Icons.EXCLAMATION + " " + mensaje);
        lblError.setVisible(true);
        infoCardPanel.setVisible(false);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    private void aplicarEstiloModerno() {
        // Método mantenido para compatibilidad
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
        btnBuscarCuenta = new javax.swing.JButton();
        txtNumeroCuenta = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        lblCuentaEncontrada = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblMovimientosEncontrado = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblSaldoEncontrado1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel1.setText("Ingrese el número de Cuenta");

        btnBuscarCuenta.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnBuscarCuenta.setText("Buscar");

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCuentaEncontrada.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel3.setText("Cuenta: ");

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel4.setText("Saldo");

        lblMovimientosEncontrado.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        jLabel5.setText("Movimientos");

        lblSaldoEncontrado1.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(lblSaldoEncontrado1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(26, 26, 26)
                                .addComponent(lblMovimientosEncontrado, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(lblCuentaEncontrada, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(72, 72, 72))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblCuentaEncontrada, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(lblSaldoEncontrado1, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblMovimientosEncontrado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(58, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel2.setText("Datos de la Cuenta");
        jLabel2.setName("lblPassword"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(55, 55, 55)
                        .addComponent(btnBuscarCuenta))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(93, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(238, 238, 238))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarCuenta))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(66, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarCuentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarCuentaActionPerformed
        String numeroCuenta = "";
        if (txtCuentaField != null) {
            numeroCuenta = txtCuentaField.getText() != null ? txtCuentaField.getText().trim() : "";
        } else if (txtNumeroCuenta != null) {
            numeroCuenta = txtNumeroCuenta.getText() != null ? txtNumeroCuenta.getText().trim() : "";
        }
        
        if (numeroCuenta.isEmpty()) {
            mostrarError("Por favor, ingrese un número de cuenta.");
            return;
        }
        
        try {
            CuentaModel cuentaModel = cuentaService.obtenerCuentaPorNumero(numeroCuenta);
            
            if (cuentaModel == null) {
                mostrarError("No se encontraron datos para la cuenta: " + numeroCuenta);
                return;
            }
            
            mostrarInformacionCuenta(cuentaModel);
            lblError.setVisible(false);
            
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Error al obtener los datos de la cuenta", e);
            mostrarError("Error al obtener los datos de la cuenta: " + e.getMessage());
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
        java.awt.EventQueue.invokeLater(() -> new CuentaView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarCuenta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblCuentaEncontrada;
    private javax.swing.JLabel lblMovimientosEncontrado;
    private javax.swing.JLabel lblSaldoEncontrado1;
    private javax.swing.JTextField txtNumeroCuenta;
    // End of variables declaration//GEN-END:variables
}
