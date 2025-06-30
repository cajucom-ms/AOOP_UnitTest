/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UnitTestAOOP;

import Models.EmployeeModel;
import Models.EmployeeModel.EmployeeStatus;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author i-Gits <lr.jdelasarmas@mmdc.mcl.edu.ph>
 */

public class EmployeeModelTest {
    private EmployeeModel employee;
    private LocalDateTime testStartTime;
    
    @Before
    public void setUp() {
        testStartTime = LocalDateTime.now();
        employee = new EmployeeModel();
    }
    
    @After
    public void tearDown() {
        employee = null;
    }
    
    // ==================== CONSTRUCTOR TESTS ====================
    
    @Test
    public void testDefaultConstructor() {
        EmployeeModel emp = new EmployeeModel();
        assertNotNull("Employee should not be null", emp);
        assertNotNull("CreatedAt should be initialized", emp.getCreatedAt());
        assertNotNull("UpdatedAt should be initialized", emp.getUpdatedAt());
        assertEquals("Default status should be PROBATIONARY", EmployeeStatus.PROBATIONARY, emp.getStatus());
        assertEquals("Default userRole should be Employee", "Employee", emp.getUserRole());
    }
    
    @Test
    public void testEssentialFieldsConstructor() {
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        EmployeeModel emp = new EmployeeModel("John", "Doe", birthDate, 
                                            "john.doe@example.com", "hashedPassword123", 1);
        
        assertEquals("First name should match", "John", emp.getFirstName());
        assertEquals("Last name should match", "Doe", emp.getLastName());
        assertEquals("Birth date should match", birthDate, emp.getBirthDate());
        assertEquals("Email should match", "john.doe@example.com", emp.getEmail());
        assertEquals("Password hash should match", "hashedPassword123", emp.getPasswordHash());
        assertEquals("Position ID should match", Integer.valueOf(1), emp.getPositionId());
        assertNotNull("CreatedAt should be initialized", emp.getCreatedAt());
        assertNotNull("UpdatedAt should be initialized", emp.getUpdatedAt());
    }
    
    @Test
    public void testFullConstructor() {
        LocalDate birthDate = LocalDate.of(1985, 3, 20);
        LocalDateTime createdAt = LocalDateTime.of(2020, 1, 1, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2023, 6, 15, 10, 30);
        LocalDateTime lastLogin = LocalDateTime.of(2023, 6, 15, 9, 0);
        
        EmployeeModel emp = new EmployeeModel(
            123, "Jane", "Smith", birthDate, "09171234567", "jane.smith@example.com",
            new BigDecimal("50000.00"), new BigDecimal("288.46"), "Admin",
            "hashedPassword456", EmployeeStatus.REGULAR, createdAt,
            updatedAt, lastLogin, 5, 10
        );
        
        assertEquals("Employee ID should match", Integer.valueOf(123), emp.getEmployeeId());
        assertEquals("Status should be REGULAR", EmployeeStatus.REGULAR, emp.getStatus());
        assertEquals("User role should be Admin", "Admin", emp.getUserRole());
        assertEquals("Supervisor ID should match", Integer.valueOf(10), emp.getSupervisorId());
    }
    
    // ==================== BASIC SETTER/GETTER TESTS ====================
    
    @Test
    public void testSetAndGetEmployeeId() {
        employee.setEmployeeId(999);
        assertEquals("Employee ID should be 999", Integer.valueOf(999), employee.getEmployeeId());
    }
    
    @Test
    public void testSetAndGetEmployeeId_Null() {
        employee.setEmployeeId(null);
        assertNull("Employee ID should be null", employee.getEmployeeId());
    }
    
    @Test
    public void testSetAndGetFirstName() {
        employee.setFirstName("TestFirstName");
        assertEquals("First name should match", "TestFirstName", employee.getFirstName());
    }
    
    @Test
    public void testSetAndGetFirstName_EmptyString() {
        employee.setFirstName("");
        assertEquals("Empty first name should be stored", "", employee.getFirstName());
    }
    
    @Test
    public void testSetAndGetFirstName_Null() {
        employee.setFirstName(null);
        assertNull("First name should be null", employee.getFirstName());
    }
    
    @Test
    public void testSetAndGetFirstName_SpecialCharacters() {
        String specialName = "José María O'Connor-Smith";
        employee.setFirstName(specialName);
        assertEquals("Special characters should be preserved", specialName, employee.getFirstName());
    }
    
    @Test
    public void testSetAndGetLastName_LongName() {
        String longName = "VeryLongLastNameThatExceedsNormalLengthButShouldStillWork";
        employee.setLastName(longName);
        assertEquals("Long last name should be stored", longName, employee.getLastName());
    }
    
    // ==================== BIRTH DATE TESTS ====================
    
