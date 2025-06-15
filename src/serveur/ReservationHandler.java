package serveur;

import exception.ReservationException;
import model.Abonne;
import model.Document;

import java.io.*;
import java.net.Socket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReservationHandler extends ServiceHandler {
    private static final int PORT = 2000;

    @Override
    public int getPORT() {
        return PORT;
    }

    public ReservationHandler(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(getClientSocket().getInputStream()));
                PrintWriter out = new PrintWriter(getClientSocket().getOutputStream(), true)
        ) {
            out.println("Bienvenue. Entrez votre numéro abonné et numéro document à réserver (ex : 1;101)");

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

            if (ab.estBanni()) {
                out.println(" Vous êtes actuellement banni jusqu’au " + ab.getDateFinBannissementFormatee());
                return;
            }

            if (doc == null) {
                out.println("Document introuvable.");
                return;
            }

            //Geronimo
            if(ab.estBanni()){
                out.println("ERREUR : vous êtes banni jusqu’au " + ab.getDateFinBannissementFormatee());
                return;
            }

            Abonne abReserve = doc.getReservePar();
            LocalDateTime expiration = doc.getExpirationReservation();

            //  Certification Grand Chaman
            if (abReserve != null && !abReserve.equals(ab) && expiration != null) {
                long secondesRestantes = Duration.between(LocalDateTime.now(), expiration).getSeconds();
                if (secondesRestantes <= 30 && secondesRestantes > 0) {
                    out.println("Ce document est encore réservé par un autre abonné mais va bientôt se libérer...");
                    out.println(" Musique céleste en cours... Merci de patienter pendant " + secondesRestantes + " secondes...");

                    try {
                        Thread.sleep(secondesRestantes * 1000L);
                    } catch (InterruptedException e) {
                        out.println("Une force céleste vous a interrompu.");
                        Thread.currentThread().interrupt();
                        return;
                    }

                    abReserve = doc.getReservePar(); // re-vérifie après l’attente
                    if (abReserve == null && !doc.estEmprunte()) {
                        try {
                            doc.reserver(ab);
                            out.println(" Réservation validée après la patience céleste ! Document réservé jusqu'à " + ab.getHeureExpirationReservation().format(DateTimeFormatter.ofPattern("HH'h'mm")) );
                        } catch (ReservationException e) {
                            out.println(" Échec après l’attente : " + e.getMessage());
                        }
                        return;
                    } else {
                        out.println("Un autre guerrier a été plus rapide après la transe du chaman");
                        return;
                    }
                }
            }

            // SITTING BULL
            if (abReserve != null && !abReserve.equals(ab) && expiration != null) {
                String fin = expiration.format(DateTimeFormatter.ofPattern("HH'h'mm"));
                out.println("ERREUR : Ce document est réservé jusqu’à " + fin + ". Voulez-vous activer l’alerte Sitting Bull ? (oui/non)");

                String reponse = in.readLine();
                if (reponse != null && reponse.equalsIgnoreCase("oui")) {
                    doc.ajouterAlerte(ab);
                    out.println("Alerte activée ! Vous recevrez un signal de fumée au retour.");
                } else {
                    out.println("Alerte non activée.");
                }
                return;
            }

            //  Réservation normale
            try {
                doc.reserver(ab);
                out.println(" Réservation réussie ! Réservation valable jusqu'à " + ab.getHeureExpirationReservation().format(DateTimeFormatter.ofPattern("HH'h'mm")));
            } catch (ReservationException e) {
                out.println(" Échec réservation : " + e.getMessage());
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
