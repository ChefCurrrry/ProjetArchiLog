package model;

import exception.EmpruntException;
import exception.ReservationException;

import java.time.LocalDateTime;

public interface Document {
    int numero();
    void reserver(Abonne ab) throws ReservationException;
    void emprunter(Abonne ab) throws EmpruntException;
    void retourner();

    // Pour Grand Chaman
    Abonne getReservePar();
    LocalDateTime getExpirationReservation();
    boolean estEmprunte();

    // Pour Géronimo
    Abonne getDernierEmprunteur();
    LocalDateTime getDateEmprunt();
    void setDateEmprunt(LocalDateTime dateEmprunt); // Pour tester le banissement en cas de retard

    // Sitting Bull
    void ajouterAlerte(Abonne ab);
    void notifierAlertes();

}