    @Test
    public void testSetAndGetBirthDate() {
        LocalDate birthDate = LocalDate.of(1995, 12, 25);
        employee.setBirthDate(birthDate);
        assertEquals("Birth date should match", birthDate, employee.getBirthDate());
    }
    
    @Test
    public void testSetBirthDate_Null() {
        employee.setBirthDate(null);
        assertNull("Birth date should be null", employee.getBirthDate());
    }
    
    @Test
    public void testSetBirthDate_FutureDate() {
        // NEGATIVE TEST: Setting future birth date (invalid but model doesn't validate)
        LocalDate futureDate = LocalDate.now().plusDays(1);
        employee.setBirthDate(futureDate);
        assertEquals("Future birth date should be stored (no validation in model)", 
                    futureDate, employee.getBirthDate());
    }
    
    @Test
    public void testSetBirthDate_VeryOldDate() {
        LocalDate veryOldDate = LocalDate.of(1900, 1, 1);
        employee.setBirthDate(veryOldDate);
        assertEquals("Very old birth date should be stored", veryOldDate, employee.getBirthDate());
    }
    
    // ==================== CONTACT INFORMATION TESTS ====================
    
    @Test
    public void testSetAndGetPhoneNumber() {
        employee.setPhoneNumber("09171234567");
        assertEquals("Phone number should match", "09171234567", employee.getPhoneNumber());
    }
    
    @Test
    public void testSetPhoneNumber_InvalidFormat() {
        // NEGATIVE TEST: Invalid phone format (model doesn't validate)
        String invalidPhone = "12345";
        employee.setPhoneNumber(invalidPhone);
        assertEquals("Invalid phone should be stored (no validation in model)", 
                    invalidPhone, employee.getPhoneNumber());
    }
    
    @Test
    public void testSetPhoneNumber_WithSpecialCharacters() {
        String phoneWithChars = "+63-917-123-4567";
        employee.setPhoneNumber(phoneWithChars);
        assertEquals("Phone with special chars should be stored", phoneWithChars, employee.getPhoneNumber());
    }
    
    @Test
    public void testSetAndGetEmail() {
        employee.setEmail("test@example.com");
        assertEquals("Email should match", "test@example.com", employee.getEmail());
    }
    
    @Test
    public void testSetEmail_InvalidFormat() {
        // NEGATIVE TEST: Invalid email format (model doesn't validate)
        String invalidEmail = "not-an-email";
        employee.setEmail(invalidEmail);
        assertEquals("Invalid email should be stored (no validation in model)", 
                    invalidEmail, employee.getEmail());
    }
    
    @Test
    public void testSetEmail_EmptyString() {
        employee.setEmail("");
        assertEquals("Empty email should be stored", "", employee.getEmail());
    }
    
    // ==================== SALARY TESTS ====================
    
    @Test
    public void testSetAndGetBasicSalary() {
        BigDecimal salary = new BigDecimal("45000.50");
        employee.setBasicSalary(salary);
        assertEquals("Basic salary should match", salary, employee.getBasicSalary());
        assertTrue("UpdatedAt should be updated", 
                  employee.getUpdatedAt().isAfter(testStartTime) || 
                  employee.getUpdatedAt().isEqual(testStartTime));
    }
    
    @Test
    public void testSetBasicSalary_Negative() {
        // NEGATIVE TEST: Negative salary
        BigDecimal negativeSalary = new BigDecimal("-5000.00");
        employee.setBasicSalary(negativeSalary);
        assertEquals("Negative salary should be stored (no validation in model)", 
                    negativeSalary, employee.getBasicSalary());
    }
    
    @Test
    public void testSetBasicSalary_Zero() {
        BigDecimal zeroSalary = BigDecimal.ZERO;
        employee.setBasicSalary(zeroSalary);
        assertEquals("Zero salary should be stored", zeroSalary, employee.getBasicSalary());
    }
    
    @Test
    public void testSetBasicSalary_VeryLarge() {
        BigDecimal largeSalary = new BigDecimal("99999999.99");
        employee.setBasicSalary(largeSalary);
        assertEquals("Very large salary should be stored", largeSalary, employee.getBasicSalary());
    }
    
    @Test
    public void testSetBasicSalary_Null() {
        employee.setBasicSalary(null);
        assertNull("Basic salary should be null", employee.getBasicSalary());
    }
    
    @Test
    public void testSetAndGetHourlyRate() {
        BigDecimal rate = new BigDecimal("250.75");
        employee.setHourlyRate(rate);
        assertEquals("Hourly rate should match", rate, employee.getHourlyRate());
        assertTrue("UpdatedAt should be updated", 
                  employee.getUpdatedAt().isAfter(testStartTime) || 
                  employee.getUpdatedAt().isEqual(testStartTime));
    }
    
