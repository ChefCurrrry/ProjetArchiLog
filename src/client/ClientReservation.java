package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientReservation {
    public static void main(String[] args) {
        try (
                Scanner scanner = new Scanner(System.in);
                Socket socket = new Socket("localhost", 2000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("\n=== Client Réservation ===");
            System.out.println(in.readLine()); // message d'accueil du serveur

            System.out.print("Entrez idAbonné;idDocument pour réserver : ");
            String saisie = scanner.nextLine();
            out.println(saisie);

            String reponse;
            while ((reponse = in.readLine()) != null) {
                System.out.println(">> " + reponse);

                // Sitting Bull
                if (reponse.contains("Sitting Bull")) {
                    Scanner sc = new Scanner(System.in);
                    System.out.print("> ");
                    String choix = sc.nextLine();
                    out.println(choix);
                }
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur de réservation : " + e.getMessage());
        }
    }
}
