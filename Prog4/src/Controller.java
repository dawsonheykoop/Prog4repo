import java.sql.Connection;
import java.sql.Date;
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
	
	
	public void showEmployees() {
		String test_query = "select empID, name, title from dheykoop1.Employee";
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        
        try {
        	stmt = dbconn.createStatement();
            answer = stmt.executeQuery(test_query);  
            
            if (answer != null) {

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
                    System.out.println(answer.getInt("empID") + "\t"
                        + answer.getString("name") + "\t" 
                        + answer.getString("title"));
                   
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
		
		System.out.println("Option 4. Show the patients with an appointment on a certain date.");
		
		System.out.println("Option 5. Display the scheduled services for patients with "
				+ "upcoming appointments.");
		
		System.out.println("Option 6. Retrieve a copy of the bill for a certain patient's"
				+ " most recent vist.");
		
		//TODO: DEVELOP OWN QUERY
		System.out.println("Option 7. TBD\n");
		
		System.out.print("Enter a digit (1-7) of the option you'd like, or "
				+ "\'exit\' if you are finished: ");
		
		String desired_query = ask_user.next();
		
		System.out.println();
		
		if (desired_query.toUpperCase().equals("EXIT")) {
			System.out.println("Have a nice day :D\n");
			closeConnection();
			System.exit(0);
		}
		
		Integer query_digit = null; // The option digit will be stored here
		
		boolean error = false;		// Will tell if the response is not viable.
		
		try {
			query_digit = Integer.parseInt(desired_query);
		} 
		catch (NumberFormatException ex) {
			error = true;
			
		}
		if (query_digit == null) {
			System.out.println("You have entered an invalid response. When you try again "
					+ "please enter a digit one through six.");
			closeConnection();
			System.exit(-1);
		}
		if (query_digit < 1 || query_digit > 7 || error) {
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
        
        // First get the name of the customer
        
        System.out.println("What is the first name of the patient?");
        System.out.println("Enter Response Here: ");
        String f_name = scan.next();
        scan.nextLine();
        
        System.out.println("What is the last name of the patient?");
        System.out.println("Enter Response Here: ");
        String l_name = scan.next();
        scan.nextLine();
        
        String name = f_name + " " + l_name;
        
        // Next get the address of the customer.
        
        System.out.println("Please enter the street address of the patient.");
        System.out.println("Enter Response Here:");
        String address = scan.nextLine();
        
        // Thirdly, get a phone number to reach the customer at.
        
        System.out.println("Please enter a phone number for the patient. NO SPECIAL "
        		+ "CHARACTERS. ONLY NUMBERS");
        System.out.println("Enter Response Here: ");
        String phone = scan.next();
        
        Integer phone_number = null;	// the phone number of the new customer
        
        try {
        	phone_number = Integer.parseInt(phone);
        }
        catch (NumberFormatException ex) {
        	System.out.println("Invalid phone number. You'll have to start from the beginning :(");
        	return;
        }
        
        // Ask the customer whether they have insurance.
        
        System.out.println("Does the patient have insurance? (Yes/No)");
        System.out.println("Enter Response Here: ");
        
        String insurance = scan.next(); // The user's response to whether the user has insurance.
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
        
        // Make the query to insert the new user into the database
        
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
        
        // Repeat to the user what customer has been inserted and their credentials
        
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
		Scanner scan = new Scanner(System.in); // need to ask the user which customer to remove
		
		System.out.println("Enter the id of the patient you'd like to remove.");
		
		String id = scan.next();		// string version of the id we want to get rid of
		Integer delete_id = null;		// the id of the customer we want to delete
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
	
	public void upcomingProcedures() {
		//TODO: retrieve current date.
		
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        String get_date = "select TO_CHAR(SYSDATE,\'DD-MON-YYYY\'), TO_CHAR(SYSDATE+7,\'DD-MON-YYYY\') FROM dual";
      
        String current_date = null;
        String one_week = null;
        
        try {
        	stmt = dbconn.createStatement();
        	answer = stmt.executeQuery(get_date);
        	
        	if (answer != null) {

                // Use next() to advance cursor through the result
                // tuples and print their attribute values
        		
        		answer.next();
        		current_date = answer.getString("TO_CHAR(SYSDATE,\'DD-MON-YYYY\')");
        		one_week = answer.getString("TO_CHAR(SYSDATE+7,\'DD-MON-YYYY\')");
        		System.out.println("Current date: " + current_date);
        		System.out.println("One week from today: " + one_week);

        }
        	System.out.println();
            stmt.close();  
        } 
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Something went wrong in the query process.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                closeConnection();
                System.exit(-1);
        }
        
        String get_upcoming_apts = "select aptID, aptDate, custID, name from dheykoop1.Customer "
        		+ "join dheykoop1.Appointment on (custID = customerNo) "
        		+ "where aptDate >= TO_DATE(" + "\'" + current_date + "\', \'DD-MON-YYYY\') ";//and "
        				//+ "aptDate <= TO_DATE(" + "\'" + one_week + "\', \'DD-MON-YYYY\')";
        
        stmt = null;
        answer = null;
        try {
        	stmt = dbconn.createStatement();
        	answer = stmt.executeQuery(get_upcoming_apts);
        	
        	if (answer != null) {

                // Use next() to advance cursor through the result
                // tuples and print their attribute values

            while (answer.next()) {
            	int aptID = answer.getInt("aptID");
            	String custName = answer.getString("name");
            	int custId = answer.getInt("custID");
            	Date date = answer.getDate("aptDate");
            	
            	System.out.println("The appointment scheduled for " + custName.toUpperCase() + " with "
            			+ "identification number " + custId + " on the date: " + date + " has "
            					+ "the following scheudled procedures: \n");
            	
            	String inner_query = "select description as \"Service\" from dheykoop1.Service join dheykoop1.ScheduledProcedure "
            			+ "on (Service.sID = ScheduledProcedure.sID) where aptID = " + aptID;
            	
            	Statement stmt2 = null; 			// The query statement that will be executed
                ResultSet answer2 = null;			// the result of the executed query
                stmt2 = dbconn.createStatement();
                answer2 = stmt2.executeQuery(inner_query);
                
                if (answer2 != null) {
                	ResultSetMetaData inner_answer_meta = answer2.getMetaData();
                	
                	// now print the services.
                	while (answer2.next()) {
                		System.out.println(answer2.getString("Service"));
                	}
                	
                }
                
                System.out.println();
                
                stmt2.close();
                System.out.println("-------------------------------");
               
            }
        }
        System.out.println();

            // Shut down the connection to the DBMS.

        stmt.close(); 
        }
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Something went wrong in the query process.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                closeConnection();
                System.exit(-1);
        }
	}
	
	public void whichPatients() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Which date are you inquiring about? Please enter in the form: "
				+ "08-jun-2020");
		boolean error = false;
		
		String response = scan.next();
		
		String[] date = response.split("-");

		if (date.length != 3 || date[0].length() != 2 || date[1].length() != 3 || date[2].length() != 4) {
			error = true;
		}
		
		if (error) {
			System.out.println("Invalid date entered. Maybe try again with the correct form.");
			closeConnection();
			System.exit(-1);
		}
		
		String query = "select custID as \"ID\", name from dheykoop1.Customer join dheykoop1.Appointment"
				+ " on (custID = customerNo) where aptDate = TO_DATE(\'" + response + "\', \'DD-MON-YYYY\')";
		
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        
        try {
        	stmt = dbconn.createStatement();
            answer = stmt.executeQuery(query);  
            
            if (answer != null) {
            	
            	System.out.println();
            	
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
                    System.out.println(answer.getInt("ID") + "\t"
                        + answer.getString("name") + "\t");
                   
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
}
