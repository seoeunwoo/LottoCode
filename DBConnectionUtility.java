package hello.hellospring.lottoconnection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.hellospring.studentconnection.LinkedAccount.*;

@Slf4j
public class DBConnectionUtility {
    public static Connection getConnection()
    {
        try
        {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get Connection ={}, class = {}", connection, connection.getClass());
            return connection;
        }
        catch (SQLException exception)
        {
            throw new IllegalStateException(exception);
        }
    }
}
