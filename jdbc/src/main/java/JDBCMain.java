import java.sql.*;
import java.util.Arrays;

public class JDBCMain {
    public static final String DB_BASE_URL = "jdbc:mysql://localhost:3306";
    public static final String USER = "root";
    public static final String PASSWORD = "123456";
    public static final String DB_NAME = "Test";
    public static final String TABLE_NAME = "Test_Table";
    public static final String TABLE_NAME_2 = "Test_Table_2";
    public static final String DB_FULL_URL = "jdbc:mysql://localhost:3306/" + DB_NAME;

    public static void main(String[] args) throws SQLException {

        DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        JDBCMain JDBCMain = new JDBCMain();
//        JDBCMain.createDatabase();
//        JDBCMain.createTable();
//        JDBCMain.fillTableWithTestData();
//        JDBCMain.selectAll();
//        JDBCMain.selectWhere();
//        JDBCMain.doCallableStatement();
//        JDBCMain.doBatch();
        JDBCMain.doMetadata();

    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_FULL_URL, USER, PASSWORD);
    }

    private void createDatabase() {
        try (Connection connection = DriverManager.getConnection(DB_BASE_URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {
            String createDBSQL = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            statement.execute(createDBSQL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        // try-with-resources
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String createDBSQL = """
                    CREATE TABLE IF NOT EXISTS %s
                    (id INTEGER NOT NULL, first_name VARCHAR(255), PRIMARY KEY (id));
                    """.formatted(TABLE_NAME);
            statement.execute(createDBSQL);
            String createDBSQL2 = """
                    CREATE TABLE IF NOT EXISTS %s
                    (id INTEGER NOT NULL, city VARCHAR(255), PRIMARY KEY (id));
                    """.formatted(TABLE_NAME_2);
            statement.execute(createDBSQL2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillTableWithTestData() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String insertQuery = """
                    INSERT INTO %s VALUES 
                    (1, "First name"),
                    (2, "Second name"),
                    (3, "Third name"),
                    (4, "Fourth name")
                    """.formatted(TABLE_NAME);
            statement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selectAll() {
        try (Connection connection = getConnection();
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

    private void doPreparedStatement() throws SQLException {
        PreparedStatement ps = null;
        try (Connection connection = getConnection()) {
            String query = """
                    SELECT * FROM %s
                    WHERE first_name=?
                    """.formatted(TABLE_NAME);
            ps = connection.prepareStatement(query);
            ps.setString(1, "Second name");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                System.out.println("id: " + rs.getInt(1) + ", first name: " + rs.getString(2));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    // for stored procedures
    private void doCallableStatement() {
        final String sql = "{call findfirstname (?)}";
        try (Connection connection = getConnection()) {
            CallableStatement cs = connection.prepareCall(sql);
            cs.setInt(1, 2);
//            cs.registerOutParameter(2, VARCHAR);
            cs.execute();
            ResultSet rs = cs.getResultSet();
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void doBatch() {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.addBatch("INSERT INTO %s VALUES (23, 'LAldalsd')".formatted(TABLE_NAME));
            statement.addBatch("INSERT INTO %s VALUES (24, 'Bugaga')".formatted(TABLE_NAME));
            statement.addBatch("INSERT INTO %s VALUES (1, 'Kyiv')".formatted(TABLE_NAME_2));
            statement.addBatch("INSERT INTO %s VALUES (2, 'Lviv')".formatted(TABLE_NAME_2));
            int[] updatedColumns = statement.executeBatch();
            System.out.println(Arrays.toString(updatedColumns));
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void doSavepoint() {
        Connection connection = null;
        Savepoint savepoint = null;
        Savepoint namedSavepoint = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            Statement statement1 = connection.createStatement();
            statement1.execute("Some query");
            savepoint = connection.setSavepoint();
            Statement statement2 = connection.createStatement();
            statement2.execute("Some query 2");
            namedSavepoint = connection.setSavepoint("named");
            Statement statement3 = connection.createStatement();
            statement3.execute("Some query 3");
        } catch (SQLException e) {
            try {
                if (savepoint != null) {
                    connection.rollback(savepoint);
                }
                if (namedSavepoint != null) {
                    connection.rollback(namedSavepoint);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private void doMetadata() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            String selectAllQuery = """
                    SELECT * FROM %s
                    """.formatted(TABLE_NAME);
            ResultSet rs = statement.executeQuery(selectAllQuery);
            if (rs.next()) {
                ResultSetMetaData rsmd = rs.getMetaData();
                System.out.println(rsmd.getColumnCount());
                System.out.println(rsmd.getColumnLabel(1));
                System.out.println(rsmd.getScale(1));
                System.out.println(rsmd.isAutoIncrement(1));
            }
            DatabaseMetaData dmd = connection.getMetaData();
            System.out.println("-------------------------");
            System.out.println(dmd.getMaxColumnsInIndex());
            System.out.println(dmd.getDatabaseProductName());
            System.out.println(dmd.getDriverName());
            System.out.println(dmd.getCatalogs());
            System.out.println(dmd.getURL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}