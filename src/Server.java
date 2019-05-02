import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private File file;
	private static FileWriter writer;
	private ArrayList<ClientThread> userThreads;
	private static ArrayList<String> userNames = new ArrayList<>();
	private static int uniqueID;
	private int port;
	private ObjectInputStream sInput;
	private ObjectOutputStream sOutput;

	private Socket socket;
	private String username;

	public static FileWriter getWriter() {
		return writer;
	}

	public Server(int port) throws IOException {
		this.port = port;
		userThreads = new ArrayList<ClientThread>();
		file = new File("users.csv");
		writer = new FileWriter(file);
		writer.write("Username , Password");
		writer.write('\n');
		writer.close();

	}

	public void execute() {
		try {
			System.out.println("Server waiting for clients on port " + port
					+ ".");

			ServerSocket serverSocket = new ServerSocket(port);

			while (true) {

				Socket socket = serverSocket.accept();

				ClientThread clientThread = new ClientThread(socket);
				userThreads.add(clientThread);

				clientThread.start();

			}

		} catch (IOException ex) {
			System.out.println("Error in the server: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	public synchronized void remove(int id) {
		String removedUser = "";

		for (int i = 0; i < userThreads.size(); ++i) {
			ClientThread c = userThreads.get(i);
			if (c.id == id) {
				removedUser = c.getUsername();
				userNames.remove(removedUser);
				userThreads.remove(i);
				break;
			}
		}

		broadcast(new Message(removedUser + " has left the chat.", null));
	}

	public void sendMessage(String msg) {
		System.out.println("ServerSendsMessage" +msg);
		try {
			sOutput.writeObject(msg);
		} catch (IOException e) {
			System.out.println("Exception writing to server: " + e);
		}
	}
	

	public synchronized boolean broadcast(Message m) {
		String[] arr = m.getContent().split(" ", 3);
		String oldMsg = m.getContent();

		boolean isPrivate = false;
		if (arr[1].charAt(0) == '@')
			isPrivate = true;

		if (isPrivate == true) {
			String recipient = arr[1].substring(1, arr[1].length());

			String message1 = arr[0] + " " + arr[2] + "\n";
			boolean found = false;
			for (int y = userThreads.size(); --y >= 0;) {
				ClientThread c = userThreads.get(y);
				String uname = c.getUsername();
				if (uname != null) {
					if (uname.equals(recipient)) {
						if (!c.writeMessage(new Message(message1, m.getImageInstance()))) {
							userThreads.remove(y);
							System.out.println("Disconnected Client "
									+ c.username + " removed from list.");
						}
						found = true;

						return true;
					}

				}
			}
			if (!found) {
				try {

					String msgToSend = oldMsg;
					connect();
					sendMessage(msgToSend);
				} catch (Exception e) {

				}
				return true;
			}

			else {
				return false;
			}
		} else {
			String message2 = m.getContent() + "\n";

			for (int i = userThreads.size(); --i >= 0;) {
				ClientThread c = userThreads.get(i);
				String uname = c.getUsername();
				String[] arr4 = arr[0].split(":", 2);
				if (uname != null) {
					if (!(uname.equals(arr4[0]))) {
						if (!c.writeMessage(new Message(message2, m.getImageInstance()))) {
							userThreads.remove(i);
							System.out.println("Disconnected Client "
									+ c.username + " removed from list.");
						}
					}
				}

			}
			return true;


		}
	}

	public void MemberListResponse(ClientThread c) {
		String memberlist = "";
		memberlist += "List of the users connected on Server 1:" + " \n";
		// c.writeMessage("List of the users connected on Server 1:" + " \n");
		String members = "";
		for (int i = 0; i < userNames.size(); i++) {
			if (userNames.get(i) != null && i == 0) {
				members += userNames.get(i);

			}
			if (userNames.get(i) != null && i != 0) {
				members += " - " + userNames.get(i);
			}

		}
		

		c.writeMessage(new Message(members, null));
	}

	public boolean connect() {
		try {
			socket = new Socket("localhost", 2000);
		} catch (Exception ec) {
			System.out.println("Error connectiong to server:" + ec);
			return false;
		}

		try {
			sInput = new ObjectInputStream(socket.getInputStream());
			sOutput = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: "
					+ eIO);
			return false;
		}

		new ListenFromServer().start();

		try {
			sOutput.writeObject(username);
		} catch (IOException eIO) {
			System.out.println("Exception doing login : " + eIO);
			return false;
		}
		return true;
	}

	public class ListenFromServer extends Thread {

		public void run() {
			while (true) {
				try {
					String msg = (String) sInput.readObject();
					System.out.println("ServerListen From Server" +msg);
					System.out.println(msg);
					System.out.print("> ");
				} catch (IOException e) {
					System.out.println("Server has closed the connection");
					break;
				} catch (ClassNotFoundException e2) {
				}
			}
		}
	}

	public static void main(String args[]) throws Exception {

		Server server1 = new Server(6000);
		server1.execute();

	}

	public class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		int id;
		String username;
		String clientMessage;

		ClientThread(Socket socket) {
			id = ++uniqueID;
			this.socket = socket;
			try {
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sInput = new ObjectInputStream(socket.getInputStream());
				username = (String) sInput.readObject();
				userNames.add(username);
				String user = username + "";
				if (!user.equals("null"))
					broadcast(new Message(username + " has joined the chat.", null));
				else {

					String members = "";
					for (int i = 0; i < userNames.size(); i++) {
						if (userNames.get(i) != null && i == 0) {
							members += userNames.get(i);

						}
						if (userNames.get(i) != null && i != 0) {
							members += " - " + userNames.get(i);
						}

					}
					writeMessage(new Message(members, null));
				}
			} catch (IOException e) {
				System.out
						.println("Exception creating new Input/output Streams: "
								+ e);
				return;
			} catch (ClassNotFoundException e) {
			}
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public boolean writeMessage(Message m) {
			if (!socket.isConnected()) {
				close();
				return false;
			}
			try {
				sOutput.writeObject(m);
			} catch (IOException e) {
				System.out.println("Error sending message to " + username);
				System.out.println(e.toString());
			}
			return true;
		}

		public void run() {
			try {
				while (true) {
					Object o;
					try {
						o = sInput.readObject();
						Message msg = (Message) o;
						clientMessage = msg.getContent();
						String message = clientMessage;
						if (clientMessage.equalsIgnoreCase("quit")) {
							remove(id);
							break;

						} else if (clientMessage.equalsIgnoreCase("show members")) {

							MemberListResponse(this);
						}
						 else if (clientMessage.equalsIgnoreCase("deactivate")) {

								DeactivateAccount(username,id);
								break;
							}else {

							if (username != null) {
								
								Message newM = new Message(username + ": "+ message, msg.getImageInstance());
								boolean confirmation = broadcast(newM);

							} else {

								boolean confirmation = broadcast(msg);
								if (!confirmation) {
									writeMessage(new Message("This user does not exist", null));
								}

							}

						}
					} catch (ClassNotFoundException e1) {
						System.out.println(e1.getMessage());
					}

				}	
				close();
			} catch (IOException ex) {

			}

		}

		private void DeactivateAccount(String username, int id) {
			File myDB = new File("users.csv");
			
			
			try {
				
				BufferedReader reader = new BufferedReader(new FileReader(myDB));
				String line ="";
				ArrayList <String>users= new ArrayList<>();
				while((line = reader.readLine()) != null){
					
					if(!(line.startsWith(username))){
						users.add(line);
					}
					
					
				}
				FileWriter writer= new FileWriter("users.csv");

				for(int i=0;i<users.size();i++){
					String [] data= users.get(i).split(",");
					writer.write(data[0]+","+data[1]);
					writer.write('\n');
					
				}
			    writer.close();
				remove(id);


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	    
		}

		public void close() {
			try {
				if (sOutput != null)
					sOutput.close();
			} catch (Exception e) {
			}
			try {
				if (sInput != null)
					sInput.close();
			} catch (Exception e) {
			}
			;
			try {
				if (socket != null)
					socket.close();
			} catch (Exception e) {
			}
		}

	}
}