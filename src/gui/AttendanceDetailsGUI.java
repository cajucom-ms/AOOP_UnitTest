
package gui;

import CSV.CSVDatabaseProcessor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import oop.classes.actors.User;

/**
 * This class displays attendance details for the logged-in user
 * It can be accessed from different dashboards (AdminIT, AdminHR, etc.)
 */
public class AttendanceDetailsGUI extends javax.swing.JFrame {

    private CSVDatabaseProcessor csvProcessor;
    private String employeeId;
    private String employeeName;
    // Store the logged-in user for navigation back to the correct dashboard
    private User loggedInUser;
    
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
   
    /**
     * Main constructor that takes a User object
     * This constructor should be used when navigating from dashboards
     * 
     * @param user The currently logged-in user
     */
    public AttendanceDetailsGUI(User user) {
        initComponents();
        
        // Store the user for back navigation
        this.loggedInUser = user;
        
        // Set employee ID from the user object
        this.employeeId = String.valueOf(user.getEmployeeID());
        
        // Set employee name
        this.employeeName = user.getFirstName() + " " + user.getLastName();
        
        // Center the form on screen
        this.setLocationRelativeTo(null);
        
        // Initialize CSV processor
        csvProcessor = new CSVDatabaseProcessor();
        
        // Load employee and attendance data
        csvProcessor.loadEmployeeCSVData();
        csvProcessor.loadAttendanceData();
        
        // Display employee info in the UI
        InputIDNo.setText(employeeId);
        InputEmpName.setText(employeeName);
        
        // Set window title
        setTitle("Attendance Details - " + employeeName);
        
        // Load attendance data
        loadAttendanceData();
        
        System.out.println("AttendanceDetailsGUI created for user: " + employeeName + " (ID: " + employeeId + ")");
    }

