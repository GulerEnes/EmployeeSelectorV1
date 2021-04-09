
package Database;

import Roles.*;

public class Register {
    private String firstName;
    private String lastName;
    private String username;
    private String passwd;
    private String email;
    private String role;
    private int    id;

    public Register(String firstName, String lastName, String username, 
                    String passwd, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.passwd = passwd;
        this.email = email;
        this.role = role;
        
        if(role.equals("C")){
            this.id = Chief.getNumOfChief() + 1;
        }
        else if(role.equals("EO")){
            this.id = ExecutiveOfficer.getNumOfExecutiveOfficer() + 1;
        }
        else if(role.equals("E")){
            this.id = Employee.getNumOfEmployee() + 1;
        }
    }

    public Register(String firstName, String lastName, String username,
                    String email, String role, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    
}
