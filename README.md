# EmployeeSelectorV1
Desktop version of a kind of Freelancer App Demo


BUILD OUTPUT DESCRIPTION


When you build an Java application project that has a main class, the IDE
automatically copies all of the JAR
files on the projects classpath to your projects dist/lib folder. The IDE
also adds each of the JAR files to the Class-Path element in the application
JAR files manifest file (MANIFEST.MF).

To run the project from the command line, go to the dist folder and
type the following:

java -jar "EmployeeSelector_V.1.0_G8.jar" 

To distribute this project, zip up the dist folder (including the lib folder)
and distribute the ZIP file.

Notes:

* If two JAR files on the project classpath have the same name, only the first
JAR file is copied to the lib folder.
* Only JAR files are copied to the lib folder.
If the classpath contains other types of files or folders, these files (folders)
are not copied.
* If a library on the projects classpath also has a Class-Path element
specified in the manifest,the content of the Class-Path element has to be on
the projects runtime path.
* To set a main class in a standard Java project, right-click the project node
in the Projects window and choose Properties. Then click Run and enter the
class name in the Main Class field. Alternatively, you can manually type the
class name in the manifest Main-Class element.






Project dependencies:
	All necessary jar files are under the jar_files folder.
	While resolving you can find the jar files there. (3 files)
	After resolving or after building, the dist folder will be clean.
	If the takePhoto.exe file does not exists in the dist folder, please 
	copy the exe to under the dist folder. It should be near to the project jar file.

Project Description:
	Our project is an employee selector and job hunting program.
	It's working like a demo of a freelancer website. You can create a job advert,
	apply for a job, create a CV even with a photo. All parts of this program
	are showing in only one frame as in real-world professional programs.

Extra Features:
	1- After register, we are sending an email to say welcome.
	We use javax.mail framework to able to send an email.
	2- You can take a photo from your laptop cam if you are on Windows OS.
	We used the OpenCV framework and wrote codes in Python then, converted to exe file
	to make working on every computer, and run it with cmd codes in Java.
	3- We also use a global database. In this way wherever you run this program
	you will see the same tables, like in professional programs.
