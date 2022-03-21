package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT extends Thread {
	
	private boolean isActive = true;
	private int numClient=0;
	
	public static void main(String[] args) {
		
		new ServeurMT().start();
	}
	
	
	@Override
	public void run() {
		try {
			
			System.out.println("Démarrage du serveur ...");
			ServerSocket serverSocket = new ServerSocket(1234);
			
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
				
				while(true) {
					String req = br.readLine();
					String rep ="Length :"+ req.length();
					pw.println(rep);
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		}
	}

}
