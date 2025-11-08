/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.monster.vista;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author JOHAN
 */
public class MenuView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MenuView.class.getName());
    private Color colorFondo = new Color(118, 75, 162); // #764ba2
    private Color colorCard = new Color(102, 126, 234); // #667eea

    private String username;
    
    /**
     * Creates new form MenuView
     */
    public MenuView() {
        this("Usuario");
    }
    
    public MenuView(String username) {
        this.username = username;
        initComponents();
        configurarEventos();
        aplicarEstiloModerno();
    }
    
    // Componentes del diseño web
    private JPanel navbarPanel;
    private RoundedCardPanel cardPanel;
    private JPanel menuGridPanel;
    private JLabel lblMikeSullivan;
    
    private void aplicarEstiloModerno() {
        // Configurar frame
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIHelper.COLOR_FONDO);
        setBackground(UIHelper.COLOR_FONDO);
        
        // Crear navbar
        crearNavbar();
        
        // Crear card principal
        crearCardPrincipal();
        
        // Crear imagen mikeysullivan
        crearImagenMikeSullivan();
        
        // Configurar tamaño
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }
    
    private void crearNavbar() {
        navbarPanel = new JPanel(new BorderLayout());
        navbarPanel.setBackground(new Color(255, 255, 255, 242)); // rgba(255,255,255,0.95)
        navbarPanel.setBorder(BorderFactory.createEmptyBorder(16, 32, 16, 32));
        
        // Brand
        JLabel brandLabel = new JLabel(UIHelper.Icons.UNIVERSITY + " Eureka Bank");
        brandLabel.setFont(new Font("Poppins", Font.BOLD, 24));
        brandLabel.setForeground(UIHelper.COLOR_GRADIENTE_1);
        
        // User info y botón logout
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 0));
        userPanel.setOpaque(false);
        
        JLabel userLabel = new JLabel(UIHelper.Icons.USER + " " + username);
        userLabel.setFont(new Font("Poppins", Font.BOLD, 14));
        userLabel.setForeground(new Color(85, 85, 85));
        
        GradientButton logoutBtn = new GradientButton(UIHelper.Icons.USER + " Salir");
        logoutBtn.setGradientColors(new Color(240, 147, 251), new Color(245, 87, 108));
        logoutBtn.setPreferredSize(new Dimension(120, 35));
        logoutBtn.addActionListener(evt -> {
            this.setVisible(false);
            java.awt.EventQueue.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setLocationRelativeTo(null);
                loginView.setVisible(true);
                this.dispose();
            });
        });
        
        userPanel.add(userLabel);
        userPanel.add(logoutBtn);
        
        navbarPanel.add(brandLabel, BorderLayout.WEST);
        navbarPanel.add(userPanel, BorderLayout.EAST);
        
        getContentPane().add(navbarPanel, BorderLayout.NORTH);
    }
    
    private void crearCardPrincipal() {
        cardPanel = new RoundedCardPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        
        // Título
        JLabel titulo = new JLabel(UIHelper.Icons.USER + " Bienvenido, " + username);
        titulo.setFont(new Font("Poppins", Font.BOLD, 28));
        titulo.setForeground(UIHelper.COLOR_TEXTO);
        
        // Subtítulo
        JLabel subtitulo = new JLabel("Seleccione una opción del menú para continuar");
        subtitulo.setFont(new Font("Poppins", Font.PLAIN, 18));
        subtitulo.setForeground(UIHelper.COLOR_TEXTO_SECUNDARIO);
        subtitulo.setBorder(new EmptyBorder(0, 0, 32, 0));
        
        // Grid de menu cards
        menuGridPanel = new JPanel(new GridLayout(1, 5, 24, 24));
        menuGridPanel.setOpaque(false);
        menuGridPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // Crear menu cards y agregarlos al grid
        menuGridPanel.add(crearMenuCard(btnDeposito, UIHelper.Icons.ARROW_DOWN, "Depósito", "Realizar un depósito a una cuenta"));
        menuGridPanel.add(crearMenuCard(btnRetiro, UIHelper.Icons.ARROW_UP, "Retiro", "Realizar un retiro de una cuenta"));
        menuGridPanel.add(crearMenuCard(btnTransferencia, UIHelper.Icons.EXCHANGE, "Transferencia", "Transferir dinero entre cuentas"));
        menuGridPanel.add(crearMenuCard(btnDatosCuenta, UIHelper.Icons.INFO, "Datos de Cuenta", "Consultar información de una cuenta"));
        menuGridPanel.add(crearMenuCard(btnMovimientos, UIHelper.Icons.LIST, "Movimientos", "Ver historial de movimientos"));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new javax.swing.BoxLayout(contentPanel, javax.swing.BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(titulo);
        contentPanel.add(subtitulo);
        contentPanel.add(menuGridPanel);
        
        cardPanel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setOpaque(false);
        containerPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        containerPanel.add(cardPanel, BorderLayout.CENTER);
        
        getContentPane().add(containerPanel, BorderLayout.CENTER);
    }
    
    private JPanel crearMenuCard(javax.swing.JButton btn, String icono, String titulo, String descripcion) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(UIHelper.COLOR_GRADIENTE_1);
                RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
                    0, 0, getWidth(), getHeight(), 20, 20
                );
                g2d.fill(roundedRectangle);
                g2d.dispose();
            }
        };
        card.setLayout(new javax.swing.BoxLayout(card, javax.swing.BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(24, 24, 24, 24));
        card.setPreferredSize(new Dimension(180, 180));
        card.setMinimumSize(new Dimension(150, 150));
        
        // Icono grande
        JLabel iconLabel = new JLabel(icono);
        iconLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 40));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        iconLabel.setBorder(new EmptyBorder(0, 0, 12, 0));
        
        // Título
        JLabel titleLabel = new JLabel(titulo);
        titleLabel.setFont(new Font("Poppins", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        titleLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        // Descripción
        JLabel descLabel = new JLabel("<html><center>" + descripcion + "</center></html>");
        descLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        descLabel.setForeground(new Color(255, 255, 255, 230));
        descLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        
        card.add(iconLabel);
        card.add(titleLabel);
        card.add(descLabel);
        
        // Hacer que el card sea clickeable y ejecute la acción del botón
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                btn.doClick();
            }
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                // Efecto hover: ligeramente más claro
                card.setToolTipText(titulo);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            }
        });
        
        // Ocultar el botón original (mantener funcionalidad)
        btn.setVisible(false);
        btn.setPreferredSize(new Dimension(0, 0));
        
        return card;
    }
    
    private void estiloBotonCard(javax.swing.JButton btn) {
        // Este método ya no se usa directamente
    }
    
    private void crearImagenMikeSullivan() {
        try {
            java.net.URL imageUrl = getClass().getResource("/ec/edu/monster/images/mikeysullivan.png");
            if (imageUrl == null) {
                File imageFile = new File("src/main/java/ec/edu/monster/images/mikeysullivan.png");
                if (!imageFile.exists()) {
                    imageFile = new File("02.CLIESC/CLIESC_EUREKA_GR08/src/main/java/ec/edu/monster/images/mikeysullivan.png");
                }
                if (imageFile.exists()) {
                    imageUrl = imageFile.toURI().toURL();
                }
            }
            if (imageUrl != null) {
                Image img = ImageIO.read(imageUrl);
                int maxWidth = 350;
                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);
                double scale = (double)maxWidth / imgWidth;
                int scaledWidth = (int)(imgWidth * scale);
                int scaledHeight = (int)(imgHeight * scale);
                Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                lblMikeSullivan = new JLabel(new ImageIcon(scaledImg));
                lblMikeSullivan.setHorizontalAlignment(JLabel.CENTER);
                
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                imagePanel.setOpaque(false);
                imagePanel.setBorder(new EmptyBorder(32, 0, 0, 0));
                imagePanel.add(lblMikeSullivan);
                
                getContentPane().add(imagePanel, BorderLayout.SOUTH);
            }
        } catch (Exception e) {
            logger.warning("No se pudo cargar la imagen mikeysullivan.png: " + e.getMessage());
        }
    }
    
    private void configurarEventos() {
        btnDeposito.addActionListener(evt -> {
            DepositoView depositoView = new DepositoView();
            depositoView.setVisible(true);
        });
        
        btnRetiro.addActionListener(evt -> {
            RetiroView retiroView = new RetiroView();
            retiroView.setVisible(true);
        });
        
        btnTransferencia.addActionListener(evt -> {
            TransferenciaView transferenciaView = new TransferenciaView();
            transferenciaView.setVisible(true);
        });
        
        btnMovimientos.addActionListener(evt -> {
            MovimientoView movimientoView = new MovimientoView();
            movimientoView.setVisible(true);
        });
        
        btnDatosCuenta.addActionListener(evt -> {
            CuentaView cuentaView = new CuentaView();
            cuentaView.setVisible(true);
        });
        
        btnCerrarSesion.addActionListener(evt -> {
            this.setVisible(false);
            java.awt.EventQueue.invokeLater(() -> {
                LoginView loginView = new LoginView();
                loginView.setLocationRelativeTo(null);
                loginView.setVisible(true);
                this.dispose();
            });
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDatosCuenta = new javax.swing.JButton();
        btnRetiro = new javax.swing.JButton();
        btnDeposito = new javax.swing.JButton();
        btnTransferencia = new javax.swing.JButton();
        btnMovimientos = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnDatosCuenta.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnDatosCuenta.setText("Ver datos de Cuenta");

        btnRetiro.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnRetiro.setText("Retiro");

        btnDeposito.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnDeposito.setText("Depósitos");

        btnTransferencia.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnTransferencia.setText("Transferencia");

        btnMovimientos.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnMovimientos.setText("Movimientos");

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 24)); // NOI18N
        jLabel1.setText("Bienvenido a EurekaBank");

        btnCerrarSesion.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnCerrarSesion.setText("Cerrar Sesión");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(124, 124, 124)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(78, 78, 78)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(256, 256, 256)
                        .addComponent(btnDatosCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(148, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(btnCerrarSesion)
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCerrarSesion)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnMovimientos, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(261, 261, 261))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnTransferencia, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(btnRetiro, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(52, 52, 52))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeposito, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(38, 38, 38)))
                        .addComponent(btnDatosCuenta, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new MenuView().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnDatosCuenta;
    private javax.swing.JButton btnDeposito;
    private javax.swing.JButton btnMovimientos;
    private javax.swing.JButton btnRetiro;
    private javax.swing.JButton btnTransferencia;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
