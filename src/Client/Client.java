package Client;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Client {
	public static void main(String argv[]) throws IOException{
		try {
		 // Le client crée un socket UDP
		 DatagramSocket socket = new DatagramSocket();
		 
         Scanner sc = new Scanner(System.in);
         System.out.println(" CLIENT CHAT UDP BIDIRECTIONNEL");
         System.out.print("Entez votre nom d'utilisateur :");
         String Name = sc.nextLine().trim();
         
         // Adresse du serveur (localhost)
         InetAddress adresseServeur = InetAddress.getByName("localhost");
         System.out.println("Tapez vos messages (tapez 'quit' pour quitter)\n");
         
      //AJOUT : Démarrer le thread de réception
         Thread threadReception = new Thread(new ThreadReception(socket));
         threadReception.setDaemon(true);  // Thread daemon (se ferme avec le programme)
         threadReception.start();
         
         
         while (true) {
             System.out.print(Name + " > ");
             String msg = sc.nextLine();
             
          // Vérifier si l'utilisateur veut quitter
             if (msg.trim().equalsIgnoreCase("quit")) {
                 System.out.println("\nDéconnexion...");
                 break;
             }
             
             // Formatage du message avec le nom d'utilisateur
             String messageFormatte = "[" + Name + "] : " + msg;
          
          // Conversion du message en bytes
             byte[] donnees = messageFormatte.getBytes();
             
             // Création du paquet UDP
             DatagramPacket paqueemis = new DatagramPacket(
            		 donnees,
            		 donnees.length,
            		 adresseServeur,
            		 1234
            		 );
             // Envoi du paquet
				socket.send(paqueemis);
             System.out.println("✓ Message envoyé\n"); 
         }
		}catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
