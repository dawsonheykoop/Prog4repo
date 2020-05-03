import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// A controller to interact with the view and oracle
public class Controller {
	
	private Connection dbconn;	// Connection to JDBC
	private String username;	// an authorized user to connect to the database
	private String password;	// the password the authorized account
	
	//TODO make the constructor for the view
	public Controller(String username, String password) {
		this.username = username;
		this.password = password;
		this.setup(username,password);
	}
	
	public void closeConnection() {
		try {
			dbconn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//TODO
	/**
	 * setup()
	 * 
	 * Connects the program to oracle using the given username and password and
	 * initializes the connection variable object.
	 */
	private void setup(String username, String password) {
		final String oracleURL =   // Magic lectura -> aloe access spell
                "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";
	        
	     // load the (Oracle) JDBC driver by initializing its base
         // class, 'oracle.jdbc.OracleDriver'.

        try {

                Class.forName("oracle.jdbc.OracleDriver");

        } catch (ClassNotFoundException e) {

                System.err.println("*** ClassNotFoundException:  "
                    + "Error loading Oracle JDBC driver.  \n"
                    + "\tPerhaps the driver is not on the Classpath?");
                System.exit(-1);

        }
        
        // make and return a database connection to the user's
        // Oracle database

	    Connection dbconn = null;
	
	    try {
	            dbconn = DriverManager.getConnection
	                           (oracleURL,username,password);
	
	    } catch (SQLException e) {
	
	            System.err.println("*** SQLException:  "
	                + "Could not open JDBC connection.");
	            System.err.println("\tMessage:   " + e.getMessage());
	            System.err.println("\tSQLState:  " + e.getSQLState());
	            System.err.println("\tErrorCode: " + e.getErrorCode());
	            System.exit(-1);
	
	    }
	    this.dbconn = dbconn;
	}
	
	public void testQuery() {
		String test_query = "select custID, name from dheykoop1.Customer";
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        
        try {
        	stmt = dbconn.createStatement();
            answer = stmt.executeQuery(test_query);  
            
            if (answer != null) {

                System.out.println("\nThe results of the query [" + test_query 
                                 + "] are:\n");

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                ResultSetMetaData answermetadata = answer.getMetaData();

                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                    System.out.print(answermetadata.getColumnName(i) + "\t");
                }
                System.out.println();

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values

                while (answer.next()) {
                    System.out.println(answer.getInt("custID") + "\t"
                        + answer.getString("name"));
                }
            }
            System.out.println();

                // Shut down the connection to the DBMS.

            stmt.close();  
        } 
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                closeConnection();
                System.exit(-1);
        }
        
					
	}
	
	//TODO
	/**
	 * queryUser()
	 * 
	 * Tells the view to query the user
	 */
	public int queryUser() {
		Scanner ask_user = new Scanner(System.in);
		
		System.out.println("Please select from the following.\n");
		
		System.out.println("Option 1. Insert a Patient.");
		
		System.out.println("Option 2. Delete a Patient.");
		
		System.out.println("Option 3. Display the current employees of Dr. Denton's office.");
		
		System.out.println("Option 4. Display the scheduled services for patients with "
				+ "upcoming appointments.");
		
		System.out.println("Option 5. Retrieve a copy of the bill for a certain patient's"
				+ " most recent vist.");
		
		//TODO: DEVELOP OWN QUERY
		System.out.println("Option 6. TBD\n");
		
		System.out.print("Enter a digit (1-6) of the option you'd like, or "
				+ "\'exit\' if you are finished: ");
		
		String desired_query = ask_user.next();
		
		System.out.println();
		
		if (desired_query.toUpperCase().equals("EXIT")) {
			System.out.println("Have a nice day :D\n");
			closeConnection();
			System.exit(-1);
		}
		
		Integer query_digit = null; // The option digit will be stored here
		
		boolean error = false;		// Will tell if the response is not viable.
		
		try {
			query_digit = Integer.parseInt(desired_query);
		} 
		catch (NumberFormatException ex) {
			error = true;
			
		}
		
		if (query_digit < 1 || query_digit > 6 || error) {
			System.out.println("You have entered an invalid response. When you try again "
					+ "please enter a digit one through six.");
			closeConnection();
			System.exit(-1);
		}
		
		return query_digit;
	}
	
	
	//TODO
	/**
	 * insertPatient()
	 * 
	 * puts a new patient into the database
	 */
	public void insertPatient() {
		String test_query = "select max(custID) from dheykoop1.Customer";
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        Integer max_id = null;			// the id in the customer table with the highest id number
        
        Scanner scan = new Scanner(System.in);  // Need to get the new patient's info
        
        try {
        	stmt = dbconn.createStatement();
            answer = stmt.executeQuery(test_query);  
            if (answer == null) {
            	max_id = 1;
            }
            
            else {

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                ResultSetMetaData answermetadata = answer.getMetaData();
                
                answer.next();
                
                max_id = answer.getInt("max(custID)");

            }
            System.out.println();

                // Shut down the connection to the DBMS.

            stmt.close();  
            //dbconn.close();
        } 
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                closeConnection();
                System.exit(-1);
        }
        
