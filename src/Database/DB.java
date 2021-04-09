package Database;

import Roles.*;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class DB {
    private final String dbHost = "mysql5044.site4now.net";
    private final String dbName = "db_a6c9e9_oophome";
    private final String dbUserName = "a6c9e9_oophome";
    private final String dbpass = "java1454";
    
    private static Connection con;
    private static Statement st;
    private static ResultSet rs;
    
    public DB(){
        getConnection();
    }
    
    public final void getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            DB.con = DriverManager.getConnection("jdbc:mysql://"+dbHost+"/"+dbName, dbUserName, dbpass);
            DB.st = con.createStatement();
            System.out.println("Database successfully accessed!");
        }catch(Exception ex){
            System.out.println("An error occured: (DB.getConnection())" + ex);
            throw new RuntimeException(ex);
        }
    }
    
    public boolean addDataToRegisterTable(Register reg){
        try{
            Statement statement = con.createStatement();
            String query = "INSERT INTO `register` (`id`, `name`, `surname`, `username`, `passwd`, `email`, `role`) "+
                    String.format("VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                            Integer.toString(reg.getId()), reg.getFirstName(), reg.getLastName(), 
                            reg.getUsername(), reg.getPasswd(), reg.getEmail(), reg.getRole());
            statement.executeUpdate(query);
            System.out.println("Data successfully appended");
            if (reg.getRole().equalsIgnoreCase("E")) Employee.increaseNumOfEmployee();
            else if (reg.getRole().equalsIgnoreCase("C")) Chief.increaseNumOfChief();
            else ExecutiveOfficer.increaseNumOfExecutiveOfficer();
            return true;
        }catch(Exception ex){
            System.out.println("Data couldn't append! (DB.addDataToRegisterTable()): " + ex);
            getConnection();
            throw new RuntimeException(ex);
            
        }
    }
    
    public <T extends Person>void addDataToResumesTable(T p){
        try{
            String query = "INSERT INTO `cvtable` (`id`, `name`, `surname`, `email`, `old`, `gender`,"
                        + " `graduatedUni`, `preferedjob`, `uniGPA`, `maritalStatus`, `militaryStatus`,"
                        + " `foreignLanguages`, `appliedJobPosts`, `nationality`, `currentJob`, `jobrole`, "
                        + "`openedJobPosts`, `managedPeople`, `skills`, `birthdate`) VALUES "
                        + String.format("('%s', '%s', '%s', '%s', '%d', '%s',",
                                p.getId(),p.getName(),p.getSurname(),p.getEmail(),p.getOld(),p.getGender())
                        + String.format(" '%s', '%s', '%s', '%s', '%s', '%s',",
                                p.getGraduatedUni(),p.getJob(),String.valueOf(p.getUniGPA()).replace(",", "."),p.getMaritalStatus(),p.getMilitaryStatus(),p.getForeignLanguages())
                        + String.format(" '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                                "NULL",p.getNationality(),"NULL",p.getJobRole(),"NULL","NULL",p.getSkills(),p.getBirthdate());
            
            System.out.println(query + "\nDB.addDataToResumesTable()\n");
            st.executeUpdate(query);
            System.out.println("Cv successfully appended!");
        }
        catch(Exception ex){
            System.out.println("err msg DB.addDataToResumesTable()"+ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public void addDataToJobPostingsTable(JobPosting jp){
        try{
            Statement statement = con.createStatement();
            String query = "INSERT INTO `jobpostings` (`id`, `companyname`, `department`, `jobname`, "
            +"`city`, `salary`, `advertiserid`, `requirements`, `definition`, `jobrole`, `applicants`) "+
            String.format("VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    jp.getId(), jp.getCompanyName(), jp.getDepartment(), jp.getJobName(), jp.getCity(),
                    jp.getSalary(), jp.getAdvertiseId(),jp.getRequirements(),jp.getDefinition(),
                    jp.getJobrole(), "NULL");
            statement.executeUpdate(query);
            System.out.println("Data successfully appended");
            JobPosting.increaseNumberOfPosts();
        }catch(Exception ex){
            System.out.println("Data couldn't append! (DB.addDataToJobPostingsTable()): " + ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public static int howManyChief(){
        try{
            String query = "SELECT * FROM `register` WHERE id = (SELECT MAX(id) FROM `register` WHERE role='C')";
            rs = st.executeQuery(query);
            rs.next();
            return Integer.parseInt(rs.getString("id"))-100;
        }catch(Exception ex){
            System.out.println("Err msg from DB.howManyChief(): " + ex);
            return 0;
        }
    }
    public static int howManyExecutiveOfficer(){
        try{
            String query = "SELECT * FROM `register` WHERE id = (SELECT MAX(id) FROM `register` WHERE role='EO')";
            rs = st.executeQuery(query);
            rs.next();
            return Integer.parseInt(rs.getString("id"))-200;
        }catch(Exception ex){
            System.out.println("Err msg from DB.howManyExecutiveOfficer(): " + ex);
            return 0;
        }
    }
    public static int howManyEmployee(){
        try{
            String query = "SELECT * FROM `register` WHERE id = (SELECT MAX(id) FROM `register` WHERE role='E')";
            rs = st.executeQuery(query);
            rs.next();
            return Integer.parseInt(rs.getString("id"))-300;
        }catch(Exception ex){
            System.out.println("Err msg from DB.howManyEmployee(): " + ex);
            return 0;
        }
    }
    public static int howManyJobPostings(){
        try{
            String query = "SELECT MAX(id) FROM `jobpostings`";
            rs = st.executeQuery(query);
            rs.next();
            return Integer.valueOf(rs.getString("MAX(id)"))-1000;
        }catch(Exception ex){
            System.out.println("Err msg from DB.howManyJobPostings(): " + ex);
            return 0;
        }
    }
    
    public boolean isUserCanLogIn(String username, String passwd){
        try{
            String query = String.format("SELECT * FROM `register` "
                       + "WHERE username='%s' AND passwd='%s'",username,passwd);
            rs = st.executeQuery(query);
            if(rs.next())
                return true;
            else
                return false;
        }
        catch(Exception ex){
            System.out.println("err msg DB.isUserCanLogIn()"+ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    public Register registerInfosOfUser(String username){
        try{
            String query = String.format("SELECT * FROM `register` WHERE username='%s'",username);
            rs = st.executeQuery(query);
            rs.next();
            
            return new Register(rs.getString("name"), rs.getString("surname"), rs.getString("username"),
                      rs.getString("email"), rs.getString("role"), Integer.parseInt(rs.getString("id")));
        }
        catch(Exception ex){
            System.out.println("There is not a user with this username or passwd!");
            getConnection();
            return null;
        }
    }
    public Register registerInfosOfUser(int id){
        try{
            String query = String.format("SELECT * FROM `register` WHERE id='%s'",id);
            rs = st.executeQuery(query);
            rs.next();
            
            return new Register(rs.getString("name"), rs.getString("surname"), rs.getString("username"),
                      rs.getString("email"), rs.getString("role"), Integer.parseInt(rs.getString("id")));
        }
        catch(Exception ex){
            System.out.println("There is not a user with this username or passwd!");
            getConnection();
            return null;
        }
    }
    
    public JobPosting getSpecisifJobPosting(String id){
        try{
            String query = "SELECT * FROM jobpostings WHERE id = " + id;
            rs = st.executeQuery(query);
            rs.next();
            return new JobPosting(Integer.parseInt(rs.getString("id")), 
                    rs.getString("companyname"), rs.getString("department"), 
                    rs.getString("jobname"), rs.getString("city"),
                    Integer.parseInt(rs.getString("salary")), 
                    Integer.parseInt(rs.getString("advertiserid")), 
                    rs.getString("requirements"), rs.getString("definition"),
                    rs.getString("jobrole"), rs.getString("applicants")); 
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getSpecisifJobPosting(): "+ ex);
            getConnection();
            return null;
        }
    }
    public Person getSpecificCv(String id){
        try{
            String query = "SELECT * FROM cvtable WHERE id = " + id;
            rs = st.executeQuery(query);
            rs.next();
            
            String name = rs.getString("name");
            String surname = rs.getString("surname");
            String email = rs.getString("email");
            int    old = Integer.parseInt(rs.getString("old"));
            String gender = rs.getString("gender");
            String graduatedUni = rs.getString("graduatedUni");
            String preferedJob = rs.getString("preferedJob");
            double uniGPA = Double.parseDouble(rs.getString("uniGPA"));
            String maritalStatus = rs.getString("maritalStatus");
            String militaryStatus = rs.getString("militaryStatus");
            String foreignLanguages = rs.getString("foreignLanguages");
            String appliedJobPosts = rs.getString("appliedJobPosts");
            String nationality = rs.getString("nationality");
            String currentJob = rs.getString("currentJob");
            String jobrole = rs.getString("jobrole");
            String openedJobPosts = rs.getString("openedJobPosts");
            String managedPeople = rs.getString("managedPeople");
            String skills = rs.getString("skills");
            String birthdate = rs.getString("birthdate");
            int    superviserid = Integer.parseInt(rs.getString("superviserid"));

            Person p;
            if (jobrole.equals("C"))
                p = new Chief(Integer.parseInt(id),name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                            uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                        skills,appliedJobPosts,jobrole,openedJobPosts,managedPeople);
            else if (jobrole.equals("EO"))
                p = new ExecutiveOfficer(Integer.parseInt(id),name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                            uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                        skills,appliedJobPosts,jobrole,openedJobPosts,managedPeople,superviserid);
            else
                p = new Employee(Integer.parseInt(id),name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                            uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                        skills,appliedJobPosts,jobrole,superviserid);
            
            return p;
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getSpecificCv(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public ArrayList<Person> getAllCvs(){
        try{
            String query = "SELECT * FROM cvtable";
            rs = st.executeQuery(query);
            ArrayList<Person> persons = new ArrayList<Person>();
            while(rs.next()){
                int id = Integer.parseInt(rs.getString("id"));
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String email = rs.getString("email");
                int    old = Integer.parseInt(rs.getString("old"));
                String gender = rs.getString("gender");
                String graduatedUni = rs.getString("graduatedUni");
                String preferedJob = rs.getString("preferedJob");
                double uniGPA = Double.parseDouble(rs.getString("uniGPA"));
                String maritalStatus = rs.getString("maritalStatus");
                String militaryStatus = rs.getString("militaryStatus");
                String foreignLanguages = rs.getString("foreignLanguages");
                String appliedJobPosts = rs.getString("appliedJobPosts");
                String nationality = rs.getString("nationality");
                String currentJob = rs.getString("currentJob");
                String jobrole = rs.getString("jobrole");
                String openedJobPosts = rs.getString("openedJobPosts");
                String managedPeople = rs.getString("managedPeople");
                String skills = rs.getString("skills");
                String birthdate = rs.getString("birthdate");
                int    superviserid = Integer.parseInt(rs.getString("superviserid"));
                
                Person p;
                if (jobrole.equals("C"))
                    p = new Chief(id,name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                                uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                            skills,appliedJobPosts,jobrole,openedJobPosts,managedPeople);
                else if (jobrole.equals("EO"))
                    p = new ExecutiveOfficer(id,name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                                uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                            skills,appliedJobPosts,jobrole,openedJobPosts,managedPeople,superviserid);
                else
                    p = new Employee(id,name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                                uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                            skills,appliedJobPosts,jobrole,superviserid);
                persons.add(p);
               
            }
            return persons;
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getAllCvs(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    public ArrayList<JobPosting> getAllJobPostings(){
        try{
            String query = "SELECT * FROM jobpostings";
            
            rs = st.executeQuery(query);
            ArrayList<JobPosting> posts = new ArrayList<JobPosting>();
            while(rs.next()){
                posts.add(new JobPosting(Integer.parseInt(rs.getString("id")), 
                            rs.getString("companyname"), rs.getString("department"), 
                            rs.getString("jobname"), rs.getString("city"),
                            Integer.parseInt(rs.getString("salary")), 
                            Integer.parseInt(rs.getString("advertiserid")), 
                            rs.getString("requirements"), rs.getString("definition"),
                            rs.getString("jobrole"), rs.getString("applicants")));
               
            }
            return posts;
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getAllJobPostings(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    public ArrayList<JobPosting> getAllMyJobPostings(int advertiserId){
        try{
            Statement statement = con.createStatement();
            ResultSet rs2;
            String query = "SELECT * FROM jobpostings WHERE advertiserid = '"+advertiserId+"'";
            rs2 = statement.executeQuery(query);
            ArrayList<JobPosting> posts = new ArrayList<JobPosting>();
            while(rs2.next()){
                posts.add(new JobPosting(Integer.parseInt(rs2.getString("id")), 
                            rs2.getString("companyname"), rs2.getString("department"), 
                            rs2.getString("jobname"), rs2.getString("city"),
                            Integer.parseInt(rs2.getString("salary")), 
                            Integer.parseInt(rs2.getString("advertiserid")), 
                            rs2.getString("requirements"), rs2.getString("definition"),
                            rs2.getString("jobrole"), rs2.getString("applicants")));
               
            }
            return posts;
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getAllMyJobPostings(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public ArrayList<JobPosting> getFilterJobPostings(String jobRole, String city,
                    String company, int minSalary, int maxSalary){
        try{
            boolean flag = false;
            String query = "SELECT * FROM `jobpostings` WHERE";
            if(!jobRole.equals("All")) {
                query += " jobrole='"+jobRole+"'";
                flag = true;
            }
            if(!city.equals("All")){
                query += (flag?" AND":" ")+" city='"+city+"'";
                flag = true;
            }
            if(!company.equals("All")) {
                query += (flag?" AND":" ") + " companyname='"+company+"'";
                flag = true;
            }
            query += (flag?" AND":" ")+" salary >= "+minSalary+" AND salary <= "+maxSalary+"";
            
            rs = st.executeQuery(query);
            ArrayList<JobPosting> posts = new ArrayList<JobPosting>();
            while(rs.next()){
                posts.add(new JobPosting(Integer.parseInt(rs.getString("id")),
                            rs.getString("companyname"), rs.getString("department"), 
                            rs.getString("jobname"), rs.getString("city"),
                            Integer.parseInt(rs.getString("salary")), 
                            Integer.parseInt(rs.getString("advertiserid")), 
                            rs.getString("requirements"), rs.getString("definition"),
                            rs.getString("jobrole"), rs.getString("applicants")));
            }
            return posts;
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getFilterJobPostings(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
        
        
    } 
    public ArrayList<String> getDistinctDatasOfComboBox(String comboBoxName, String tableName){
        try{
            String query = "SELECT DISTINCT "+ comboBoxName +" FROM `"+ tableName +"`";
            rs = st.executeQuery(query);
            ArrayList<String> result = new ArrayList<String>();
            while(rs.next())
                result.add(rs.getString(comboBoxName));
            return result;
        }
        catch(Exception ex){
            System.out.println("Err msg DB.getDistinctDatasOfComboBox(): "+ ex);
            getConnection();
            return new ArrayList<String>();
        }
    }

    public ArrayList<Person> getAllMyApplicants(String jobId){
        try{
            String query = "SELECT * FROM jobpostings WHERE id = " + jobId;
            rs = st.executeQuery(query);
            rs.next();
            String applicants = rs.getString("applicants");
            
            query = "SELECT * FROM cvtable WHERE id IN ( " + applicants + " )";
            rs = st.executeQuery(query);
            ArrayList<Person> persons = new ArrayList<Person>();
            while(rs.next()){
                int id = Integer.parseInt(rs.getString("id"));
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String email = rs.getString("email");
                int    old = Integer.parseInt(rs.getString("old"));
                String gender = rs.getString("gender");
                String graduatedUni = rs.getString("graduatedUni");
                String preferedJob = rs.getString("preferedJob");
                double uniGPA = Double.parseDouble(rs.getString("uniGPA"));
                String maritalStatus = rs.getString("maritalStatus");
                String militaryStatus = rs.getString("militaryStatus");
                String foreignLanguages = rs.getString("foreignLanguages");
                String appliedJobPosts = rs.getString("appliedJobPosts");
                String nationality = rs.getString("nationality");
                String currentJob = rs.getString("currentJob");
                String jobrole = rs.getString("jobrole");
                String openedJobPosts = rs.getString("openedJobPosts");
                String managedPeople = rs.getString("managedPeople");
                String skills = rs.getString("skills");
                String birthdate = rs.getString("birthdate");
                int    superviserid = Integer.parseInt(rs.getString("superviserid"));
                
                Person p;
                if (jobrole.equals("C"))
                    p = new Chief(id,name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                                uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                            skills,appliedJobPosts,jobrole,openedJobPosts,managedPeople);
                else if (jobrole.equals("EO"))
                    p = new ExecutiveOfficer(id,name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                                uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                            skills,appliedJobPosts,jobrole,openedJobPosts,managedPeople,superviserid);
                else
                    p = new Employee(id,name,surname,email,old,birthdate,gender,graduatedUni,preferedJob,
                                uniGPA,nationality,currentJob,maritalStatus,militaryStatus,foreignLanguages,
                            skills,appliedJobPosts,jobrole,superviserid);
                persons.add(p);
               
            }
            return persons;
        }catch(Exception ex){
            System.out.println("Err msg DB.getAllMyAppicants(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public void applyToJob(String jobId, String EmployeeId){
        try{
            //Getting old value
            String query = "SELECT * FROM cvtable WHERE id = " + EmployeeId;
            rs = st.executeQuery(query);
            rs.next();
            String appliedJobPosts = rs.getString("appliedJobPosts");
            //Appending new value to employee
            String newValue =  (appliedJobPosts.equalsIgnoreCase("NULL")?jobId:appliedJobPosts + ","+ jobId);
            query = "UPDATE cvtable SET appliedJobPosts = '" + newValue + "' WHERE id = " + EmployeeId;
            System.out.println(query);
            st.executeUpdate(query);
            
            //Appending employeeId to jobPost's applicants 
            //Getting old value
            query = "SELECT * FROM jobpostings WHERE id = " + jobId;
            rs = st.executeQuery(query);
            rs.next();
            String applicants = rs.getString("applicants");
            
            //Appending new value to jobpost
            newValue =  (applicants.equalsIgnoreCase("NULL")?EmployeeId:applicants + ","+ EmployeeId);
            query = "UPDATE jobpostings SET applicants = '" + newValue + "' WHERE id = " + jobId;
            st.executeUpdate(query);
            
            System.out.println(EmployeeId + "applied to job "+ jobId +" succesfully!");
        }catch(Exception ex){
            System.out.println("Err msg DB.applyToJob(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public <T extends Person>void updateCv(T p){
        try{
            String query = String.format(
                "UPDATE cvtable SET old = %d, gender = '%s', graduatedUni = '%s', preferedjob = '%s', "
                + "uniGPA = %f, maritalStatus = '%s', militaryStatus = '%s', foreignLanguages = '%s', "
                + "nationality = '%s', jobrole = '%s', skills = '%s', birthdate = '%s' WHERE id = %d;",
                p.getOld(),p.getGender(),p.getGraduatedUni(),p.getJob(),p.getUniGPA(),
                p.getMaritalStatus(),p.getMilitaryStatus(),p.getForeignLanguages(),
                p.getNationality(),p.getJobRole(),p.getSkills(),p.getBirthdate(),p.getId());
            
            st.executeUpdate(query);
            System.out.println("Cv successfully updated!");
        }
        catch(Exception ex){
            System.out.println("err msg DB.updateCv()"+ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public void deleteJobPosting(String jobId){
        try{
            String query = "DELETE FROM jobpostings WHERE id = " + jobId;
            st.executeUpdate(query);
        }catch(Exception ex){
            System.out.println("Err msg DB.deleteJobPosting(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public void employSomeoneToJob(String cvid, String jobid){
        try{
            String query = "SELECT advertiserid,jobname FROM jobpostings WHERE id = " + jobid;
            rs = st.executeQuery(query);
            rs.next();
            String advertiserid = rs.getString("advertiserid");
            String jobname = rs.getString("jobname");
            query = String.format("UPDATE cvtable SET currentJob = '%s', superviserid = %s, appliedJobPosts = 'NULL' WHERE id = %s",
                    jobname, advertiserid, cvid);
            st.executeUpdate(query);
            System.out.println(query);
            deleteJobPosting(jobid);
            System.out.println("Successfully employed!");
        }catch(Exception ex){
            System.out.println("Err msg DB.employSomeoneToJob(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    
    public void uploadCVPhotoTocvimagesTable(String path, int id){
            FileInputStream inp = null;
            PreparedStatement pst = null;
        try{
            //checking is there already a photo 
            String query = "SELECT * FROM cvimages WHERE id = " + id;
            rs = st.executeQuery(query);
            if(rs.next()){
                query = "UPDATE cvimages SET img = ? WHERE id = " + id;
                pst = con.prepareStatement(query);
            }
                
            else{
                query = "INSERT INTO cvimages (id, img) VALUES (" +id+ ", ?)"; 
                pst = con.prepareStatement(query);
            }
                
            File img = new File(path);
            inp = new FileInputStream(img);

            pst.setBinaryStream(1, inp);

            pst.executeUpdate();
        }catch(Exception ex){
            System.out.println("Err msg DB.uploadCVPhotoTocvimagesTable(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
    }
    
    public String getLatestFileName(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
           if (lastModifiedFile.lastModified() < files[i].lastModified()) {
               lastModifiedFile = files[i];
           }
        }
        return lastModifiedFile.getName();
    }
    
    public void getCVPhotoOfSpecificId(int id){
        LocalDateTime now = LocalDateTime.now();
        String pngDate = ""+now.getYear()+"_"+now.getDayOfMonth()+"_"+now.getHour()+"_"+now.getMinute()+"_"+now.getSecond();
        try{
            String query = "SELECT img FROM cvimages WHERE id = " + id;
            rs = st.executeQuery(query);
            
            File imgFile = new File(System.getProperty("user.home") + "\\Desktop\\cvImage" +pngDate+ ".png");
            FileOutputStream out = new FileOutputStream(imgFile);
            
            if(rs.next()){
                InputStream inp = rs.getBinaryStream("img");
                byte[] buffer = new byte[1024*5];
                while(inp.read(buffer) > 0) 
                    out.write(buffer);
            }
            
        }catch(Exception ex){
            System.out.println("Err msg DB.getCVPhotoOfSpecificId(): "+ ex);
            getConnection();
            throw new RuntimeException(ex);
        }
        
    }
    
    
}
