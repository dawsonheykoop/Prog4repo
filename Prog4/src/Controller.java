import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

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
            dbconn.close();
        } 
        
        // Catch any unexpected errors from the query.
        catch (SQLException e) {
        	System.err.println("*** SQLException:  "
                    + "Could not fetch query results.");
                System.err.println("\tMessage:   " + e.getMessage());
                System.err.println("\tSQLState:  " + e.getSQLState());
                System.err.println("\tErrorCode: " + e.getErrorCode());
                System.exit(-1);
        }
        
					
	}
	
	//TODO
	/**
	 * queryUser()
	 * 
	 * Tells the view to query the user
	 */
	public void queryUser() {
		
	}
	
	
	//TODO
	/**
	 * insertPatient()
	 * 
	 * puts a new patient into the database
	 */
	public void insertPatient(Object patient) {
		
	}
	
	//TODO
	/**
	 * deletePatient()
	 * 
	 * takes a certain patient out of the database
	 */
	public void deletePatient(Object patient) {
		
	}
}
