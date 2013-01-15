

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;

public class Client implements Runnable {
	private BufferedWriter output;
	private BufferedReader input;
	private Socket socket;
	private String message;
	private DefaultListModel<String> users;
	private String ipAdress;
	private int portNmbr;
	private String nick;
	
	private GUI gui;
	public static Client client;
	   
	public Client(String ipAdress, int portNmbr, String nick, GUI gui) {
		
		users = new DefaultListModel<String>();
		this.gui = gui;
		this.ipAdress = ipAdress;
		this.portNmbr = portNmbr;
		this.nick = nick;
		
	}

	@Override
	public void run() {
		try {
			connect(ipAdress, portNmbr);
			sendNickname(nick);
			//gui.showChat();
			chatting();
		} catch (UnknownHostException e) {
			System.err.println("Unknown host exception: " + e.getMessage());
			System.exit(2);
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
		
	}
	
	public void startProgram(String ipAdress, int portNmbr, String nick) {
	}
	
	public void connect(String address, int port)throws IOException{
		socket = new Socket(address, port);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
		output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()) );
		System.out.println("Streams are good to go!");
	}
	
	public void chatting(){
		try{
			do{
				message = input.readLine();					//Läser in raden som kommer från server
				System.out.println(message);
				if(message.startsWith("MESSAGE:")){			//Servern skickar med en string specifikt för chatt
					String mess = message.substring(message.indexOf(":") + 1);	//Delar på meddelandet efter :
					gui.addText(mess); 				//showText-metoden skickar upp meddelandet på chattfältet
				}else if(message.startsWith("NEW USER:")){
					String mess = message.split(":")[1];	
					gui.addText(mess + " has started chatting!");
					users.addElement(mess);
				}else if(message.startsWith("USER LEFT:")){
					String mess = message.split(":")[1];
					gui.addText(mess + " has logged off.");
					users.removeElement(mess);
				}else if(message.startsWith("NICK:TAKEN")){
					gui.addText("Nickname already taken, please choose another.");
					gui.newNickPopup();
				}else if(message.startsWith("NICK:OK")){
				}else if(message.startsWith("NICKLIST:")) {
					String nickName = message.substring(message.indexOf(":") +1);
					String[] nickList = nickName.split(",");
					addAllUsers(nickList);
				}
			}while(!message.equals("END"));						//Om man skriver END så avslutas chatten

		}catch(IOException notfound){
			gui.addText("Error on recieve..." );
		}
	}
	
	public void addAllUsers(String[] userNames){
		users = new DefaultListModel<String>();
		for(int i = 0; i < userNames.length; i++) {
			System.out.println(i + " : " + userNames[i]);
			users.addElement(userNames[i]);
		}
	}
	
	
	public void sendMessage(String message){
			try{
				output.write("MESSAGE:"+message+"\n");				//Skickar ett meddelande
				output.flush();									//Spolar toaletten :)
			}catch (IOException e){
				gui.addText("Can't send message..."); 
			}
	}
	
	public void sendNickname(String message){
		try{
			output.write("NICK:"+message+"\n");				//Skickar ett meddelande
			output.flush();									//Spolar toaletten :)
		}catch (IOException e){
			gui.addText("Can't send nickname..."); 
		}
	}

	public void close(){
		try {
			input.close();
			output.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Failed to close.");
		}
	}
}
