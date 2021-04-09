package Database;

import java.applet.Applet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Photo extends Applet  {
    private String osName;
    public BufferedImage img;
    private String imagePath;
    
    public Photo(){
        this.osName = detectOS();
        
    }

    public Photo(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public boolean takePhoto(){
        try{
            Process process = null;
            if(this.osName.equals("windows")){
                
                File f = new File("takingPhoto.exe");
                String currentDirectory = System.getProperty("user.dir");
                 System.out.println("The current working directory is " + currentDirectory);
                if(f.exists() && !f.isDirectory()) 
                    process = new ProcessBuilder("takingPhoto.exe").start();
                else
                    process = new ProcessBuilder("dist\\takingPhoto.exe").start();
            }
            
            else{
                System.out.println("You can't use this feature from MacOS or Linux!");
                return false;
            }
            process.waitFor();
            this.imagePath = new DB().getLatestFileName(
                    System.getProperty("user.home") + "\\Desktop");
            return true;
        }
        catch(IOException | InterruptedException ex){
            System.out.println("Exe couldn't found!: " + ex);
            return false;
        }
    }
    
    private String detectOS(){
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win"))
            return "windows";
        else if (os.contains("osx"))
            return "macos";
        else if (os.contains("nix") || os.contains("aix") || os.contains("nux"))
            return "linux";
        else
            return "unknown";
    }
    
    public void uploadPhotoToCV(int id){
        DB con = new DB();
        try{
            
            con.uploadCVPhotoTocvimagesTable(System.getProperty("user.home") + "\\Desktop\\"+this.imagePath, id);
        }catch(Exception ex){
            System.out.println("Err msg Photo.uploadPhotoToCV(): " + ex);
            con.uploadCVPhotoTocvimagesTable(System.getProperty("user.home") + "\\Desktop\\"+this.imagePath, id);
        }
    }
   public void uploadPhotoToCV(int id, String flag){
        DB con = new DB();
        try{
            con.uploadCVPhotoTocvimagesTable(this.imagePath, id);
        }catch(Exception ex){
            System.out.println("Err msg Photo.uploadPhotoToCV(): " + ex);
            con.uploadCVPhotoTocvimagesTable(this.imagePath, id);
        }
    }
    
}
