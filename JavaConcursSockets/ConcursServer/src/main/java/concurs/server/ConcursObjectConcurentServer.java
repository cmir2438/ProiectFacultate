package concurs.server;

import concurs.network.proxy.ConcursClientWorker;
import concurs.services.IConcursServer;

import java.net.Socket;

public class ConcursObjectConcurentServer extends AbstractConcurentServer {

    private IConcursServer concursServer;

    public ConcursObjectConcurentServer(int port, IConcursServer concursServer) {
        super(port);
        this.concursServer = concursServer;
    }

    @Override
    protected Thread createWorker(Socket client) {
        ConcursClientWorker worker = new ConcursClientWorker(concursServer, client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
