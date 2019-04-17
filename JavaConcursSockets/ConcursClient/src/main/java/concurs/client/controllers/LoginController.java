package concurs.client.controllers;


import concurs.model.ConcursException;
import concurs.model.Proba;
import concurs.model.User;
import concurs.services.IConcursObserver;
import concurs.services.IConcursServer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController  {
    private static IConcursServer server;
    private InscrieriControler controler;
    @FXML
    public TextField UserFiled = new TextField();
    @FXML
    public PasswordField PasswordField = new PasswordField();

    Parent mainParent;

    private User userConcurs;

    public void setServer(IConcursServer s){
        server= s;

    }

    public void setParent(Parent p){
        mainParent=p;
    }

    public void login(ActionEvent actionEvent){

        String username = UserFiled.getText();
        String passwd = PasswordField.getText();
        userConcurs = new User(username, passwd);
        try {
            server.login(userConcurs, controler);
            controler.setService(server);
            Stage stage = new Stage();
            stage.setTitle("Application window for " + username);
            stage.setScene(new Scene(mainParent));

            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    controler.logout();
                    System.exit(0);

                }
            });

            stage.show();
            controler.setUser(userConcurs);
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        }catch (ConcursException e){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Problem to login");
            alert.setHeaderText("Authentication failure");
            alert.setContentText("Wrong username or password");
            alert.showAndWait();
        }
    }


    public void setControler(InscrieriControler controler){
        this.controler = controler;
    }

}
