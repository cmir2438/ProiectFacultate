package concurs.persistance.jdbc;

import concurs.model.Participant;
import concurs.persistance.RepositoryCRUD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryParticipanti implements RepositoryCRUD<String, Participant> {
    private static Connection connection;
    private static final Logger logger = LogManager.getLogger();
    private static JavaDataBaseConnection jdbcUtil;

    public RepositoryParticipanti(Properties prop) {
        jdbcUtil= new JavaDataBaseConnection(prop);
        connection= jdbcUtil.getConnection();
    }

    @Override
    public void  save(Participant entity) {
        logger.traceEntry("save Participant with  idParticipant:{} ",  entity.getID());
        try(PreparedStatement preStmt=connection.prepareStatement("insert into Participanti values (?,?,?)")){
            preStmt.setString(1,entity.getID());
            preStmt.setString(2, entity.getName());
            preStmt.setInt(3,entity.getVarsta());
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();


    }

    @Override
    public void delete(String id) {
        logger.traceEntry("deleting Participant with  idProba:{}",  id);
        try(PreparedStatement preStmt=connection.prepareStatement("delete from Participanti where  id=? ")){
            preStmt.setString(1, id);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public void update(String id, Participant entity) {
        logger.traceEntry("update Participant with  idParticipant:{}", id);
        try(PreparedStatement preStmt=connection.prepareStatement("update Participanti set nume=? , varsta= ? where id=? ")){
            preStmt.setString(3,id);
            preStmt.setString(1, entity.getName());
            preStmt.setInt(2, entity.getVarsta());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public Participant findOne(String id) {
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Participanti where id=? ")) {
            preStmt.setString(1,id);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next())
                    return new Participant(result.getString(1),result.getString(2), result.getInt(3));
                return null;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }

    @Override
    public Iterable<Participant> getAll() {
        List<Participant> participanti = new ArrayList<>();
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Participanti")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()){
                    participanti.add(new Participant(result.getString(1),result.getString(2), result.getInt(3)));
                }

                return participanti;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }

    }


}
