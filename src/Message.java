import java.io.Serializable;

public class Message implements Serializable {
	private String content;
	private ImageInstance imageInstance;
	
	public Message(String content, ImageInstance imageInstance) {
		this.content = content;
		this.imageInstance = imageInstance;
	}
	public String getContent() {
		return content;
	}
	public ImageInstance getImageInstance() {
		return imageInstance;
	}
	public boolean containsAttachement() {
		return getImageInstance()!=null;
	}
	
}
