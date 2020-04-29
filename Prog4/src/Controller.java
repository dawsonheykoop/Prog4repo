import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
