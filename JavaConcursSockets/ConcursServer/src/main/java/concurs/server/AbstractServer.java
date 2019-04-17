package concurs.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket server = null;

    public AbstractServer(int port) {
        this.port = port;
    }

    public void start() throws ServerException{
        try{
            server = new ServerSocket(port);
            while(true) {
                System.out.println("Waiting for clients....");
                Socket client = server.accept();
                System.out.println("Client connected...");
                procesRequest(client);
            }

        }catch ( IOException e){
            throw  new ServerException("Accept server error ", e);
        }
    }

    protected  abstract void procesRequest(Socket client);

    public void stop() throws ServerException{
        try{
            server.close();

        }catch (IOException e){
            throw new ServerException("Clossing server error", e);
        }
    }
}
