/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package gui;

import oop.classes.actors.User;
import oop.classes.actors.Employee;
import oop.classes.calculations.SalaryCalculation;
import oop.classes.calculations.DeductionCalculation;
import oop.classes.empselfservice.Payslip;
import CSV.CSVDatabaseProcessor;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.time.YearMonth;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
/**
 * This class is the payroll details overview of all employees. Managed by Accounting for approval & disbursement
 * @author USER
 */
public class PayrollSummaryReport extends javax.swing.JFrame {

    private User loggedInUser;
    private SalaryCalculation salaryCalculation;
    private DeductionCalculation deductionCalculation;
    private CSVDatabaseProcessor csvProcessor;
    private YearMonth currentPayrollMonth;
    private boolean payslipsGenerated = false;
    private boolean payrollApproved = false;

    /**
     * Constructor initializes the payroll management form.
     * @param user The logged-in user.
     */
    public PayrollSummaryReport(User user) {
        this.loggedInUser = user;
        this.salaryCalculation = new SalaryCalculation();
        this.deductionCalculation = new DeductionCalculation();
        this.csvProcessor = new CSVDatabaseProcessor();

        // Load attendance data - important for payroll calculations
        this.csvProcessor.loadAttendanceData();

        // Initialize to current payroll month
        this.currentPayrollMonth = YearMonth.now();

        initComponents();
        setupTableColumns();
        setupTableProperties();
        populateEmployeeDropdown();

        // Set default selection to current month and year
        selectDateJComboBox2.setSelectedItem(currentPayrollMonth.getMonth()
            .getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        selectYearJComboBox3.setSelectedItem(String.valueOf(currentPayrollMonth.getYear()));

        // Initially disable approval/denial buttons until payslips are generated
        updateButtonStates();
    }
    
    /**
     * Sets up table properties including horizontal scrolling
     */
    private void setupTableProperties() {
        // Enable auto resize mode to allow horizontal scrolling
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        // Set selection mode to allow multiple row selection
        jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Ensure horizontal scrollbar is always visible
        jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    }
    
    /**
     * Updates the enabled/disabled state of buttons based on the current state
     */
    private void updateButtonStates() {
        // Approval/Denial buttons should only be enabled if payslips have been generated
        approveBttn.setEnabled(payslipsGenerated && !payrollApproved);
        approveAllBttn.setEnabled(payslipsGenerated && !payrollApproved);
        denyBttn.setEnabled(payslipsGenerated && !payrollApproved);
        denyAllBttn.setEnabled(payslipsGenerated && !payrollApproved);
        
        // Download button should only be enabled if payslips have been generated
        downloadPayslip.setEnabled(payslipsGenerated);
    }
    
    /**
     * Populates the employee dropdown with all employee IDs from the CSV database.
     */
    private void populateEmployeeDropdown() {
        try {
            // Clear existing items
            selectDepJComboBox1.removeAllItems();
            
            // Add "All" option
            selectDepJComboBox1.addItem("All");
            
            // Get all employees from the database
            List<Map<String, String>> allEmployees = csvProcessor.getAllEmployeeRecords();
            
            // Sort employees by ID
            allEmployees.sort((emp1, emp2) -> {
                String id1 = emp1.get("Employee ID");
                String id2 = emp2.get("Employee ID");
                return id1.compareTo(id2);
            });
            
            // Add each employee ID to the dropdown
            for (Map<String, String> employee : allEmployees) {
                String empId = employee.get("Employee ID");
                if (empId != null && !empId.isEmpty()) {
                    selectDepJComboBox1.addItem(empId);
                }
            }
        } catch (Exception e) {
            System.err.println("Error populating employee dropdown: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets up the table columns to match the required payroll data fields.
     */
    private void setupTableColumns() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setColumnIdentifiers(new Object[]{
            "Employee ID", "Last Name", "First Name", "Position", 
            "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Total Allow.", 
            "Gross Pay", "SSS", "PhilHealth", "Pag-Ibig", "Late Deductions", "With. Tax", 
            "Total Deductions", "Net Pay"
        });

        // Clear any existing data
        model.setRowCount(0);
    }

    /**
     * Loads payroll data for all employees or a selected employee.
     */
    private void loadPayrollData() {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            // Get selected employee ID (if any)
            String selectedEmployeeId = (String) selectDepJComboBox1.getSelectedItem();

            // Get selected month and year
            String selectedMonthStr = (String) selectDateJComboBox2.getSelectedItem();
            String selectedYearStr = (String) selectYearJComboBox3.getSelectedItem();

            // Set the current payroll month based on selection
            Month selectedMonth = Month.valueOf(selectedMonthStr.toUpperCase());
            int selectedYear = Integer.parseInt(selectedYearStr);
            currentPayrollMonth = YearMonth.of(selectedYear, selectedMonth);

            // Get employee IDs to process
            List<String> employeeIds = new ArrayList<>();

            if ("ALL".equalsIgnoreCase(selectedEmployeeId) || "All".equals(selectedEmployeeId)) {
                // Process all employees by getting IDs from CSVDatabaseProcessor
                List<Map<String, String>> allEmployees = csvProcessor.getAllEmployeeRecords();
                for (Map<String, String> employee : allEmployees) {
                    String empId = employee.get("Employee ID");
                    if (empId != null && !empId.isEmpty()) {
                        employeeIds.add(empId);
                    }
                }
            } else {
                // Process only the selected employee
                employeeIds.add(selectedEmployeeId);
            }

            // Load data for each employee
            for (String employeeId : employeeIds) {
                loadEmployeePayrollData(employeeId, model);
            }

            // Mark payslips as generated
            payslipsGenerated = true;
            updateButtonStates();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading payroll data: " + e.getMessage(), 
                "Payroll Data Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
        /**
         * Loads payroll data for a specific employee.
         * 
         * @param employeeId The employee ID to load data for
         * @param model The table model to add the data to
         */
        private void loadEmployeePayrollData(String employeeId, DefaultTableModel model) {
            try {
                // Get employee details
                Map<String, String> employeeData = csvProcessor.getEmployeeRecordsByEmployeeId(employeeId);

                if (employeeData == null) {
                    System.err.println("No employee record found for ID: " + employeeId);
                    return;
                }

                // Extract employee information
                String lastName = employeeData.get("Last Name");
                String firstName = employeeData.get("First Name");
                String position = employeeData.get("Position");

                // Extract allowances (remove commas and convert to numeric)
                double riceSubsidy = parseAmount(employeeData.get("Rice Subsidy"));
                double phoneAllowance = parseAmount(employeeData.get("Phone Allowance"));
                double clothingAllowance = parseAmount(employeeData.get("Clothing Allowance"));
                double totalAllowances = riceSubsidy + phoneAllowance + clothingAllowance;

                // Calculate gross pay
                double grossPay = salaryCalculation.calculateGrossMonthlySalary(
                    employeeId, currentPayrollMonth, csvProcessor);

                // Debug logging
                System.out.println("Employee ID: " + employeeId + ", Gross Pay: " + grossPay);

                // Get hourly rate for late deduction calculation
                double hourlyRate = parseAmount(employeeData.get("Hourly Rate"));

                // Calculate government deductions
                double sssDeduction = deductionCalculation.calculateSSS(grossPay);
                double philHealthDeduction = deductionCalculation.calculatePhilHealth(grossPay);
                double pagIbigDeduction = deductionCalculation.calculatePagibig(grossPay);

                // Calculate late deductions from attendance records
                double lateHours = csvProcessor.getTotalLateHours(employeeId, currentPayrollMonth);
                double lateDeduction = 0.0;
                if (lateHours > 0) {
                    lateDeduction = deductionCalculation.calculateLateDeductions(lateHours, hourlyRate);
                    System.out.println("Late hours: " + lateHours + ", Late deduction: " + lateDeduction);
                }

                // Calculate total government contributions (without late deductions)
                double totalContributions = sssDeduction + philHealthDeduction + pagIbigDeduction;

                // Calculate taxable income (gross pay minus government contributions AND late deductions)
                double taxableIncome = grossPay - totalContributions - lateDeduction;

                // Calculate withholding tax based on taxable income
                double withholdingTax = deductionCalculation.calculateTax(taxableIncome);

                // Debug logging for deductions
                System.out.println("SSS: " + sssDeduction + ", PhilHealth: " + philHealthDeduction + 
                    ", Pag-Ibig: " + pagIbigDeduction + ", Tax: " + withholdingTax);

                // Calculate total deductions (government + late + tax)
                double totalDeductions = totalContributions + lateDeduction + withholdingTax;

                // Calculate net pay
                double netPay = grossPay - totalDeductions;

                // Add row to the table
                model.addRow(new Object[]{
                    employeeId,
                    lastName,
                    firstName,
                    position,
                    formatCurrency(riceSubsidy),
                    formatCurrency(phoneAllowance),
                    formatCurrency(clothingAllowance),
                    formatCurrency(totalAllowances),
                    formatCurrency(grossPay),
                    formatCurrency(sssDeduction),
                    formatCurrency(philHealthDeduction),
                    formatCurrency(pagIbigDeduction),
                    formatCurrency(lateDeduction),  // Added late deduction column
                    formatCurrency(withholdingTax),
                    formatCurrency(totalDeductions),
                    formatCurrency(netPay)
                });

            } catch (Exception e) {
                System.err.println("Error processing employee ID " + employeeId + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

            /**
             * Helper method to parse currency amounts from strings, handling commas.
             * 
             * @param amountStr The amount string to parse
             * @return The parsed numeric value
             */
            private double parseAmount(String amountStr) {
                if (amountStr == null || amountStr.isEmpty()) {
                    return 0.0;
                }
                // Remove commas and other non-numeric characters except decimal point
                String cleanedAmount = amountStr.replaceAll("[^0-9.]", "");
                try {
                    return Double.parseDouble(cleanedAmount);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing amount: " + amountStr);
                    return 0.0;
                }
            }

            /**
             * Formats a numeric value as a currency string.
             * 
             * @param amount The amount to format
             * @return The formatted currency string
             */
            private String formatCurrency(double amount) {
                return String.format("₱%.2f", amount);
            }

            /**
             * Generates and downloads a payslip for a specific employee.
             * 
             * @param employeeId ID of the employee
             * @param firstName First name of the employee
             * @param lastName Last name of the employee
             */
            private void generateAndDownloadPayslip(String employeeId, String firstName, String lastName) {
                try {
                    // Create an Employee object for the Payslip class
                    Employee employee = new Employee(
                        Integer.parseInt(employeeId),
                        firstName,
                        lastName,
                        "", // Email (not needed for payslip)
                        "", // Password (not needed for payslip)
                        ""  // Role (not needed for payslip)
                    );

                    // Find the row in the table that corresponds to this employee
                    int rowCount = jTable1.getRowCount();
                    int rowIndex = -1;

                    for (int i = 0; i < rowCount; i++) {
                        String id = jTable1.getValueAt(i, 0).toString();
                        if (id.equals(employeeId)) {
                            rowIndex = i;
                            break;
                        }
                    }

                    if (rowIndex == -1) {
                        JOptionPane.showMessageDialog(this, 
                            "Employee data not found in the table. Please generate payslip first.", 
                            "Payslip Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Extract values from the table
                    double riceSubsidy = parseAmount(jTable1.getValueAt(rowIndex, 4).toString());
                    double phoneAllowance = parseAmount(jTable1.getValueAt(rowIndex, 5).toString());
                    double clothingAllowance = parseAmount(jTable1.getValueAt(rowIndex, 6).toString());
                    double totalAllowances = parseAmount(jTable1.getValueAt(rowIndex, 7).toString());
                    double grossPay = parseAmount(jTable1.getValueAt(rowIndex, 8).toString());
                    double sssDeduction = parseAmount(jTable1.getValueAt(rowIndex, 9).toString());
                    double philHealthDeduction = parseAmount(jTable1.getValueAt(rowIndex, 10).toString());
                    double pagIbigDeduction = parseAmount(jTable1.getValueAt(rowIndex, 11).toString());
                    double lateDeduction = parseAmount(jTable1.getValueAt(rowIndex, 12).toString());
                    double withholdingTax = parseAmount(jTable1.getValueAt(rowIndex, 13).toString());
                    double totalDeductions = parseAmount(jTable1.getValueAt(rowIndex, 14).toString());
                    double netPay = parseAmount(jTable1.getValueAt(rowIndex, 15).toString());

                    // Calculate taxable income 
                    double taxableIncome = grossPay - sssDeduction - philHealthDeduction - pagIbigDeduction - lateDeduction;

                    // Create Payslip
                    Payslip payslip = new Payslip(employee);

                    // Set the payroll month
                    payslip.setPayrollMonth(currentPayrollMonth);

                    // Set all pre-calculated values
                    payslip.setCalculatedValues(
                        grossPay, // Using gross pay as basic salary too
                        grossPay,
                        sssDeduction,
                        philHealthDeduction,
                        pagIbigDeduction,
                        lateDeduction,
                        withholdingTax,
                        totalDeductions,
                        taxableIncome,
                        netPay
                    );

                    // Generate and download the payslip
                    payslip.printPayslip(
                        currentPayrollMonth.getMonthValue(),
                        currentPayrollMonth.getYear()
                    );

                } catch (IOException | DocumentException e) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Error generating payslip: " + e.getMessage(),
                        "Payslip Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                    e.printStackTrace();
                }
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
        backpyrllmngmntbttn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        selectDepJComboBox1 = new javax.swing.JComboBox<>();
        selectDateJComboBox2 = new javax.swing.JComboBox<>();
        generatePayslip = new javax.swing.JButton();
        downloadPayslip1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(220, 95, 0));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Payroll Summary Report");

        backpyrllmngmntbttn.setBackground(new java.awt.Color(207, 10, 10));
        backpyrllmngmntbttn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        backpyrllmngmntbttn.setForeground(new java.awt.Color(255, 255, 255));
        backpyrllmngmntbttn.setText("Back");
        backpyrllmngmntbttn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backpyrllmngmntbttnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(backpyrllmngmntbttn)
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(656, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(backpyrllmngmntbttn)
                    .addComponent(jLabel1))
                .addGap(15, 15, 15))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Employee ID", "Last Name", "First Name", "Position", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Total Allow.", "Gross Pay", "SSS", "PhilHealth", "PagIbig", "Late Deductions", "With. Tax", "Total Deductions", "Net Pay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(jTable1);

        jLabel2.setText("Department:");

        jLabel3.setText("Select Date:");

        selectDepJComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Leadership", "IT", "HR", "Accounting", "Accounts", "Sales & Marketing", "Supply Chain & Logistics", "Customer Service" }));
        selectDepJComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDepJComboBox1ActionPerformed(evt);
            }
        });

        selectDateJComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "2024-06", "2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12" }));
        selectDateJComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectDateJComboBox2ActionPerformed(evt);
            }
        });

        generatePayslip.setBackground(new java.awt.Color(220, 95, 0));
        generatePayslip.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        generatePayslip.setForeground(new java.awt.Color(255, 255, 255));
        generatePayslip.setText("Generate Payroll Summary Report");
        generatePayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatePayslipActionPerformed(evt);
            }
        });

        downloadPayslip1.setBackground(new java.awt.Color(220, 95, 0));
        downloadPayslip1.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        downloadPayslip1.setForeground(new java.awt.Color(255, 255, 255));
        downloadPayslip1.setText("View Generated Reports History");
        downloadPayslip1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downloadPayslip1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(selectDepJComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(selectDateJComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(generatePayslip)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(downloadPayslip1, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 939, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(6, 6, 6)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(selectDepJComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(selectDateJComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(generatePayslip)
                    .addComponent(downloadPayslip1))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   // Select month filter
    private void selectDateJComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDateJComboBox2ActionPerformed
        // Reset the payslips generated when user selection changes
        payslipsGenerated = false;
        payrollApproved = false;
        updateButtonStates();
        
        // Clear the table
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_selectDateJComboBox2ActionPerformed

    private void backpyrllmngmntbttnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backpyrllmngmntbttnActionPerformed
        new AdminAccounting(loggedInUser).setVisible(true);
        this.dispose(); //close window
    }//GEN-LAST:event_backpyrllmngmntbttnActionPerformed
    //Select employee ID filter action 
    private void selectDepJComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectDepJComboBox1ActionPerformed
        // Reset the payslips generated when user selection changes
        payslipsGenerated = false;
        payrollApproved = false;
        updateButtonStates();
        
        // Clear the table
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        model.setRowCount(0);
    }//GEN-LAST:event_selectDepJComboBox1ActionPerformed

   //Generate payslip button action; basically generates or calculates all employee's payroll for a pay period
    private void generatePayslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatePayslipActionPerformed
    String selectedEmployeeId = (String) selectDepJComboBox1.getSelectedItem();
    String selectedMonthStr = (String) selectDateJComboBox2.getSelectedItem();
    String selectedYearStr = (String) selectYearJComboBox3.getSelectedItem();
    
    // Show loading message
    JOptionPane.showMessageDialog(this, 
        "Calculating payroll for " + 
        (selectedEmployeeId.equalsIgnoreCase("ALL") || selectedEmployeeId.equals("All") ? "all employees" : "employee " + selectedEmployeeId) + 
        " for " + selectedMonthStr + " " + selectedYearStr + "...",
        "Generating Payslips",
        JOptionPane.INFORMATION_MESSAGE);
    
    // Load the payroll data
    loadPayrollData();
    
    // Show confirmation message
    int employeeCount = jTable1.getRowCount();
    JOptionPane.showMessageDialog(this, 
        "Successfully generated payslips for " + employeeCount + " employees.\n" +
        "Pay period: " + selectedMonthStr + " " + selectedYearStr + "\n\n" +
        "You can now approve or deny the payroll.",
        "Payslips Generated",
        JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_generatePayslipActionPerformed

    private void downloadPayslip1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_downloadPayslip1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_downloadPayslip1ActionPerformed

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
            java.util.logging.Logger.getLogger(PayrollSummaryReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayrollSummaryReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayrollSummaryReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayrollSummaryReport.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Create an Employee object (concrete subclass of User)
                Employee user = new Employee(10001, "Test", "User", "test@email.com", "password", "ACCOUNTING");
                new PayrollSummaryReport(user).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton backpyrllmngmntbttn;
    private javax.swing.JButton downloadPayslip1;
    private javax.swing.JButton generatePayslip;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JComboBox<String> selectDateJComboBox2;
    private javax.swing.JComboBox<String> selectDepJComboBox1;
    // End of variables declaration//GEN-END:variables
}
