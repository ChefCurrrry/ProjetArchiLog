package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurCommun {

    public static void main(String[] args) {
        int portReservation = 2000;
        int portEmprunt = 3000;
        int portRetour = 4000;

        GestionnaireMediatheque gestionnaire = new GestionnaireMediatheque();

        // Thread pour les réservations
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(portReservation)) {
                System.out.println("Serveur de réservation démarré sur le port " + portReservation);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[Réservation] Client connecté");
                    new Thread(new ReservationHandler(clientSocket, gestionnaire)).start();
                }
            } catch (IOException e) {
                System.err.println("Erreur serveur réservation : " + e.getMessage());
            }
        }).start();

        // Thread pour les emprunts
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(portEmprunt)) {
                System.out.println("Serveur d'emprunt démarré sur le port " + portEmprunt);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[Emprunt] Client connecté");
                    new Thread(new EmpruntHandler(clientSocket, gestionnaire)).start();
                }
            } catch (IOException e) {
                System.err.println("Erreur serveur emprunt : " + e.getMessage());
            }
        }).start();

        // Thread pour les retours
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(portRetour)) {
                System.out.println("Serveur de retour démarré sur le port " + portRetour);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("[Retour] Client connecté");
                    new Thread(new RetourHandler(clientSocket, gestionnaire)).start();
                }
            } catch (IOException e) {
                System.err.println("Erreur serveur retour : " + e.getMessage());
            }
        }).start();
    }
}
