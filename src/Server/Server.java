package Server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {
	static byte buffer[]=new byte[1024];
	 // Ajout List pour stocker les adresse des client connectés
	static List<InetSocketAddress> ClientConnectes = new ArrayList<>();
	public static void main (String[] args) throws IOException {
		
		try {
			DatagramSocket socketServeur = new DatagramSocket(1234);

			System.out.println("Je suis un serveur en attente des Messages... ");
			
			while(true) {
                // Le serveur attend un paquet UDP
                DatagramPacket paquerecu = new DatagramPacket(buffer, buffer.length);
                socketServeur.receive(paquerecu);
                
                int taille = paquerecu.getLength();
                String msg = new String(paquerecu.getData(), 0, taille);
                
                //recupere l'adresse de client
                InetAddress adrClient =paquerecu.getAddress(); 
                int portClt = paquerecu.getPort();
                InetSocketAddress adresseComplete = new InetSocketAddress(adrClient, portClt);
                
                //verifiest si c'est un nouveau client 
                if(!contientClient(adresseComplete)) {
                	ClientConnectes.add (adresseComplete);
                	 System.out.println("Nouveau client connecté !");
                     System.out.println( adrClient.getHostAddress() + ":" + portClt);
                     //System.out.println(" Total clients : " + ClientConnectes.size());
                }
                
                // Extraire le nom d'utilisateur et le message
                String Name = "Client";
                String contenuMessage = msg;
                
                if (msg.startsWith("[") && msg.contains("] : ")) {
                    int finNom = msg.indexOf("] : ");
                    Name = msg.substring(1, finNom);
                    contenuMessage = msg.substring(finNom + 4);
                }
                
                // Affichage style chat simple
                System.out.println(Name + " > " + contenuMessage);
                
                diffuserMessage(socketServeur, msg, adresseComplete);

                
                // Réinitialiser le buffer
                buffer = new byte[1024];
            }

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	//NOUVELLE MÉTHODE : Vérifie si un client est déjà dans la liste
    private static boolean contientClient(InetSocketAddress adresse) {
        for (InetSocketAddress client : ClientConnectes) {
            if (client.getAddress().equals(adresse.getAddress()) && 
                client.getPort() == adresse.getPort()) {
                return true;
            }
        }
        return false;
    }
    
    // NOUVELLE MÉTHODE : Diffuse le message à tous les clients sauf l'expéditeur
     
    private static void diffuserMessage(DatagramSocket socket, String message, InetSocketAddress expediteur) {
        byte[] donnees = message.getBytes();
        int nbDiffusions = 0;
        
        for (InetSocketAddress client : ClientConnectes) {
            // Ne pas renvoyer le message à l'expéditeur
            if (client.getAddress().equals(expediteur.getAddress()) && 
                client.getPort() == expediteur.getPort()) {
                continue;
            }
            
            try {
                // Créer et envoyer le paquet au client
                DatagramPacket paquet = new DatagramPacket(
                    donnees, 
                    donnees.length, 
                    client.getAddress(), 
                    client.getPort()
                );
                socket.send(paquet);
                nbDiffusions++;
            } catch (IOException e) {
                System.err.println("❌ Erreur envoi à " + client + " : " + e.getMessage());
            }
        }
        
            System.out.println("  Diffusé à " + nbDiffusions + " client(s)\n");
        
    }
}