    @Test
    public void testSetHourlyRate_Negative() {
        // NEGATIVE TEST: Negative hourly rate
        BigDecimal negativeRate = new BigDecimal("-100.00");
        employee.setHourlyRate(negativeRate);
        assertEquals("Negative rate should be stored (no validation in model)", 
                    negativeRate, employee.getHourlyRate());
    }
    
    // ==================== USER ROLE AND PASSWORD TESTS ====================
    
    @Test
    public void testSetAndGetUserRole() {
        employee.setUserRole("Manager");
        assertEquals("User role should be Manager", "Manager", employee.getUserRole());
    }
    
    @Test
    public void testSetUserRole_EmptyString() {
        employee.setUserRole("");
        assertEquals("Empty user role should be stored", "", employee.getUserRole());
    }
    
    @Test
    public void testSetUserRole_VeryLongString() {
        String longRole = "VeryLongUserRoleThatExceedsNormalLengthForTestingPurposes";
        employee.setUserRole(longRole);
        assertEquals("Long user role should be stored", longRole, employee.getUserRole());
    }
    
    @Test
    public void testSetAndGetPasswordHash() {
        String hash = "5f4dcc3b5aa765d61d8327deb882cf99";
        employee.setPasswordHash(hash);
        assertEquals("Password hash should match", hash, employee.getPasswordHash());
        assertTrue("UpdatedAt should be updated for security changes", 
                  employee.getUpdatedAt().isAfter(testStartTime) || 
                  employee.getUpdatedAt().isEqual(testStartTime));
    }
    
    @Test
    public void testSetPasswordHash_Null() {
        employee.setPasswordHash(null);
        assertNull("Password hash should be null", employee.getPasswordHash());
    }
    
    // ==================== EMPLOYEE STATUS TESTS ====================
    
    @Test
    public void testSetAndGetStatus() {
        employee.setStatus(EmployeeStatus.REGULAR);
        assertEquals("Status should be REGULAR", EmployeeStatus.REGULAR, employee.getStatus());
        assertTrue("UpdatedAt should be updated for status changes", 
                  employee.getUpdatedAt().isAfter(testStartTime) || 
                  employee.getUpdatedAt().isEqual(testStartTime));
    }
    
    @Test
    public void testSetStatus_AllValues() {
        for (EmployeeStatus status : EmployeeStatus.values()) {
            employee.setStatus(status);
            assertEquals("Status should be " + status, status, employee.getStatus());
        }
    }
    
    @Test
    public void testEmployeeStatus_FromString() {
        assertEquals(EmployeeStatus.PROBATIONARY, EmployeeStatus.fromString("Probationary"));
        assertEquals(EmployeeStatus.REGULAR, EmployeeStatus.fromString("Regular"));
        assertEquals(EmployeeStatus.TERMINATED, EmployeeStatus.fromString("Terminated"));
    }
    
    @Test
    public void testEmployeeStatus_FromString_CaseInsensitive() {
        assertEquals(EmployeeStatus.PROBATIONARY, EmployeeStatus.fromString("probationary"));
        assertEquals(EmployeeStatus.REGULAR, EmployeeStatus.fromString("REGULAR"));
        assertEquals(EmployeeStatus.TERMINATED, EmployeeStatus.fromString("TeRmInAtEd"));
    }
    
