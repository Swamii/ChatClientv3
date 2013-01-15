import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class GUI extends JFrame {

	private JTextField userText;
	private JTextArea chatWindow;
	private Client client;
	private final int WIDTH = 640;
	private final int HEIGHT = 480;

	public GUI() {
		client = Client.client;
		initGUI();
	}

	private void initGUI() {
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
		
		chatWindow = new JTextArea();
		
		JPanel chatPanel = new JPanel(new BorderLayout());
		chatPanel.add(userText, BorderLayout.SOUTH);
		chatPanel.add(new JScrollPane(chatWindow), BorderLayout.CENTER);
		
		JList<String> userList = new JList<String>();
		JScrollPane userWindow = new JScrollPane(userList);
		
		setLayout(new BorderLayout());
		
		add(chatPanel, BorderLayout.CENTER);
		add(userWindow, BorderLayout.EAST);
		
		
		
	}
	
	
	public void showChat() {
		
		setTitle("Chat Client!");
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
	}
}
