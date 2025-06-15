package serveur;

import model.GestionnaireMediatheque;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class LancerServeur {


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
