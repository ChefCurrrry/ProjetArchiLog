package serveur;

import java.net.Socket;

public abstract class Service implements Runnable {

    private final Socket client;

    public Service(Socket client) {
        this.client = client;
    }

    public Socket getClient() {
        return client;
    }
    public abstract int getPORT();

    protected void finalize() throws Throwable {
        client.close();
    }
}
