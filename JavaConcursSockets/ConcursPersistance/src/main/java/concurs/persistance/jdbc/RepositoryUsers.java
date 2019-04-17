package concurs.persistance.jdbc;

import concurs.model.User;
import concurs.persistance.IRepositoryUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class RepositoryUsers implements IRepositoryUsers {
    private static JavaDataBaseConnection jdbcUtil;
    private static Connection connection;
    private static final Logger logger = LogManager.getLogger();

    public RepositoryUsers(Properties prop) {
        jdbcUtil = new JavaDataBaseConnection(prop);
        connection = jdbcUtil.getConnection();
    }

    @Override
    public User findBy(String user) {
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Users where user=? ")) {
            preStmt.setString(1, user);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next())
                    return new User(result.getString(1), result.getString(2));
                return null;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }

    @Override
    public User findBy(String user, String passwd) {
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Users where user=? and passwd = ?")) {
            preStmt.setString(1, user);
            preStmt.setString(2,passwd);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next())
                    return new User(result.getString(1), result.getString(2));
                return null;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }
}
