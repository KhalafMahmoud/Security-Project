
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingUtilities;

public class ClientJoinListener implements ActionListener {

	public ClientJoinListener() {
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println(e.getActionCommand());
		if (e.getActionCommand().equals("Login")) {
			try {
				String userName = (String) JOptionPane.showInputDialog(null, "Please Enter Your Username", "Username",
						JOptionPane.PLAIN_MESSAGE, null, null, "");

				// String password = (String)JOptionPane.showInputDialog(null,
				// "Please Enter Your Password",
				// "Password", JOptionPane.PLAIN_MESSAGE, null, null,"");

				String password = "";
				JPasswordField passwordField = new JPasswordField();
				passwordField.setEchoChar('*');
				Object[] obj = { "Please enter the password:\n\n", passwordField };
				Object stringArray[] = { "OK", "Cancel" };
				if (JOptionPane.showOptionDialog(null, obj, "Please Enter Your Password", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.PLAIN_MESSAGE, null, null, obj) == JOptionPane.OK_OPTION)
					for (int i = 0; i < passwordField.getPassword().length; i++) {
						password += passwordField.getPassword()[i];
					}

				if (userName == null || userName.isEmpty() || password == null || password.isEmpty())
					return;

				Client client = new Client(6000, userName, password);
				client.start();

				// String serverName = (String)JOptionPane.showInputDialog(null,
				// "Please Enter Server Number You Want To Join(1/2)",
				// "Server Number", JOptionPane.PLAIN_MESSAGE, null, null, "");

				final DummyRunnable runnable = new DummyRunnable();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						ClientStartWindow testThreadingAndGUI = new ClientStartWindow(userName, client, runnable);

					}
				});
				new Thread(runnable).start();
			} catch (CannotAuthenticateUserException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Server Error!!");
			}
		}

		if (e.getActionCommand().equals("Register")) {
			String userName = (String) JOptionPane.showInputDialog(null, "Please Enter Your Username", "Username",
					JOptionPane.PLAIN_MESSAGE, null, null, "");
			
//			String password = (String) JOptionPane.showInputDialog(null, "Please Enter Your Password", "Password",
//					JOptionPane.PLAIN_MESSAGE, null, null, "");
			
			String password = "";
			JPasswordField passwordField = new JPasswordField();
			passwordField.setEchoChar('*');
			Object[] obj = {"Please enter the password:\n\n", passwordField};
			Object stringArray[] = {"OK","Cancel"};
			if (JOptionPane.showOptionDialog(null, obj, "Please Enter Your Password",
			JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, obj) == JOptionPane.OK_OPTION)
			for(int i = 0; i < passwordField.getPassword().length; i++){
				password += passwordField.getPassword()[i];
			}
			System.out.println(password);

			if (userName == null || userName.isEmpty() || password == null || password.isEmpty())
				return;

			try {
				User user = new User(userName, password);
				JOptionPane.showMessageDialog(null, "User Created <3");
			} catch (UsernameAlreadyExistsException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage());
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Server Error!!");
			}
		}

	}

}
