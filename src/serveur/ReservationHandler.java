package serveur;

import java.io.*;
import java.net.Socket;

public class ReservationHandler implements Runnable {
    private Socket socket;
    private GestionnaireMediatheque gestionnaire;

    public ReservationHandler(Socket socket, GestionnaireMediatheque gestionnaire) {
        this.socket = socket;
        this.gestionnaire = gestionnaire;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Bienvenue. Entrez votre numéro abonné et numéro document à réserver (ex : 1;101)");

            String ligne = in.readLine();
            if (ligne == null) return;

            String[] infos = ligne.split(";");
            if (infos.length != 2) {
                out.println(" Format invalide.");
                return;
            }

            int idAbonne = Integer.parseInt(infos[0]);
            int idDocument = Integer.parseInt(infos[1]);

            Abonne ab = gestionnaire.getAbonne(idAbonne);
            Document doc = gestionnaire.getDocument(idDocument);

            if (ab == null) {
                out.println(" Abonné inconnu.");
                return;
            }

            if (doc == null) {
                out.println(" Document introuvable.");
                return;
            }

            try {
                doc.reserver(ab);
                out.println(" Réservation réussie !");
            } catch (ReservationException e) {
                out.println(" Échec réservation : " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("Erreur communication : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
