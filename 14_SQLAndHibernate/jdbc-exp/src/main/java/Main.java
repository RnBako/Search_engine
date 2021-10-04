import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox";
        String usr = "root";
        String pass = "mtesttest";

        try {
            Connection connection = DriverManager.getConnection(url, usr, pass);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select pl.course_name, count(MONTH(pl.subscription_date))/8 as avg_purchase from purchaseList pl group by pl.course_name");
            while (resultSet.next()) {
                System.out.println(resultSet.getString("course_name") + " - " + resultSet.getString("avg_purchase") );
            }
            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
