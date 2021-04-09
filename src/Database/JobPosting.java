package Database;

public class JobPosting {
    private static int numberOfPosts = DB.howManyJobPostings() + 1000;
    private int    id;
    private String companyName;
    private String department;
    private String jobName;
    private String city;
    private int    salary;
    private int    advertiseId;
    private String requirements;
    private String definition;
    private String jobrole;
    private String applicants;
    public JobPosting(int id, String companyName, String department, 
            String jobName, String city, int salary, int advertiseId, 
            String requirements, String definition, String jobrole, String applicants) {
        this.id = id;
        this.companyName = companyName;
        this.department = department;
        this.jobName = jobName;
        this.city = city;
        this.salary = salary;
        this.advertiseId = advertiseId;
        this.requirements = requirements;
        this.definition = definition;
        this.jobrole = jobrole;
        this.applicants = applicants;
    }
    
    public static void increaseNumberOfPosts(){
        numberOfPosts++;
    }
    public JobPosting(String companyName, String department, String jobName, String city,
            int salary, int advertiseId, String requirements, String definition, String jobrole) {
        this.companyName = companyName;
        this.department = department;
        this.jobName = jobName;
        this.city = city;
        this.salary = salary;
        this.advertiseId = advertiseId;
        this.requirements = requirements;
        this.definition = definition;
        this.jobrole = jobrole;
        this.id = numberOfPosts + 1;
    }


    public int getId() {
        return id;
    }

    public static int getNumberOfPosts() {
        return numberOfPosts;
    }

    public String getApplicants() {
        return applicants;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDepartment() {
        return department;
    }

    public String getJobName() {
        return jobName;
    }

    public String getCity() {
        return city;
    }

    public int getSalary() {
        return salary;
    }

    public int getAdvertiseId() {
        return advertiseId;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getDefinition() {
        return definition;
    }

    public String getJobrole() {
        return jobrole;
    }
    
    
}