    @Test
    public void testEmployeeStatus_FromString_Null() {
        assertEquals("Null should return default PROBATIONARY", 
                    EmployeeStatus.PROBATIONARY, EmployeeStatus.fromString(null));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmployeeStatus_FromString_Invalid() {
        // NEGATIVE TEST: Invalid status string
        EmployeeStatus.fromString("InvalidStatus");
    }
    
    @Test
    public void testEmployeeStatus_GetValue() {
        assertEquals("Probationary", EmployeeStatus.PROBATIONARY.getValue());
        assertEquals("Regular", EmployeeStatus.REGULAR.getValue());
        assertEquals("Terminated", EmployeeStatus.TERMINATED.getValue());
    }
    
    @Test
    public void testEmployeeStatus_GetAllValues() {
        String[] values = EmployeeStatus.getAllValues();
        assertEquals("Should have 3 status values", 3, values.length);
        assertEquals("Probationary", values[0]);
        assertEquals("Regular", values[1]);
        assertEquals("Terminated", values[2]);
    }
    
    // ==================== TIMESTAMP TESTS ====================
    
    @Test
    public void testSetAndGetCreatedAt() {
        LocalDateTime created = LocalDateTime.of(2020, 1, 1, 0, 0);
        employee.setCreatedAt(created);
        assertEquals("CreatedAt should match", created, employee.getCreatedAt());
    }
    
    @Test
    public void testSetAndGetUpdatedAt() {
        LocalDateTime updated = LocalDateTime.of(2023, 6, 15, 10, 30);
        employee.setUpdatedAt(updated);
        assertEquals("UpdatedAt should match", updated, employee.getUpdatedAt());
    }
    
    @Test
    public void testSetAndGetLastLogin() {
        LocalDateTime lastLogin = LocalDateTime.now();
        employee.setLastLogin(lastLogin);
        assertEquals("LastLogin should match", lastLogin, employee.getLastLogin());
    }
    
    @Test
    public void testUpdateLastLogin() {
        LocalDateTime beforeUpdate = LocalDateTime.now().minusSeconds(1);
        employee.updateLastLogin();
        assertNotNull("LastLogin should not be null", employee.getLastLogin());
        assertTrue("LastLogin should be after beforeUpdate", 
                  employee.getLastLogin().isAfter(beforeUpdate));
    }
    
    // ==================== FOREIGN KEY RELATIONSHIP TESTS ====================
    
    @Test
    public void testSetAndGetPositionId() {
        employee.setPositionId(5);
        assertEquals("Position ID should be 5", Integer.valueOf(5), employee.getPositionId());
    }
    
    @Test
    public void testSetPositionId_Null() {
        employee.setPositionId(null);
        assertNull("Position ID should be null", employee.getPositionId());
    }
    
    @Test
    public void testSetAndGetSupervisorId() {
        employee.setSupervisorId(10);
        assertEquals("Supervisor ID should be 10", Integer.valueOf(10), employee.getSupervisorId());
    }
    
    @Test
    public void testSetSupervisorId_Null() {
        employee.setSupervisorId(null);
        assertNull("Supervisor ID should be null", employee.getSupervisorId());
    }
    
    @Test
    public void testHasSupervisor() {
        assertFalse("Should not have supervisor initially", employee.hasSupervisor());
        employee.setSupervisorId(1);
        assertTrue("Should have supervisor after setting ID", employee.hasSupervisor());
        employee.setSupervisorId(null);
        assertFalse("Should not have supervisor after setting null", employee.hasSupervisor());
    }
    
    // ==================== BUSINESS LOGIC TESTS ====================
    
    @Test
    public void testGetFullName() {
        employee.setFirstName("John");
        employee.setLastName("Doe");
        assertEquals("Full name should be 'John Doe'", "John Doe", employee.getFullName());
    }
    
    @Test
    public void testGetFullName_WithNullValues() {
        employee.setFirstName(null);
        employee.setLastName("Doe");
        assertEquals("Full name with null first name", "null Doe", employee.getFullName());
        
        employee.setFirstName("John");
        employee.setLastName(null);
        assertEquals("Full name with null last name", "John null", employee.getFullName());
    }
    
    @Test
    public void testGetFullName_EmptyStrings() {
        employee.setFirstName("");
        employee.setLastName("");
        assertEquals("Full name with empty strings", " ", employee.getFullName());
    }
    
    @Test
    public void testIsActive() {
        employee.setStatus(EmployeeStatus.PROBATIONARY);
        assertTrue("Probationary employee should be active", employee.isActive());
        
        employee.setStatus(EmployeeStatus.REGULAR);
        assertTrue("Regular employee should be active", employee.isActive());
        
        employee.setStatus(EmployeeStatus.TERMINATED);
        assertFalse("Terminated employee should not be active", employee.isActive());
    }
    
    @Test
    public void testIsRegular() {
        employee.setStatus(EmployeeStatus.PROBATIONARY);
        assertFalse("Probationary employee should not be regular", employee.isRegular());
        
        employee.setStatus(EmployeeStatus.REGULAR);
        assertTrue("Regular employee should be regular", employee.isRegular());
        
        employee.setStatus(EmployeeStatus.TERMINATED);
        assertFalse("Terminated employee should not be regular", employee.isRegular());
    }
    
    @Test
    public void testIsSupervisor() {
        // Note: This always returns false in the current implementation
        assertFalse("isSupervisor should return false (placeholder implementation)", 
                   employee.isSupervisor());
    }
    
    @Test
    public void testGetYearsOfService() {
        // Test with no createdAt
        employee.setCreatedAt(null);
        assertEquals("Years of service should be 0 with null createdAt", 0, employee.getYearsOfService());
        
        // Test with recent creation
        employee.setCreatedAt(LocalDateTime.now());
        assertEquals("Years of service should be 0 for recently created", 0, employee.getYearsOfService());
        
        // Test with 2 years ago
        employee.setCreatedAt(LocalDateTime.now().minusYears(2));
        assertEquals("Years of service should be 2", 2, employee.getYearsOfService());
        
        // Test with 10 years and some months
        employee.setCreatedAt(LocalDateTime.now().minusYears(10).minusMonths(6));
        assertEquals("Years of service should be 10 (months ignored)", 10, employee.getYearsOfService());
    }
    

    
    // ==================== UPDATE TIMESTAMP BEHAVIOR TESTS ====================
    
    @Test
    public void testUpdateTimestamp_OnlyWhenEmployeeIdExists() throws InterruptedException {
        LocalDateTime initialTime = employee.getUpdatedAt();
        
        // Setting fields without employeeId should not update timestamp
        employee.setFirstName("John");
        assertEquals("UpdatedAt should not change without employeeId", 
                    initialTime, employee.getUpdatedAt());
        
        // Set employeeId
        employee.setEmployeeId(100);
        
        // Wait a bit to ensure time difference
        Thread.sleep(10);
        
        // Now setting fields should update timestamp
        employee.setFirstName("Jane");
        assertTrue("UpdatedAt should change with employeeId", 
                  employee.getUpdatedAt().isAfter(initialTime));
    }
    
    // ==================== EQUALS AND HASHCODE TESTS ====================
    
    @Test
    public void testEquals_SameObject() {
        assertTrue("Employee should equal itself", employee.equals(employee));
    }
    
    @Test
    public void testEquals_Null() {
        assertFalse("Employee should not equal null", employee.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        assertFalse("Employee should not equal different class", employee.equals("String"));
    }
    
    @Test
    public void testEquals_SameId() {
        employee.setEmployeeId(123);
        EmployeeModel other = new EmployeeModel();
        other.setEmployeeId(123);
        assertTrue("Employees with same ID should be equal", employee.equals(other));
    }
    
    @Test
    public void testEquals_DifferentId() {
        employee.setEmployeeId(123);
        EmployeeModel other = new EmployeeModel();
        other.setEmployeeId(456);
        assertFalse("Employees with different IDs should not be equal", employee.equals(other));
    }
    
    @Test
    public void testEquals_BothNullId() {
        EmployeeModel other = new EmployeeModel();
        assertFalse("Employees with null IDs should not be equal", employee.equals(other));
    }
    
    @Test
    public void testHashCode_Consistency() {
        employee.setEmployeeId(123);
        int hash1 = employee.hashCode();
        int hash2 = employee.hashCode();
        assertEquals("HashCode should be consistent", hash1, hash2);
    }
    
    @Test
    public void testHashCode_NullId() {
        assertEquals("HashCode should be 0 for null ID", 0, employee.hashCode());
    }
    
    @Test
    public void testHashCode_EqualObjects() {
        employee.setEmployeeId(123);
        EmployeeModel other = new EmployeeModel();
        other.setEmployeeId(123);
        assertEquals("Equal objects should have same hashCode", 
                    employee.hashCode(), other.hashCode());
    }
    
    // ==================== TO STRING TEST ====================
    
    @Test
    public void testToString() {
        employee.setEmployeeId(123);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setUserRole("Employee");
        employee.setStatus(EmployeeStatus.REGULAR);
        employee.setPositionId(5);
        employee.setSupervisorId(10);
        
        String result = employee.toString();
        assertTrue("ToString should contain employeeId", result.contains("employeeId=123"));
        assertTrue("ToString should contain firstName", result.contains("firstName='John'"));
        assertTrue("ToString should contain lastName", result.contains("lastName='Doe'"));
        assertTrue("ToString should contain email", result.contains("email='john.doe@example.com'"));
        assertTrue("ToString should contain status", result.contains("status=REGULAR"));
        assertTrue("ToString should contain active status", result.contains("active=true"));
    }
    
    @Test
    public void testToString_WithNullValues() {
        String result = employee.toString();
        assertTrue("ToString should handle null values", result.contains("employeeId=null"));
        assertTrue("ToString should handle null firstName", result.contains("firstName='null'"));
    }
    
    // ==================== EDGE CASE AND BOUNDARY TESTS ====================
    
    @Test
    public void testBoundaryValues_EmployeeId() {
        // Test maximum integer value
        employee.setEmployeeId(Integer.MAX_VALUE);
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), employee.getEmployeeId());
        
        // Test minimum positive value
        employee.setEmployeeId(1);
        assertEquals(Integer.valueOf(1), employee.getEmployeeId());
        
        // Test zero (might be invalid in real scenario but model allows it)
        employee.setEmployeeId(0);
        assertEquals(Integer.valueOf(0), employee.getEmployeeId());
    }
    
