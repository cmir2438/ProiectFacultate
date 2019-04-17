package concurs.network.proxy;

import concurs.model.*;
import concurs.network.objectprotocol.*;
import concurs.services.IConcursObserver;
import concurs.services.IConcursServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class ConcursClientWorker implements Runnable, IConcursObserver {
    private IConcursServer server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    private String probaCurenta;

    public ConcursClientWorker(IConcursServer server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProbe(Proba[] probe) throws ConcursException{
        try{
            sendResponse(new ProbeUpdateResponse(probe));
        }catch (IOException e){
           throw  new ConcursException("Sending error...");
        }
    }


    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Object response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse((Response) response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private  Response handleRequest(Request request){
        Response response = null;
        if(request instanceof LoginRequest){
            LoginRequest logReq= (LoginRequest) request;
            try{
                server.login(logReq.getUser(), this);
                return  new OKResponse();
            }catch (ConcursException ex){
                connected = false;
                return  new ErrorResponse(ex.getMessage());
            }
        }
        if(request instanceof LogoutRequest){
            LogoutRequest logReq = (LogoutRequest) request;
            try{
                server.logout(logReq.getUser(), this);
                connected = false;
                return  new OKResponse();
            }catch (ConcursException ex){
                return new ErrorResponse(ex.getMessage());
            }

        }

        if(request instanceof GetInscrieriRequest){
            GetInscrieriRequest req = (GetInscrieriRequest) request;
            probaCurenta= req.getInscriere();
            try{
                return (new GetInscrieriResponse(server.getParticipantiByProba(probaCurenta)));
            }catch ( ConcursException e){
                return  new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof GetProbeRequest){
            try{
                return new GetProbeResponse(server.getAllProbe());
            }catch (ConcursException e){
                return  new ErrorResponse(e.getMessage());
            }
        }
        if(request instanceof InscriereRequest){
            InscriereRequest req = (InscriereRequest) request;
            try{
                server.saveInscriere(req.getInscriere());
                return  new OKResponse();
            }catch (ConcursException e){
                return  new ErrorResponse(e.getMessage());
            }
        }
        return response;
    }

    private void sendResponse (Response response) throws  IOException{
        output.writeObject(response);
        output.flush();
    }


}
