import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MonoServ {

    public static void main(String[] args) throws IOException {

        final int port = 4000;
        boolean deconnexionClientDemandee = false;
        char[] bufferEntree = new char[65535];
        String messageRecu = null;
        String reponse = null;
        Outils outils = new Outils();
        System.out.println(outils.getSystemIP().get(0).interfaceType());

        ServerSocket monServerDeSocket = new ServerSocket(port);
        System.out.println("Serveur en fonctionnement.");

        while (true) {
            Socket socketClient = monServerDeSocket.accept();
            System.out.println("Connexion : " + socketClient.getInetAddress().getHostAddress());

            BufferedReader fluxEntree = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            PrintStream fluxSortie = new PrintStream(socketClient.getOutputStream());
            reponse = null;

            while (!deconnexionClientDemandee && socketClient.isConnected()) {
                reponse = null;
                System.out.println("En attente...");
                fluxSortie.println("Requête possible : HELLO / TIME / ECHO + phrase / WHOAREYOU? / WHOAMI? / EXIT");
                fluxSortie.println("Entrez la requête : ");
                int NbLus = fluxEntree.read(bufferEntree);
                messageRecu = new String(bufferEntree, 0, NbLus);

                if (!messageRecu.isEmpty()) {
                    System.out.println("Message reçu : " + messageRecu);
                }

                if (messageRecu.equalsIgnoreCase("exit")) {
                    reponse = "Déconnexion";
                    deconnexionClientDemandee = true;
                } else {
                    messageRecu = new String(bufferEntree, 0, NbLus - 2);
                    System.out.println("Traitement...");

                    if (messageRecu.equalsIgnoreCase("HELLO")) {
                        reponse = "Bienvenue sur le serveur !";
                    }
                    if (messageRecu.equalsIgnoreCase("TIME")) {
                        LocalDateTime ld = LocalDateTime.now();
                        reponse = ld.toString();
                    }
                    if (messageRecu.matches("^ECHO.*") || messageRecu.matches("^echo.*")) {
                        reponse = messageRecu.substring(4);
                    }
                    if (messageRecu.equalsIgnoreCase("WHOAREYOU?")) {
                        InetAddress inetAddress = InetAddress.getLocalHost();
                        reponse = inetAddress.toString();
                    }
                    if (messageRecu.equalsIgnoreCase("WHOAMI?")) {
                        reponse = socketClient.getInetAddress().getHostAddress();
                    }
                }

                fluxSortie.println(reponse);
                System.out.println("\t\t Message envoyé : " + reponse);
            }
            socketClient.close();

        }

    }
}