    @Test
    public void testSalaryPrecision() {
        // Test salary with many decimal places
        BigDecimal preciseSalary = new BigDecimal("12345.6789");
        employee.setBasicSalary(preciseSalary);
        assertEquals("Precise salary should be preserved", preciseSalary, employee.getBasicSalary());
        
        // Test very small salary
        BigDecimal smallSalary = new BigDecimal("0.01");
        employee.setBasicSalary(smallSalary);
        assertEquals("Small salary should be preserved", smallSalary, employee.getBasicSalary());
    }
    
    @Test
    public void testConcurrentModification() {
        // Simulate multiple updates to test timestamp consistency
        LocalDateTime firstUpdate = employee.getUpdatedAt();
        employee.setEmployeeId(1); // Enable timestamp updates
        
        employee.setFirstName("First");
        LocalDateTime secondUpdate = employee.getUpdatedAt();
        
        employee.setLastName("Last");
        LocalDateTime thirdUpdate = employee.getUpdatedAt();
        
        assertTrue("Second update should be after or equal to first", 
                  secondUpdate.isAfter(firstUpdate) || secondUpdate.equals(firstUpdate));
        assertTrue("Third update should be after or equal to second", 
                  thirdUpdate.isAfter(secondUpdate) || thirdUpdate.equals(secondUpdate));
    }
    
