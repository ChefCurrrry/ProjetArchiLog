package serveur;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurCommun {


    public static void main(String[] args) throws IOException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        GestionnaireMediatheque gestionnaire = new GestionnaireMediatheque();
        ServiceHandler.setGestionnaireStatic(gestionnaire);
        Serveur reserv = new Serveur(ReservationHandler.class);
        new Thread(reserv).start();
        Serveur emprunt = new Serveur(EmpruntHandler.class);
        new Thread(emprunt).start();
        Serveur retour = new Serveur(RetourHandler.class);
        new Thread(retour).start();
    }

}
