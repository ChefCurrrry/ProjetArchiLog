package serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
                doc.retourner();
                out.println(" Retour réussi !");

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
