package Server;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;

public class Server {
	static byte buffer[]=new byte[1024];
	public static void main (String[] args) throws IOException {
		
		try {
			DatagramSocket socketServeur = new DatagramSocket(1234);
			//socketServeur.bind(address);

			System.out.println("Je suis un serveur en attente des Messages... ");
			
			while(true) {
                // Le serveur attend un paquet UDP
                DatagramPacket paquerecu = new DatagramPacket(buffer, buffer.length);
                socketServeur.receive(paquerecu);
                
                int taille = paquerecu.getLength();
                String msg = new String(paquerecu.getData(), 0, taille);
                
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
                
                // Renvoyer le message au client
                DatagramPacket paqueemis = new DatagramPacket(
                    msg.getBytes(), 
                    msg.length(), 
                    paquerecu.getAddress(), 
                    paquerecu.getPort()
                );
                socketServeur.send(paqueemis);
                
                // Réinitialiser le buffer
                buffer = new byte[1024];
            }

		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
