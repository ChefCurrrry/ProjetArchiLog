package serveur;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

public class DVD implements Document {
    private int numero;
    private String titre;

    private boolean adulte;
    private boolean estEmprunte = false;


    private Abonne abonneReserve = null;
    private LocalDateTime dateReservation = null;
    private LocalDateTime dateEmprunt = null;


    public DVD(int numero, String titre, boolean adulte) {
        this.numero = numero;
        this.titre = titre;
        this.adulte = adulte;
    }

    @Override
    public int numero() {
        return numero;
    }

    @Override
    public synchronized void reserver(Abonne ab) throws ReservationException {
        if (adulte && ab.getAge() < 16)
            throw new ReservationException("Ce DVD est réservé aux plus de 16 ans");
        if (estEmprunte) throw new ReservationException("Ce DVD est déjà emprunté.");
        if (abonneReserve != null && !reservationExpiree()) throw new ReservationException("Ce DVD est réservé.");
        if (abonneReserve != null && reservationExpiree()) {
            abonneReserve = null;
        }
        abonneReserve = ab;
        dateReservation = LocalDateTime.now();

    }

    @Override
    public synchronized void emprunter(Abonne ab) throws EmpruntException {
        if (estEmprunte) throw new EmpruntException("Ce DVD est déjà emprunté.");

        if (adulte && !aLAge(ab)) throw new EmpruntException("Vous n'avez pas l'âge requis pour ce DVD (+16 ans).");

        if (abonneReserve != null && !abonneReserve.equals(ab) && !reservationExpiree()) {
            throw new EmpruntException("Ce DVD est réservé par un autre abonné.");
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
        return dateReservation != null && dateReservation.plusHours(1).isBefore(LocalDateTime.now());
    }

    private boolean aLAge(Abonne ab) {
        return Period.between(ab.getDateNaissance(), LocalDate.now()).getYears() > 16;
    }
}
