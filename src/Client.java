

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {
	private static final long serialVersionUID = 1L;
	private BufferedWriter output;
	private BufferedReader input;
	private Socket socket;
	private String message;
	private String ipAdress = "localhost";
	private int portNmbr = 5000;
	private DefaultListModel<String> users;
	
	private String nickName;
	private boolean uniqueNick = false;
	
	private JTextField userText;
	private JTextArea chatWindow;
	
	public static Client client;
	   
	public Client() {
		
		client = this;
		
	}
	
	public void startProgram() {
		try {
			connect(ipAdress, portNmbr);
		} catch (UnknownHostException e) {
			System.err.println("Unknown host exception: " + e.getMessage());
			System.exit(2);
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
			System.exit(1);
		}
	}
	
	public void connect(String address, int port)throws IOException{
		socket = new Socket(address, port);
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
		output = new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()) );
		System.out.println("Streams are good to go!");
	}

	public void showText(final String message){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				chatWindow.append(message);				//append gör att det skrivs till efter nuvarande text
			}
		});
	}
	
	public void chatting(){
		do{
			try{
				message = input.readLine();					//Läser in raden som kommer från server
				System.out.println(message);
				if(message.startsWith("MESSAGE:")){			//Servern skickar med en string specifikt för chatt
					String mess = message.substring(message.indexOf(":") + 1);	//Delar på meddelandet efter :
					showText("\n " + mess); 				//showText-metoden skickar upp meddelandet på chattfältet
				}else if(message.startsWith("NEW USER:")){
					String mess = message.split(":")[1];	
					showText("\n " + mess + " has started chatting!");
					users.addElement(mess);
				}else if(message.startsWith("USER LEFT:")){
					String mess = message.split(":")[1];
					showText("\n" + mess + " has logged off.");
					users.removeElement(mess);
				}else if(message.startsWith("NICK:TAKEN")){
					showText("\nNickname already taken, please choose another.");
					uniqueNick = false;
				}else if(message.startsWith("NICK:OK")){
					uniqueNick = true;
				}else if(message.startsWith("NICKLIST:")) {
					nickName = message.substring(message.indexOf(":") +1);
					String[] nickList = nickName.split(",");
					addAllUsers(nickList);
				}
			
			}catch(IOException notfound){
				showText("\n Error on recieve..." );
			}
		}while(!message.equals("END"));						//Om man skriver END så avslutas chatten
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
				chatWindow.append("\nCan't send message..."); 
			}
	}
	
	public void sendNickname(String message){
		try{
			output.write("NICK:"+message+"\n");				//Skickar ett meddelande
			output.flush();									//Spolar toaletten :)
		}catch (IOException e){
			chatWindow.append("\nCan't send nickname..."); 
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