    /**
     * Default constructor for direct testing or from NetBeans form editor
     * This should NOT be used in production code
     */
    public AttendanceDetailsGUI() {
        initComponents();

        // Center the form on screen
        this.setLocationRelativeTo(null);

        // Initialize CSV processor
        csvProcessor = new CSVDatabaseProcessor();
        
        // For testing purposes only - use a default employee ID
        this.employeeId = "10001"; // Example ID for testing
        
        // Load CSV data
        csvProcessor.loadEmployeeCSVData();
        csvProcessor.loadAttendanceData();

        // Get employee name for the default ID
        Map<String, String> employeeData = csvProcessor.getEmployeeRecordsByEmployeeId(employeeId);

        if (employeeData != null) {
            // Set employee info in the UI
            String firstName = employeeData.get("First Name");
            String lastName = employeeData.get("Last Name");
            employeeName = firstName + " " + lastName;

            InputIDNo.setText(employeeId);
            InputEmpName.setText(employeeName);

            // Load attendance data
            loadAttendanceData();
        } else {
            JOptionPane.showMessageDialog(this, "Employee data not found for ID: " + employeeId,
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        System.out.println("AttendanceDetailsGUI created with default constructor (testing only)");
    }

    /**
     * Load attendance data from CSV into the table
     * This method filters data to only show the logged-in user's records
     */
    private void loadAttendanceData() {
        try {
            // Make sure attendance data is loaded
            csvProcessor.loadAttendanceData();

            // Get attendance records for the specific employee only
            List<Map<String, Object>> attendanceRecords = csvProcessor.getAttendanceRecordsByEmployeeId(employeeId);

            if (attendanceRecords == null || attendanceRecords.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No attendance records found for employee ID: " + employeeId,
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                
                // Clear the table if no records
                DefaultTableModel model = (DefaultTableModel) AttendanceDetailsTbl.getModel();
                model.setRowCount(0);
                return;
            }

            // Get the table model and clear it
            DefaultTableModel model = (DefaultTableModel) AttendanceDetailsTbl.getModel();
            model.setRowCount(0); // Clear existing data

            // Add attendance records to the table
            for (Map<String, Object> record : attendanceRecords) {
                LocalDate date = (LocalDate) record.get("Date");
                LocalTime timeIn = (LocalTime) record.get("Log In");
                LocalTime timeOut = (LocalTime) record.get("Log Out");

                model.addRow(new Object[]{
                    employeeId,
                    date != null ? date.format(dateFormatter) : "N/A",
                    timeIn != null ? timeIn.format(timeFormatter) : "N/A",
                    timeOut != null ? timeOut.format(timeFormatter) : "N/A"
                });
            }
            
            System.out.println("Loaded " + attendanceRecords.size() + " attendance records for employee ID: " + employeeId);
        } catch (Exception e) {
            System.err.println("Error loading attendance data: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading attendance data: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Navigate back to the appropriate dashboard based on user role
     */
    private void goBack() {
        if (loggedInUser != null) {
            String role = loggedInUser.getRole();
            System.out.println("Navigating back based on role: " + role);
            
            try {
                switch (role) {
                    case "IT":
                        AdminIT adminIT = new AdminIT(loggedInUser);
                        adminIT.setVisible(true);
                        break;
                        
                    case "HR":
                        AdminHR adminHR = new AdminHR(loggedInUser);
                        adminHR.setVisible(true);
                        break;
                        
                    case "ACCOUNTING":
                        AdminAccounting adminAccounting = new AdminAccounting(loggedInUser);
                        adminAccounting.setVisible(true);
                        break;
                        
                    case "IMMEDIATE SUPERVISOR":
                        AdminSupervisor adminSupervisor = new AdminSupervisor(loggedInUser);
                        adminSupervisor.setVisible(true);
                        break;
                        
                    case "EMPLOYEE":
                        EmployeeSelfService employeeSelfService = new EmployeeSelfService(loggedInUser);
                        employeeSelfService.setVisible(true);
                        break;
                        
                    default:
                        // Unknown role - return to login screen
                        System.err.println("Unknown role: " + role + ", returning to login");
                        new Login().setVisible(true);
                        break;
                }
            } catch (Exception e) {
                System.err.println("Error navigating back: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error navigating back: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
                // Return to login as fallback
                new Login().setVisible(true);
            }
        } else {
            // No user data - return to login
            System.err.println("No user data available, returning to login");
            new Login().setVisible(true);
        }
        
        // Close this window regardless of navigation result
        this.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        backattnddtlsbttn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        AttendanceDetailsTbl = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        InputIDNo3 = new javax.swing.JLabel();
        Date2 = new java.awt.Label();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(220, 95, 0));
        jPanel1.setPreferredSize(new java.awt.Dimension(211, 57));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("ATTENDANCE DETAILS");

        backattnddtlsbttn.setBackground(new java.awt.Color(207, 10, 10));
        backattnddtlsbttn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        backattnddtlsbttn.setForeground(new java.awt.Color(255, 255, 255));
        backattnddtlsbttn.setText("Back");
        backattnddtlsbttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backattnddtlsbttnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(backattnddtlsbttn)
                .addGap(39, 39, 39)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(backattnddtlsbttn))
                .addContainerGap(10, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(984, 442));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AttendanceDetailsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Employee ID", "Date", "Time In", "Time Out", "Late Hours", "Overtime Hours"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(AttendanceDetailsTbl);

        jPanel2.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 71, 830, 300));

        jButton1.setBackground(new java.awt.Color(220, 95, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Overtime Requests");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 220, 45));

        jPanel6.setBackground(new java.awt.Color(220, 95, 0));

        InputIDNo3.setForeground(new java.awt.Color(255, 255, 255));

        Date2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Date2.setForeground(new java.awt.Color(255, 255, 255));
        Date2.setText("Date:");

        jComboBox1.setBackground(new java.awt.Color(255, 255, 255));
        jComboBox1.setEditable(true);
        jComboBox1.setForeground(new java.awt.Color(255, 255, 255));
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "2024-06", "2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(Date2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(565, 565, 565)
                .addComponent(InputIDNo3, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addComponent(InputIDNo3))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(Date2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 250, 40));

        jButton3.setBackground(new java.awt.Color(220, 95, 0));
        jButton3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Submit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 20, 141, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backattnddtlsbttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backattnddtlsbttnActionPerformed
      // Navigate back to appropriate dashboard based on user role
        goBack();
        this.dispose();        
    }//GEN-LAST:event_backattnddtlsbttnActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AttendanceDetailsGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new AttendanceDetailsGUI().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable AttendanceDetailsTbl;
    private java.awt.Label Date2;
    private javax.swing.JLabel InputIDNo3;
    private javax.swing.JButton backattnddtlsbttn;
    private javax.swing.JButton findEmployeeBtn;
    private javax.swing.JButton findEmployeeBtn1;
    private javax.swing.JButton findEmployeeBtn2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
