package serveur;

import java.time.LocalDateTime;

public class Livre implements Document {
    private int numero;
    private String titre;
    private int nbPages;

    private boolean estEmprunte = false;

    private Abonne abonneReserve = null;
    private LocalDateTime dateReservation = null;
    private LocalDateTime dateEmprunt = null;


    public Livre(int numero, String titre, int nbPages) {
        this.numero = numero;
        this.titre = titre;
        this.nbPages = nbPages;
    }

    @Override
    public int numero() {
        return numero;
    }

    @Override
    public synchronized void reserver(Abonne ab) throws ReservationException {
        if (estEmprunte) throw new ReservationException("Ce livre est déjà emprunté.");
        if (abonneReserve != null && !reservationExpiree()) throw new ReservationException("Ce livre est réservé par : " + abonneReserve.getNom());
        if (abonneReserve != null && reservationExpiree()) {
            abonneReserve = null;
        }
        abonneReserve = ab;
        dateReservation = LocalDateTime.now();

    }

    @Override
    public synchronized void emprunter(Abonne ab) throws EmpruntException {
        if (estEmprunte) throw new EmpruntException("Ce livre est déjà emprunté.");
        if (abonneReserve != null && !abonneReserve.equals(ab) && !reservationExpiree()) {
            throw new EmpruntException("Ce livre est réservé par un autre abonné.");
        }
        if (abonneReserve != null && reservationExpiree()) {
            abonneReserve = null;
        }
        estEmprunte = true;
        abonneReserve = null;
        dateReservation = null;
        dateEmprunt = LocalDateTime.now();

    }

    @Override
    public synchronized void retourner() {
        estEmprunte = false;
        abonneReserve = null;
        dateReservation = null;
        dateEmprunt = null;

    }

    private boolean reservationExpiree() {
        return dateReservation != null && dateReservation.plusMinutes(1).isBefore(LocalDateTime.now());
    }

    public LocalDateTime getDateEmprunt() {
        return dateEmprunt;
    }



}
