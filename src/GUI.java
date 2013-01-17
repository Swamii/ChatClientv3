import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class GUI extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	JList<String> userList;
	JScrollPane userWindow;
	
	private Client client;
	private final int WIDTH = 640;
	private final int HEIGHT = 480;

	public GUI() {
		initGUI();
	}
	
	public void setClient(final Client client) {
		this.client = client;
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if (userText.getText().length() == 0) {
							return; }
						else {
							client.sendMessage(userText.getText());
							userText.setText("");
						}
					}
				});
	}

	private void initGUI() {
		
		JMenuBar menubar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem connect = new JMenuItem("Connect");
		JMenuItem exit = new JMenuItem("Exit");
		
		file.add(connect);
		file.add(exit);
		menubar.add(file);
		add(menubar);
		setJMenuBar(menubar);
		
		userText = new JTextField();
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						if (userText.getText().length() == 0) {
							return; }
						else {
							client.sendMessage(userText.getText());
							userText.setText("");
						}
					}
				});
		userText.setEditable(false);
		
		chatWindow = new JTextArea();
		chatWindow.setEditable(false);
		
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.add(userText, BorderLayout.SOUTH);
		chatPanel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		
		userList = new JList<String>();
		userWindow = new JScrollPane(userList);
		userWindow.setPreferredSize(new Dimension(150, 500));
		
		setLayout(new BorderLayout());
		
		add(chatPanel, BorderLayout.CENTER);
		add(userWindow, BorderLayout.EAST);
		
		setTitle("Chat Client!");
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		connectPopup();
		
	}
	
	public void updateUsers(final DefaultListModel<String> users) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userList.setModel(users);
				userWindow.revalidate();
				userWindow.repaint();				
			}
		});
	}
	
	public void enableChatting() {
		userText.setEditable(true);
	}
	
	public void addText(final String message){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				chatWindow.append(message + "\n");				//append gör att det skrivs till efter nuvarande text
			}
		});
	}
	
	public void newNickPopup() {
		String newNick;
		do {
			newNick = JOptionPane.showInputDialog("Nick name was taken, please enter another one");
		} while (newNick == null); 
		
		client.sendNickname(newNick);

	}
	
	public void connectPopup() {
		new ConnectPopup(this);
	}
	
}
