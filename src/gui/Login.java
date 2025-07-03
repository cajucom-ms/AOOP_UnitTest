
package gui;

import CSV.CSVDatabaseProcessor;
import oop.classes.management.UserAuthentication;
import javax.swing.*;
import oop.classes.actors.User;

/**
 * This GUI is responsible for handling user login!
 * It checks the user's email and password, then sends them to the right landing page according to their user role!
 * @author Admin
 */

public class Login extends javax.swing.JFrame {

    private final UserAuthentication userAuth;

    public Login() {
        CSVDatabaseProcessor csvProcessor = new CSVDatabaseProcessor();
        csvProcessor.loadUserCredentialData(); // Load user credentials here!
 
        userAuth = new UserAuthentication(csvProcessor);
        initComponents();
    }

    private void login() {
        String email = txtEmail.getText().trim();
        String password = new String(jPasswordField1.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Oops! Please enter both email and password!", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Checking if the user exists in the system
        User user = userAuth.validateCredentials(email, password);

        if (user != null) {
            System.out.println("Login successful for user: " + user.getEmail());
            JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            redirectUserBasedOnRole(user);
        } else {
            System.out.println("Login failed for email: " + email);
            JOptionPane.showMessageDialog(this, "Uh-oh! Invalid Email or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void redirectUserBasedOnRole(User user) {
        if (user == null) {
            JOptionPane.showMessageDialog(this, "Oops! Something went wrong!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String role = user.getRole();

        switch (role) {
            case "HR":
                AdminHR adminHR = new AdminHR(user);
                adminHR.setVisible(true);
                break;
            
            case "ACCOUNTING":
                AdminAccounting adminAccounting = new AdminAccounting(user);
                adminAccounting.setVisible(true);
                break;
            
            case "IT":
                AdminIT adminIT = new AdminIT(user);
                adminIT.setVisible(true);
                break;
            
            
            case "EMPLOYEE":
                EmployeeSelfService employeeSelfService = new EmployeeSelfService(user);
                employeeSelfService.setVisible(true);
                break;

            case "IMMEDIATE SUPERVISOR":
                AdminSupervisor adminSupervisor = new AdminSupervisor(user);
                adminSupervisor.setVisible(true);
                break;
                
            default:
                JOptionPane.showMessageDialog(this, "Unknown role: " + role, "Error", JOptionPane.ERROR_MESSAGE);
                return; // Prevents closing the login window if role is invalid
        }

        this.dispose(); // Closes the login window after opening the correct dashboard
    }




    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        showPassword = new javax.swing.JCheckBox();
        forgetPassword = new javax.swing.JLabel();
        btnLogin = new javax.swing.JButton();
        jPasswordField1 = new javax.swing.JPasswordField();
        motorPHogLogo = new javax.swing.JLabel();
        redOrangeDesign = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel2.setText("LOGIN");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Email");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Password");

        showPassword.setText("Show password");
        showPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showPasswordActionPerformed(evt);
            }
        });

        forgetPassword.setText("Forget password?");
        forgetPassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                forgetPasswordMouseClicked(evt);
            }
        });

        btnLogin.setBackground(new java.awt.Color(220, 95, 0));
        btnLogin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLogin.setForeground(new java.awt.Color(255, 255, 255));
        btnLogin.setText("Login");
        btnLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoginActionPerformed(evt);
            }
        });

        jPasswordField1.setActionCommand("<Not Set>");
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        motorPHogLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/OG Logo _ 100X124.png"))); // NOI18N
        motorPHogLogo.setText("motorPHogLogo");
        motorPHogLogo.setToolTipText("");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4)
                                .addComponent(txtEmail)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(showPassword)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                                    .addComponent(forgetPassword))
                                .addComponent(jPasswordField1))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(32, 32, 32)
                                .addComponent(motorPHogLogo))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(156, 156, 156)
                        .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(jLabel2)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(motorPHogLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(showPassword)
                    .addComponent(forgetPassword))
                .addGap(41, 41, 41)
                .addComponent(btnLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(59, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(516, 0, -1, 500));

        redOrangeDesign.setIcon(new javax.swing.ImageIcon(getClass().getResource("/media/Red-Orange Design.png"))); // NOI18N
        redOrangeDesign.setText("RedOrangeDesign");
        redOrangeDesign.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        redOrangeDesign.setPreferredSize(new java.awt.Dimension(725, 500));
        getContentPane().add(redOrangeDesign, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 500));

        pack();
    }// </editor-fold>//GEN-END:initComponents

   
       
    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        login();
    }//GEN-LAST:event_btnLoginActionPerformed

    private void showPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showPasswordActionPerformed
    if (showPassword.isSelected()) {
        jPasswordField1.setEchoChar((char) 0); // Show password
    } else {
        jPasswordField1.setEchoChar('•'); // Hide password
    }
    }//GEN-LAST:event_showPasswordActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed
       login();
    }//GEN-LAST:event_jPasswordField1ActionPerformed

    private void forgetPasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_forgetPasswordMouseClicked
       JOptionPane.showMessageDialog(this, "Reset password code has been sent to your company email!", 
                                  "Password Reset", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_forgetPasswordMouseClicked

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JLabel forgetPassword;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JLabel motorPHogLogo;
    private javax.swing.JLabel redOrangeDesign;
    private javax.swing.JCheckBox showPassword;
    private javax.swing.JTextField txtEmail;
    // End of variables declaration//GEN-END:variables
}
