/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ec.edu.monster.vista;
import ec.edu.monster.controlador.MainController;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author JOHAN
 */
public class LoginView extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginView.class.getName());
    private final MainController mainController = new MainController();
    private Color colorFondo = new Color(118, 75, 162); // #764ba2
    private Color colorGradiente1 = new Color(102, 126, 234); // #667eea
    private Color colorGradiente2 = new Color(118, 75, 162); // #764ba2

    // Nuevos componentes para el diseño web
    private RoundedCardPanel cardPanel;
    private JPanel mainPanel;
    private JLabel lblIconoUniversidad;
    private JLabel lblTitulo;
    private JLabel lblSubtitulo;
    private JLabel lblUsuario;
    private JLabel lblPassword;
    private IconTextField txtUsuario;
    private IconPasswordField txtPassword;
    private GradientButton btnIniciarSesion;
    private JLabel lblError;
    private JLabel lblSullyvan;
    private String usuarioActual;
    
    /**
     * Creates new form LoginView
     */
    public LoginView() {
        initComponents();
        crearDiseñoWeb();
    }
    
    private void crearDiseñoWeb() {
        // Configurar frame
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(UIHelper.COLOR_FONDO);
        setBackground(UIHelper.COLOR_FONDO);
        
        // Panel principal con layout null para posicionamiento absoluto
        mainPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(UIHelper.COLOR_FONDO);
            }
        };
        mainPanel.setBackground(UIHelper.COLOR_FONDO);
        mainPanel.setOpaque(true);
        
        // Cargar imagen de sullyvan - usar classpath
        try {
            java.net.URL imageUrl = getClass().getResource("/ec/edu/monster/images/sullyvan.png");
            if (imageUrl == null) {
                // Intentar ruta de archivo
                File imageFile = new File("src/main/java/ec/edu/monster/images/sullyvan.png");
                if (!imageFile.exists()) {
                    imageFile = new File("02.CLIESC/CLIESC_EUREKA_GR08/src/main/java/ec/edu/monster/images/sullyvan.png");
                }
                if (imageFile.exists()) {
                    imageUrl = imageFile.toURI().toURL();
                }
            }
            if (imageUrl != null) {
                Image img = ImageIO.read(imageUrl);
                int maxHeight = 600;
                int maxWidth = 400;
                int imgWidth = img.getWidth(null);
                int imgHeight = img.getHeight(null);
                double scale = Math.min((double)maxWidth / imgWidth, (double)maxHeight / imgHeight);
                int scaledWidth = (int)(imgWidth * scale);
                int scaledHeight = (int)(imgHeight * scale);
                Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                lblSullyvan = new JLabel(new ImageIcon(scaledImg));
                mainPanel.add(lblSullyvan);
            }
        } catch (Exception e) {
            logger.warning("No se pudo cargar la imagen sullyvan.png: " + e.getMessage());
        }
        
        // Crear card blanco centrado
        cardPanel = new RoundedCardPanel();
        cardPanel.setLayout(new BorderLayout());
        cardPanel.setBackground(Color.WHITE);
        int cardWidth = 450;
        int cardHeight = 550;
        int cardX = (800 - cardWidth) / 2; // Centrado horizontalmente (asumiendo ancho de 800)
        int cardY = 50; // Margen superior
        cardPanel.setBounds(cardX, cardY, cardWidth, cardHeight);
        
        // Panel de contenido dentro del card
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new javax.swing.BoxLayout(contentPanel, javax.swing.BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(48, 48, 48, 48)); // 3rem = 48px
        
        // Header del login
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, 40, 0)); // margin-bottom: 2.5rem
        
        // Icono de universidad grande con gradiente (simulado)
        lblIconoUniversidad = new JLabel("🏛️") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 64)); // 4rem
                // Dibujar texto con gradiente (simulado con color intermedio)
                g2d.setColor(new Color(102, 126, 234));
                g2d.drawString("🏛️", 0, getHeight() - 10);
                g2d.dispose();
            }
        };
        lblIconoUniversidad.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 64));
        lblIconoUniversidad.setHorizontalAlignment(JLabel.CENTER);
        lblIconoUniversidad.setPreferredSize(new Dimension(100, 80));
        
        // Título
        lblTitulo = new JLabel("Eureka Bank");
        lblTitulo.setFont(new Font("Poppins", Font.BOLD, 32)); // 2rem
        lblTitulo.setForeground(UIHelper.COLOR_TEXTO);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        
        // Subtítulo
        lblSubtitulo = new JLabel("Iniciar Sesión");
        lblSubtitulo.setFont(new Font("Poppins", Font.PLAIN, 15));
        lblSubtitulo.setForeground(UIHelper.COLOR_TEXTO_SECUNDARIO);
        lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);
        
        headerPanel.add(lblIconoUniversidad, BorderLayout.NORTH);
        headerPanel.add(lblTitulo, BorderLayout.CENTER);
        headerPanel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        // Panel de formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new javax.swing.BoxLayout(formPanel, javax.swing.BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        
        // Label y campo de usuario
        lblUsuario = new JLabel(UIHelper.Icons.USER + " Usuario");
        lblUsuario.setFont(new Font("Poppins", Font.BOLD, 15));
        lblUsuario.setForeground(new Color(85, 85, 85)); // #555
        lblUsuario.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        txtUsuario = new IconTextField();
        txtUsuario.setIcon(UIHelper.Icons.USER);
        txtUsuario.setPlaceholder("Ingrese su usuario");
        txtUsuario.setPreferredSize(new Dimension(0, 50));
        txtUsuario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JPanel userGroup = new JPanel();
        userGroup.setLayout(new javax.swing.BoxLayout(userGroup, javax.swing.BoxLayout.Y_AXIS));
        userGroup.setOpaque(false);
        userGroup.setBorder(new EmptyBorder(0, 0, 24, 0)); // margin-bottom: 1.5rem
        userGroup.add(lblUsuario);
        userGroup.add(txtUsuario);
        
        // Label y campo de contraseña
        lblPassword = new JLabel(UIHelper.Icons.LOCK + " Contraseña");
        lblPassword.setFont(new Font("Poppins", Font.BOLD, 15));
        lblPassword.setForeground(new Color(85, 85, 85));
        lblPassword.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        txtPassword = new IconPasswordField();
        txtPassword.setIcon(UIHelper.Icons.LOCK);
        txtPassword.setPreferredSize(new Dimension(0, 50));
        txtPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        
        JPanel passwordGroup = new JPanel();
        passwordGroup.setLayout(new javax.swing.BoxLayout(passwordGroup, javax.swing.BoxLayout.Y_AXIS));
        passwordGroup.setOpaque(false);
        passwordGroup.setBorder(new EmptyBorder(0, 0, 24, 0));
        passwordGroup.add(lblPassword);
        passwordGroup.add(txtPassword);
        
        // Botón de login con gradiente
        btnIniciarSesion = new GradientButton(UIHelper.Icons.USER + " Iniciar Sesión");
        btnIniciarSesion.setPreferredSize(new Dimension(0, 55));
        btnIniciarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        btnIniciarSesion.addActionListener(evt -> {
            String username = txtUsuario.getText() != null ? txtUsuario.getText().trim() : "";
            String password = new String(txtPassword.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                mostrarError("Ingrese usuario y contraseña.");
                return;
            }
            boolean ok = mainController.iniciarSesion(username, password);
            if (ok) {
                usuarioActual = username;
                this.setVisible(false);
                java.awt.EventQueue.invokeLater(() -> {
                    MenuView mv = new MenuView(usuarioActual);
                    mv.setLocationRelativeTo(null);
                    mv.setVisible(true);
                    this.dispose();
                });
            } else {
                mostrarError("Credenciales inválidas.");
            }
        });
        
        // Label de error
        lblError = new JLabel();
        lblError.setFont(new Font("Poppins", Font.PLAIN, 14));
        lblError.setForeground(UIHelper.COLOR_ERROR);
        lblError.setVisible(false);
        lblError.setBorder(new EmptyBorder(16, 24, 0, 24));
        lblError.setBackground(UIHelper.COLOR_ALERTA_ERROR);
        lblError.setOpaque(true);
        lblError.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, new Color(245, 87, 108)),
            new EmptyBorder(16, 20, 16, 20)
        ));
        
        // Agregar componentes al form panel
        formPanel.add(headerPanel);
        formPanel.add(userGroup);
        formPanel.add(passwordGroup);
        formPanel.add(btnIniciarSesion);
        formPanel.add(lblError);
        
        contentPanel.add(formPanel);
        cardPanel.add(contentPanel, BorderLayout.CENTER);
        
        mainPanel.add(cardPanel);
        
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        
        // Configurar tamaño del frame y layout
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Actualizar posiciones cuando el frame se redimensiona
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                actualizarPosiciones();
            }
        });
        
        // Posicionar elementos
        actualizarPosiciones();
        
        // Sincronizar campos antiguos con nuevos (para mantener funcionalidad)
        txtUser = txtUsuario;
        jTextField1 = txtPassword;
        btnLogin = btnIniciarSesion;
        jLabel3 = lblError;
    }
    
    private void actualizarPosiciones() {
        int frameWidth = getWidth();
        int frameHeight = getHeight();
        
        // Centrar card
        if (cardPanel != null) {
            int cardWidth = 450;
            int cardHeight = 550;
            int cardX = (frameWidth - cardWidth) / 2;
            int cardY = (frameHeight - cardHeight) / 2 - 50;
            cardPanel.setBounds(cardX, cardY, cardWidth, cardHeight);
        }
        
        // Posicionar imagen sullyvan en esquina inferior izquierda
        if (lblSullyvan != null) {
            int imgWidth = lblSullyvan.getPreferredSize().width;
            int imgHeight = lblSullyvan.getPreferredSize().height;
            lblSullyvan.setBounds(0, frameHeight - imgHeight, imgWidth, imgHeight);
        }
    }
    
    private void mostrarError(String mensaje) {
        lblError.setText(UIHelper.Icons.EXCLAMATION + " " + mensaje);
        lblError.setVisible(true);
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    
    private void aplicarEstiloModerno() {
        // Este método ya no se usa, pero se mantiene para compatibilidad
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLogin = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnLogin.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        btnLogin.setText("Iniciar Sesión");
        btnLogin.setName("btnLogin"); // NOI18N
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel1.setText("Usuario");
        jLabel1.setName("lblUser"); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI Black", 0, 36)); // NOI18N
        jLabel2.setText("EUREKA BANK");
        jLabel2.setName("lblPassword"); // NOI18N

        txtUser.setName("txtUser"); // NOI18N
        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        jLabel3.setName("lblMensaje"); // NOI18N

        jLabel4.setFont(new java.awt.Font("Segoe UI Black", 0, 18)); // NOI18N
        jLabel4.setText("Contraseña:");
        jLabel4.setName("lblPassword"); // NOI18N

        jTextField1.setText("jPasswordField1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(142, 142, 142)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(42, 42, 42)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 284, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(255, 255, 255)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(220, 220, 220)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(193, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(304, Short.MAX_VALUE)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(187, 187, 187)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(156, 156, 156)
                    .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(295, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUserActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        String username = txtUser.getText() != null ? txtUser.getText().trim() : "";
        String password = new String(((javax.swing.JPasswordField) jTextField1).getPassword());
        if (username.isEmpty() || password.isEmpty()) {
            jLabel3.setText("Ingrese usuario y contraseña.");
            return;
        }
        boolean ok = mainController.iniciarSesion(username, password);
        if (ok) {
            this.setVisible(false);
            java.awt.EventQueue.invokeLater(() -> {
                MenuView mv = new MenuView();
                mv.setLocationRelativeTo(null);
                mv.setVisible(true);
                this.dispose();
            });
        } else {
            jLabel3.setText("Credenciales inválidas.");
        }
    }//GEN-LAST:event_btnLoginActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new LoginView().setVisible(true));
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jTextField1;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
