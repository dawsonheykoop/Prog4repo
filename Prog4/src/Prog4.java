/**
 * Author: Dawson Heykoop
 * Course: CSc 460: Database Design
 * Program 4: Database Design and Implementation
 * Instructor: Dr. McCann
 * TA's: Prathyusha Butti, Zheng Tang
 * Due: 5/4/2020
 * 
 * Purpose: To design a database which represents a database that assists the dental practice
 * of fictional character named 'Dr. Denton'. This database holds information about the employees
 * at the office, the patients, the visits those patients make to the office, the services of the
 * office and the costs of them. The requirements of the database schema were that they had to be in 
 * BCNF. This makes the data easier to manage. We input that schema into oracle, where the data
 * was stored. The data itself was example data that I believed to be useful in using the given 
 * queries. These queries are inserting/deleting patients from the database, displaying the employees
 * of the office, showing which patients had appointments on a certain dates. In addition we were to
 * give a query that would list all the services of each patient that had an upcoming appointment.
 * Also, for a certain patient, we needed to be able to retrieve their latest bill, list the 
 * patient's info on it, display the services, how much each one cost, and the total cost. My custom
 * query is to display all the possible services the office provides and the cost of each. Java
 * and oracle's sqlpl were used to complete this program.
 * 
 * USAGE:
 * 
 * First, you have to add the Oracle JDBC driver to your CLASSPATH environment variable:
 *
 *         export CLASSPATH=/opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar:${CLASSPATH}
 *         
 *        
 * Then, you can compile the java files (Prog4.java, Controller.java) and run the program with the following usage.
 * 
 * 		   java Prog4 <username> <password>
 * 
 * 		
 * In the future I would want to make a more interesting front end. It would make entering information
 * ten times easier for the user and easy to gather the info as well. 
 */
import java.sql.Connection;

public class Prog4 {
	
	// USAGE ** in terminal **:
	// java Prog4 <oracle username> <oracle password>
	// 
	public static void main(String[] args) {
		String username = null,    // Oracle DBMS username
	           password = null;    // Oracle DBMS password

        if (args.length == 2) {    // get username/password from cmd line args
            username = args[0];
            password = args[1];
        } else {
            System.out.println("\nUsage:  java Prog4 <username> <password>\n"
                             + "    where <username> is your Oracle DBMS"
                             + " username,\n    and <password> is your Oracle"
                             + " password (not your system password).\n");
            System.exit(-1);
        }
        
        // Create the controller with the authorized user's credentials.
        
        Controller prog4_controller = new Controller(username,password);
       
        //System.out.println("Successfully connected to the database.\n");
        System.out.println();
        System.out.println("Welcome to Dr. Denton's database assistant!\n.\n.\n.\n"
				+ "I suppose you'd like some help retrieving some info...\n"
				+ "Let's see what I can do.");  
        Integer query_num;
        
        // Allow the user to keep asking the program for information
        // Until a certain condition in the Controller class is met.
        
        while (true) {
        	query_num = prog4_controller.queryUser();
        	
        	if (query_num == 1) {
        		prog4_controller.insertPatient();
        	}
        	
        	else if (query_num == 2) {
        		prog4_controller.deletePatient();
        	}
        	
        	else if (query_num == 3) {
        		prog4_controller.showEmployees();
        	}
        	
        	else if (query_num == 4) {
        		prog4_controller.whichPatients();
        	}
        	
        	else if (query_num == 5) {
        		prog4_controller.upcomingProcedures();
        	}
        	
        	else if (query_num == 6) {
        		prog4_controller.getBill();
        	}
        	
        	else if (query_num == 7){
        		prog4_controller.showServices();
        	}
        	
        	else {
        		System.out.println("Response not understood. Try again");
        	}
        	
        }

	        
	}

}
