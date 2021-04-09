package System;


import Database.*;
import Roles.*;
import java.awt.Color;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author enesguler
 */
public class SystemClass extends javax.swing.JFrame {
    DB connect;
    int loggedId;
    
    
    /**
     * Creates new form MainFrame
     */
    public SystemClass() {
        initComponents();
        showLoginPanel();
        connect = new DB();
        Chief.getNumOfChief();
        Employee.getNumOfEmployee();
        ExecutiveOfficer.getNumOfExecutiveOfficer();
        JobPosting.getNumberOfPosts();
        
        loginErrorLabel.setVisible(false);
        cvErrorLabel.setVisible(false);
        jobApplyErrorLabel.setVisible(false);
        jobAdvertErrorLabel.setVisible(false);
        employErrorLabel.setVisible(false);
        myJobAdvertsTableErrorLabel.setVisible(false);
        showChosenImageBtn.setVisible(false);
    }
   
    
    
    public void showEmployerPanel(){
        try{
            logInSignInPanel.setVisible(false);
            EmployeePanel.setVisible(false);
            EmployerPanel.setVisible(true);
            fillMyJobPostingsTable(connect.getAllMyJobPostings(this.loggedId));
            fillAllCvsTable(connect.getAllCvs());
        }catch(Exception ex){
            System.out.println("err msg MainFrame.showEmployerPanel: "+ ex);
            showEmployerPanel();
        }
    }
    public void showEmployeePanel(){
        logInSignInPanel.setVisible(false);
        EmployeePanel.setVisible(true);
        EmployerPanel.setVisible(false);
        try{
            fillJobPostingsTable(connect.getAllJobPostings());

            ArrayList<String> Dcities = connect.getDistinctDatasOfComboBox("city", "jobpostings");
            cityComboBoxFilterEmployee.removeAllItems();
            cityComboBoxFilterEmployee.addItem("All");
            for(String i: Dcities)
                cityComboBoxFilterEmployee.addItem(i);
            ArrayList<String> Dcompanies = connect.getDistinctDatasOfComboBox("companyname", "jobpostings");
            companyComboBoxFilterEmployee.removeAllItems();
            companyComboBoxFilterEmployee.addItem("All");
            for(String i: Dcompanies)
                companyComboBoxFilterEmployee.addItem(i);
            jobPostingsTable.setAutoCreateRowSorter(true);
            if (this.loggedId>300) passToEmployerPanelBtn.setVisible(false);
            else passToEmployerPanelBtn.setVisible(true);
        }catch(Exception ex){
            System.out.println("err msg MainFrame.showEmployeePanel: "+ ex);
            showEmployeePanel();
        }
    }
    public void showLoginPanel(){
        try{
            logInSignInPanel.setVisible(true);
            EmployeePanel.setVisible(false);
            EmployerPanel.setVisible(false);
            registerErrorLabel.setVisible(false);
            usernameLoginTF.setText("");
            passwdLoginTF.setText("");
        }catch(Exception ex){
            System.out.println("err msg MainFrame.showLoginPanel: "+ ex);
            showLoginPanel();
        }
        
    }
    
    public void fillJobPostingsTable(ArrayList<JobPosting> posts){
        DefaultTableModel tModel = (DefaultTableModel)jobPostingsTable.getModel();
        tModel.setRowCount(0);
        for(JobPosting p: posts){
            Object[] o = {p.getId(), p.getCompanyName(), p.getDepartment(),
                                p.getJobName(), p.getCity(), p.getSalary()};
            tModel.addRow(o);
        }
    }
    
    public void fillMyJobPostingsTable(ArrayList<JobPosting> posts){
        DefaultTableModel model = (DefaultTableModel)myJobPostingsTable.getModel();
        model.setRowCount(0);
        for(JobPosting p: posts){
            Object[] o = {p.getId(), p.getCompanyName(), p.getDepartment(),
                                p.getJobName(), p.getCity(), p.getSalary()};
            model.addRow(o);
        }
    }
    
    public void fillAllCvsTable(ArrayList<Person> persons){
        DefaultTableModel model = (DefaultTableModel)cvstable.getModel();
        model.setRowCount(0);
        for(Person p: persons){
            int flNumber = p.getForeignLanguages().equals("")?0:p.getForeignLanguages().split(",").length;
            Object[] o = {p.getId(), p.getName(), p.getSurname(),
                                p.getOld(), p.getUniGPA(), flNumber};
            model.addRow(o);
        }
    }
    
    public void fillJobSummaryPanel(JobPosting jp){
        companyInfoLabelJS.setText("<html>" + ": " + jp.getCompanyName()+ "</html>");
        departmentInfoLabelJS.setText("<html>" + ": " + jp.getDepartment()+ "</html>");
        jobNameInfoLabelJS.setText("<html>" + ": " + jp.getJobName()+ "</html>");
        cityInfoLabelJS.setText(": " + jp.getCity());
        salaryInfoLabelJS.setText(": " + Integer.toString(jp.getSalary()));
        requirementsInfoLabelJS.setText("<html>" + ": " + jp.getRequirements()+ "</html>");
        definitionInfoLabelJS.setText("<html>" + ": " + jp.getDefinition() + "</html>");
        
    }
    
