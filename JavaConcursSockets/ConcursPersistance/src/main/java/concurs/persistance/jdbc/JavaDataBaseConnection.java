package concurs.persistance.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JavaDataBaseConnection {
    private static Properties jdbcProps ;
    private static final Logger logger= LogManager.getLogger();
    private static Connection instance=null;

    public JavaDataBaseConnection(Properties prop) {
        jdbcProps = prop;
    }
    private Connection getNewConnection(){
        logger.traceEntry();

        String driver=jdbcProps.getProperty("concurs.jdbc.driver");
        String url=jdbcProps.getProperty("concurs.jdbc.url");

        logger.info("trying to connect to database ... {}",url);
        Connection con=null;
        try {
            Class.forName(driver);
            logger.info("Loaded driver ...{}",driver);
            con= DriverManager.getConnection(url);
        } catch (ClassNotFoundException e) {
            logger.error(e);
            System.out.println("Error loading driver "+e);
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error getting connection "+e);
        }
        return con;
    }

    public Connection getConnection(){
        logger.traceEntry();
        try {
            if (instance==null || instance.isClosed())
                instance=getNewConnection();

        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(instance);
        return instance;
    }
    public static Boolean CloseConnection(){
        logger.traceEntry();
        try{
            instance.close();
            instance = null;
            logger.info("Connection was closed with succes");
            return true;
        }catch (SQLException e){
            logger.error(e);
            return false;
        }

    }
}