        int new_customer_id = max_id + 1;
        
        System.out.println("What is the first name of the patient?");
        System.out.println("Enter Response Here: ");
        String f_name = scan.next();
        scan.nextLine();
        
        System.out.println("What is the last name of the patient?");
        System.out.println("Enter Response Here: ");
        String l_name = scan.next();
        scan.nextLine();
        
        String name = f_name + " " + l_name;
        
        System.out.println("Please enter the street address of the patient.");
        System.out.println("Enter Response Here:");
        String address = scan.nextLine();
        
        System.out.println("Please enter a phone number for the patient. NO SPECIAL "
        		+ "CHARACTERS. ONLY NUMBERS");
        System.out.println("Enter Response Here: ");
        String phone = scan.next();
        
        Integer phone_number = null;
        
        try {
        	phone_number = Integer.parseInt(phone);
        }
        catch (NumberFormatException ex) {
        	System.out.println("Invalid phone number. You'll have to start from the beginning :(");
        	return;
        }
        
        System.out.println("\nDoes the patient have insurance? (Yes/No)\n");
        System.out.println("Enter Response Here: ");
        String insurance = scan.next();
        Integer insured = null;
        if (insurance.toUpperCase().equals("YES")) {
        	insured = 1;
        }
        
        else if (insurance.toUpperCase().equals("NO")) {
        	insured = 0;
        }
        
        else {
        	System.out.println("You have entered an invalid response. Starting over...");
        	return;
        }
        
        String query = "insert into dheykoop1.Customer values(" + new_customer_id +
        		",\'" + name + "\',\'" + address + "\'," + phone_number + "," + insured + 
        		")";

        stmt = null;
        answer = null;
        
        try {
        	stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);  
            stmt.close();  
        } 
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Could not insert the new patient.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                closeConnection();
                System.exit(-1);
                
        }
        
        System.out.println("You have successfully entered " + name + " into the "
        		+ "database, with the following credentials.\n");
        System.out.println("Customer ID: " + new_customer_id);
        System.out.println("Address: " + address);
        System.out.println("Phone: " + phone_number);
        System.out.println("Insured: " + insurance + "\n");
        
        
        
	}
	
	//TODO
	/**
	 * deletePatient()
	 * 
	 * takes a certain patient out of the database
	 */
	public void deletePatient() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the id of the patient you'd like to remove.");
		String id = scan.next();
		Integer delete_id = null;
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
		
		try {
			delete_id = Integer.parseInt(id);
		}
		catch (NumberFormatException ex) {
			System.out.println("Invalid reponse.\n");
			return;
		}
		
		// Make the queries that will remove the customer from the database.
		// This includes any of the tables that contain information relating to
		// this customer. 
		
		String cust_del_query = "delete from dheykoop1.Customer "
				+ "where custID = " + delete_id;
		String app_del_query = "delete from dheykoop1.Appointment "
				+ "where customerNo = " + delete_id;
		String schedProc_del_query = "delete from dheykoop1.ScheduledProcedure "
				+ "where customerID = " + delete_id;
		try {
        	stmt = dbconn.createStatement();
            stmt.executeQuery(cust_del_query);  
            stmt.executeQuery(app_del_query);
            stmt.executeQuery(schedProc_del_query);
            stmt.close();  
        } 
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Something went wrong in the deletion process.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                closeConnection();
                System.exit(-1);
        }
		System.out.println("If the person with the " + delete_id + " id was in "
				+ "the database before, they aren't anymore along with any records about them...");
       
	}
}
