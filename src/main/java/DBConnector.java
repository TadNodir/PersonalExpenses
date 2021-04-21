import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class helps to connect to the MySQL database 'spendingsdb'
 *
 * @author Nodirjon Tadjiev
 * @version 1.0
 */
public class DBConnector {

    // this is the url to my database
    private static final String URL = "jdbc:mysql://localhost:3306/spendingsdb?autoReconnect=true&useSSL=false";
    // this is my username
    private static final String ROOT = "Nodir";
    // this is my password to access to the database
    private static final String PASSWORD = "12131415";

    // object of the Connection class, with the help of which we connect to the database
    private Connection connection;

    /* Having created an object of the DBConnector, the connection to the db will
    automatically be created
    */
    public DBConnector() {
        try{
            connection = DriverManager.getConnection(URL, ROOT, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // getter of our connection
    public Connection getConnection() {
        return connection;
    }
}
