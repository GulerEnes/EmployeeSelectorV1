
package Roles;

import Database.DB;

public class Chief extends Person {
    private static int numOfChief = 100 + DB.howManyChief();
    public String openedJobPosts;
    public String managedPeople;

    public Chief(String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole) {
        super(numOfChief+1,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
    }
    public Chief(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole) {
        super(id,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
    }
    
    public Chief(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, double uniGPA, 
            String nationality, String currentJob, String maritalStatus, String militaryStatus,
            String foreignLanguages, String skills, String appliedJobPosts, String jobRole,
            String openedJobPosts,String managedPeople) {
        super(id,name, surname, email, old, 
            birthdate, gender, graduatedUni, job, 
            uniGPA, nationality, currentJob, 
            maritalStatus, militaryStatus, foreignLanguages, 
            skills, appliedJobPosts, jobRole);
        this.managedPeople=managedPeople;
        this.openedJobPosts=openedJobPosts;
    }
    public static int getNumOfChief() {
        return numOfChief;
    }

    public static void increaseNumOfChief() {
        Chief.numOfChief += 1;
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

    
}
