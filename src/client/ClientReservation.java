package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientReservation {
    public static void main(String[] args) {
        int port = Integer.parseInt(args[0]);
        try (
                Socket socket = new Socket("localhost", port);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println(in.readLine()); // message d’accueil serveur
            System.out.print("Entrez idAbonné;idDocument pour la réservation : ");
            String saisie = scanner.nextLine();
            out.println(saisie);

            String reponse = in.readLine();
            System.out.println(">> " + reponse);

        } catch (IOException e) {
            System.err.println("Erreur client : " + e.getMessage());
        }
    }
}

