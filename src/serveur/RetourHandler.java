package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class RetourHandler implements Runnable{
    private Socket socket;
    private GestionnaireMediatheque gestionnaire;

    public RetourHandler(Socket socket, GestionnaireMediatheque gestionnaire) {
        this.socket = socket;
        this.gestionnaire = gestionnaire;
    }


    @Override
    public void run() {
        try(
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        )
        {
            out.println("Bienvenue. Entrez le numéro du document à rendre");

            String ligne = in.readLine();
            if (ligne == null) return;

            int idDocument = Integer.parseInt(ligne.trim());

            Document doc = gestionnaire.getDocument(idDocument);



            if (doc == null) {
                out.println(" Document introuvable.");
                return;
            }
                doc.retourner();
                out.println(" Retour réussi !");

        } catch (IOException e) {
            System.err.println("Erreur communication : " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
        }
    }
}
