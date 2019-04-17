package concurs.persistance.jdbc;

import concurs.model.DTOInscriere;
import concurs.model.Inscriere;
import concurs.model.Pair;
import concurs.persistance.IRepositoryInscrieri;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepositoryInscrieri  implements IRepositoryInscrieri {
    private static Connection connection;
    private static final Logger logger = LogManager.getLogger();
    private static JavaDataBaseConnection jdbcUtil;

    public RepositoryInscrieri(Properties prop) {
        jdbcUtil = new JavaDataBaseConnection(prop);
        connection= jdbcUtil.getConnection();
    }

    @Override
    public void  save(Inscriere entity) {
        logger.traceEntry("save Concurs with  idParticipant:{} and idProba:{}",  entity.getID().idParticipant,entity.getID().idProba);
        try(PreparedStatement preStmt=connection.prepareStatement("insert into Concurs values (?,?)")){
            Pair<String, String > id = entity.getID();
            preStmt.setString(1,id.idParticipant);
            preStmt.setString(2, id.idProba);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();


    }

    @Override
    public void delete(Pair<String, String> id) {
        logger.traceEntry("deleting Concurs with  idParticipant:{} and idProba:{}",  id.idParticipant,id.idProba);
        try(PreparedStatement preStmt=connection.prepareStatement("delete from Concurs where IDParticipant=? and IDProba=? ")){
            preStmt.setString(1,id.idParticipant);
            preStmt.setString(2, id.idProba);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public void update(Pair<String, String> id, Inscriere entity) {
        logger.traceEntry("update Concurs with  idParticipant:{} and idProba:{}",  id.idParticipant,id.idProba);
        try(PreparedStatement preStmt=connection.prepareStatement("update  Concurs set IDProba=? where IDParticipant=? and IDProba=? ")){
            preStmt.setString(1,id.idParticipant);
            preStmt.setString(2, id.idProba);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);

        }
        logger.traceExit();
    }

    @Override
    public Inscriere findOne(Pair<String, String> id) {
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Concurs where IDParticipant=? and IDProba=? ")) {
            preStmt.setString(1,id.idParticipant);
            preStmt.setString(2, id.idProba);
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next())
                    return new Inscriere(result.getString(1), result.getString(2));
                return null;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }

    @Override
    public Iterable<Inscriere> getAll() {
        List<Inscriere> concurs = new ArrayList<>();
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement("select * from Concurs ")) {
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()){
                    concurs.add(new Inscriere(result.getString(1), result.getString(2)));
                }

                return concurs;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }

    }


    @Override
    public Iterable<DTOInscriere> findParticipantiByProba(String idProba) {
        List<DTOInscriere> inscrieri = new ArrayList<>();
        logger.traceEntry();
        try(PreparedStatement preStmt=connection.prepareStatement(
                "select distinct Pa.nume, Pa.varsta, Pa.id from Participanti Pa " +
                        "inner  join Concurs C on C.IDParticipant = Pa.id  where C.IDProba = ?")) {
            preStmt.setString(1,idProba);
            try(ResultSet result = preStmt.executeQuery()) {
                while (result.next()){
                    String nume =result.getString(1);
                    int varsta = result.getInt(2);
                    String id = result.getString(3);
                    String probe="";
                    try(PreparedStatement prSt = connection.prepareStatement("select Pb.distanta, Pb.stil " +
                            "from Proba Pb inner join Concurs C2 on Pb.id = C2.IDProba where C2.IDParticipant = ?")){
                        prSt.setString(1,id );
                        try(ResultSet rs = prSt.executeQuery()){

                            while(rs.next()){
                                probe +="Proba "+rs.getString(1) + " metri , stil "+rs.getString(2)+"\n";
                            }
                        }
                    }catch(SQLException ex) {
                        logger.error(ex);
                        System.out.println(ex);
                    }
                    inscrieri.add(new DTOInscriere(nume,varsta,probe));


                }
                logger.info(inscrieri);

                return inscrieri;

            }
        }catch(SQLException ex){
            logger.error(ex);
            return null;
        }
    }
}