    @Test
    public void testFieldIndependence() {
        // Test that setting one field doesn't affect others
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john@example.com");
        
        // Change first name
        employee.setFirstName("Jane");
        
        // Others should remain unchanged
        assertEquals("Last name should be unchanged", "Doe", employee.getLastName());
        assertEquals("Email should be unchanged", "john@example.com", employee.getEmail());
    }
    
    @Test
    public void testSpecialCharactersInAllFields() {
        // Test special characters in text fields
        String special = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
        
        employee.setFirstName(special);
        employee.setLastName(special);
        employee.setEmail(special);
        employee.setPhoneNumber(special);
        employee.setUserRole(special);
        employee.setPasswordHash(special);
        
        assertEquals("Special chars in firstName", special, employee.getFirstName());
        assertEquals("Special chars in lastName", special, employee.getLastName());
        assertEquals("Special chars in email", special, employee.getEmail());
        assertEquals("Special chars in phoneNumber", special, employee.getPhoneNumber());
        assertEquals("Special chars in userRole", special, employee.getUserRole());
        assertEquals("Special chars in passwordHash", special, employee.getPasswordHash());
    }
    
    @Test
    public void testUnicodeCharacters() {
        // Test Unicode/international characters
        String unicode = "José María 李明 محمد Владимир";
        
        employee.setFirstName(unicode);
        employee.setLastName(unicode);
        
        assertEquals("Unicode in firstName", unicode, employee.getFirstName());
        assertEquals("Unicode in lastName", unicode, employee.getLastName());
    }
    
    // ==================== ADDITIONAL NEGATIVE TEST SCENARIOS ====================
    
    @Test
    public void testNegativeScenario_InvalidDateCalculations() {
        // Test with future created date (shouldn't happen but testing edge case)
        employee.setCreatedAt(LocalDateTime.now().plusYears(1));
        long yearsOfService = employee.getYearsOfService();
        assertEquals("Future created date should result in negative years (shown as 0)", 
                    -1, yearsOfService);
    }
    
    @Test
    public void testNegativeScenario_SelfSupervision() {
        // Employee supervising themselves (data integrity issue)
        employee.setEmployeeId(100);
        employee.setSupervisorId(100);
        assertEquals("Model allows self-supervision (no validation)", 
                    employee.getEmployeeId(), employee.getSupervisorId());
    }
    
    @Test
    public void testNegativeScenario_MultipleSalaryTypes() {
        // Test conflicting salary information
        employee.setBasicSalary(new BigDecimal("50000"));
        employee.setHourlyRate(new BigDecimal("250"));
        // Model allows both - no validation
        assertNotNull("Basic salary should exist", employee.getBasicSalary());
        assertNotNull("Hourly rate should exist", employee.getHourlyRate());
    }
    
    @Test
    public void testNegativeScenario_InvalidStatusTransition() {
        // Test invalid status transition (e.g., Terminated to Probationary)
        employee.setStatus(EmployeeStatus.TERMINATED);
        employee.setStatus(EmployeeStatus.PROBATIONARY);
        // Model allows any transition - no validation
        assertEquals("Model allows terminated to probationary transition", 
                    EmployeeStatus.PROBATIONARY, employee.getStatus());
    }
    
    // ==================== STRESS TESTS ====================
    
    @Test
    public void testStress_RapidUpdates() {
        // Simulate rapid updates to test timestamp handling
        employee.setEmployeeId(1);
        LocalDateTime start = employee.getUpdatedAt();
        
        for (int i = 0; i < 100; i++) {
            employee.setFirstName("Name" + i);
        }
        
        LocalDateTime end = employee.getUpdatedAt();
        assertTrue("Timestamp should be updated after rapid changes", 
                  end.isAfter(start) || end.equals(start));
    }
    
    @Test
    public void testStress_LargeDataValues() {
        // Test with maximum length strings (simulating database limits)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            sb.append("A");
        }
        String maxString = sb.toString();
        
        employee.setFirstName(maxString);
        employee.setLastName(maxString);
        employee.setEmail(maxString);
        employee.setUserRole(maxString);
        
