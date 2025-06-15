package serveur;

import model.Abonne;
import model.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class RetourHandler extends ServiceHandler{
    private static int PORT = 4000;

    @Override
    public int getPORT() {
        return PORT;
    }
    public RetourHandler(Socket socket ) {
        super(socket);
    }


    @Override
    public void run() {
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
                PrintWriter out = new PrintWriter(getClientSocket().getOutputStream(), true)
        )
        {
            out.println("Bienvenue. Entrez le numéro du document à rendre");

            String ligne = in.readLine();
            if (ligne == null) return;

            int idDocument = Integer.parseInt(ligne.trim());

            Document doc = gestionnaireStatic.getDocument(idDocument);



            if (doc == null) {
                out.println(" Document introuvable.");
                return;
            }
            if (doc.getDateEmprunt() == null) {
                out.println("ERREUR : Document non emprunté");
                return;
            }
            doc.retourner();
            out.println(" Retour réussi !");

            //Geronimo
            Random rand = new Random();
            boolean degrade = rand.nextDouble() < 0.3; // test au hasard document dégradé
            //doc.setDateEmprunt(LocalDateTime.now().minusHours(3)); // test retard + bannissement GERONIMO

            LocalDateTime dateEmprunt = doc.getDateEmprunt();
            boolean retard = false;

            if (dateEmprunt != null) {
                Duration duree = Duration.between(dateEmprunt, LocalDateTime.now());
                retard = duree.toHours() > 1;
                System.out.println(doc.getDernierEmprunteur());
                System.out.println("Délai d'emprunt : " + duree.toMinutes() + " minutes");
            }

            Abonne ab = doc.getDernierEmprunteur();
            if (ab != null && (degrade || retard)) {
                ab.bannirPourUnMois();
                out.println("⚠️ Abonné " + ab.getNom() + " banni jusqu’au " +
                        ab.getDateFinBannissementFormatee() + " (cause : " +
                        (retard ? "retard" : "") + (retard && degrade ? " + " : "") +
                        (degrade ? "dégradation" : "") + ")");
            }
            out.println("OK : Document retourné avec succès");

        } catch (IOException e) {
            System.err.println("Erreur communication : " + e.getMessage());
        }finally {
            try {
                getClientSocket().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
