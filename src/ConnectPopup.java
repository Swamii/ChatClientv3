import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;


public class ConnectPopup extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField ipField;
	private JTextField nickField;
	private JTextField portField;
	private GUI gui;
	
	private static ConnectPopup connectPopupInstance = null;

	/**
	 * Create the dialog.
	 */
	public ConnectPopup(GUI gui) {
		
		this.gui = gui;
		
		setTitle("Connect");
		setBounds(100, 100, 250, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		JLabel lblIpAddress = new JLabel("IP Address:");
		
		ipField = new JTextField();
		ipField.setColumns(10);
		
		JLabel lblNickName = new JLabel("Nick name:");
		
		nickField = new JTextField();
		nickField.setColumns(10);
		
		JLabel lblPort = new JLabel("Port:");
		
		portField = new JTextField();
		portField.setText("5000");
		portField.setColumns(10);
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblIpAddress)
						.addComponent(lblPort)
						.addComponent(lblNickName))
					.addGap(12)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(portField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(ipField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(nickField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(17, Short.MAX_VALUE))
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIpAddress)
						.addComponent(ipField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPort)
						.addComponent(portField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPanel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNickName)
						.addComponent(nickField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(11, Short.MAX_VALUE))
		);
		contentPanel.setLayout(gl_contentPanel);
		{
			ButtonListener buttonListener = new ButtonListener();
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Connect");
				okButton.setActionCommand("connect");
				okButton.addActionListener(buttonListener);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("cancel");
				cancelButton.addActionListener(buttonListener);
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("connect")) {
				System.out.println("connect!");
				if (portField.getText().equals("") || nickField.equals("") || ipField.equals("")) {
					return;
				}
				final String ip = ipField.getText();
				final int port = Integer.parseInt(portField.getText());
				final String nick = nickField.getText();
				dispose();
				setVisible(false);
				
				Client client = new Client(ip, port, nick, gui);
				gui.setClient(client);
				
				Thread t = new Thread(client, "Client!");
				t.start();


			} else {
				System.out.println("Cancel!");
				dispose();
				setVisible(false);
			}
		}
	}
}
