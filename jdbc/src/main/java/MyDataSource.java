import com.mysql.cj.jdbc.MysqlDataSource;

public class MyDataSource {
    public static final String USER = "root";
    public static final String PASSWORD = "123456";
    public static final String DB_NAME = "Test";
    public static final String DB_FULL_URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?max-connections=10";

    private static final MysqlDataSource dataSource;

    static {
        dataSource = new MysqlDataSource();
        dataSource.setDatabaseName(DB_NAME);
        dataSource.setUser(USER);
        dataSource.setPassword(PASSWORD);
        dataSource.setUrl(DB_FULL_URL);
    }

    public static MysqlDataSource getDataSource() {
        return dataSource;
    }

}
