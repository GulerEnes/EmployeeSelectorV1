
package Roles;

import Database.DB;

public class Employee extends Person {
    private static int numOfEmployee = 300 + DB.howManyEmployee();
    private int superviserId;
    
    public Employee(String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole) {
        super(numOfEmployee+1,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
        
    }
    public Employee(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole) {
        super(id,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
        
    }
    public Employee(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole,int superviserId) {
        super(id,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
        this.superviserId = superviserId;
    }
    public static int getNumOfEmployee() {
        return numOfEmployee;
    }

    public static void increaseNumOfEmployee() {
        Employee.numOfEmployee += 1;
    }

    public int getSuperviserId() {
        return superviserId;
    }

    public void setSuperviserId(int superviserId) {
        this.superviserId = superviserId;
    }
    
    
}
