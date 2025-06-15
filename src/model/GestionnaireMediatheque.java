package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GestionnaireMediatheque {
    private static GestionnaireMediatheque instance;
    private List<Document> documents;
    private List<Abonne> abonnes;

    public GestionnaireMediatheque() {
        this.abonnes = new ArrayList<>();
        this.documents = new ArrayList<>();

        // Création des abonnés
        abonnes.add(new Abonne(1, "Alice", LocalDate.of(2004, 5, 10)));
        abonnes.add(new Abonne(2, "Bob", LocalDate.of(1980, 3, 25)));
        abonnes.add(new Abonne(3, "Charlie", LocalDate.of(2010, 11, 1))); // moins de 16 ans

        // Création des documents
        documents.add(new Livre(101, "Le Petit Prince", 96));
        documents.add(new Livre(102, "1984", 328));
        documents.add(new DVD(201, "Matrix", false));
        documents.add(new DVD(202, "Fight Club", true)); // Adulte
    }

    public Document getDocument(int numero) {
        for (Document d : documents) {
            if (d.numero() == numero) return d;
        }
        return null;
    }

    public Abonne getAbonne(int numero) {
        for (Abonne a : abonnes) {
            if (a.getId() == numero) return a;
        }
        return null;
    }

    public static GestionnaireMediatheque getInstance() {
        if (instance == null) {
            instance = new GestionnaireMediatheque();
        }
        return instance;
    }
}
