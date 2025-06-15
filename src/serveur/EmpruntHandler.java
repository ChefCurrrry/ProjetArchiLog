package serveur;

import exception.EmpruntException;
import model.Abonne;
import model.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;

public class EmpruntHandler extends ServiceHandler {
    private static final int PORT = 3000;

    public EmpruntHandler(Socket socket) {
        super(socket);
    }

    @Override
    public int getPORT() {
        return PORT;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
                PrintWriter out = new PrintWriter(getClientSocket().getOutputStream(), true)
        ) {
            out.println("Bienvenue. Entrez votre numéro abonné et numéro document à emprunter (ex : 1;101)");

            String ligne = in.readLine();
            if (ligne == null) return;

            String[] infos = ligne.split(";");
            if (infos.length != 2) {
                out.println("Format invalide. Format attendu : idAbonné;idDocument");
                return;
            }

            int idAbonne = Integer.parseInt(infos[0]);
            int idDocument = Integer.parseInt(infos[1]);

            Abonne ab = gestionnaireStatic.getAbonne(idAbonne);
            Document doc = gestionnaireStatic.getDocument(idDocument);

            if (ab == null) {
                out.println("Abonné inconnu.");
                return;
            }

            if (doc == null) {
                out.println("Document introuvable.");
                return;
            }

            //  CERTIF GÉRONIMO : vérifier si l’abonné est banni
            if (ab.estBanni()) {
                out.println(" Vous êtes banni jusqu’au " + ab.getDateFinBannissementFormatee());
                return;
            }

            LocalDateTime ancienneDate = doc.getDateEmprunt();
            Abonne dernierEmprunteur = doc.getDernierEmprunteur();

            try {
                doc.emprunter(ab);
                out.println(" Emprunt réussi pour " + ab.getNom());

            } catch (EmpruntException e) {
                out.println(" Échec d'emprunt : " + e.getMessage());

            }

        } catch (IOException e) {
            System.err.println("Erreur communication : " + e.getMessage());
        } finally {
            try {
                getClientSocket().close();
            } catch (IOException e) {
                System.err.println("Erreur fermeture socket : " + e.getMessage());
            }
        }
    }
}
