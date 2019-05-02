

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class ClientStartWindow extends JFrame implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextArea feedback = new JTextArea("Hello ");
	private JTextArea def;
	private JLabel welcome;
	private JTextField input = new JTextField();
	
	private JButton sendButton = new JButton("Send");
	
	private JScrollPane chat;
	private JTextArea mList;
	private JList<String> list;
	
	private Client client;
	private JButton openButton, encryptButton, decryptButton, stats, quit, deactivate;
	private JFileChooser fc;
	private String username = "";
	private File openedFile;
	private Steganography st = new Steganography();
	
	private String Outname,message;

	public ClientStartWindow(String name, Client client, DummyRunnable runnable) 
	{
		super(name);
		this.username = name;
		def = new JTextArea("Welcome to the Chat\n"
				
				+ "1. Type @username<space>yourmessage to send a message to a client\n" +
				"2. Type show members to see a list of available clients\n"+
				"3. Type @username<space><comma><space>encryptedImageFile\n"+
				"4.	To broadcast encrypted message type message<space><comma><space>encryptedImageFile\n"+
				"5. To broadcast a direct message just type your message and send", 20, 72);
		welcome = new JLabel("Welcome "+ name, SwingConstants.CENTER);
		setContentPane(new JLabel(new ImageIcon("resources/images/clientSWBack.jpg")));
		setSize(1366, 725);
		setResizable(false);
		setUndecorated(true);
		setLayout(new BorderLayout());
		setLayout(null);

		addMiddlePannel();
		
		addUpperPannel();
		
		addChooserPannel();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		
		
		
		this.client = client;
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String msg = input.getText();
				def.setText(def.getText() + "\n" + " > "+msg);
				input.setText("");
				
				if(msg.equalsIgnoreCase("QUIT")) {
    				client.sendMessage("quit");
    				dispose();
    				
    			}else if (msg.equalsIgnoreCase("deactivate")){
    				client.sendMessage("deactivate");
    				dispose();
    			}
    			else if(msg.equalsIgnoreCase("GETMEMBERLIST")) {
    				client.sendMessage("getmemberlist");				
    			}
    			else {
    				if(msg.contains(".png")){
    					msg += ", "+Outname;
    				}
    				client.sendMessage(msg);
    			}
				
			}
		});
		runnable.addPropertyChangeListener(this);
	}

	



	
	private void addUpperPannel()
	{
		Border border = BorderFactory.createLineBorder(Color.CYAN, 5);
		Color color = new Color(240, 255, 255);

		welcome.setBounds(260, 50, 830, 50);
		welcome.setBorder(border);
		welcome.setOpaque(true);
		welcome.setBackground(color);
		getContentPane().add(welcome);

		

	}


	private void addChooserPannel()
	{
		Color color = new Color(240, 255, 255);
		fc = new JFileChooser();

		openButton = new JButton("Upload a File...");
		openButton.addActionListener(this);
		
		 encryptButton = new JButton("Encrypt");
		 encryptButton.addActionListener(this);
		 
		 decryptButton = new JButton("Decrypt");
		 decryptButton.addActionListener(this);
		 
		 getContentPane().add(openButton);
		 openButton.setBounds(1100, 570, 240, 50);
		 openButton.setBackground(color);
		 
		 getContentPane().add(encryptButton);
		 encryptButton.setBounds(1100, 620, 120, 50);
		 encryptButton.setBackground(color);
		 
		 getContentPane().add(decryptButton);
		 decryptButton.setBounds(1220, 620, 120, 50);
		 decryptButton.setBackground(color);
		 
		 
		 stats = new JButton("Show Stats");
		 stats.addActionListener(this);
		 
		 getContentPane().add(stats);
		 stats.setBounds(20, 570, 200, 50);
		 stats.setBackground(color);
		 
		 quit = new JButton("Logout");
		 quit.addActionListener(this);
		 
		 getContentPane().add(quit);
		 quit.setBounds(20, 620, 100, 50);
		 quit.setBackground(color);
		 
		 deactivate = new JButton("Deactivate");
		 deactivate.addActionListener(this);
		 
		 getContentPane().add(deactivate);
		 deactivate.setBounds(120, 620, 100, 50);
		 deactivate.setBackground(color);

	}
	
	public void actionPerformed(ActionEvent e) {

	    //Handle open button action.
	    if (e.getSource() == openButton) {
	      int returnVal = fc.showOpenDialog(ClientStartWindow.this);

	      if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fc.getSelectedFile();
	        openedFile = file;
	        JOptionPane.showMessageDialog(null,
	                "You uploaded the image "+ file.getName(),"Alert!",
	                JOptionPane.PLAIN_MESSAGE);
	      } else {
	       JOptionPane.showMessageDialog(null,
              "No uploads..","Alert!",
              JOptionPane.ERROR_MESSAGE);
	      }

	      //Handle save button action.
	    } else if (e.getSource() == decryptButton) {
	     if(openedFile == null){
	    	 JOptionPane.showMessageDialog(null,
	                 "No file uploaded to decrypt","Alert!",
	                 JOptionPane.ERROR_MESSAGE);
	     }else{
	    	String sc = st.decode(openedFile.getPath());
	    	JOptionPane.showMessageDialog(null,
	                 "Your Message: "+ sc,"Alert!",
	                 JOptionPane.PLAIN_MESSAGE);
	     }
	    } 
	    else if (e.getSource() == stats) {
			if(openedFile == null || Outname ==null){
				JOptionPane.showMessageDialog(null,
									"No file uploaded to Show stats","Alert!",
									JOptionPane.ERROR_MESSAGE);
			}else{
			BufferedImage im1, im2;
			im1 = null;
			im2 = null;
			Path p = Paths.get(openedFile.getPath());
			String dir = p.getParent().toString();
			double mse = 0;
			double noOfpixels = 0;
			try {
				im2 = ImageIO.read(new File(Outname+".png"));
				im1  = ImageIO.read(openedFile);
				System.out.println(im1.equals(null));
    int width = im1.getWidth();
    int height = im1.getHeight();
    Raster r1 = im1.getRaster();
			Raster r2 = im2.getRaster();
			
			byte[] pixels = (byte[]) r1.getDataElements(0,0,im2.getWidth(),im2.getHeight(),null);
			noOfpixels = pixels.length;
    for (int j = 0; j < height; j++)
        for (int i = 0; i < width; i++)
            mse += Math.pow(r1.getSample(i, j, 0) - r2.getSample(i, j, 0), 2);

    mse /= (double) (width * height);
			} catch (Exception exception) {
				System.out.println(exception.toString());
				//TODO: handle exception
			}
			
			
			
    
    double psnr = 10.0 * Math.log(Math.pow(255, 2) / mse) / Math.log(10);



			 JOptionPane.showMessageDialog(null,
									"Capacity ratio: " +  ((message.length())/(noOfpixels))*100 +"%\n"+
									
									"Peak Signal to noise ratio: "+ psnr + " DB","Alert!",
									JOptionPane.PLAIN_MESSAGE);
			}}
	    else if(e.getSource() == encryptButton){
	    	if(openedFile == null){
	    		 JOptionPane.showMessageDialog(null,
		                 "No file uploaded to encrypt","Alert!",
		                 JOptionPane.ERROR_MESSAGE);
	    	}else{
	    		Outname = (String)JOptionPane.showInputDialog(null, "Output file name?",
						"Output File", JOptionPane.PLAIN_MESSAGE, null,  null,"");
				
				String message = (String)JOptionPane.showInputDialog(null, "The Message you want to encrypt?",
						"Message", JOptionPane.PLAIN_MESSAGE, null,  null,"");
				
				if ( Outname == null || Outname.isEmpty()
						|| message == null || message.isEmpty())
						return;
				if(st.encode(username, openedFile.getPath(), Outname, message)){
					JOptionPane.showMessageDialog(null,
			                 "Encryption Done to File "+ Outname,"Alert!",
			                 JOptionPane.PLAIN_MESSAGE);
				}
	    	}
	    }else if(e.getSource() == quit){
	    	client.sendMessage("quit");
			dispose();
	    }else if(e.getSource() == deactivate){
	    	client.sendMessage("deactivate");
			dispose();
	    }
	  }

	private void addMiddlePannel() 
	{
		Border border = BorderFactory.createLineBorder(Color.CYAN, 5);
		Color color = new Color(240, 255, 255);

		addFeedback(def);

		getContentPane().add(input);
		input.setBounds(260, 570, 680, 100);
		input.setBorder(border);
		input.setBackground(color);

		getContentPane().add(sendButton);
		sendButton.setBounds(950, 570, 140, 100);
		sendButton.setBackground(color);
	}

	public void addFeedback(JTextArea feedback2) 
	{
		feedback = feedback2;
		feedback.setEditable(false);
		SetChat(feedback);
	}

	private void SetChat(JTextArea feedback2) 
	{
		Border border = BorderFactory.createLineBorder(Color.CYAN, 5);
		Color color = new Color(240, 255, 255);
		chat = new JScrollPane(feedback2);
		chat.setBounds(260, 200, 830, 360);
		chat.setBorder(border);
		chat.setOpaque(true);
		chat.setBackground(color);
		getContentPane().add(chat);
		repaint();
		validate();
	}
	

	public JScrollPane getChat() { return chat; }

	public JTextArea getDef() {	return def; }

	public JList<String> getList() { return list; }


	public JButton getSendButton() { return sendButton; }

	public JTextField getInput() { return input; }

	public JTextArea getFeedback() { return feedback; }


	public JTextArea getmList() { return mList; }


	public void setWelcome(String x) { welcome.setText(x); }






	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		 if (evt.getPropertyName().equals("command")) {
	            // Received new command (outside EDT)
	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                	if(client.getUpdated()){
	        				def.setText(def.getText() + "\n" + " > "+client.getUpdatedMessage());
	        				client.setUpdated(false);
	        			}
	                }
	            });
	        }
		
	}



}
