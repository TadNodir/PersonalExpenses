import exceptions.ProgramException;
import functional.Categories;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The main class, which is responsible for functionality of the program
 *
 * @author Nodirjon Tadjiev
 * @version 1.0
 */
public class MainController {
    // Regular expression to check, if the input arguments of expenses addition method in command line are correct
    private static final String ADD = "^add ([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9]):(rent|bills|clothes|food|entertainment|hobby):([+-]?([0-9]*[.])?[0-9]+)$";
    /* Regular expression to check, if the input arguments of requesting expenses by the date method in command
     line are correct */
    private static final String REQUEST = "^request ([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
    // Regular expression to check, if the input arguments of printing all expenses method in command line are correct
    private static final String PRINT = "^print$";
    // Regular expression to check, if the input arguments of deleting of expenses from the table method in command line are correct
    private static final String DELETE = "^delete ([0-9]{4})-(1[0-2]|0[1-9])-(3[01]|[12][0-9]|0[1-9])$";
    /* Regular expression to check, if the input arguments of requesting monthly expenses in specific category method
     in command line are correct */
    private static final String MONTHLY = "^month ([0-9]{4})-(1[0-2]|0[1-9]):(rent|bills|clothes|food|entertainment|hobby)$";
    // String command of selection of all values from the table expenditure
    private static final String SELECTALL = "SELECT * FROM spendingsdb.expenditure ORDER BY occasion;";
    // attribute of the Categories class
    private static Categories categories;


    public static void main(String[] args) {
        while (true) {
            // reads the command from the command line
            String readLine = edu.kit.informatik.Terminal.readLine();
            try {
                // connects to the database 'spendingsdb'
                DBConnector dbc = new DBConnector();
                Statement statement = dbc.getConnection().createStatement();
                // if the read string is the same as expected in our regular expressions, then go on
                if (Pattern.matches(ADD, readLine)) {
                    addExpense(readLine, statement);
                }
                else if (Pattern.matches(REQUEST, readLine)) {
                    requestByDate(readLine, statement);
                }
                else if (Pattern.matches(PRINT, readLine)) {
                    printAll(statement);
                }
                else if (Pattern.matches(DELETE, readLine)) {
                    delete(readLine, statement);
                }
                else if (Pattern.matches(MONTHLY, readLine)) {
                    countMonth(readLine, statement);
                }
                else {
                    throw new SQLException("incorrect command arguments. Please try again.");
                }
            } catch (SQLException | ProgramException e) {
                edu.kit.informatik.Terminal.printError(e.getMessage());
            }
        }
    }

    /**
     * Adds expenses to the spendingsdb.expenditure table
     * the syntax is: add YYYY-MM-DD:rent|food|entertainment|clothes|bills|hobby:amount of expense(double)
     * @param input read string from the command line
     * @param statement of creation of connection
     * @throws SQLException if could not find a match in the db
     */
    private static void addExpense(String input, Statement statement) throws SQLException {
        Matcher matchCommand = Pattern.compile(ADD).matcher(input);
        if (matchCommand.find()) {
            String dateStr = matchCommand.group(1) + "-" + matchCommand.group(2) + "-" + matchCommand.group(3);
            if (findMatch(Date.valueOf(dateStr), statement)) {
                statement.execute("UPDATE spendingsdb.expenditure SET " + matchCommand.group(4) + " = "
                        + Float.parseFloat(matchCommand.group(5)) + " WHERE occasion = '" + dateStr + "';");
            } else {
                statement.execute("INSERT INTO spendingsdb.expenditure (" + matchCommand.group(4)
                        + ", occasion) VALUES (" + Float.parseFloat(matchCommand.group(5)) + ", '" + dateStr + "');");
            }
        }
    }

    /**
     * Searches for expenses from the spendingsdb.expenditure table
     * @param day to search for
     * @param state statement of creation of connection
     * @return true if the given day is found
     * @throws SQLException if not found
     */
    private static boolean findMatch (Date day, Statement state) throws SQLException {
        ResultSet resultSet = state.executeQuery("SELECT" +
                " occasion" +
                " FROM" +
                " spendingsdb.expenditure " +
                "WHERE occasion = '" + day.toString() + "';");
        return resultSet.isBeforeFirst();
    }