    public void fillCvSummaryPanel(Person p){
        nameSurnameEILabel.setText("<html>" + ": " + p.getName() + " " + p.getSurname()+ "</html>");
        emailEILabel.setText("<html>" + ": " + p.getEmail() + "</html>");
        birthdateEILabel.setText("<html>" + ": " + p.getBirthdate()+ "</html>");
        graduatedUniEILabel.setText("<html>" + ": " + p.getGraduatedUni()+ "</html>");
        gpaEILabel.setText("<html>" + ": " + p.getUniGPA()+ "</html>");
        preferedJobEILabel.setText("<html>" + ": " + p.getJob()+ "</html>");
        currentJobEILabel.setText("<html>" + ": " + p.getCurrentJob()+ "</html>");
        nationalityEILabel.setText("<html>" + ": " + p.getNationality()+ "</html>");
        maritalStatusEILabel.setText("<html>" + ": " + p.getMaritalStatus()+ "</html>");
        militaryStatusEILabel.setText("<html>" + ": " + p.getMilitaryStatus()+ "</html>");
        genderEILabel.setText("<html>" + ": " + p.getGender()+ "</html>");
        foreignLanguagesEILabel.setText("<html>" + ": " + p.getForeignLanguages()+ "</html>");
        skillsEILabel.setText("<html>" + ": " + p.getSkills()+ "</html>");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroupLogInJobRole = new javax.swing.ButtonGroup();
        buttonGroupNationality = new javax.swing.ButtonGroup();
        buttonGroupGender = new javax.swing.ButtonGroup();
        mainLayeredPane = new javax.swing.JLayeredPane();
        EmployerPanel = new javax.swing.JPanel();
        advertPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jobNameTF = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cityTF = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        companyTF = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        salaryTF = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        definitionTA = new javax.swing.JTextArea();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        requirementsTA = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        departmentTF = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jobRoleCBJobAdvert = new javax.swing.JComboBox<>();
        clearJobAdvertBtn = new javax.swing.JButton();
        createJobAdvertBtn = new javax.swing.JButton();
        updateJobAdvertBtn2 = new javax.swing.JButton();
        jobAdvertErrorLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        myJobPostingsTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        cvstable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        emailEILabel = new javax.swing.JLabel();
        nameSurnameEILabel = new javax.swing.JLabel();
        birthdateEILabel = new javax.swing.JLabel();
        graduatedUniEILabel = new javax.swing.JLabel();
        gpaEILabel = new javax.swing.JLabel();
        preferedJobEILabel = new javax.swing.JLabel();
        currentJobEILabel = new javax.swing.JLabel();
        nationalityEILabel = new javax.swing.JLabel();
        maritalStatusEILabel = new javax.swing.JLabel();
        militaryStatusEILabel = new javax.swing.JLabel();
        genderEILabel = new javax.swing.JLabel();
        foreignLanguagesEILabel = new javax.swing.JLabel();
        skillsEILabel = new javax.swing.JLabel();
        employBtn = new javax.swing.JButton();
        employErrorLabel = new javax.swing.JLabel();
        showCVImageBtn = new javax.swing.JButton();
        deleteJobAdvertBtn = new javax.swing.JButton();
        updateJobAdvertBtn = new javax.swing.JButton();
        logOutBtnEmployerPanel = new javax.swing.JButton();
        passToEmployeePanelBtn = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        showAllCvsBtn = new javax.swing.JButton();
        refreshMyJobAdvertsTableBtn = new javax.swing.JButton();
        myJobAdvertsTableErrorLabel = new javax.swing.JLabel();
        EmployeePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jobPostingsTable = new javax.swing.JTable();
        FilterTablePanelEmployee = new javax.swing.JPanel();
        jobRoleComboBoxFilterEmployee = new javax.swing.JComboBox<>();
        jobRoleLabelFilterEmployee = new javax.swing.JLabel();
        cityLabelFilterEmployee = new javax.swing.JLabel();
        cityComboBoxFilterEmployee = new javax.swing.JComboBox<>();
        oldLabelFilterEmployee = new javax.swing.JLabel();
        companyNameLabelFilterEmployee = new javax.swing.JLabel();
        companyComboBoxFilterEmployee = new javax.swing.JComboBox<>();
        salaryLabelFilterEmployee = new javax.swing.JLabel();
        getFilterBtnEmployee = new javax.swing.JButton();
        minSalaryTFFilterEmployee = new javax.swing.JTextField();
        maxSalaryTFFilterEmployee = new javax.swing.JTextField();
        logoutBtnEmployee = new javax.swing.JButton();
        jobSumPanel = new javax.swing.JPanel();
        applyToJobBtn = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        departmentInfoLabelJS = new javax.swing.JLabel();
        cityInfoLabelJS = new javax.swing.JLabel();
        jobNameInfoLabelJS = new javax.swing.JLabel();
        requirementsInfoLabelJS = new javax.swing.JLabel();
        definitionInfoLabelJS = new javax.swing.JLabel();
        companyInfoLabelJS = new javax.swing.JLabel();
        salaryInfoLabelJS = new javax.swing.JLabel();
        jobApplyErrorLabel = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        graduatedUniTFCV = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        preferedJobTFCV = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        birthdateChooserCV = new com.toedter.calendar.JDateChooser();
        femaleRb = new javax.swing.JRadioButton();
        maleRb = new javax.swing.JRadioButton();
        jLabel9 = new javax.swing.JLabel();
        TCnationalityRB = new javax.swing.JRadioButton();
        othernationalityRB = new javax.swing.JRadioButton();
        gpaSpinnerCv = new javax.swing.JSpinner();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        takePhotoBtn = new javax.swing.JButton();
        choosePhotoBtn = new javax.swing.JButton();
        showChosenImageBtn = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        languagePanel = new javax.swing.JPanel();
        lng1 = new javax.swing.JTextField();
        lngLevel1 = new javax.swing.JComboBox<>();
        lng2 = new javax.swing.JTextField();
        lngLevel2 = new javax.swing.JComboBox<>();
        lng3 = new javax.swing.JTextField();
        lngLevel3 = new javax.swing.JComboBox<>();
        skillPanel = new javax.swing.JPanel();
        skilll1 = new javax.swing.JTextField();
        skillLevel1 = new javax.swing.JComboBox<>();
        skilll2 = new javax.swing.JTextField();
        skillLevel2 = new javax.swing.JComboBox<>();
        skilll3 = new javax.swing.JTextField();
        skillLevel3 = new javax.swing.JComboBox<>();
        skilll4 = new javax.swing.JTextField();
        skillLevel4 = new javax.swing.JComboBox<>();
        saveCvBtnEmployee = new javax.swing.JButton();
        maritalCBCV = new javax.swing.JComboBox<>();
        militaryCBCV = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        updateCvBtn = new javax.swing.JButton();
        cvErrorLabel = new javax.swing.JLabel();
        passToEmployerPanelBtn = new javax.swing.JButton();
        logInSignInPanel = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        confirmPasswdErrorMsgLabelRegister = new javax.swing.JLabel();
        emailErrorMsgLabelRegister = new javax.swing.JLabel();
        lastNameLabelRegister = new javax.swing.JLabel();
        firstNameLabelRegister = new javax.swing.JLabel();
        usernameLoginTF = new javax.swing.JTextField();
        UsernameLoginLabel = new javax.swing.JLabel();
        passwdLoginLabel = new javax.swing.JLabel();
        LoginBtn = new javax.swing.JButton();
        registerErrorLabel = new javax.swing.JLabel();
        passwdLoginTF = new javax.swing.JPasswordField();
        nameTFRegister = new javax.swing.JTextField();
        surnameTFRegister = new javax.swing.JTextField();
        emailLabelRegister = new javax.swing.JLabel();
        emailTFRegister = new javax.swing.JTextField();
        usernameLabelRegister = new javax.swing.JLabel();
        usernameTFRegister = new javax.swing.JTextField();
        passwdLabelRegister = new javax.swing.JLabel();
        passwdTFRegister = new javax.swing.JPasswordField();
        confirmPasswdLabelRegister = new javax.swing.JLabel();
        confirmPasswdTFRegister = new javax.swing.JPasswordField();
        RegisterBtn = new javax.swing.JButton();
        roleLabelRegister = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        ChiefRegister = new javax.swing.JRadioButton();
        ExecutiveOfficerRegister = new javax.swing.JRadioButton();
        EmployeeRegister = new javax.swing.JRadioButton();
        cnflab = new javax.swing.JLabel();
        loginErrorLabel = new javax.swing.JLabel();
        ImageLable = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainLayeredPane.setPreferredSize(new java.awt.Dimension(1100, 690));

        EmployerPanel.setBackground(new java.awt.Color(51, 110, 123));
        EmployerPanel.setPreferredSize(new java.awt.Dimension(1100, 690));

        advertPanel.setBackground(new java.awt.Color(189, 195, 199));
        advertPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Job Advert", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel4.setText("Job Name :");

        jLabel14.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel14.setText("City:");

        jLabel15.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel15.setText("Company:");

        jLabel16.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel16.setText("Salary:");

        definitionTA.setColumns(18);
        definitionTA.setRows(3);
        jScrollPane3.setViewportView(definitionTA);

        jLabel17.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel17.setText("Definition:");

        jLabel18.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel18.setText("Requirements:");

        requirementsTA.setColumns(18);
        requirementsTA.setRows(3);
        jScrollPane4.setViewportView(requirementsTA);

        jLabel1.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel1.setText("Department:");

        jLabel19.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel19.setText("Job Role:");

        jobRoleCBJobAdvert.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jobRoleCBJobAdvert.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "Chief", "Executive Officer", "Employee" }));

        clearJobAdvertBtn.setBackground(new java.awt.Color(0, 0, 0));
        clearJobAdvertBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        clearJobAdvertBtn.setForeground(new java.awt.Color(255, 255, 255));
        clearJobAdvertBtn.setText("Clear");
        clearJobAdvertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearJobAdvertBtnActionPerformed(evt);
            }
        });

        createJobAdvertBtn.setBackground(new java.awt.Color(0, 0, 0));
        createJobAdvertBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        createJobAdvertBtn.setForeground(new java.awt.Color(255, 255, 255));
        createJobAdvertBtn.setText("Create");
        createJobAdvertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createJobAdvertBtnActionPerformed(evt);
            }
        });

        updateJobAdvertBtn2.setBackground(new java.awt.Color(0, 0, 0));
        updateJobAdvertBtn2.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        updateJobAdvertBtn2.setForeground(new java.awt.Color(255, 255, 255));
        updateJobAdvertBtn2.setText("Update");
        updateJobAdvertBtn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateJobAdvertBtn2ActionPerformed(evt);
            }
        });

        jobAdvertErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jobAdvertErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        jobAdvertErrorLabel.setText("* Some inputs are empty");

        javax.swing.GroupLayout advertPanelLayout = new javax.swing.GroupLayout(advertPanel);
        advertPanel.setLayout(advertPanelLayout);
        advertPanelLayout.setHorizontalGroup(
            advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(advertPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel1)
                    .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(salaryTF, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                        .addComponent(departmentTF, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(companyTF, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cityTF, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jobNameTF, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jobAdvertErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18)
                    .addComponent(jobRoleCBJobAdvert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, advertPanelLayout.createSequentialGroup()
                        .addComponent(updateJobAdvertBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(clearJobAdvertBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(createJobAdvertBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        advertPanelLayout.setVerticalGroup(
            advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(advertPanelLayout.createSequentialGroup()
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(advertPanelLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, advertPanelLayout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(advertPanelLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jobNameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cityTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(companyTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(departmentTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(advertPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(salaryTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jobRoleCBJobAdvert, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(advertPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(clearJobAdvertBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(createJobAdvertBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updateJobAdvertBtn2)
                    .addComponent(jobAdvertErrorLabel))
                .addGap(5, 5, 5))
        );

        myJobPostingsTable.setAutoCreateRowSorter(true);
        myJobPostingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "Company", "Department", "Job Name", "City", "Salary"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        myJobPostingsTable.getTableHeader().setReorderingAllowed(false);
        myJobPostingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myJobPostingsTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(myJobPostingsTable);
        if (myJobPostingsTable.getColumnModel().getColumnCount() > 0) {
            myJobPostingsTable.getColumnModel().getColumn(0).setPreferredWidth(1);
            myJobPostingsTable.getColumnModel().getColumn(5).setPreferredWidth(1);
        }

        cvstable.setAutoCreateRowSorter(true);
        cvstable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "Name", "Surname", "Old", "GPA", "Foreign Languages"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        cvstable.getTableHeader().setReorderingAllowed(false);
        cvstable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cvstableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(cvstable);
        if (cvstable.getColumnModel().getColumnCount() > 0) {
            cvstable.getColumnModel().getColumn(0).setPreferredWidth(1);
            cvstable.getColumnModel().getColumn(3).setPreferredWidth(1);
            cvstable.getColumnModel().getColumn(4).setPreferredWidth(1);
            cvstable.getColumnModel().getColumn(5).setPreferredWidth(1);
        }

        jPanel4.setBackground(new java.awt.Color(189, 195, 199));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Information", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        jLabel22.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel22.setText("Name Surname");

        jLabel23.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel23.setText("Email");

        jLabel30.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel30.setText("Birth Date");

        jLabel31.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel31.setText("Graduated University");

        jLabel32.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel32.setText("Prefered Job");

        jLabel33.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel33.setText("GPA");

        jLabel34.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel34.setText("Foreign Languages");

        jLabel35.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel35.setText("Nationality");

        jLabel36.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel36.setText("Current Job");

        jLabel37.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel37.setText("Marital Status");

        jLabel38.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel38.setText("Military Status");

        jLabel39.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel39.setText("Gender");

        jLabel40.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        jLabel40.setText("Skills");

        emailEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        emailEILabel.setText(":");

        nameSurnameEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        nameSurnameEILabel.setText(":");

        birthdateEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        birthdateEILabel.setText(":");

        graduatedUniEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        graduatedUniEILabel.setText(":");

        gpaEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        gpaEILabel.setText(":");

        preferedJobEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        preferedJobEILabel.setText(":");

        currentJobEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        currentJobEILabel.setText(":");

        nationalityEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        nationalityEILabel.setText(":");

        maritalStatusEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        maritalStatusEILabel.setText(":");

        militaryStatusEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        militaryStatusEILabel.setText(":");

        genderEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        genderEILabel.setText(":");

        foreignLanguagesEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        foreignLanguagesEILabel.setText(":");

        skillsEILabel.setFont(new java.awt.Font("Sitka Text", 0, 12)); // NOI18N
        skillsEILabel.setText(":");
        skillsEILabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        skillsEILabel.setAutoscrolls(true);

        employBtn.setBackground(new java.awt.Color(0, 0, 0));
        employBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        employBtn.setForeground(new java.awt.Color(255, 255, 255));
        employBtn.setText("Employ");
        employBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employBtnActionPerformed(evt);
            }
        });

        employErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        employErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        employErrorLabel.setText("<html><center>* You must select a job from table</center></html>");

        showCVImageBtn.setBackground(new java.awt.Color(0, 0, 0));
        showCVImageBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        showCVImageBtn.setForeground(new java.awt.Color(255, 255, 255));
        showCVImageBtn.setText("CV Img");
        showCVImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCVImageBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel30, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel32, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(showCVImageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(emailEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nameSurnameEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(birthdateEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(graduatedUniEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(gpaEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(preferedJobEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(currentJobEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(nationalityEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(maritalStatusEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(militaryStatusEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(genderEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(foreignLanguagesEILabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(skillsEILabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(employBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(employErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(nameSurnameEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(emailEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(birthdateEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(graduatedUniEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(gpaEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(preferedJobEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(currentJobEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(nationalityEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(maritalStatusEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(militaryStatusEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel39)
                    .addComponent(genderEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(foreignLanguagesEILabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(skillsEILabel, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel40)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
                        .addComponent(showCVImageBtn)))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(employBtn)
                    .addComponent(employErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        deleteJobAdvertBtn.setBackground(new java.awt.Color(0, 0, 0));
        deleteJobAdvertBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        deleteJobAdvertBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteJobAdvertBtn.setText("Delete");
        deleteJobAdvertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteJobAdvertBtnActionPerformed(evt);
            }
        });

        updateJobAdvertBtn.setBackground(new java.awt.Color(0, 0, 0));
        updateJobAdvertBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        updateJobAdvertBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateJobAdvertBtn.setText("Update");
        updateJobAdvertBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateJobAdvertBtnActionPerformed(evt);
            }
        });

        logOutBtnEmployerPanel.setBackground(new java.awt.Color(0, 0, 0));
        logOutBtnEmployerPanel.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        logOutBtnEmployerPanel.setForeground(new java.awt.Color(255, 255, 255));
        logOutBtnEmployerPanel.setText("Log out");
        logOutBtnEmployerPanel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logOutBtnEmployerPanelActionPerformed(evt);
            }
        });

        passToEmployeePanelBtn.setBackground(new java.awt.Color(0, 0, 0));
        passToEmployeePanelBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        passToEmployeePanelBtn.setForeground(new java.awt.Color(255, 255, 255));
        passToEmployeePanelBtn.setText("<html><center>Pass to<br>Employee<br>Panel</center></html>");
        passToEmployeePanelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passToEmployeePanelBtnActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("My Job Adverts:");

        jLabel21.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Resumes:");

        showAllCvsBtn.setBackground(new java.awt.Color(0, 0, 0));
        showAllCvsBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        showAllCvsBtn.setForeground(new java.awt.Color(255, 255, 255));
        showAllCvsBtn.setText("Show All");
        showAllCvsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showAllCvsBtnActionPerformed(evt);
            }
        });

        refreshMyJobAdvertsTableBtn.setBackground(new java.awt.Color(0, 0, 0));
        refreshMyJobAdvertsTableBtn.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        refreshMyJobAdvertsTableBtn.setForeground(new java.awt.Color(255, 255, 255));
        refreshMyJobAdvertsTableBtn.setText("Refresh");
        refreshMyJobAdvertsTableBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshMyJobAdvertsTableBtnActionPerformed(evt);
            }
        });

        myJobAdvertsTableErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 12)); // NOI18N
        myJobAdvertsTableErrorLabel.setForeground(new java.awt.Color(255, 0, 51));
        myJobAdvertsTableErrorLabel.setText("* You must select a job from table");

        javax.swing.GroupLayout EmployerPanelLayout = new javax.swing.GroupLayout(EmployerPanel);
        EmployerPanel.setLayout(EmployerPanelLayout);
        EmployerPanelLayout.setHorizontalGroup(
            EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmployerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmployerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addContainerGap(1023, Short.MAX_VALUE))
                    .addGroup(EmployerPanelLayout.createSequentialGroup()
                        .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 980, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(EmployerPanelLayout.createSequentialGroup()
                                    .addGap(0, 0, Short.MAX_VALUE)
                                    .addComponent(myJobAdvertsTableErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(EmployerPanelLayout.createSequentialGroup()
                                    .addComponent(advertPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(11, 11, 11)
                                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel10))
                        .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(EmployerPanelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(logOutBtnEmployerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                                    .addComponent(passToEmployeePanelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(5, 5, 5))
                            .addGroup(EmployerPanelLayout.createSequentialGroup()
                                .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(EmployerPanelLayout.createSequentialGroup()
                                        .addGap(5, 5, 5)
                                        .addComponent(showAllCvsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(EmployerPanelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(refreshMyJobAdvertsTableBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(updateJobAdvertBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(deleteJobAdvertBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap())))))
        );
        EmployerPanelLayout.setVerticalGroup(
            EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EmployerPanelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EmployerPanelLayout.createSequentialGroup()
                        .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(EmployerPanelLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(logOutBtnEmployerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(passToEmployeePanelBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(advertPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(myJobAdvertsTableErrorLabel))
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EmployerPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(deleteJobAdvertBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateJobAdvertBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshMyJobAdvertsTableBtn)))
                .addGap(0, 0, 0)
                .addComponent(jLabel21)
                .addGap(0, 0, 0)
                .addGroup(EmployerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showAllCvsBtn)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        EmployeePanel.setBackground(new java.awt.Color(51, 110, 123));
        EmployeePanel.setMaximumSize(new java.awt.Dimension(920, 575));
        EmployeePanel.setPreferredSize(new java.awt.Dimension(1100, 690));
        EmployeePanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jobPostingsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Company Name", "Department", "Job Name", "City", "Salary"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jobPostingsTable.getTableHeader().setReorderingAllowed(false);
        jobPostingsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jobPostingsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jobPostingsTable);
        jobPostingsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jobPostingsTable.getColumnModel().getColumnCount() > 0) {
            jobPostingsTable.getColumnModel().getColumn(0).setMinWidth(1);
            jobPostingsTable.getColumnModel().getColumn(0).setPreferredWidth(1);
            jobPostingsTable.getColumnModel().getColumn(5).setMinWidth(1);
            jobPostingsTable.getColumnModel().getColumn(5).setPreferredWidth(2);
        }

        EmployeePanel.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 500, 1060, 139));

        FilterTablePanelEmployee.setBackground(new java.awt.Color(51, 110, 123));

        jobRoleComboBoxFilterEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Chief", "Executive Officer", "Employee" }));

        jobRoleLabelFilterEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jobRoleLabelFilterEmployee.setForeground(new java.awt.Color(255, 255, 255));
        jobRoleLabelFilterEmployee.setText("Job Role:");

        cityLabelFilterEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        cityLabelFilterEmployee.setForeground(new java.awt.Color(255, 255, 255));
        cityLabelFilterEmployee.setText("City: ");

        cityComboBoxFilterEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));

        oldLabelFilterEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        oldLabelFilterEmployee.setForeground(new java.awt.Color(255, 255, 255));
        oldLabelFilterEmployee.setText("Min Salary:");

        companyNameLabelFilterEmployee.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        companyNameLabelFilterEmployee.setForeground(new java.awt.Color(255, 255, 255));
        companyNameLabelFilterEmployee.setText("Company:");

        companyComboBoxFilterEmployee.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All" }));

        salaryLabelFilterEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        salaryLabelFilterEmployee.setForeground(new java.awt.Color(255, 255, 255));
        salaryLabelFilterEmployee.setText("Max Salary:");

        getFilterBtnEmployee.setBackground(new java.awt.Color(0, 0, 0));
        getFilterBtnEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        getFilterBtnEmployee.setForeground(new java.awt.Color(255, 255, 255));
        getFilterBtnEmployee.setText("Get Filter");
        getFilterBtnEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getFilterBtnEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout FilterTablePanelEmployeeLayout = new javax.swing.GroupLayout(FilterTablePanelEmployee);
        FilterTablePanelEmployee.setLayout(FilterTablePanelEmployeeLayout);
        FilterTablePanelEmployeeLayout.setHorizontalGroup(
            FilterTablePanelEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(FilterTablePanelEmployeeLayout.createSequentialGroup()
                .addComponent(jobRoleLabelFilterEmployee)
                .addGap(2, 2, 2)
                .addComponent(jobRoleComboBoxFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addComponent(cityLabelFilterEmployee)
                .addGap(1, 1, 1)
                .addComponent(cityComboBoxFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(companyNameLabelFilterEmployee)
                .addGap(3, 3, 3)
                .addComponent(companyComboBoxFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(oldLabelFilterEmployee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(minSalaryTFFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(salaryLabelFilterEmployee)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(maxSalaryTFFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(getFilterBtnEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        FilterTablePanelEmployeeLayout.setVerticalGroup(
            FilterTablePanelEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, FilterTablePanelEmployeeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(FilterTablePanelEmployeeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jobRoleComboBoxFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jobRoleLabelFilterEmployee)
                    .addComponent(cityLabelFilterEmployee)
                    .addComponent(cityComboBoxFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oldLabelFilterEmployee)
                    .addComponent(companyNameLabelFilterEmployee)
                    .addComponent(companyComboBoxFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salaryLabelFilterEmployee)
                    .addComponent(getFilterBtnEmployee)
                    .addComponent(minSalaryTFFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(maxSalaryTFFilterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        EmployeePanel.add(FilterTablePanelEmployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 446, 1080, -1));

        logoutBtnEmployee.setBackground(new java.awt.Color(0, 0, 0));
        logoutBtnEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logoutBtnEmployee.setForeground(new java.awt.Color(255, 255, 255));
        logoutBtnEmployee.setText("Log out");
        logoutBtnEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnEmployeeActionPerformed(evt);
            }
        });
        EmployeePanel.add(logoutBtnEmployee, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 20, 100, -1));

        jobSumPanel.setBackground(new java.awt.Color(189, 195, 199));
        jobSumPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Job Summary", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18))); // NOI18N

        applyToJobBtn.setBackground(new java.awt.Color(0, 0, 0));
        applyToJobBtn.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        applyToJobBtn.setForeground(new java.awt.Color(255, 255, 255));
        applyToJobBtn.setText("Apply");
        applyToJobBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyToJobBtnActionPerformed(evt);
            }
        });

        jLabel20.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel20.setText("Company");

        jLabel24.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel24.setText("Job Name");

        jLabel25.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel25.setText("Department");

        jLabel26.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel26.setText("Salary");

        jLabel27.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel27.setText("City");

        jLabel28.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel28.setText("Definition");

        jLabel29.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel29.setText("Requirements");

        departmentInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        departmentInfoLabelJS.setText(":");

        cityInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        cityInfoLabelJS.setText(":");

        jobNameInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jobNameInfoLabelJS.setText(":");

        requirementsInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        requirementsInfoLabelJS.setText(":");
        requirementsInfoLabelJS.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        definitionInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        definitionInfoLabelJS.setText(":");
        definitionInfoLabelJS.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        companyInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        companyInfoLabelJS.setText(":");

        salaryInfoLabelJS.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        salaryInfoLabelJS.setText(":");

        jobApplyErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        jobApplyErrorLabel.setForeground(new java.awt.Color(255, 0, 0));
        jobApplyErrorLabel.setText("<html><center>* You must<br>select a job<br>from table</center></html>");

        javax.swing.GroupLayout jobSumPanelLayout = new javax.swing.GroupLayout(jobSumPanel);
        jobSumPanel.setLayout(jobSumPanelLayout);
        jobSumPanelLayout.setHorizontalGroup(
            jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jobSumPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cityInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jobNameInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                        .addGap(9, 9, 9))
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(departmentInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(salaryInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(companyInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(applyToJobBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jobApplyErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(definitionInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(requirementsInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jobSumPanelLayout.setVerticalGroup(
            jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jobSumPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(companyInfoLabelJS, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(departmentInfoLabelJS, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jobNameInfoLabelJS, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityInfoLabelJS, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(salaryInfoLabelJS, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(0, 18, Short.MAX_VALUE))
                    .addComponent(requirementsInfoLabelJS, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jobSumPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jobSumPanelLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(41, 41, 41)
                        .addComponent(jobApplyErrorLabel)
                        .addGap(18, 18, 18)
                        .addComponent(applyToJobBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(definitionInfoLabelJS, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        EmployeePanel.add(jobSumPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 370, 420));

        jTabbedPane1.setBackground(new java.awt.Color(228, 241, 254));
        jTabbedPane1.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(189, 195, 199));

        jLabel2.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel2.setText("Gender      ");

        jLabel3.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel3.setText("Graduated University");

        jLabel5.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel5.setText("Prefered Job      ");

        jLabel6.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel6.setText("GPA                ");

        jLabel11.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel11.setText("Birthdate      ");

        buttonGroupGender.add(femaleRb);
        femaleRb.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        femaleRb.setText("Female");

        buttonGroupGender.add(maleRb);
        maleRb.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        maleRb.setText("Male");

        jLabel9.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel9.setText("Nationality   ");

        buttonGroupNationality.add(TCnationalityRB);
        TCnationalityRB.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        TCnationalityRB.setText("TC");

        buttonGroupNationality.add(othernationalityRB);
        othernationalityRB.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        othernationalityRB.setText("Other");

        gpaSpinnerCv.setModel(new javax.swing.SpinnerNumberModel(0.0d, 0.0d, 4.0d, 0.010000000000000009d));

        jLabel41.setText(":");

        jLabel42.setText(":");

        jLabel43.setText(":");

        jLabel44.setText(":");

        jLabel46.setText(":");

        jLabel47.setText("  :");

        takePhotoBtn.setText("Take Photo");
        takePhotoBtn.setPreferredSize(new java.awt.Dimension(90, 30));
        takePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                takePhotoBtnActionPerformed(evt);
            }
        });

        choosePhotoBtn.setText("Choose Photo");
        choosePhotoBtn.setPreferredSize(new java.awt.Dimension(90, 30));
        choosePhotoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                choosePhotoBtnActionPerformed(evt);
            }
        });

        showChosenImageBtn.setText("Show Chosen Image");
        showChosenImageBtn.setPreferredSize(new java.awt.Dimension(90, 30));
        showChosenImageBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showChosenImageBtnActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel45.setText("Add a Photo for CV:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(10, 10, 10))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(52, 52, 52)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(graduatedUniTFCV, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addComponent(gpaSpinnerCv, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(birthdateChooserCV, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel45, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TCnationalityRB, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(femaleRb))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(maleRb)
                            .addComponent(othernationalityRB))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(preferedJobTFCV, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(takePhotoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(choosePhotoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(showChosenImageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(graduatedUniTFCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(gpaSpinnerCv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(preferedJobTFCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(birthdateChooserCV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel42)))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9)
                            .addComponent(jLabel43))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(femaleRb)
                            .addComponent(maleRb)
                            .addComponent(jLabel46))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel45))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(TCnationalityRB)
                        .addComponent(othernationalityRB)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(takePhotoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(choosePhotoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(showChosenImageBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69))
        );

        jTabbedPane1.addTab("CV Page 1", jPanel1);

        jPanel2.setBackground(new java.awt.Color(189, 195, 199));

        languagePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Language", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Sitka Text", 1, 13))); // NOI18N

        lngLevel1.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        lngLevel1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A1", "A2", "B1", "B2", "C1", "C2" }));

        lngLevel2.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        lngLevel2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A1", "A2", "B1", "B2", "C1", "C2" }));

        lngLevel3.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        lngLevel3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "A1", "A2", "B1", "B2", "C1", "C2" }));

        javax.swing.GroupLayout languagePanelLayout = new javax.swing.GroupLayout(languagePanel);
        languagePanel.setLayout(languagePanelLayout);
        languagePanelLayout.setHorizontalGroup(
            languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(languagePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lng1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lng2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lng3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lngLevel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lngLevel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lngLevel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        languagePanelLayout.setVerticalGroup(
            languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(languagePanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lng1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lngLevel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lng2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lngLevel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(languagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lng3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lngLevel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );

        skillPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Skill", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Sitka Text", 1, 13))); // NOI18N

        skillLevel1.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        skillLevel1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Beginner", "Intermediate", "Advanced" }));

        skillLevel2.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        skillLevel2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Beginner", "Intermediate", "Advanced" }));

        skillLevel3.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        skillLevel3.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Beginner", "Intermediate", "Advanced" }));

        skillLevel4.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        skillLevel4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Beginner", "Intermediate", "Advanced" }));

        javax.swing.GroupLayout skillPanelLayout = new javax.swing.GroupLayout(skillPanel);
        skillPanel.setLayout(skillPanelLayout);
        skillPanelLayout.setHorizontalGroup(
            skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(skillPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(skilll1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skilll2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skilll3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skilll4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(skillLevel4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        skillPanelLayout.setVerticalGroup(
            skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(skillPanelLayout.createSequentialGroup()
                .addGroup(skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(skilll1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(skilll2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(skilll3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(skillPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(skilll4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillLevel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        saveCvBtnEmployee.setBackground(new java.awt.Color(0, 0, 0));
        saveCvBtnEmployee.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        saveCvBtnEmployee.setForeground(new java.awt.Color(255, 255, 255));
        saveCvBtnEmployee.setText("Save");
        saveCvBtnEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveCvBtnEmployeeActionPerformed(evt);
            }
        });

        maritalCBCV.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        maritalCBCV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "Married", "Single" }));

        militaryCBCV.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        militaryCBCV.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Choose", "Delayed", "Done" }));

        jLabel8.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel8.setText("Military Service Status ");

        jLabel7.setFont(new java.awt.Font("Sitka Text", 1, 13)); // NOI18N
        jLabel7.setText("Marital Status ");

        jLabel48.setText(":");

        jLabel49.setText(":");

        updateCvBtn.setBackground(new java.awt.Color(0, 0, 0));
        updateCvBtn.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        updateCvBtn.setForeground(new java.awt.Color(255, 255, 255));
        updateCvBtn.setText("Update");
        updateCvBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateCvBtnActionPerformed(evt);
            }
        });

        cvErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        cvErrorLabel.setForeground(new java.awt.Color(255, 0, 0));
        cvErrorLabel.setText("* Some inputs are empty");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(languagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(militaryCBCV, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(maritalCBCV, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(skillPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cvErrorLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateCvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(saveCvBtnEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(militaryCBCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(maritalCBCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(languagePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(skillPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveCvBtnEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(updateCvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cvErrorLabel))
                .addGap(18, 18, 18))
        );

        jTabbedPane1.addTab("CV Page 2", jPanel2);

        EmployeePanel.add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 11, 572, 420));

        passToEmployerPanelBtn.setBackground(new java.awt.Color(0, 0, 0));
        passToEmployerPanelBtn.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        passToEmployerPanelBtn.setForeground(new java.awt.Color(255, 255, 255));
        passToEmployerPanelBtn.setText("<html><center>Pass to<br>Employer<br>Panel</center></html>");
        passToEmployerPanelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passToEmployerPanelBtnActionPerformed(evt);
            }
        });
        EmployeePanel.add(passToEmployerPanelBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 60, 100, -1));

        logInSignInPanel.setMaximumSize(new java.awt.Dimension(920, 575));
        logInSignInPanel.setPreferredSize(new java.awt.Dimension(1100, 690));
        logInSignInPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel12.setBackground(new java.awt.Color(0, 0, 0));
        jLabel12.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("LOGIN");
        logInSignInPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 60, 110, 40));

        confirmPasswdErrorMsgLabelRegister.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        logInSignInPanel.add(confirmPasswdErrorMsgLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        emailErrorMsgLabelRegister.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        emailErrorMsgLabelRegister.setForeground(new java.awt.Color(255, 10, 0));
        emailErrorMsgLabelRegister.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        logInSignInPanel.add(emailErrorMsgLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 230, 170, 20));

        lastNameLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        lastNameLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        lastNameLabelRegister.setText("Last Name");
        logInSignInPanel.add(lastNameLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 170, -1, -1));

        firstNameLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        firstNameLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        firstNameLabelRegister.setText("First Name");
        logInSignInPanel.add(firstNameLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 110, -1, -1));

        usernameLoginTF.setBackground(new java.awt.Color(102, 102, 102));
        usernameLoginTF.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logInSignInPanel.add(usernameLoginTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 130, 250, -1));

        UsernameLoginLabel.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        UsernameLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        UsernameLoginLabel.setText("Username");
        logInSignInPanel.add(UsernameLoginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 110, -1, -1));

        passwdLoginLabel.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        passwdLoginLabel.setForeground(new java.awt.Color(255, 255, 255));
        passwdLoginLabel.setText("Password");
        logInSignInPanel.add(passwdLoginLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 170, -1, -1));

        LoginBtn.setBackground(new java.awt.Color(0, 0, 0));
        LoginBtn.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        LoginBtn.setForeground(new java.awt.Color(255, 255, 255));
        LoginBtn.setText("Login");
        LoginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LoginBtnActionPerformed(evt);
            }
        });
        logInSignInPanel.add(LoginBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 80, 40));

        registerErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        registerErrorLabel.setForeground(new java.awt.Color(255, 13, 0));
        registerErrorLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        registerErrorLabel.setText("* All input spaces must be filled");
        logInSignInPanel.add(registerErrorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 640, 230, -1));

        passwdLoginTF.setBackground(new java.awt.Color(102, 102, 102));
        passwdLoginTF.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logInSignInPanel.add(passwdLoginTF, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 190, 250, -1));

        nameTFRegister.setBackground(new java.awt.Color(102, 102, 102));
        nameTFRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logInSignInPanel.add(nameTFRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 130, 227, -1));

        surnameTFRegister.setBackground(new java.awt.Color(102, 102, 102));
        surnameTFRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logInSignInPanel.add(surnameTFRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 190, 227, -1));

        emailLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        emailLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        emailLabelRegister.setText("Email");
        logInSignInPanel.add(emailLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 230, -1, -1));

        emailTFRegister.setBackground(new java.awt.Color(102, 102, 102));
        emailTFRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        emailTFRegister.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                emailTFRegisterKeyTyped(evt);
            }
        });
        logInSignInPanel.add(emailTFRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 250, 227, -1));

        usernameLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        usernameLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        usernameLabelRegister.setText("Username");
        logInSignInPanel.add(usernameLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 290, -1, -1));

        usernameTFRegister.setBackground(new java.awt.Color(102, 102, 102));
        usernameTFRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logInSignInPanel.add(usernameTFRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 310, 227, -1));

        passwdLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        passwdLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        passwdLabelRegister.setText("Password");
        logInSignInPanel.add(passwdLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 350, -1, -1));

        passwdTFRegister.setBackground(new java.awt.Color(102, 102, 102));
        passwdTFRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        logInSignInPanel.add(passwdTFRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 370, 227, -1));

        confirmPasswdLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        confirmPasswdLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        confirmPasswdLabelRegister.setText("Confirm");
        logInSignInPanel.add(confirmPasswdLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 410, -1, -1));

        confirmPasswdTFRegister.setBackground(new java.awt.Color(102, 102, 102));
        confirmPasswdTFRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        confirmPasswdTFRegister.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                confirmPasswdTFRegisterKeyReleased(evt);
            }
        });
        logInSignInPanel.add(confirmPasswdTFRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 430, 227, -1));

        RegisterBtn.setBackground(new java.awt.Color(0, 0, 0));
        RegisterBtn.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        RegisterBtn.setForeground(new java.awt.Color(255, 255, 255));
        RegisterBtn.setText("Register");
        RegisterBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegisterBtnActionPerformed(evt);
            }
        });
        logInSignInPanel.add(RegisterBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 590, 112, 39));

        roleLabelRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        roleLabelRegister.setForeground(new java.awt.Color(255, 255, 255));
        roleLabelRegister.setText("Job Role");
        logInSignInPanel.add(roleLabelRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 470, -1, -1));

        jLabel13.setFont(new java.awt.Font("Sitka Text", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("REGISTER");
        logInSignInPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 60, -1, -1));

        buttonGroupLogInJobRole.add(ChiefRegister);
        ChiefRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        ChiefRegister.setForeground(new java.awt.Color(255, 255, 255));
        ChiefRegister.setText("Chief");
        logInSignInPanel.add(ChiefRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 490, -1, -1));

        buttonGroupLogInJobRole.add(ExecutiveOfficerRegister);
        ExecutiveOfficerRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        ExecutiveOfficerRegister.setForeground(new java.awt.Color(255, 255, 255));
        ExecutiveOfficerRegister.setText("Executive Officer");
        logInSignInPanel.add(ExecutiveOfficerRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 520, -1, -1));

        buttonGroupLogInJobRole.add(EmployeeRegister);
        EmployeeRegister.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        EmployeeRegister.setForeground(new java.awt.Color(255, 255, 255));
        EmployeeRegister.setText("Employee");
        logInSignInPanel.add(EmployeeRegister, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 550, -1, -1));

        cnflab.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        cnflab.setForeground(new java.awt.Color(255, 13, 0));
        logInSignInPanel.add(cnflab, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 410, 180, 20));

        loginErrorLabel.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        loginErrorLabel.setForeground(new java.awt.Color(255, 13, 0));
        loginErrorLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        loginErrorLabel.setText("* Wrong username or password");
        logInSignInPanel.add(loginErrorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 290, -1));

        ImageLable.setBackground(new java.awt.Color(102, 102, 102));
        ImageLable.setFont(new java.awt.Font("Sitka Text", 1, 14)); // NOI18N
        ImageLable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/4.png"))); // NOI18N
        logInSignInPanel.add(ImageLable, new org.netbeans.lib.awtextra.AbsoluteConstraints(-20, -20, 1230, 710));

        mainLayeredPane.setLayer(EmployerPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainLayeredPane.setLayer(EmployeePanel, javax.swing.JLayeredPane.DEFAULT_LAYER);
        mainLayeredPane.setLayer(logInSignInPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout mainLayeredPaneLayout = new javax.swing.GroupLayout(mainLayeredPane);
        mainLayeredPane.setLayout(mainLayeredPaneLayout);
        mainLayeredPaneLayout.setHorizontalGroup(
            mainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logInSignInPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1055, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(mainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(EmployeePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1080, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(mainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainLayeredPaneLayout.createSequentialGroup()
                    .addComponent(EmployerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1094, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        mainLayeredPaneLayout.setVerticalGroup(
            mainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainLayeredPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(logInSignInPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(mainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainLayeredPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(EmployeePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
                    .addContainerGap()))
            .addGroup(mainLayeredPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(mainLayeredPaneLayout.createSequentialGroup()
                    .addComponent(EmployerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 703, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainLayeredPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1104, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(mainLayeredPane, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LoginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LoginBtnActionPerformed
        String username = usernameLoginTF.getText();
        String passwd   = passwdLoginTF.getText();
        if(username.equals("") | passwd.equals("")){
            loginErrorLabel.setText("* Username or passwd can't be empty");
                loginErrorLabel.setVisible(true);
                return;
        }
            
        try{
            if (connect.isUserCanLogIn(username, passwd)){
                Register r = connect.registerInfosOfUser(username);
                this.loggedId = r.getId();
                if(r.getRole().equals("C")) 
                    showEmployerPanel();
                else 
                    showEmployeePanel();
                loginErrorLabel.setVisible(false);
                clearRegisterInputs();
            }
            else {
                loginErrorLabel.setText("* Wrong username or password");
                loginErrorLabel.setVisible(true);
            }
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.LoginBtnActionPerformed()" + ex);
            if (connect.isUserCanLogIn(username, passwd)){
                Register r = connect.registerInfosOfUser(username);
                this.loggedId = r.getId();
                if(r.getRole().equals("C")) 
                    showEmployerPanel();
                else 
                    showEmployeePanel();
                clearRegisterInputs();
                loginErrorLabel.setVisible(false);
            }
            else loginErrorLabel.setVisible(true);
        }
    }//GEN-LAST:event_LoginBtnActionPerformed

    private void logoutBtnEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnEmployeeActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", 
                                "Exit Program Message Box", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION){
            showLoginPanel();
            clearEmployeeInputs();
            cvErrorLabel.setVisible(false);
            jobApplyErrorLabel.setVisible(false);
            showChosenImageBtn.setVisible(false);
        }
    }//GEN-LAST:event_logoutBtnEmployeeActionPerformed

    public void clearRegisterInputs(){
        usernameTFRegister.setText("");
        passwdTFRegister.setText("");
        emailTFRegister.setText("");
        nameTFRegister.setText("");
        surnameTFRegister.setText("");
        confirmPasswdTFRegister.setText("");
        buttonGroupLogInJobRole.clearSelection();
        emailErrorMsgLabelRegister.setVisible(false);
        cnflab.setVisible(false);
        registerErrorLabel.setVisible(false);
    }
    private void RegisterBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegisterBtnActionPerformed
        String username = usernameTFRegister.getText();
        String passwd   = passwdTFRegister.getText();
        String email    = emailTFRegister.getText();
        String firstName= nameTFRegister.getText();
        String lastName = surnameTFRegister.getText();
        String role;
        role = ChiefRegister.isSelected()?"C":(EmployeeRegister.isSelected()?"E":
                                            (ExecutiveOfficerRegister.isSelected()?"EO":""));
        if(username.equals("") | passwd.equals("") | email.equals("") | 
                firstName.equals("") | lastName.equals("") | role.equals("")){
            registerErrorLabel.setVisible(true);
            return;
        }
        try{
            Register newReg = new Register(firstName, lastName, username, passwd, email, role);
            connect.addDataToRegisterTable(newReg);
            clearRegisterInputs();
            
            try {
                JOptionPane.showMessageDialog(null, "You successfully registered and we send an email to say welcome!", "InfoBox: " + "Register Succesful", JOptionPane.INFORMATION_MESSAGE);
                JavaEmailSender.sendMail(email);
            } catch (Exception ex1) {
                Logger.getLogger(SystemClass.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        catch(Exception ex){
            System.out.println("Data couldn't append! (MainFrame.RegisterBtnActionPerformed()) :" + ex);
            //Mysql server is downing after a few sec. So we need to reconnect and append same object again
            //reconnect operation is making automatically in DB when error occur.
            //if you are here its mean you reconnected
            RegisterBtnActionPerformed(evt);
            
        }
    }//GEN-LAST:event_RegisterBtnActionPerformed

    private void emailTFRegisterKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailTFRegisterKeyTyped
        //emailErrorMsgLabelRegister
        String reg = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*"
                + "|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\"
                + "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*"
                + "[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4]"
                + "[0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9]"
                + "[0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-"
                + "\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        
        if (Pattern.matches(reg, emailTFRegister.getText())){
            emailErrorMsgLabelRegister.setText("Valid Email format");
            emailErrorMsgLabelRegister.setForeground(Color.GREEN);
        }
        else{
            emailErrorMsgLabelRegister.setText("Not Valid Email Format!");
            emailErrorMsgLabelRegister.setForeground(Color.RED);
        }
    }//GEN-LAST:event_emailTFRegisterKeyTyped

    private void getFilterBtnEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getFilterBtnEmployeeActionPerformed
        String jobRole="All", city="All", company="All";
        int minSalary=0, maxSalary=1000000;
        try{
            jobRole = jobRoleComboBoxFilterEmployee.getSelectedItem().toString().equals("Chief")?"C":
                    (jobRoleComboBoxFilterEmployee.getSelectedItem().toString().equals("Employee")?"E":
                    (jobRoleComboBoxFilterEmployee.getSelectedItem().toString().equals("All")?"All":"EO"));
            city = cityComboBoxFilterEmployee.getSelectedItem().toString();
            company = companyComboBoxFilterEmployee.getSelectedItem().toString();
            minSalary = minSalaryTFFilterEmployee.getText().equals("")? 0:Integer.parseInt(minSalaryTFFilterEmployee.getText());
            maxSalary = maxSalaryTFFilterEmployee.getText().equals("")? Integer.MAX_VALUE:Integer.parseInt(maxSalaryTFFilterEmployee.getText());
            try{
                ArrayList<JobPosting> posts = connect.getFilterJobPostings(jobRole, city, company, minSalary, maxSalary);
                fillJobPostingsTable(posts);
            }
            catch(Exception ex){
                System.out.println("err msg1 MainFrame.getFilterBtnEmployeeActionPerformed()"+ ex);
                System.out.println(jobRole+ city+ company+ minSalary+ maxSalary);
                ArrayList<JobPosting> posts = connect.getFilterJobPostings(jobRole, city, company, minSalary, maxSalary);
                fillJobPostingsTable(posts);
            }
        }
        catch(Exception ex){
            System.out.println("err msg2 MainFrame.getFilterBtnEmployeeActionPerformed()"+ ex);
            
        }
        
        
    }//GEN-LAST:event_getFilterBtnEmployeeActionPerformed

    private void logOutBtnEmployerPanelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logOutBtnEmployerPanelActionPerformed
        int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit the program?", 
                                "Exit Program Message Box", JOptionPane.YES_NO_OPTION);
        if (confirmed == JOptionPane.YES_OPTION) {
            showLoginPanel();
            clearJobAdvertBtnActionPerformed(evt);
            employErrorLabel.setVisible(false);
            myJobAdvertsTableErrorLabel.setVisible(false);
            jobAdvertErrorLabel.setVisible(false);
            showChosenImageBtn.setVisible(false);
        }
        
    }//GEN-LAST:event_logOutBtnEmployerPanelActionPerformed

    public void clearEmployeeInputs(){
        graduatedUniTFCV.setText("");
        preferedJobTFCV.setText("");
        birthdateChooserCV.setCalendar(null);
        TCnationalityRB.setSelected(false);
        othernationalityRB.setSelected(false);
        femaleRb.setSelected(false);
        maleRb.setSelected(false);
        militaryCBCV.setSelectedIndex(0);
        maritalCBCV.setSelectedIndex(0);
        lng1.setText("");
        lng2.setText("");
        lng3.setText("");
        lngLevel1.setSelectedIndex(0);
        lngLevel2.setSelectedIndex(0);
        lngLevel3.setSelectedIndex(0);
        skilll1.setText("");
        skilll2.setText("");
        skilll3.setText("");
        skilll4.setText("");
        skillLevel1.setSelectedIndex(0);
        skillLevel2.setSelectedIndex(0);
        skillLevel3.setSelectedIndex(0);
        skillLevel4.setSelectedIndex(0);
        minSalaryTFFilterEmployee.setText("");
        maxSalaryTFFilterEmployee.setText("");
        companyComboBoxFilterEmployee.setSelectedIndex(0);
        cityComboBoxFilterEmployee.setSelectedIndex(0);
        jobRoleComboBoxFilterEmployee.setSelectedIndex(0);
    }
    private void passToEmployerPanelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passToEmployerPanelBtnActionPerformed
        showEmployerPanel();
        clearEmployeeInputs();
        cvErrorLabel.setVisible(false);
        jobApplyErrorLabel.setVisible(false);
        showChosenImageBtn.setVisible(false);
    }//GEN-LAST:event_passToEmployerPanelBtnActionPerformed

    private void jobPostingsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jobPostingsTableMouseClicked
        DefaultTableModel table = (DefaultTableModel)jobPostingsTable.getModel();
        try{
        fillJobSummaryPanel(connect.getSpecisifJobPosting(
                table.getValueAt(jobPostingsTable.getSelectedRow(), 0).toString()));
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.jobPostingsTableMouseClicked()"+ ex);
            fillJobSummaryPanel(connect.getSpecisifJobPosting(
                table.getValueAt(jobPostingsTable.getSelectedRow(), 0).toString()));
        }
    }//GEN-LAST:event_jobPostingsTableMouseClicked

    private void saveCvBtnEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveCvBtnEmployeeActionPerformed
        Register ri = connect.registerInfosOfUser(this.loggedId);
        
        String graduatedUni = graduatedUniTFCV.getText();
        String gpa          = gpaSpinnerCv.getValue().toString().replace(",", ".");
        String preferedJob  = preferedJobTFCV.getText();
        String nationality  = TCnationalityRB.isSelected()?"TC":"Other";
        String gender  = femaleRb.isSelected()?"Female":"Male";
        String maritalStatus = maritalCBCV.getSelectedItem().toString();
        String militaryStatus = militaryCBCV.getSelectedItem().toString();
        String foreignLanguages = (lng1.getText().length() == 0?"":(lng1.getText() + "-" + lngLevel1.getSelectedItem().toString() + ", ")) +
                                (lng2.getText().length() == 0?"":(lng2.getText() + "-" + lngLevel2.getSelectedItem().toString() + ", ")) +
                (lng3.getText().length() == 0?"":(lng3.getText() + "-" + lngLevel3.getSelectedItem().toString() + ", "));
        String skills = (skilll1.getText().length()==0?"":(skilll1.getText()+" - "+skillLevel1.getSelectedItem().toString()) +", ")+
                (skilll2.getText().length()==0?"":(skilll2.getText()+" - "+skillLevel2.getSelectedItem().toString()) +", ")+
                (skilll3.getText().length()==0?"":(skilll3.getText()+" - "+skillLevel3.getSelectedItem().toString()) +", ")+
                (skilll4.getText().length()==0?"":(skilll4.getText()+" - "+skillLevel4.getSelectedItem().toString()));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        
        
        if(graduatedUni.equals("") | gpa.equals("") |nationality.equals("") |
                gender.equals("") |maritalStatus.equals("") |militaryStatus.equals("")){
            cvErrorLabel.setVisible(true);
            return;
        }
        String birthdate = df.format(birthdateChooserCV.getDate());
        int old = Calendar.getInstance().get(Calendar.YEAR) - birthdateChooserCV.getDate().getYear()-1900;
        try{
        Person p = null;
        if(this.loggedId >100 && this.loggedId < 200)
            p = new Chief(ri.getId(),ri.getFirstName(),ri.getLastName(),ri.getEmail(),old,
                        birthdate,gender,graduatedUni,preferedJob,Double.parseDouble(gpa),nationality,"NULL",
                        maritalStatus,militaryStatus,foreignLanguages,skills,"NULL",ri.getRole());
        else if(this.loggedId >200 && this.loggedId < 300)
            p = new ExecutiveOfficer(ri.getId(),ri.getFirstName(),ri.getLastName(),ri.getEmail(),old,
                        birthdate,gender,graduatedUni,preferedJob,Double.parseDouble(gpa),nationality,"NULL",
                        maritalStatus,militaryStatus,foreignLanguages,skills,"NULL",ri.getRole());
        else if(this.loggedId >300 && this.loggedId < 400)
            p = new Employee(ri.getId(),ri.getFirstName(),ri.getLastName(),ri.getEmail(),old,
                        birthdate,gender,graduatedUni,preferedJob,Double.parseDouble(gpa),nationality,"NULL",
                        maritalStatus,militaryStatus,foreignLanguages,skills,"NULL",ri.getRole());
        
            System.out.println(gpa +"\nMainFrame.saveCvBtnEmployeeActionPerformed()\n" );
            connect.addDataToResumesTable(p);
            cvErrorLabel.setVisible(false);
            JOptionPane.showMessageDialog(null, "You CV successfully saved!", "InfoBox: " + "CV Saved Succesful", JOptionPane.INFORMATION_MESSAGE);
                
        }catch(Exception ex){
            System.out.println("err msg MainFrame.saveCvBtnEmployeeActionPerformed: " + ex);
            
            saveCvBtnEmployeeActionPerformed(evt);
        }
    }//GEN-LAST:event_saveCvBtnEmployeeActionPerformed

    private void passToEmployeePanelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passToEmployeePanelBtnActionPerformed
        showEmployeePanel();
        clearJobAdvertBtnActionPerformed(evt);
        employErrorLabel.setVisible(false);
        myJobAdvertsTableErrorLabel.setVisible(false);
        jobAdvertErrorLabel.setVisible(false);
        nameSurnameEILabel.setText(":");
        emailEILabel.setText(":");
        birthdateEILabel.setText(":");
        graduatedUniEILabel.setText(":");
        gpaEILabel.setText(":");
        preferedJobEILabel.setText(":");
        currentJobEILabel.setText(":");
        nationalityEILabel.setText(":");
        maritalStatusEILabel.setText(":");
        militaryStatusEILabel.setText(":");
        genderEILabel.setText(":");
        foreignLanguagesEILabel.setText(":");
        skillsEILabel.setText(":");
        
    }//GEN-LAST:event_passToEmployeePanelBtnActionPerformed

    private void applyToJobBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyToJobBtnActionPerformed
        DefaultTableModel table = (DefaultTableModel)jobPostingsTable.getModel();
        if(jobPostingsTable.getSelectionModel().isSelectionEmpty()){
            jobApplyErrorLabel.setVisible(true);
            return;
        }
        try{
            String jobId = table.getValueAt(jobPostingsTable.getSelectedRow(), 0).toString();
            connect.applyToJob(jobId, Integer.toString(this.loggedId));
            jobApplyErrorLabel.setVisible(false);
            JOptionPane.showMessageDialog(null, "You successfully applied to job! Now take a coffe and wait.", "InfoBox: " + "Job Apply Succesful", JOptionPane.INFORMATION_MESSAGE);
                
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.applyToJobBtnActionPerformed()"+ ex);
            applyToJobBtnActionPerformed(evt);
        }
    }//GEN-LAST:event_applyToJobBtnActionPerformed

    private void createJobAdvertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createJobAdvertBtnActionPerformed
        String company = companyTF.getText();
        String department = departmentTF.getText();
        String jobName = jobNameTF.getText();
        String city = cityTF.getText();
        int salary;
        try{
            salary = Integer.parseInt(salaryTF.getText());
        }catch(NumberFormatException ex){
            salary = 0;
        }
        int advertiserId = this.loggedId;
        String requirements = requirementsTA.getText();
        String definition = definitionTA.getText();
        String jobRole = jobRoleCBJobAdvert.getSelectedItem().toString().equals("Employee")?"E":
                (jobRole = jobRoleCBJobAdvert.getSelectedItem().toString().equals("Chief")?"C":
                (jobRoleCBJobAdvert.getSelectedItem().toString().equals("Executive Officer")?"EO":""));
        JobPosting jp = new JobPosting(company, department, jobName, city, salary,
                                advertiserId, requirements, definition, jobRole);
        if(company.equals("") | department.equals("") | jobName.equals("") | 
                city.equals("") | salary == 0 | jobRole.equals("")){
            jobAdvertErrorLabel.setVisible(true);
            return;
        }
        
        try{
            connect.addDataToJobPostingsTable(jp);
            jobAdvertErrorLabel.setVisible(false);
            JOptionPane.showMessageDialog(null, "You created a job advert successfully!", "InfoBox: " + "Job Advert Succesful", JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.createJobAdvertBtnActionPerformed: "+ ex);
            createJobAdvertBtnActionPerformed(evt);
        }
    }//GEN-LAST:event_createJobAdvertBtnActionPerformed

    private void showAllCvsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showAllCvsBtnActionPerformed
        try{
            fillAllCvsTable(connect.getAllCvs());
        }catch(Exception ex){
            System.out.println("Err msg MailFrame.showAllCvsBtnActionPerformed(): " + ex);
            fillAllCvsTable(connect.getAllCvs());
        }
    }//GEN-LAST:event_showAllCvsBtnActionPerformed

    private void cvstableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cvstableMouseClicked
        DefaultTableModel table = (DefaultTableModel)cvstable.getModel();
        try{
        fillCvSummaryPanel(connect.getSpecificCv(
                table.getValueAt(cvstable.getSelectedRow(), 0).toString()));
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.jobPostingsTableMouseClicked()"+ ex);
            fillCvSummaryPanel(connect.getSpecificCv(
                table.getValueAt(cvstable.getSelectedRow(), 0).toString()));
        }
    }//GEN-LAST:event_cvstableMouseClicked

    private void clearJobAdvertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearJobAdvertBtnActionPerformed
        jobNameTF.setText("");
        cityTF.setText("");
        companyTF.setText("");
        departmentTF.setText("");
        salaryTF.setText("");
        definitionTA.setText("");
        requirementsTA.setText("");
        jobRoleCBJobAdvert.setSelectedIndex(0);
        jobAdvertErrorLabel.setVisible(false);
    }//GEN-LAST:event_clearJobAdvertBtnActionPerformed

    private void myJobPostingsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJobPostingsTableMouseClicked
        DefaultTableModel table = (DefaultTableModel)myJobPostingsTable.getModel();
        try{
            String jobId = table.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString();
            fillAllCvsTable(connect.getAllMyApplicants(jobId));
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.myJobPostingsTableMouseClicked()"+ ex);
            String jobId = table.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString();
            fillAllCvsTable(connect.getAllMyApplicants(jobId));
        }
    }//GEN-LAST:event_myJobPostingsTableMouseClicked

    private void refreshMyJobAdvertsTableBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshMyJobAdvertsTableBtnActionPerformed
        try{
            fillMyJobPostingsTable(connect.getAllMyJobPostings(this.loggedId));
            myJobAdvertsTableErrorLabel.setVisible(false);
        }catch(Exception ex){
            System.out.println("err msg MainFrame.refreshMyJobAdvertsTableBtnActionPerformed()"+ ex);
            fillMyJobPostingsTable(connect.getAllMyJobPostings(this.loggedId));
        }
    }//GEN-LAST:event_refreshMyJobAdvertsTableBtnActionPerformed

    private void confirmPasswdTFRegisterKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_confirmPasswdTFRegisterKeyReleased
        // TODO add your handling code here:
        String psw=passwdTFRegister.getText();
        if(confirmPasswdTFRegister.getText().equals(psw)){
            cnflab.setText("Passwords are same");
            cnflab.setForeground(Color.GREEN);
        }
        else{
            cnflab.setText("Passwords aren't same!");
            cnflab.setForeground(Color.RED);
        } 
          
    }//GEN-LAST:event_confirmPasswdTFRegisterKeyReleased

    private void deleteJobAdvertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteJobAdvertBtnActionPerformed
        DefaultTableModel table = (DefaultTableModel)myJobPostingsTable.getModel();
        if(myJobPostingsTable.getSelectionModel().isSelectionEmpty()){
            myJobAdvertsTableErrorLabel.setVisible(true);
            return;
        }
        String jobId = table.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString();
        try{
            connect.deleteJobPosting(jobId);
            myJobAdvertsTableErrorLabel.setVisible(false);
            JOptionPane.showMessageDialog(null, "You deleted a job advert successfully!", "InfoBox: " + "Delete Job Advert Succesful", JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.deleteJobAdvertBtnActionPerformed()"+ ex);
            connect.deleteJobPosting(jobId);
        }
        fillMyJobPostingsTable(connect.getAllMyJobPostings(this.loggedId));
    }//GEN-LAST:event_deleteJobAdvertBtnActionPerformed

    private void updateJobAdvertBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateJobAdvertBtnActionPerformed
        try{
            DefaultTableModel table = (DefaultTableModel)myJobPostingsTable.getModel();
            if(myJobPostingsTable.getSelectionModel().isSelectionEmpty()){
                myJobAdvertsTableErrorLabel.setVisible(true);
                return;
            }
            String jobId = table.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString();
            JobPosting jp;
            jp = connect.getSpecisifJobPosting(jobId);
            
            jobNameTF.setText(jp.getJobName());
            cityTF.setText(jp.getCity());
            companyTF.setText(jp.getCompanyName());
            departmentTF.setText(jp.getDepartment());
            salaryTF.setText(Integer.toString(jp.getSalary()));
            definitionTA.setText(jp.getDefinition());
            requirementsTA.setText(jp.getRequirements());
            jobRoleCBJobAdvert.setSelectedIndex(jp.getJobrole().equalsIgnoreCase("C")?1:
                                            (jp.getJobrole().equalsIgnoreCase("EO")?2:3));
            myJobAdvertsTableErrorLabel.setVisible(false);
        }
        catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("Doesn't selected any row!");
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.updateJobAdvertBtnActionPerformed()"+ ex);
            updateJobAdvertBtnActionPerformed(evt);
        }
    }//GEN-LAST:event_updateJobAdvertBtnActionPerformed

    private void updateJobAdvertBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateJobAdvertBtn2ActionPerformed
        try{
        DefaultTableModel table = (DefaultTableModel)myJobPostingsTable.getModel();
        JobPosting jp = connect.getSpecisifJobPosting(table.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString());
        
        String company = companyTF.getText();
        String department = departmentTF.getText();
        String jobName = jobNameTF.getText();
        String city = cityTF.getText();
        int salary;
        try{
            salary = Integer.parseInt(salaryTF.getText());
        }catch(NumberFormatException ex){
            salary = 0;
        }
        int advertiserId = this.loggedId;
        String requirements = requirementsTA.getText();
        String definition = definitionTA.getText();
        String jobRole = jobRoleCBJobAdvert.getSelectedItem().toString().equals("Employee")?"E":
                (jobRole = jobRoleCBJobAdvert.getSelectedItem().toString().equals("Chief")?"C":"EO");
        if(company.equals("") | department.equals("") | jobName.equals("") | 
                city.equals("") | salary == 0 | jobRole.equals("")){
            jobAdvertErrorLabel.setVisible(true);
            return;
        }
        JobPosting jpNew = new JobPosting(jp.getId(),company, department, jobName, city, salary,
                                advertiserId, requirements, definition, jobRole, jp.getApplicants());
        deleteJobAdvertBtnActionPerformed(evt);
        connect.addDataToJobPostingsTable(jpNew);
        jobAdvertErrorLabel.setVisible(false);
        JOptionPane.showMessageDialog(null, "You updated a job advert successfully", "InfoBox: " + "Update Job Advert Succesful", JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(ArrayIndexOutOfBoundsException ex){
            System.out.println("Doesn't selected any row!");
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.updateJobAdvertBtn2ActionPerformed()"+ ex);
            updateJobAdvertBtn2ActionPerformed(evt);
        }
        
        clearJobAdvertBtnActionPerformed(evt);
        refreshMyJobAdvertsTableBtnActionPerformed(evt);
    }//GEN-LAST:event_updateJobAdvertBtn2ActionPerformed

    private void updateCvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateCvBtnActionPerformed
        Register ri = connect.registerInfosOfUser(this.loggedId);
        String graduatedUni = graduatedUniTFCV.getText();
        String gpa          = gpaSpinnerCv.getValue().toString().replace(",", ".");
        String preferedJob  = preferedJobTFCV.getText();
        String nationality  = TCnationalityRB.isSelected()?"TC":"Other";
        String gender  = femaleRb.isSelected()?"Female":"Male";
        String maritalStatus = maritalCBCV.getSelectedItem().toString();
        String militaryStatus = militaryCBCV.getSelectedItem().toString();
        String foreignLanguages = (lng1.getText().length() == 0?"":(lng1.getText() + "-" + lngLevel1.getSelectedItem().toString() + ", ")) +
                                (lng2.getText().length() == 0?"":(lng2.getText() + "-" + lngLevel2.getSelectedItem().toString() + ", ")) +
                (lng3.getText().length() == 0?"":(lng3.getText() + "-" + lngLevel3.getSelectedItem().toString() + ","));
        String skills = (skilll1.getText().length()==0?"":(skilll1.getText()+", "+skillLevel1.getSelectedItem().toString()))+
                (skilll2.getText().length()==0?"":(skilll2.getText()+", "+skillLevel2.getSelectedItem().toString()))+
                (skilll3.getText().length()==0?"":(skilll3.getText()+", "+skillLevel3.getSelectedItem().toString()))+
                (skilll4.getText().length()==0?"":(skilll4.getText()+", "+skillLevel4.getSelectedItem().toString()));
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        if(graduatedUni.equals("") | gpa.equals("") |nationality.equals("") |
                gender.equals("") |maritalStatus.equals("") |militaryStatus.equals("")){
            cvErrorLabel.setVisible(true);
            return;
        }
        String birthdate = df.format(birthdateChooserCV.getDate());
        
        int old = Calendar.getInstance().get(Calendar.YEAR) - birthdateChooserCV.getDate().getYear()-1900;
        try{
        Person p = null;
        if(this.loggedId >100 && this.loggedId < 200)
            p = new Chief(ri.getId(),ri.getFirstName(),ri.getLastName(),ri.getEmail(),old,
                        birthdate,gender,graduatedUni,preferedJob,Double.parseDouble(gpa),nationality,"NULL",
                        maritalStatus,militaryStatus,foreignLanguages,skills,"NULL",ri.getRole());
        else if(this.loggedId >200 && this.loggedId < 300)
            p = new ExecutiveOfficer(ri.getId(),ri.getFirstName(),ri.getLastName(),ri.getEmail(),old,
                        birthdate,gender,graduatedUni,preferedJob,Double.parseDouble(gpa),nationality,"NULL",
                        maritalStatus,militaryStatus,foreignLanguages,skills,"NULL",ri.getRole());
        else if(this.loggedId >300 && this.loggedId < 400)
            p = new Employee(ri.getId(),ri.getFirstName(),ri.getLastName(),ri.getEmail(),old,
                        birthdate,gender,graduatedUni,preferedJob,Double.parseDouble(gpa),nationality,"NULL",
                        maritalStatus,militaryStatus,foreignLanguages,skills,"NULL",ri.getRole());
            connect.updateCv(p);
            cvErrorLabel.setVisible(false);
            JOptionPane.showMessageDialog(null, "You CV successfully updated!", "InfoBox: " + "CV Update Succesful", JOptionPane.INFORMATION_MESSAGE);
            
        }catch(Exception ex){
            System.out.println("err msg MainFrame.saveCvBtnEmployeeActionPerformed: " + ex);
            
            updateCvBtnActionPerformed(evt);
        }
    }//GEN-LAST:event_updateCvBtnActionPerformed

    private void employBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employBtnActionPerformed
        DefaultTableModel table = (DefaultTableModel)cvstable.getModel();
        DefaultTableModel table2 = (DefaultTableModel)myJobPostingsTable.getModel();
        if(cvstable.getSelectionModel().isSelectionEmpty()){
            employErrorLabel.setVisible(true);
            return;
        }
        try{
            String cvid = table.getValueAt(cvstable.getSelectedRow(), 0).toString();
            String jobid = table2.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString();
            connect.employSomeoneToJob(cvid, jobid);
            employErrorLabel.setVisible(false);
            JOptionPane.showMessageDialog(null, "You hired successfully!", "InfoBox: " + "Employed Succesful", JOptionPane.INFORMATION_MESSAGE);
            
        }
        catch(Exception ex){
            System.out.println("err msg MainFrame.employBtnActionPerformed()"+ ex);
            String cvid = table.getValueAt(cvstable.getSelectedRow(), 0).toString();
            String jobid = table2.getValueAt(myJobPostingsTable.getSelectedRow(), 0).toString();
            connect.employSomeoneToJob(cvid, jobid);
        }
        
        
        
    }//GEN-LAST:event_employBtnActionPerformed

    private void takePhotoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_takePhotoBtnActionPerformed
        Photo p = new Photo();
        boolean isTaken = p.takePhoto();
        if (isTaken)
            showChosenImageBtn.setVisible(true);
        else{
            JOptionPane.showMessageDialog(null, "You can not use this feature on Linux or MacOS. Only Windows is usable", "InfoBox: " + "OS Type Error", JOptionPane.INFORMATION_MESSAGE);
            
        }
        try{
            p.uploadPhotoToCV(this.loggedId);
        }catch(Exception ex){
            p.uploadPhotoToCV(this.loggedId);
        }
        
    }//GEN-LAST:event_takePhotoBtnActionPerformed

    private void choosePhotoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_choosePhotoBtnActionPerformed
        JFileChooser file = new JFileChooser();
        file.setCurrentDirectory(new File(System.getProperty("user.home")));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images","jpg","gif","png");
        file.addChoosableFileFilter(filter);
        int result = file.showSaveDialog(null);
        if(result == JFileChooser.APPROVE_OPTION){
            File selectedFile = file.getSelectedFile();
            String path = selectedFile.getAbsolutePath();
            Photo p = new Photo(path);
            try{
                p.uploadPhotoToCV(this.loggedId, "flag");
            }catch(Exception ex){
                p.uploadPhotoToCV(this.loggedId, "flag");
            }
            showChosenImageBtn.setVisible(true);
        }
        else if(result == JFileChooser.CANCEL_OPTION){
            System.out.println("no file selected!");
            showChosenImageBtn.setVisible(false);
        }
    }//GEN-LAST:event_choosePhotoBtnActionPerformed

    private void showChosenImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showChosenImageBtnActionPerformed
        try{
            connect.getCVPhotoOfSpecificId(this.loggedId);
        }catch(Exception ex){
            connect.getCVPhotoOfSpecificId(this.loggedId);
        }
        String desktopPath = System.getProperty("user.home") + "\\Desktop";
        ImageIcon img = new ImageIcon(desktopPath +"\\"+ connect.getLatestFileName(desktopPath));
        JFrame frame = new JFrame();
        JLabel label = new JLabel(img);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true); 
    }//GEN-LAST:event_showChosenImageBtnActionPerformed

    private void showCVImageBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCVImageBtnActionPerformed
        DefaultTableModel table = (DefaultTableModel)cvstable.getModel();
        if(cvstable.getSelectionModel().isSelectionEmpty()){
            employErrorLabel.setVisible(true);
            return;
        }
        String ids = table.getValueAt(cvstable.getSelectedRow(), 0).toString();
        
        int id = Integer.parseInt(ids);
        try{
            connect.getCVPhotoOfSpecificId(id);
        }catch(Exception ex){
            connect.getCVPhotoOfSpecificId(id);
        }
        ImageIcon img = new ImageIcon(System.getProperty("user.home") + "\\Desktop\\" + connect.getLatestFileName(
                                                                System.getProperty("user.home") + "\\Desktop"));
        JFrame frame = new JFrame();
        JLabel label = new JLabel(img);
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true); 
    }//GEN-LAST:event_showCVImageBtnActionPerformed

    
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
            java.util.logging.Logger.getLogger(SystemClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SystemClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SystemClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SystemClass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SystemClass().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton ChiefRegister;
    private javax.swing.JPanel EmployeePanel;
    private javax.swing.JRadioButton EmployeeRegister;
    private javax.swing.JPanel EmployerPanel;
    private javax.swing.JRadioButton ExecutiveOfficerRegister;
    private javax.swing.JPanel FilterTablePanelEmployee;
    private javax.swing.JLabel ImageLable;
    private javax.swing.JButton LoginBtn;
    private javax.swing.JButton RegisterBtn;
    private javax.swing.JRadioButton TCnationalityRB;
    private javax.swing.JLabel UsernameLoginLabel;
    private javax.swing.JPanel advertPanel;
    private javax.swing.JButton applyToJobBtn;
    private com.toedter.calendar.JDateChooser birthdateChooserCV;
    private javax.swing.JLabel birthdateEILabel;
    private javax.swing.ButtonGroup buttonGroupGender;
    private javax.swing.ButtonGroup buttonGroupLogInJobRole;
    private javax.swing.ButtonGroup buttonGroupNationality;
    private javax.swing.JButton choosePhotoBtn;
    private javax.swing.JComboBox<String> cityComboBoxFilterEmployee;
    private javax.swing.JLabel cityInfoLabelJS;
    private javax.swing.JLabel cityLabelFilterEmployee;
    private javax.swing.JTextField cityTF;
    private javax.swing.JButton clearJobAdvertBtn;
    private javax.swing.JLabel cnflab;
    private javax.swing.JComboBox<String> companyComboBoxFilterEmployee;
    private javax.swing.JLabel companyInfoLabelJS;
    private javax.swing.JLabel companyNameLabelFilterEmployee;
    private javax.swing.JTextField companyTF;
    private javax.swing.JLabel confirmPasswdErrorMsgLabelRegister;
    private javax.swing.JLabel confirmPasswdLabelRegister;
    private javax.swing.JPasswordField confirmPasswdTFRegister;
    private javax.swing.JButton createJobAdvertBtn;
    private javax.swing.JLabel currentJobEILabel;
    private javax.swing.JLabel cvErrorLabel;
    private javax.swing.JTable cvstable;
    private javax.swing.JLabel definitionInfoLabelJS;
    private javax.swing.JTextArea definitionTA;
    private javax.swing.JButton deleteJobAdvertBtn;
    private javax.swing.JLabel departmentInfoLabelJS;
    private javax.swing.JTextField departmentTF;
    private javax.swing.JLabel emailEILabel;
    private javax.swing.JLabel emailErrorMsgLabelRegister;
    private javax.swing.JLabel emailLabelRegister;
    private javax.swing.JTextField emailTFRegister;
    private javax.swing.JButton employBtn;
    private javax.swing.JLabel employErrorLabel;
    private javax.swing.JRadioButton femaleRb;
    private javax.swing.JLabel firstNameLabelRegister;
    private javax.swing.JLabel foreignLanguagesEILabel;
    private javax.swing.JLabel genderEILabel;
    private javax.swing.JButton getFilterBtnEmployee;
    private javax.swing.JLabel gpaEILabel;
    private javax.swing.JSpinner gpaSpinnerCv;
    private javax.swing.JLabel graduatedUniEILabel;
    private javax.swing.JTextField graduatedUniTFCV;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jobAdvertErrorLabel;
    private javax.swing.JLabel jobApplyErrorLabel;
    private javax.swing.JLabel jobNameInfoLabelJS;
    private javax.swing.JTextField jobNameTF;
    private javax.swing.JTable jobPostingsTable;
    private javax.swing.JComboBox<String> jobRoleCBJobAdvert;
    private javax.swing.JComboBox<String> jobRoleComboBoxFilterEmployee;
    private javax.swing.JLabel jobRoleLabelFilterEmployee;
    private javax.swing.JPanel jobSumPanel;
    private javax.swing.JPanel languagePanel;
    private javax.swing.JLabel lastNameLabelRegister;
    private javax.swing.JTextField lng1;
    private javax.swing.JTextField lng2;
    private javax.swing.JTextField lng3;
    private javax.swing.JComboBox<String> lngLevel1;
    private javax.swing.JComboBox<String> lngLevel2;
    private javax.swing.JComboBox<String> lngLevel3;
    private javax.swing.JPanel logInSignInPanel;
    private javax.swing.JButton logOutBtnEmployerPanel;
    private javax.swing.JLabel loginErrorLabel;
    private javax.swing.JButton logoutBtnEmployee;
    private javax.swing.JLayeredPane mainLayeredPane;
    private javax.swing.JRadioButton maleRb;
    private javax.swing.JComboBox<String> maritalCBCV;
    private javax.swing.JLabel maritalStatusEILabel;
    private javax.swing.JTextField maxSalaryTFFilterEmployee;
    private javax.swing.JComboBox<String> militaryCBCV;
    private javax.swing.JLabel militaryStatusEILabel;
    private javax.swing.JTextField minSalaryTFFilterEmployee;
    private javax.swing.JLabel myJobAdvertsTableErrorLabel;
    private javax.swing.JTable myJobPostingsTable;
    private javax.swing.JLabel nameSurnameEILabel;
    private javax.swing.JTextField nameTFRegister;
    private javax.swing.JLabel nationalityEILabel;
    private javax.swing.JLabel oldLabelFilterEmployee;
    private javax.swing.JRadioButton othernationalityRB;
    private javax.swing.JButton passToEmployeePanelBtn;
    private javax.swing.JButton passToEmployerPanelBtn;
    private javax.swing.JLabel passwdLabelRegister;
    private javax.swing.JLabel passwdLoginLabel;
    private javax.swing.JPasswordField passwdLoginTF;
    private javax.swing.JPasswordField passwdTFRegister;
    private javax.swing.JLabel preferedJobEILabel;
    private javax.swing.JTextField preferedJobTFCV;
    private javax.swing.JButton refreshMyJobAdvertsTableBtn;
    private javax.swing.JLabel registerErrorLabel;
    private javax.swing.JLabel requirementsInfoLabelJS;
    private javax.swing.JTextArea requirementsTA;
    private javax.swing.JLabel roleLabelRegister;
    private javax.swing.JLabel salaryInfoLabelJS;
    private javax.swing.JLabel salaryLabelFilterEmployee;
    private javax.swing.JTextField salaryTF;
    private javax.swing.JButton saveCvBtnEmployee;
    private javax.swing.JButton showAllCvsBtn;
    private javax.swing.JButton showCVImageBtn;
    private javax.swing.JButton showChosenImageBtn;
    private javax.swing.JComboBox<String> skillLevel1;
    private javax.swing.JComboBox<String> skillLevel2;
    private javax.swing.JComboBox<String> skillLevel3;
    private javax.swing.JComboBox<String> skillLevel4;
    private javax.swing.JPanel skillPanel;
    private javax.swing.JTextField skilll1;
    private javax.swing.JTextField skilll2;
    private javax.swing.JTextField skilll3;
    private javax.swing.JTextField skilll4;
    private javax.swing.JLabel skillsEILabel;
    private javax.swing.JTextField surnameTFRegister;
    private javax.swing.JButton takePhotoBtn;
    private javax.swing.JButton updateCvBtn;
    private javax.swing.JButton updateJobAdvertBtn;
    private javax.swing.JButton updateJobAdvertBtn2;
    private javax.swing.JLabel usernameLabelRegister;
    private javax.swing.JTextField usernameLoginTF;
    private javax.swing.JTextField usernameTFRegister;
    // End of variables declaration//GEN-END:variables
}
