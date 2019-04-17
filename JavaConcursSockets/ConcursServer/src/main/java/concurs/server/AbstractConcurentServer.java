package concurs.server;

import java.net.Socket;

public abstract class AbstractConcurentServer extends AbstractServer {

    public AbstractConcurentServer(int port) {
        super(port);
    }

    @Override
    protected void procesRequest(Socket client) {
        Thread tw = createWorker(client);
        tw.start();
    }
    protected abstract  Thread createWorker(Socket client);

}
