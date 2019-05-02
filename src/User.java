import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class User implements Comparable {
	private String userName;
	private String password;
	
	public static final String SALT = "nour";

	public User(String userName, String password) throws UsernameAlreadyExistsException, IOException {
		this.userName = userName;
		this.password = password;

		writeInDB(this);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void writeInDB(User user) {

		File myDB = new File("users.csv");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(myDB));
			String line = "";
			while ((line = reader.readLine()) != null) {
				if (line.startsWith(user.getUserName())) {

					throw new UsernameAlreadyExistsException();

				}
			}

			FileWriter writer = new FileWriter("users.csv", true);
			
			String saltedPassword = SALT + password;
			String hashedPassword = generateHash(saltedPassword);
			
			writer.write(user.getUserName() + "," + hashedPassword);
			writer.write('\n');
			writer.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
	

	@Override
	public int compareTo(Object o) {
		User x = (User) o;
		if (x.userName == this.userName && x.password == this.password) {
			return 0;
		} else {
			return -1;
		}
	}
	
	public static String generateHash(String input) {
		StringBuilder hash = new StringBuilder();

		try {
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			byte[] hashedBytes = sha.digest(input.getBytes());
			char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
					'a', 'b', 'c', 'd', 'e', 'f' };
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
