package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {
    private static volatile CustomDataSource instance;
    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
        instance = this;
    }

    public static CustomDataSource getInstance() {
        if (instance==null) {
            synchronized (CustomDataSource.class) {
                if (instance == null) {
                    Properties properties = new Properties();
                    try {
                        properties.load(CustomDataSource.class.getClassLoader().getResourceAsStream("app.properties"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    instance = new CustomDataSource(
                            properties.getProperty("postgres.driver"),
                            properties.getProperty("postgres.url"),
                            properties.getProperty("postgres.password"),
                            properties.getProperty("postgres.name")
                    );
                }
            }
        }
        return instance;
    }
    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new CustomConnector().getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new SQLException();
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new SQLException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new SQLException();
    }
}
