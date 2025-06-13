package serveur;

import java.net.Socket;

public class HandlerFactory {
    public static ServiceHandler createHandler(int port, Socket socket) {
        return switch (port) {
            case 2000 -> new ReservationHandler(socket);
            case 3000 -> new EmpruntHandler(socket);
            case 4000 -> new RetourHandler(socket);
            default -> throw new IllegalArgumentException("Port non reconnu : " + port);
        };
    }
}