    /**
     * Calls expenses from the spendingsdb.expenditure table by date
     * the syntax is: request YYYY-MM-DD
     * @param input read string from the command line
     * @param statement of creation of connection
     * @throws SQLException if could not find a match in the db
     */
    private static void requestByDate(String input, Statement statement) throws SQLException {
        Matcher matchCommand = Pattern.compile(REQUEST).matcher(input);
        if (matchCommand.find()) {
            String dateStr = matchCommand.group(1) + "-" + matchCommand.group(2) + "-" + matchCommand.group(3);
            if (!findMatch(Date.valueOf(dateStr), statement)) {
                throw new SQLException("could not find the given date.");
            }
            ResultSet resultSet = statement.executeQuery("SELECT" +
                    " * " +
                    " FROM" +
                    " spendingsdb.expenditure" +
                    " WHERE occasion = '" + dateStr + "';");
            categories = new Categories();
            print(resultSet, categories);
        }
    }

    /**
     * Deletes expenses from the spendingsdb.expenditure table by date
     * the syntax is: delete YYYY-MM-DD
     * @param input read string from the command line
     * @param statement of creation of connection
     * @throws SQLException if could not find a match in the db
     */
    private static void delete(String input, Statement statement) throws SQLException {
        Matcher matchCommand = Pattern.compile(DELETE).matcher(input);
        if (matchCommand.find()) {
            String dateStr = matchCommand.group(1) + "-" + matchCommand.group(2) + "-" + matchCommand.group(3);
            if (!findMatch(Date.valueOf(dateStr), statement)) {
                throw new SQLException("could not find the given date.");
            }
            statement.execute("DELETE FROM spendingsdb.expenditure WHERE occasion = '" + dateStr + "';");
        }
    }


    /**
     * counts the sum of monthly expenses of specific categories from the spendingsdb.expenditure table
     * the syntax is: month YYYY-MM:rent|food|entertainment|clothes|bills|hobby
     * @param input read string from the command line
     * @param statement of creation of connection
     * @throws SQLException if could not find a match in the db
     * @throws ProgramException if any expenses were made in this category
     */
    private static void countMonth(String input, Statement statement) throws SQLException, ProgramException {
        Matcher matchCommand = Pattern.compile(MONTHLY).matcher(input);
        if (matchCommand.find()) {
            ResultSet resultSet = statement.executeQuery("SELECT SUM(" + matchCommand.group(3) +
                    ") FROM spendingsdb.expenditure WHERE MONTH(occasion)='" + matchCommand.group(2) +
                    "' AND YEAR(occasion) = '" + matchCommand.group(1) + "';");
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                throw new ProgramException("did not make any " + matchCommand.group(3) + " expenses in this month.");
            }
            System.out.println(resultSet.getInt(1));
        }
    }


    /**
     * Prints all personal expenses from all days
     * the syntax is: print
     * @param statement of creation of connection
     * @throws SQLException if could not find a match in the db
     */
    private static void printAll(Statement statement) throws SQLException {
        ResultSet set = statement.executeQuery(SELECTALL);
        categories = new Categories();
        print(set, categories);
    }


    /**
     * Prints particular expenses, not all of them
     * @param rs the set of the results from the db
     * @param categories object to convert the values of db to the values of an object
     * @throws SQLException if could not find a match in the db
     */
    private static void print(ResultSet rs, Categories categories) throws SQLException {
        while (rs.next()) {
            categories.setId(rs.getInt("id"));
            categories.setRent(rs.getDouble("rent"));
            categories.setFood(rs.getDouble("food"));
            categories.setEntertainment(rs.getDouble("entertainment"));
            categories.setClothes(rs.getDouble("clothes"));
            categories.setBills(rs.getDouble("bills"));
            categories.setHobby(rs.getDouble("hobby"));
            categories.setDate(rs.getDate("occasion"));
            System.out.println(categories);
        }
    }
}
