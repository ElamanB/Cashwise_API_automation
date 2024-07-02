import org.testng.annotations.Test;
import utilities.DBUtilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrintSQLResults {


    @Test
    public void printResults() throws SQLException {
        Connection connection = DBUtilities.getDBConnection();
        String sqlQuery = "select distinct id, title, price from products where price between 2000 and 5000";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {

            String title = resultSet.getString("title");
            String[] words = title.trim().split("\\s+");
            if (words.length == 3) {
                System.out.println(resultSet.getString("title") + "\t" +
                        resultSet.getString("id") + "\t" +
                        resultSet.getString("price"));

            }

        }
    }

    // if title is QA Engineer up
    // other in lowercase
    @Test
    public void printCategories() throws SQLException {
        Connection connection = DBUtilities.getDBConnection();
        String sqlQuery = "select distinct id, title, description, created from categories where title is not null order by title asc;";
        PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
        ResultSet resultSet = preparedStatement.executeQuery();

        int i = 0;
        String oldTitle;
        while (resultSet.next()) {
            i++;
            if (resultSet.getString("title") != null && resultSet.getString("description") != null) {
                if (resultSet.getString("title").equals("QA Engineer")) {
                    System.out.println(resultSet.getString("id") + "\t" +
                            resultSet.getString("title").toUpperCase() + "\t" +
                            resultSet.getString("description") + "\t" +
                                    resultSet.getString("created").substring(0, 10));
                } else {
                    System.out.println(resultSet.getString("id") + "\t" +
                            resultSet.getString("title").toLowerCase() + "\t" +
                            resultSet.getString("description") + "\t" +
                            resultSet.getString("created").substring(0, 10));
                }

            }
        }


    }
}
