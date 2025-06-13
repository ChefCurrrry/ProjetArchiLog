package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientEmprunt {
    public static void main(String[] args) {
        try (
                Scanner scanner = new Scanner(System.in)
        ) {
            while (true) {
                System.out.println("\n=== Client Emprunt ===");
                try (
                        //remplacer le localhost par l'adresse IP de la machine qui fait tourner le serveur (le main de la classe LancerServeur)
                        Socket socket = new Socket("localhost", 3000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    System.out.println(in.readLine()); // message d’accueil serveur
                    System.out.print("Entrez idAbonné;idDocument pour l'emprunt (ou taper 'exit' pour quitter) : ");
                    String saisie = scanner.nextLine();

                    if (saisie.equalsIgnoreCase("exit")) {
                        System.out.println("Fermeture du client...");
                        break;
                    }

                    out.println(saisie);
                    String reponse = in.readLine();
                    System.out.println(">> " + reponse);

                } catch (IOException e) {
                    System.err.println("Erreur lors de la connexion au serveur : " + e.getMessage());
                }
            }

        }
    }
}

