
package Roles;

import Database.DB;
import java.util.ArrayList;

public class ExecutiveOfficer extends Person {
    private static int numOfExecutiveOfficer = 200 + DB.howManyExecutiveOfficer();
    public String openedJobPosts;
    public String managedPeople;
    private int superviserId;
    
    public ExecutiveOfficer(String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole) {
        super(numOfExecutiveOfficer+1,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
    }
    public ExecutiveOfficer(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole) {
        super(id,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
    }
    public ExecutiveOfficer(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole,
            String openedJobPosts, String managedPeople, int superviserId) {
        super(id,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
        this.managedPeople=managedPeople;
        this.openedJobPosts=openedJobPosts;
        this.superviserId = superviserId;
    }
    public static int getNumOfExecutiveOfficer() {
        return numOfExecutiveOfficer;
    }

    public static void increaseNumOfExecutiveOfficer() {
        ExecutiveOfficer.numOfExecutiveOfficer += 1;
    }
    public String getOpenedJobPosts() {
        return openedJobPosts;
    }

    public void setOpenedJobPosts(String openedJobPosts) {
        this.openedJobPosts = openedJobPosts;
    }

    public String getManagedPeople() {
        return managedPeople;
    }

    public void setManagedPeople(String managedPeople) {
        this.managedPeople = managedPeople;
    }

    public int getSuperviserId() {
        return superviserId;
    }

    public void setSuperviserId(int superviserId) {
        this.superviserId = superviserId;
    }
    
    
}
