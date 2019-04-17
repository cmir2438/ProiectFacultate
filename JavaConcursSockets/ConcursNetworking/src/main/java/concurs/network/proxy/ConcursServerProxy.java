package concurs.network.proxy;

import concurs.model.*;
import concurs.network.objectprotocol.*;
import concurs.services.IConcursObserver;
import concurs.services.IConcursServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ConcursServerProxy implements IConcursServer {
    private String host;
    private int port;

    private IConcursObserver  client;

    private ObjectInputStream input;
    private ObjectOutputStream  output;
    private Socket connection;
    private BlockingDeque<Response> qresponses;
    private volatile boolean finished;

    public ConcursServerProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<Response>();
    }

    @Override
    public void login(User user, IConcursObserver observer) throws ConcursException {
        initializeConnection();
        sendRequest(new LoginRequest(user));
        Response response = readResponse();
        if(response instanceof OKResponse){
            this.client = observer;
            return;
        }
        if(response instanceof ErrorResponse){
            ErrorResponse err =(ErrorResponse) response;
            closeConnection();
            throw  new ConcursException(err.getMessage());
        }
    }

    @Override
    public void logout(User user, IConcursObserver observer) throws ConcursException {
        sendRequest(new LogoutRequest(user));
        Response response= readResponse();
        if(response instanceof OKResponse){
            System.out.println("conexiunea se inchide aici");
            closeConnection();
        }
        if (response instanceof ErrorResponse){
            System.out.println("dar nu pot sa inchid");
            ErrorResponse err=(ErrorResponse)response;
            throw new ConcursException(err.getMessage());
        }


    }

    @Override
    public DTOInscriere[] getParticipantiByProba(String idProba) throws ConcursException {
       sendRequest(new GetInscrieriRequest(idProba));
       Response response = readResponse();
       if( response instanceof ErrorResponse){
           ErrorResponse err=(ErrorResponse)response;
           throw new ConcursException(err.getMessage());
       }
       GetInscrieriResponse inscrieriResponse= (GetInscrieriResponse) response;
        return  inscrieriResponse.getInscrieri();
    }

    @Override
    public Proba[] getAllProbe() throws ConcursException {
        sendRequest(new GetProbeRequest());
        Response response = readResponse();
        if( response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ConcursException(err.getMessage());
        }
        GetProbeResponse probeResponse = (GetProbeResponse) response;
        return probeResponse.getProbe();

    }

    @Override
    public void saveInscriere(DTOInscrie inscrie) throws ConcursException {
        sendRequest( new InscriereRequest(inscrie));
        Response response = readResponse();
        if( response instanceof ErrorResponse){
            ErrorResponse err=(ErrorResponse)response;
            throw new ConcursException(err.getMessage());
        }


    }

    private void initializeConnection() throws  ConcursException{
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();

        }catch (IOException e){
            throw  new ConcursException(e.getMessage());
        }
    }

    private void closeConnection() {
        finished=true;
        try {
            input.close();
            output.close();
            connection.close();
            client=null;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequest(Request request)throws ConcursException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new ConcursException("Error sending object "+e);
        }

    }

    private Response readResponse() throws ConcursException {
        Response response=null;
        try{
            /*synchronized (responses){
                responses.wait();
            }
            response = responses.remove(0);    */
            response=qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void startReader(){
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }
    private void handelUpdate(UpdateResponse update){
        if(update instanceof ProbeUpdateResponse){
            try{
                client.updateProbe(((ProbeUpdateResponse) update).getProbe());
            }catch (ConcursException e){
                e.printStackTrace();
            }
        }

    }
    private class ReaderThread implements  Runnable{
        @Override
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    if (response instanceof UpdateResponse) {
                        handelUpdate((UpdateResponse) response);
                    } else {
                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
