

import java.awt.BorderLayout;
import java.awt.Color;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ClientJoin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JButton login = new JButton("Login");
	private JButton signup = new JButton("Register");
	private String serverName;

	public ClientJoin()
	{
		super("Client Name");

		setContentPane(new JLabel(new ImageIcon("resources/images/clientSWBack.jpg")));
		setBounds(400, 100, 600, 400);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setLayout(null);
		addComponents();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	private void addComponents()
	{
		Color color = new Color(240, 255, 255);
		
		
		add(login);
		login.setBounds(150, 70, 300, 100);
		login.setBackground(color);
		
		add(signup);
		signup.setBounds(150, 230, 300, 100);
		signup.setBackground(color);
	}



	public JButton getServer() { return login; }
	public JButton getCreate() { return signup; }


	public void setServerName(String serverName) { this.serverName = serverName; }
}
