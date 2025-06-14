package serveur;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;




public class Serveur implements Runnable {
    private ServerSocket listen_socket;
    private Class<? extends Service> uneClasse;

    public Serveur(Class<? extends Service> uneClasse) throws IOException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        this.uneClasse = uneClasse;
        Service s = uneClasse.getConstructor(Socket.class).newInstance(new Socket());
        listen_socket = new ServerSocket(s.getPORT());

    }

    public void run() {
        try {
            System.err.println("Lancement du serveur au port " + this.listen_socket.getLocalPort());
            while (true) {
                Service s = uneClasse.getConstructor(Socket.class).newInstance(listen_socket.accept());
                new Thread(s).start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    protected void finalize() throws Throwable {
        try {
            this.listen_socket.close();
        } catch (IOException e1) {
        }
    }
}

