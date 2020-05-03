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
        System.out.println("Welcome to Dr. Denton's database assistant!\n.\n.\n.\n"
				+ "I suppose you'd like some help retrieving some info...\n"
				+ "Let's see what I can do.");  
        Integer query_num;
        while (true) {
        	query_num = prog4_controller.queryUser();
        	
        	if (query_num == 1) {
        		prog4_controller.insertPatient();
        	}
        	
        	else if (query_num == 2) {
        		prog4_controller.deletePatient();
        	}
        	
        }

	        
	}

}
