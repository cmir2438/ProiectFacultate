package concurs.client.controllers;

import concurs.model.*;
import concurs.services.IConcursObserver;
import concurs.services.IConcursServer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class InscrieriControler implements IConcursObserver {
    public Button Logout= new Button();
    private IConcursServer server;
    private Stage primaryStage = new Stage();
    public TextField TextNume = new TextField();
    public TextField TextVarsta = new TextField();
    public TextField TextCNP = new TextField();
    public Button BtnInscriere = new Button();
    public ListView listProbe = new ListView();
    public TableView<Proba> ProbeTable = new TableView<>();
    public TableView<DTOInscriere> InscrieriTable = new TableView<>();
    public TableColumn<Proba, Integer> DistantaColumn = new TableColumn<>("Distanta");
    public TableColumn<Proba, Stil> StilColumn = new TableColumn<>("Stil");
    public TableColumn<Proba, Integer> NumarParticipantiColumn = new TableColumn<>("Numar Participanti");
    public TableColumn<DTOInscriere, String > NumeColumn = new TableColumn<>("Nume");
    public TableColumn<DTOInscriere, Integer> VarstaColumn = new TableColumn<>("Varsta");
    public TableColumn<DTOInscriere, String> ProbeColumn = new TableColumn<>("Probe");


    private User user;
    private String idProbaCurenta="";
    ObservableList<DTOInscriere> inscrieri;
    ObservableList<Proba> probe ;


    public  void setService(IConcursServer server) throws ConcursException {
            this.server = server;
            initProbe();

    }


    @FXML
    private void initialize() {
        DistantaColumn.setCellValueFactory(new PropertyValueFactory<Proba,Integer>("distanta"));
        StilColumn.setCellValueFactory(new PropertyValueFactory<Proba, Stil>("stil"));
        NumarParticipantiColumn.setCellValueFactory(new PropertyValueFactory<Proba, Integer>("noP"));

        NumeColumn.setCellValueFactory(new PropertyValueFactory<DTOInscriere,String>("nume"));
        VarstaColumn.setCellValueFactory(new PropertyValueFactory<DTOInscriere, Integer>("varsta"));
        ProbeColumn.setCellValueFactory(new PropertyValueFactory<DTOInscriere,String>("probe"));



    }
    @FXML
    public void handleLogout(ActionEvent actionEvent) {
        logout();
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    private void initProbe() throws ConcursException{
        List<Proba> l =  Arrays.asList(server.getAllProbe());

        probe = FXCollections.observableArrayList(l);
        ProbeTable.setItems(probe);

        listProbe.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listProbe.getItems().removeAll(listProbe.getItems());
        for (Proba p : server.getAllProbe()) {
            listProbe.getItems().add(p.getDistanta()+" "+p.getStil().toString());
        }
        ProbeTable.getSelectionModel().selectedItemProperty()
                .addListener(
                        new ChangeListener<Proba>() {
                            @Override
                            public void changed(ObservableValue<? extends Proba> observable, Proba oldValue, Proba newValue) {
                                if(ProbeTable.getSelectionModel().getSelectedItem() != null ){
                                    initInscrieri(ProbeTable.getSelectionModel().getSelectedItem().getID());
                                }
                            }
                        }
                );

    }

    private void initInscrieri(String id) {
        try {
            idProbaCurenta = id;
            List<DTOInscriere> lista = Arrays.asList(server.getParticipantiByProba(id));
            if (lista != null) {
                List<DTOInscriere> l = StreamSupport.stream(lista.spliterator(), false).collect(Collectors.toList());

                inscrieri = FXCollections.observableArrayList(l);
                InscrieriTable.setItems(inscrieri);
            }
        }catch (ConcursException e){
            System.out.println("Aici e o problema");
        }


    }

    public void setUser(User user){
        this.user = user;
    }


    public void adaugaInscriere(ActionEvent actionEvent) {
       try{
           ObservableList<String> attribute = listProbe.getSelectionModel().getSelectedItems();
           if(attribute.size() !=0){
               String probe="";
               for (String s : attribute){
                   probe+= s+"/";

               }
               String nume= TextNume.getText();
               String varsta = TextVarsta.getText();
               String cnp = TextCNP.getText();
               server.saveInscriere(new DTOInscrie(nume, varsta,cnp,probe));
           }
       }catch (Exception ex){

       }



    }

    public void logout() {
        try {
            server.logout(user, this);
        } catch (ConcursException e) {
            System.out.println("Logout error " + e);
        }

    }

    @Override
    public void updateProbe(Proba[] prb) throws ConcursException {
        probe.setAll(prb);

    }


}