        assertEquals("Should handle 255 character firstName", 255, employee.getFirstName().length());
        assertEquals("Should handle 255 character lastName", 255, employee.getLastName().length());
    }
    
    // ==================== SECURITY-RELATED TESTS ====================
    
    @Test
    public void testSecurity_PasswordHashNotPlaintext() {
        // Ensure password is meant to be hashed, not plaintext
        String plainPassword = "myPassword123";
        employee.setPasswordHash(plainPassword);
        
        // Model doesn't validate, but this documents expected usage
        assertEquals("Model stores whatever is provided as passwordHash", 
                    plainPassword, employee.getPasswordHash());
    }
    
    @Test
    public void testSecurity_SQLInjectionInFields() {
        // Test SQL injection attempts in fields
        String sqlInjection = "'; DROP TABLE employee; --";
        
        employee.setFirstName(sqlInjection);
        employee.setLastName(sqlInjection);
        employee.setEmail(sqlInjection);
        
        // Model should store these as regular strings
        assertEquals("SQL injection string should be stored as-is", 
                    sqlInjection, employee.getFirstName());
    }
    
    // ==================== REALISTIC BUSINESS SCENARIOS ====================
    
    @Test
    public void testBusinessScenario_NewHireWorkflow() {
        // Simulate new hire process
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        EmployeeModel newHire = new EmployeeModel("John", "Doe", birthDate,
                                                 "john.doe@company.com", "hashed_pwd", 1);
        
        // Verify initial state
        assertNull("New hire should not have ID yet", newHire.getEmployeeId());
        assertEquals("Default status should be PROBATIONARY", 
                    EmployeeStatus.PROBATIONARY, newHire.getStatus());
        assertEquals("Default role should be Employee", "Employee", newHire.getUserRole());
        assertNull("Should not have supervisor initially", newHire.getSupervisorId());
        assertNull("Should not have last login", newHire.getLastLogin());
        
        // Simulate after database save
        newHire.setEmployeeId(10001);
        newHire.setBasicSalary(new BigDecimal("35000"));
        newHire.setHourlyRate(new BigDecimal("201.92"));
        newHire.setSupervisorId(5);
        
        assertTrue("Should have supervisor after assignment", newHire.hasSupervisor());
        assertFalse("New hire cannot be promoted immediately", newHire.canBePromoted());
    }
    

    
    @Test
    public void testBusinessScenario_EmployeeTermination() {
        // Simulate employee termination
        employee.setEmployeeId(200);
        employee.setStatus(EmployeeStatus.REGULAR);
        employee.setCreatedAt(LocalDateTime.now().minusYears(5));
        
        // Terminate employee
        employee.setStatus(EmployeeStatus.TERMINATED);
        employee.updateLastLogin(); // Might login for final access
        
        assertFalse("Terminated employee should not be active", employee.isActive());
        assertFalse("Terminated employee should not be regular", employee.isRegular());
        assertFalse("Terminated employee cannot be promoted", employee.canBePromoted());
        assertEquals("Should still calculate years of service", 5, employee.getYearsOfService());
    }
    

    
    // ==================== DATA VALIDATION BOUNDARY TESTS ====================
    
    @Test
    public void testValidation_EmailEdgeCases() {
        // Test various email formats (model doesn't validate, but documenting)
        String[] emails = {
            "a@b.c",                          // Minimal valid email
            "test.email+tag@example.com",     // Valid with special chars
            "test@subdomain.example.com",     // Subdomain
            "test@127.0.0.1",                // IP address
            "@example.com",                   // Invalid: no local part
            "test@",                          // Invalid: no domain
            "test..email@example.com",        // Invalid: consecutive dots
            "",                               // Empty
            null                              // Null
        };
        
        for (String email : emails) {
            employee.setEmail(email);
            assertEquals("Model should store any email value", email, employee.getEmail());
        }
    }
    
    @Test
    public void testValidation_PhoneNumberFormats() {
        // Test various phone formats (Philippine and international)
        String[] phones = {
            "09171234567",           // Standard PH mobile
            "+639171234567",         // International format
            "02-1234-5678",         // Landline with dashes
            "(02) 1234-5678",       // Landline with parentheses
            "1234567",              // Too short
            "091712345678901234",   // Too long
            "abcdefghijk",          // Letters
            "",                     // Empty
            null                    // Null
        };
        
        for (String phone : phones) {
            employee.setPhoneNumber(phone);
            assertEquals("Model should store any phone value", phone, employee.getPhoneNumber());
        }
    }
    
    @Test
    public void testValidation_BirthDateEdgeCases() {
        // Test birth date edge cases
        LocalDate today = LocalDate.now();
        
        // Future date (invalid in real world)
        employee.setBirthDate(today.plusDays(1));
        assertEquals("Model allows future birth date", today.plusDays(1), employee.getBirthDate());
        
        // Today (newborn - edge case)
        employee.setBirthDate(today);
        assertEquals("Model allows today as birth date", today, employee.getBirthDate());
        
        // Very old (150 years ago)
        LocalDate veryOld = today.minusYears(150);
        employee.setBirthDate(veryOld);
        assertEquals("Model allows very old birth date", veryOld, employee.getBirthDate());
        
        // Minimum LocalDate
        LocalDate minDate = LocalDate.MIN;
        employee.setBirthDate(minDate);
        assertEquals("Model allows minimum date", minDate, employee.getBirthDate());
        
        // Maximum LocalDate
        LocalDate maxDate = LocalDate.MAX;
        employee.setBirthDate(maxDate);
        assertEquals("Model allows maximum date", maxDate, employee.getBirthDate());
    }
    
    // ==================== THREAD SAFETY TESTS ====================
    
    @Test
    public void testThreadSafety_NoSynchronization() {
        // Note: EmployeeModel is NOT thread-safe
        // This test documents that concurrent access could cause issues
        
        employee.setEmployeeId(1);
        final String[] results = new String[2];
        
        Thread t1 = new Thread(() -> {
            employee.setFirstName("Thread1");
            results[0] = employee.getFirstName();
        });
        
        Thread t2 = new Thread(() -> {
            employee.setFirstName("Thread2");
            results[1] = employee.getFirstName();
        });
        
        t1.start();
        t2.start();
        
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            fail("Thread interrupted");
        }
        
        // Both threads might see different values
        // This documents that the model is not thread-safe
        assertNotNull("Thread 1 result should not be null", results[0]);
        assertNotNull("Thread 2 result should not be null", results[1]);
    }
    
    // ==================== MEMORY AND PERFORMANCE TESTS ====================
    
    @Test
    public void testMemory_LargeStringStorage() {
        // Test memory usage with large strings
        int size = 10000;
        StringBuilder largeString = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            largeString.append("X");
        }
        
        String large = largeString.toString();
        employee.setFirstName(large);
        employee.setLastName(large);
        employee.setEmail(large);
        
        assertEquals("Should store large firstName", size, employee.getFirstName().length());
        assertEquals("Should store large lastName", size, employee.getLastName().length());
        assertEquals("Should store large email", size, employee.getEmail().length());
    }
    
    @Test
    public void testPerformance_GetterSetterSpeed() {
        // Test performance of getters/setters
        long startTime = System.nanoTime();
        
        for (int i = 0; i < 10000; i++) {
            employee.setFirstName("Name" + i);
            String name = employee.getFirstName();
        }
        
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000; // Convert to milliseconds
        
        assertTrue("Getter/setter operations should be fast (< 100ms for 10000 ops)", 
                  duration < 100);
    }
    
    // ==================== INTEGRATION PREPARATION TESTS ====================
    
    @Test
    public void testDatabaseCompatibility_AllFieldsSet() {
        // Ensure all fields can be set for database operations
        LocalDate birthDate = LocalDate.of(1985, 6, 15);
        LocalDateTime now = LocalDateTime.now();
        
        employee.setEmployeeId(1000);
        employee.setFirstName("Database");
        employee.setLastName("Test");
        employee.setBirthDate(birthDate);
        employee.setPhoneNumber("09171234567");
        employee.setEmail("db.test@example.com");
        employee.setBasicSalary(new BigDecimal("45000.50"));
        employee.setHourlyRate(new BigDecimal("259.62"));
        employee.setUserRole("Tester");
        employee.setPasswordHash("hashedpassword123");
        employee.setStatus(EmployeeStatus.REGULAR);
        employee.setCreatedAt(now.minusDays(30));
        employee.setUpdatedAt(now.minusDays(1));
        employee.setLastLogin(now);
        employee.setPositionId(5);
        employee.setSupervisorId(10);
        
        // Verify all fields are set
        assertNotNull("All fields should be non-null for database", employee.getEmployeeId());
        assertNotNull("All fields should be non-null for database", employee.getFirstName());
        assertNotNull("All fields should be non-null for database", employee.getLastName());
        assertNotNull("All fields should be non-null for database", employee.getBirthDate());
        assertNotNull("All fields should be non-null for database", employee.getPhoneNumber());
        assertNotNull("All fields should be non-null for database", employee.getEmail());
        assertNotNull("All fields should be non-null for database", employee.getBasicSalary());
        assertNotNull("All fields should be non-null for database", employee.getHourlyRate());
        assertNotNull("All fields should be non-null for database", employee.getUserRole());
        assertNotNull("All fields should be non-null for database", employee.getPasswordHash());
        assertNotNull("All fields should be non-null for database", employee.getStatus());
        assertNotNull("All fields should be non-null for database", employee.getCreatedAt());
        assertNotNull("All fields should be non-null for database", employee.getUpdatedAt());
        assertNotNull("All fields should be non-null for database", employee.getLastLogin());
        assertNotNull("All fields should be non-null for database", employee.getPositionId());
        assertNotNull("All fields should be non-null for database", employee.getSupervisorId());
    }
}