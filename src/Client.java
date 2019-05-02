import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.*;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class Client {
	private static volatile Instrumentation globalInstrumentation;
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;
	private Socket socket;
	private String username;
	private String password;
	private int port;
	public boolean updated = false;
	public String updatedMessage = "";

	public static final String SALT = "nour";

	public Client(int port, String username, String password) throws CannotAuthenticateUserException, IOException {
		// hena han3mel el sign in
		this.port = port;
		this.username = username;
		this.password = password;
		// henna we will hash the password el gayelna
		File myDB = new File("users.csv");
		BufferedReader reader = new BufferedReader(new FileReader(myDB));
		String line = "";
		while ((line = reader.readLine()) != null) {
			if (line.startsWith(username)) {

				String saltedPassword = SALT + password;
				String hashedPassword = generateHash(saltedPassword);

				if (line.contains(hashedPassword)) {
					(new File(username)).mkdir();
					return;
				} else {
					throw new CannotAuthenticateUserException();
				}
			}
		}

		throw new CannotAuthenticateUserException();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void start() {
		try {
			socket = new Socket("localhost", port);
		} catch (Exception ec) {
		}

		try {
			sInput = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
		}

		new ListenFromServer().start();

		try {
			sOutput.writeObject(username);
		} catch (IOException eIO) {
		}
	}

	void sendMessage(String msg) {
		try {
			ImageInstance ii = null;
			String content = msg;
			if (msg.contains(".png")) {
				String[] imageText = msg.split(", ");
				content = imageText[0];
				BufferedImage image = ImageIO.read(new File(username + "/" + imageText[1]));

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(image, "png", byteArrayOutputStream);

				byte[] size = ByteBuffer.allocate(4).putInt(byteArrayOutputStream.size()).array();
				byte[] imgeArr = byteArrayOutputStream.toByteArray();
				for(int i = imgeArr.length-50; i<imgeArr.length; i++){
					System.out.print(imgeArr[i] +", ");
				}
				System.out.println("");
				ii = new ImageInstance(imageText[1], size, imgeArr );
			}
			Message m = new Message(content, ii);

			sOutput.writeObject(m);
			sOutput.flush();

		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public static void main(String args[]) throws Exception {
		String userName = "";
		String password = "";

		Scanner scan = new Scanner(System.in);
		if (scan.nextLine().equalsIgnoreCase("Sign in")) {
			System.out.println("Enter your username: ");
			userName = scan.nextLine();
			System.out.println("Enter your password: ");
			password = scan.nextLine();

			try {

				Client client = new Client(6000, userName, password);
				client.start();
				System.out.println("Hello! Welcome to the chat.");
				System.out.println("1. Type the message to send a broadcast to all available clients");
				System.out.println("2. Type @username<space>yourmessage to send a message to a client");
				System.out.println("3. Type show members to see a list of available clients");
				System.out.println("4. Type quit to logoff from the server");
				System.out.println("5. Type deactivate to delete your account");

				while (true) {
					System.out.print("> ");
					String msg = scan.nextLine();

					if (msg.equalsIgnoreCase("QUIT")) {
						client.sendMessage("quit");
						break;
					} else if (msg.equalsIgnoreCase("SHOW MEMBERS")) {
						client.sendMessage("show members");
					} else if (msg.equalsIgnoreCase("DEACTIVATE")) {
						client.sendMessage("deactivate");
					} else {
						client.sendMessage(msg);
					}
				}
				scan.close();
			} catch (CannotAuthenticateUserException e) {
				System.out.println(e.getMessage());
			}

		} else {

			System.out.println("Enter your username: ");
			userName = scan.nextLine();
			System.out.println("Enter your password: ");
			password = scan.nextLine();

			User user = new User(userName, password);
		}

	}

	public boolean getUpdated() {
		return updated;
	}

	public class ListenFromServer extends Thread {

		public void run() {
			while (true) {
				try {
					Message msg = (Message) sInput.readObject();
					if (msg.containsAttachement()) {
						byte[] sizeAr = new byte[4];
						ImageInstance c = msg.getImageInstance();
						sizeAr = c.getSize();
						int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();

						byte[] imageAr = new byte[size];
						imageAr = c.getImgArr();
						for(int i = imageAr.length-50; i<imageAr.length; i++){
							System.out.print(imageAr[i] +", ");
						}
						BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
						//image.flush();
						System.out.println("Received " + image.getHeight() + "x" + image.getWidth() + ": "
								+ System.currentTimeMillis());
						ImageIO.write(image, "png", new File(username + '/' + c.getName()));
						//ImageIO.write(image, "png", new File("o" + ".png"));
						System.out.println(c.getName());
						updated = true;
						updatedMessage = msg.getContent() + ": " +c.getName();
					} else {
						updated = true;
						updatedMessage = msg.getContent();
					}
				} catch (IOException e) {
					System.out.println("Server has closed the connection");
					break;
				} catch (ClassNotFoundException e2) {
				}
			}
		}
	}

	public String getUpdatedMessage() {
		return updatedMessage;
	}

	public void setUpdated(boolean up) {
		updated = up;
	}

	public static String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
			for (int idx = 0; idx < hashedBytes.length; ++idx) {
				byte b = hashedBytes[idx];
				hash.append(digits[(b & 0xf0) >> 4]);
				hash.append(digits[b & 0x0f]);
			}
		} catch (NoSuchAlgorithmException e) {
			// handle error here.
		}

		return hash.toString();
	}

}
