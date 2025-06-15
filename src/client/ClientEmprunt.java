package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientEmprunt {
    public static void main(String[] args) {
        try (
                Scanner scanner = new Scanner(System.in);
                Socket socket = new Socket("localhost", 3000);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            System.out.println("\n=== Client Emprunt ===");
            System.out.println(in.readLine()); // message d’accueil serveur

            System.out.print("Entrez idAbonné;idDocument pour l'emprunt : ");
            String saisie = scanner.nextLine();
            out.println(saisie);

            String reponse;
            while ((reponse = in.readLine()) != null) {
                System.out.println(">> " + reponse);
            }

        } catch (IOException e) {
            System.err.println("Erreur lors de la connexion au serveur : " + e.getMessage());
        }
    }
}
