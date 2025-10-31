package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//NOUVELLE CLASSE : Thread pour recevoir les messages

public class ThreadReception implements Runnable{
    private static String Name = "";
	private DatagramSocket socket;
    private byte[] buffer = new byte[1024];
    
    public ThreadReception(DatagramSocket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                // Créer un paquet pour la réception
                DatagramPacket paquet = new DatagramPacket(buffer, buffer.length);
                
                // Recevoir le message (bloquant)
                socket.receive(paquet);
                
                // Extraire le message
                String messageRecu = new String(paquet.getData(), 0, paquet.getLength());
                
                // Extraire le nom et le contenu
                String nomExpediteur = "Serveur";
                String contenu = messageRecu;
                
                if (messageRecu.startsWith("[") && messageRecu.contains("] : ")) {
                    int finNom = messageRecu.indexOf("] : ");
                    nomExpediteur = messageRecu.substring(1, finNom);
                    contenu = messageRecu.substring(finNom + 4);
                }
                
                // Afficher le message reçu
                System.out.print("\r");  // Retour au début de la ligne
                System.out.println("💬 " + nomExpediteur + " : " + contenu);
                System.out.print(Name + " > ");  // Réafficher le prompt
                
                // Réinitialiser le buffer
                buffer = new byte[1024];
            }
        } catch (IOException e) {
            if (!socket.isClosed()) {
                System.err.println("Erreur de réception : " + e.getMessage());
            }
        }
    }

}
