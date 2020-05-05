/**
 * ClassName: Controller
 * Author: Dawson Heykoop
 * 
 * Purpose: The purpose of this class is to be the main entity that works to use
 * both java and the capabilities of oracle to create viability for the queries.
 * All of the methods that execute the queries are located in this class, and it also
 * retrieves information from the user when needed. This class does a lot
 * of back and forth between the java program and the database in oracle. This class
 * is responsible for connecting to oracle through the given authorized credentials.
 * 
 */
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
	
	// Constructor for the Controller class
	public Controller(String username, String password) {
		this.username = username;
		this.password = password;
		this.setup(username,password);
	}
	
	/**
	 * closeConnection()
	 * 
	 * closes the connection between the program and oracle.
	 */
	public void closeConnection() {
		try {
			dbconn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * setup()
	 * 
	 * Connects the program to oracle using the given username and password and
	 * initializes the connection variable object.
	 * 
	 * @param username is the user's username for oracle
	 * @param password is the user's password for oracle
	 * 
	 * preconditions: username,password are the users credentials
	 * postconditions: the connection has been made to oracle
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

	    Connection dbconn = null; // connection to oracle
	
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
	
	/**
	 * showEmployees()
	 * 
	 * Displays the current employees in Dr. Denton's office, as well as their titles
	 * 
	 * preconditions: the database dheykoop1.Employee exists.
	 * postconditions: The employees are shown to the user.
	 */
	public void showEmployees() {
		String test_query = "select empID, name, title from dheykoop1.Employee";
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        int max_string = 20;
        
        try {
        	stmt = dbconn.createStatement();
            answer = stmt.executeQuery(test_query);  
            
            if (answer != null) {

                    // Get the data about the query result to learn
                    // the attribute names and use them as column headers

                ResultSetMetaData answermetadata = answer.getMetaData();
                String spaces = "";
                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                	spaces = answermetadata.getColumnName(i);
                	
                	while (spaces.length() < max_string) {
                		spaces += " ";
                	}
                    System.out.print( spaces);
                }
                System.out.println();

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values

                while (answer.next()) {
                	// makes the ids look nice and uniform
                	spaces = answer.getString("empID");             
                	while (spaces.length() < max_string) {
                		spaces += " ";
                	}
                	String id = spaces;
                	
                	// makes the names look nice and uniform
                	spaces = answer.getString("name");                	
                	while (spaces.length() < max_string) {
                		spaces += " ";
                	}
                	String name = spaces;
                	
                	// makes the titles look nice and uniform
                	spaces = answer.getString("title");                	
                	while (spaces.length() < max_string) {
                		spaces += " ";
                	}
                	String title = spaces;
                	
                    System.out.println(id + name + title);
                   
                }
            }
            System.out.println();
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
	
	/**
	 * queryUser()
	 * 
	 * Finds out which query the user wants to use. There are 7 options. The user enters a single digit when prompted.
	 * The program gives back an integer that represents which query the user wants to use. The user is also
	 * given the option of leaving the program by entering 'exit'.
	 * 
	 * @return int that represents the query the user wants to user
	 * 
	 * Preconditions: none
	 * postconditions: the integer returned is between 1 and 7
	 */
	public int queryUser() {
		Scanner ask_user = new Scanner(System.in);	// Need to ask the user which query.
		
		// Display the list of queries the user can pick from
		
		System.out.println("Please select from the following.\n");
		
		System.out.println("Option 1. Insert a Patient.");
		
		System.out.println("Option 2. Delete a Patient.");
		
		System.out.println("Option 3. Display the current employees of Dr. Denton's office.");
		
		System.out.println("Option 4. Show the patients with an appointment on a certain date.");
		
		System.out.println("Option 5. Display the scheduled services for patients with "
				+ "upcoming appointments.");
		
		System.out.println("Option 6. Retrieve a copy of the bill for a certain patient's"
				+ " most recent vist.");
		
		System.out.println("Option 7. Display the list of services and their costs (pre-insurance).\n");	
		
		System.out.print("Enter a digit (1-7) of the option you'd like, or "
				+ "\'exit\' if you are finished: ");
		
		// get the query digit
		String desired_query = ask_user.next();
		
		System.out.println();
		
		// Must mean that the user was done with the program.
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
		
		// Check to see if the response is valid 
		
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
	
	
	/**
	 * insertPatient()
	 * 
	 * puts a new patient into the database. The phone number and response to the insurance question are pretty restricted. But the other
	 * data doesnt really have many restraints.
	 * 
	 * @preconditions the user wanted to insert a patient
	 * @postconditions the new patient is inserted into the database, if nothing went wrong, i.e. some invalid data was entered.
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
        
        //make unique id for the patient
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
        
        System.out.println("Please enter a phone number (10 digits) for the patient. NO SPECIAL "
        		+ "CHARACTERS. ONLY NUMBERS");
        System.out.println("Enter Response Here: ");
        String phone = scan.next();
        
        if (phone.length() != 10) {
        	System.out.println("Not a valid phone number. Rebooting program");
        	System.exit(-1);
        }
        
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
	

	/**
	 * deletePatient()
	 * 
	 * takes a certain patient out of the database. it also makes sure that the patient is removed from all records from
	 * all tables.
	 * 
	 * @precondition the user wants to remove a patient
	 * @postcondition if the user existed in the database it doesnt anymore
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
	
	/**
	 * upcomingProcedures()
	 * 
	 * displays all the services for each patient with an upcoming appointment. This one got a little tricky
	 * with the queries needed to get the final result. Originally i took 'upcoming' to be specific. So I retrieved the
	 * current date, which is needed either way, and the date one week ahead of the current date. One week in advance seemed
	 * to mean upcoming to me originally, and I could've also done one month in advance. This one just shows any appointments in the future
	 * but could be easily changed to show only a certain interval. I didn't see the necessity for this program.
	 * 
	 * @postcondition the patient's with upcoming appointments are displayed along with all of the services.
	 */
	public void upcomingProcedures() {
		
		Statement stmt = null; 			// The query statement that will be executed
        ResultSet answer = null;		// the result of the executed query
        String get_date = "select TO_CHAR(SYSDATE,\'DD-MON-YYYY\'), TO_CHAR(SYSDATE+7,\'DD-MON-YYYY\') FROM dual";
      
        String current_date = null;		// current date retrieved from oracle (very trustworthy)
        String one_week = null;			// the date one week ahead
        
        try {
        	stmt = dbconn.createStatement();
        	answer = stmt.executeQuery(get_date);
        	
        	if (answer != null) {

                // Use next() to advance cursor through the result
                // tuples and print their attribute values
        			
        		answer.next();
        		current_date = answer.getString("TO_CHAR(SYSDATE,\'DD-MON-YYYY\')");
        		one_week = answer.getString("TO_CHAR(SYSDATE+7,\'DD-MON-YYYY\')");

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
        
        // gets the upcoming appointments. Could easily be modified to get the appointments within
        // a certain period of time.
        String get_upcoming_apts = "select aptID, aptDate, TO_CHAR(aptDate,'HH24:MI'), custID, name from dheykoop1.Customer "
        		+ "join dheykoop1.Appointment on (custID = customerNo) "
        		+ "where aptDate >= SYSDATE"; //TO_DATE(" + "\'" + current_date + "\', \'DD-MON-YYYY\') ";//and "
        				//+ "aptDate <= TO_DATE(" + "\'" + one_week + "\', \'DD-MON-YYYY\')";
        
        stmt = null;
        answer = null;
        
        // now to get the results
        
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
            	String time = answer.getString("TO_CHAR(aptDate,'HH24:MI')");
            	
            	System.out.println("The appointment scheduled for " + custName.toUpperCase() + "\nwith "
            			+ "identification number " + custId + " on " + date + "\nat " + time + " has "
            					+ "the following scheudled procedures: \n");
            	
            	// Gets the services for this specific appointment.
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
	
	/**
	 * whichPatients()
	 * 
	 * shows the patients that have an appointment on a certain date. Could be easily modified to also show the time it is scheduled for
	 * as well.
	 * 
	 * @precondition the user wants to know which patients have an appointment on a certain day
	 * @postcondition these patients are displayed to the screen
	 */
	public void whichPatients() {
		int max_string = 15;
		Scanner scan = new Scanner(System.in);
		System.out.println("Which date are you inquiring about? Please enter in the form: "
				+ "08-jun-2020");
		boolean error = false;		// tells whether an error has occurred in retrieving the date.
		
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
		
		// the query that gets the customers.
		String query = "select custID as \"ID\", name, TO_CHAR(aptDate,'HH24:MI') as \"Time\" "
				+ "from dheykoop1.Customer join dheykoop1.Appointment"
				+ " on (custID = customerNo) where TO_CHAR(aptDate,'DD-MON-YYYY') = \'" +  response.toUpperCase() + "\'";//TO_DATE(\'" + response + "\', \'DD-MON-YYYY\')";
		
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

                String temp = "";
                for (int i = 1; i <= answermetadata.getColumnCount(); i++) {
                	temp = answermetadata.getColumnName(i);
                	while (temp.length() < max_string) {
                		temp += " ";
                	}
                    System.out.print(temp);
                }
                System.out.println();

                    // Use next() to advance cursor through the result
                    // tuples and print their attribute values
                String id = "";
                String name = "";
                String time = "";
                while (answer.next()) {
                	id = answer.getString("ID");
                	while (id.length() < max_string) {
                		id += " ";
                	}
                	
                	name = answer.getString("name");
                	while (name.length() < max_string) {
                		name += " ";
                	}
                	
                	time = answer.getString("Time");
                	while (time.length() < max_string) {
                		time += " ";
                	}
                    System.out.println(id + name + time);
                        
                        
                   
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
	
	/**
	 * getBill()
	 * 
	 * this produces the bill of a certain patient's most recent visit. It is formatted 
	 * in a somewhat neat, readable manner. It shows the date of the appointment, the services
	 * given, the charges for each of them, the user's info, including whether the patient has 
	 * insurance or not, and the total cost.
	 * 
	 * preconditions: the user wants to see the most recent bill of a certain patient.
	 * 
	 * postconditions: the bill has been displayed to the user if there has been an appointment
	 * in the past involving that patient. If not, the user is told of the lack of past appointments.
	 */
	public void getBill() {
		// this max helps the results look more refined on the bill
		int string_max = 30;
		
		// need to get the id of the patient in question
		Scanner scan = new Scanner(System.in); // 
		System.out.println("Enter the unique identifier for the patient: ");
		String id = scan.next();
		
		Integer custID = null;	// the id of the patient
		Integer aptID = null;	// the appointment id that may or may not be found
		
		try {
			custID = Integer.parseInt(id);
		}
		catch (NumberFormatException ex) {
			System.out.println("You must enter an integer value.");
			closeConnection();
			System.exit(-1);
		}
		
		// query that will get the customer info
		String customer_info_query = "select name, phoneNo, insurance from "
				+ " dheykoop1.Customer where custID = " + custID;
		
		// the query that will get the last appointment this patient had
		String get_last_apt = "select aptID,TO_CHAR(aptDate,'DD-MON-YYYY') from (select aptID, aptDate from dheykoop1.Appointment" + 
				" where TRUNC(aptDate) <= TRUNC(SYSDATE) and customerNo = " + custID + 
				" order by aptDate desc)" + 
				" where rownum <=1";
		
		Statement stmt = null;			// statement to exc query
		ResultSet answer = null;		// the results of the query that gets the customer info
		ResultSet last_date = null;		// the results of the query that gets the last appointment the patient had
		ResultSet services_list = null;	// the results of the query that will get the services that occurred at this last appointment
		
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(customer_info_query);
			
			if (answer != null) {
				if (answer.next()) {
					
					String name = answer.getString("name");
					String phone = answer.getString("phoneNo");
					String insured = answer.getString("insurance");
					String insurance = "No";
					
					if (insured.equals("1")) {
						insurance = "Yes";
					}
										
					last_date = stmt.executeQuery(get_last_apt);
					
					if (last_date != null) {
						if (last_date.next()) {
							// Getting the appointment ID.

							aptID = last_date.getInt("aptID");
							String date = last_date.getString("TO_CHAR(aptDate,'DD-MON-YYYY')"); // the date of the last appointment.
	
							String get_services = "select description as \"Service\", cost "
									+ "from dheykoop1.Service join dheykoop1.scheduledprocedure on (dheykoop1.Service.sID = dheykoop1.scheduledprocedure.sID) "
									+ "where aptID = " + aptID;
							
							services_list = stmt.executeQuery(get_services);
							
							if (services_list != null) {

				                    // Use next() to advance cursor through the result
				                    // tuples and print their attribute values
								
								System.out.println("---------------------------------------------------");
								System.out.println("Bill of Services\t\t\t" + date);
								System.out.println("\nCustomer ID: " + custID
										+ "\nName: " + name 
										+ "\nPhone Number: " + phone
										+ "\nInsurance: " + insurance);
								
								System.out.println("\nCHARGES:");
								
								float total_charge = 0;		// keeps track of total charge of a bill
								
				                while (services_list.next()) {
				                	float cost = 0;
				                	if (insurance.toUpperCase().equals("YES")) {
				                		
				                		cost = (float)services_list.getInt("cost") / 4; // if the patient has insurance the bill is only a quarter 
				                														// of the whole cost
				     
				                	}else {
				                		cost = services_list.getInt("cost");
				                	}
				                	String line = services_list.getString("Service");
				                	while (line.length() < string_max) {
				                		line += "-";
				                	}
				                	line += "\t";
				               
				                	System.out.println(line + "$" + cost);
				                    
				                    total_charge += cost;
				                }
				                System.out.println("\nTotal Charge-------------------\t" + "$" + total_charge);
				                System.out.println("---------------------------------------------------");
							}
							
							
						}
						
						else {
							System.out.println("No appointments for this customer have occurred.");
							return;
						}
					}
					
					
					
				}
				else {
					System.out.println("\nThere are no customers with this id!!!\n");
					return;
				}
				
			}
			
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
	
	/**
	 * showServices()
	 * 
	 * displays to the users all the services that are available and their
	 * costs. This is a short, simple, but sweet query
	 * 
	 * @precondition there have been services put into the table.
	 * @postcondition the services have been shown to the user
	 */
	public void showServices() {
		int string_max = 30;		// this number helps make the results of the query look
									// more uniform
									//
		Statement stmt = null;		// statement to exc query
		ResultSet answer = null;	// the results of the query
		
		String query = "select description as \"Service\", cost as \"Cost\" "
		+ " from dheykoop1.Service";
		
		try {
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);
			
			if (answer != null) {

                while (answer.next()) {
                	
                	// Clean up the results a bit by making them look more uniform
                	String line = answer.getString("Service");
                	while (line.length() < string_max) {
                		line += "-";
                	}
                	line += "\t";
               
                	System.out.println(line + "$" + answer.getInt("Cost"));
                			
                	
                }
                System.out.println();
                stmt.close();
			}
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
