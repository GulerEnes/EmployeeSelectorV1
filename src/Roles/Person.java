
package Roles;

import Database.Photo;
import java.util.ArrayList;

public abstract class Person {
    public Photo photo; 
    public int    id;
    public String name;
    public String surname;
    public String email;
    public int    old;
    public String birthdate;
    public String gender;
    public String graduatedUni;
    public String job;
    public double uniGPA;
    public String nationality;
    public String currentJob;
    public String maritalStatus;
    public String militaryStatus;
    public String foreignLanguages;
    public String skills;
    public String appliedJobPosts;
    public String jobRole;

    public Person(int id, String name, String surname, String email, int old, 
            String birthdate, String gender, String graduatedUni, String job, 
            double uniGPA, String nationality, String currentJob, 
            String maritalStatus, String militaryStatus, String foreignLanguages, 
            String skills, String appliedJobPosts, String jobRole) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.old = old;
        this.birthdate = birthdate;
        this.gender = gender;
        this.graduatedUni = graduatedUni;
        this.job = job;
        this.uniGPA = uniGPA;
        this.nationality = nationality;
        this.currentJob = currentJob;
        this.maritalStatus = maritalStatus;
        this.militaryStatus = militaryStatus;
        this.foreignLanguages = foreignLanguages;
        this.skills = skills;
        this.appliedJobPosts = appliedJobPosts;
        this.jobRole = jobRole;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public int getOld() {
        return old;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public String getGraduatedUni() {
        return graduatedUni;
    }

    public String getJob() {
        return job;
    }

    public double getUniGPA() {
        return uniGPA;
    }

    public String getNationality() {
        return nationality;
    }

    public String getCurrentJob() {
        return currentJob;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public String getMilitaryStatus() {
        return militaryStatus;
    }

    public String getForeignLanguages() {
        return foreignLanguages;
    }

    public String getSkills() {
        return skills;
    }

    public String getAppliedJobPosts() {
        return appliedJobPosts;
    }

    public String getJobRole() {
        return jobRole;
    }

    
    
    
}
