package serveur;

import model.GestionnaireMediatheque;

import java.net.Socket;

public abstract class ServiceHandler extends Service {
    protected static GestionnaireMediatheque gestionnaireStatic;

    public ServiceHandler(Socket clientSocket) {
        super(clientSocket);

    }

    public static void setGestionnaireStatic(GestionnaireMediatheque gestionnaireStatic) {
        ServiceHandler.gestionnaireStatic = gestionnaireStatic;
    }

    public Socket getClientSocket(){
        return getClient();
    }

    public abstract int getPORT();
}
