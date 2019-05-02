

public class ClientMain
{
	private ClientJoin joinWindow;
	private ClientJoinListener joinWindowListener;
	

	public ClientMain(){
		joinWindow = new ClientJoin();
		joinWindowListener = new ClientJoinListener();
		addActionListeners();
	}
	private void addActionListeners() 
	{
		joinWindow.getServer().addActionListener(joinWindowListener);
		joinWindow.getCreate().addActionListener(joinWindowListener);
		
	}
	
	public static void main(String[] args)	
	{	
		new ClientMain();
		
	}	
	
}
