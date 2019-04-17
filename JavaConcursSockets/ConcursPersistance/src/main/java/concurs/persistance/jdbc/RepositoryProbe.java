package concurs.persistance.jdbc;

import concurs.model.Proba;
import concurs.model.Stil;
import concurs.persistance.IRepositoryProbe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryProbe implements IRepositoryProbe {
    private static Connection connection;
    private static final Logger logger = LogManager.getLogger();
    private static JavaDataBaseConnection jdbcUtil;

    public RepositoryProbe(Properties prop) {
        jdbcUtil= new JavaDataBaseConnection(prop);
        connection= jdbcUtil.getConnection();
    }

    @Override
    public Proba findOneByDistantaStil(int distanta, String stil) {
        logger.traceEntry();
        try (PreparedStatement preStmt = connection.prepareStatement("select * from Proba where distanta=? and stil =?")) {
            preStmt.setInt(1, distanta);
            preStmt.setString(2, stil);
            try (ResultSet result = preStmt.executeQuery()) {
                if (result.next())
                    return new Proba(result.getString(1), result.getInt(2), Stil.valueOf(result.getString(3)), result.getInt(4));
                return null;

            }
        } catch (SQLException ex) {
            logger.error(ex);
            return null;
        }
    }

    @Override
    public void save(Proba entity) {
        logger.traceEntry("save Proba with  idProba:{} ",  entity.getID());
        try(PreparedStatement preStmt=connection.prepareStatement("insert into Proba values (?,?,?,?)")){
            preStmt.setString(1,entity.getID());
            preStmt.setInt(2, entity.getDistanta());
            preStmt.setString(3,entity.getStil().toString());
            preStmt.setInt(4,entity.getNoP());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public void delete(String id) {
        logger.traceEntry("deleting Proba with  idProba:{}",  id);
        try(PreparedStatement preStmt=connection.prepareStatement("delete from Proba where  id=? ")){
            preStmt.setString(1, id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public Proba findOne(String id) {
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Proba where id=? ")) {
            preStmt.setString(1,id);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next())
                    return new Proba(result.getString(1), result.getInt(2), Stil.valueOf(result.getString(3)), result.getInt(4) );
                return null;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }

    @Override
    public void update(String id, Proba entity) {
        logger.traceEntry("update Proba with  idProba:{}", id);
        try(PreparedStatement preStmt=connection.prepareStatement("update Proba set distanta=? , stil= ?, noParticipanti= ? where id=? ")){
            preStmt.setString(4,id);
            preStmt.setInt(1, entity.getDistanta());
            preStmt.setString(2, entity.getStil().toString());
            preStmt.setInt(3, entity.getNoP());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public Iterable<Proba> getAll() {
        List<Proba> probe = new ArrayList<>();
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Proba")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()){
                    probe.add(new Proba(result.getString(1), result.getInt(2), Stil.valueOf(result.getString(3)), result.getInt(4)));
                }

                return probe;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }
}
