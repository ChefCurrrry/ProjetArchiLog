package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurReservation {

    public static void main(String[] args) {
        int port = 2000;
        GestionnaireMediatheque gestionnaire = new GestionnaireMediatheque();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println(" Serveur de réservation à démarré sur le port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println(" Client connecté ");
                Thread t = new Thread(new ReservationHandler(clientSocket, gestionnaire));
                t.start();
            }

        } catch (IOException e) {
            System.err.println(" Erreur serveur : " + e.getMessage());
        }
    }
}

