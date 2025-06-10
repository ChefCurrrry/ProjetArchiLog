package serveur;

public interface Document {
    int numero();
    void reserver(Abonne ab) throws ReservationException;
    void emprunter(Abonne ab) throws EmpruntException;
    void retourner();
}
