package bsoft;

import model.Abonne;
import model.Document;

public class SittingBull {

    public static void envoyerAlerte(Abonne ab, Document doc) {
        // On envoie un mail normalement mais on prévient juste
        System.out.println(" Signal de fumée envoyé à " + ab.getNom() +
                " pour le document n°" + doc.numero());

    }
}
