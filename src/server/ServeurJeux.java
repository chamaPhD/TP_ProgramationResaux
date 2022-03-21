package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServeurJeux extends Thread {
	
	private boolean isActive = true;
	private int numClient=0;
	private int nombreSecret;
	private boolean fin;
	private String gagnant;
	
	public static void main(String[] args) {
		
		new ServeurJeux().start();
	}
	
	
	@Override
	public void run() {
		try {
			
			System.out.println("Démarrage du serveur Jeux...");
			ServerSocket serverSocket = new ServerSocket(1234);
			nombreSecret = new Random().nextInt(1000);
			
			while(isActive) {
				
				System.out.println("Waiting for new Clients !!");

				Socket socket = serverSocket.accept();
				++numClient;
				new Conversation(socket,numClient).start();
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	class Conversation extends Thread{
		
		private Socket socketClient;
		private int numClient;
		public Conversation(Socket socketClient, int numClient) {
			this.socketClient = socketClient;
			this.numClient = numClient;
		}
		
		 @Override
		public void run() {
			
			 try {
				InputStream is = socketClient.getInputStream();
				InputStreamReader isr = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(isr);
				
				PrintWriter pw = new PrintWriter(socketClient.getOutputStream(),true);
				
				String ipClient = socketClient.getRemoteSocketAddress().toString();
				
				pw.println("Bien Venue, vous etes le client num : " + numClient);
				System.out.println("Connexion de client num : " + numClient + ", IP : " + ipClient );
			    pw.println("Devinez le nombre secret .... ?");
				
				while(true) {
					String req = br.readLine();
					int nombre = Integer.parseInt(req);

					System.out.println("Client "+ numClient + " ip :"+ipClient + ", tentative avec le nombre : " + nombre );
					if(fin==false) {
						if(nombre>nombreSecret) {
							pw.println("'Votre nombre est superieure au nombre secret");
						}else if(nombre<nombreSecret) {
							pw.println("Votre nombre est inférieur au nombre secret ");
						}else {
							pw.println("Bravo, Vous avez gagné");
							gagnant = ipClient;
							System.out.println("Le gagnant est Ip Client : "+ gagnant);
							fin = true;
						}
					}else {
						pw.println("Jeux est terminé, le gagnant est + " + gagnant);
					}
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
	}

}
