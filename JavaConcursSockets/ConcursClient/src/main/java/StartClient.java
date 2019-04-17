import concurs.client.controllers.InscrieriControler;
import concurs.client.controllers.LoginController;
import concurs.network.proxy.ConcursServerProxy;
import concurs.services.IConcursServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartClient extends Application {

    private Stage primaryStage;

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws  Exception{
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartClient.class.getResourceAsStream("/clientServer.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find clientServer.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("client.server.host", defaultServer);
        int serverPort = defaultPort;


        try {
            serverPort = Integer.parseInt(clientProps.getProperty("client.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }

        IConcursServer server = new ConcursServerProxy(serverIP, serverPort);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        Parent root = loader.load();

        LoginController ctrl = loader.<LoginController>getController();
        ctrl.setServer(server);


        FXMLLoader cloader = new FXMLLoader(getClass().getResource("/views/Inscrieri.fxml"));
        Parent croot=cloader.load();


        InscrieriControler concursCtrl =
                cloader.<InscrieriControler>getController();

        ctrl.setControler(concursCtrl);
        ctrl.setParent(croot);

        primaryStage.setTitle("MPP chat");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }


}
