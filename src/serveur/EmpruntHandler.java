package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class EmpruntHandler extends ServiceHandler {
    private static int PORT = 3000;


    public int getPORT() {
        return PORT;
    }
    public EmpruntHandler(Socket socket ) {
        super(socket);
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
                out.println(" Format invalide.");
                return;
            }

            int idAbonne = Integer.parseInt(infos[0]);
            int idDocument = Integer.parseInt(infos[1]);

            Abonne ab = gestionnaireStatic.getAbonne(idAbonne);
            Document doc = gestionnaireStatic.getDocument(idDocument);

            if (ab == null) {
                out.println(" Abonné inconnu.");
                return;
            }

            if (doc == null) {
                out.println(" Document introuvable.");
                return;
            }

            try {
                doc.emprunter(ab);
                out.println(" Emprunt réussi ! Abonné : " + ab.getNom());
            } catch (EmpruntException e) {
                out.println(" Échec d'emprunt : " + e.getMessage());
            }

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
