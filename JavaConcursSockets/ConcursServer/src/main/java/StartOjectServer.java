import concurs.persistance.jdbc.RepositoryInscrieri;
import concurs.persistance.jdbc.RepositoryParticipanti;
import concurs.persistance.jdbc.RepositoryProbe;
import concurs.persistance.jdbc.RepositoryUsers;
import concurs.server.AbstractServer;
import concurs.server.ConcursObjectConcurentServer;
import concurs.server.ServerException;
import concurs.server.ServerImplementation;
import concurs.services.IConcursServer;

import java.io.IOException;
import java.util.Properties;

public class StartOjectServer {
    private static int defaultPort=55555;

    public static void main(String[] args) {
        Properties serverProps = new Properties();
        try{
            serverProps.load(StartOjectServer.class.getResourceAsStream("/concursServer.properties"));
            serverProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find concursServer.properties "+e);
            return;
        }

        RepositoryUsers userRepo = new RepositoryUsers(serverProps);
        RepositoryProbe probeRepo = new RepositoryProbe(serverProps);
        RepositoryParticipanti participantiRepo = new RepositoryParticipanti(serverProps);
        RepositoryInscrieri inscrieriRepo = new RepositoryInscrieri(serverProps);



        IConcursServer concursServer = new ServerImplementation(userRepo, inscrieriRepo, probeRepo, participantiRepo);
        int concursServerPort = defaultPort;
        try{
            concursServerPort =  Integer.parseInt(serverProps.getProperty("concurs.server.port"));
        }catch (NumberFormatException nef){
            System.err.println("Wrong Port Number" + nef.getMessage());
            System.err.println("Using default port "+defaultPort);
        }
        System.out.println("Starting server on port: "+concursServerPort);
        AbstractServer server = new ConcursObjectConcurentServer(concursServerPort,concursServer );
        try{
            server.start();
        }catch (ServerException e){
            System.err.println("Error starting the server" + e.getMessage());
        }
    }
}
