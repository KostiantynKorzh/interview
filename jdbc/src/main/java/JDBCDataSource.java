import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class JDBCDataSource {

    public static final String TABLE_NAME = "Test_Table";


    private DataSource dataSource;

    public static void main(String[] args) {
        JDBCDataSource test = new JDBCDataSource();
        test.dataSource = MyDataSource.getDataSource();

        test.selectAll();
    }

    private void selectAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            String selectAllQuery = """
                    SELECT * FROM %s
                    """.formatted(TABLE_NAME);
            ResultSet rs = statement.executeQuery(selectAllQuery);
            while (rs.next()) {
                System.out.println("id: " + rs.getInt(1) + ", first name: " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
