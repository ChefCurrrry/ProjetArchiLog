package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientRetour {
    public static void main(String[] args) {
        try (
                Scanner scanner = new Scanner(System.in)
        ) {
            while (true) {
                System.out.println("\n=== Client Retour ===");
                try (
                        Socket socket = new Socket("localhost", 4000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
                ) {
                    System.out.println(in.readLine());
                    System.out.print("Entrez idDocument pour rendre (ou taper 'exit' pour quitter) : ");
                    String saisie = scanner.nextLine();

                    if (saisie.equalsIgnoreCase("exit")) {
                        System.out.println("Fermeture du client retour...");
                        break;
                    }

                    out.println(saisie);
                    String reponse = in.readLine();
                    System.out.println(">> " + reponse);

                } catch (IOException e) {
                    System.err.println("Erreur lors de la connexion au serveur de retour : " + e.getMessage());
                }
            }
        }
    }
}
