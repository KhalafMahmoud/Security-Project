import java.io.Serializable;

public class ImageInstance implements Serializable{
	private String name;
	private byte[] size;
	private byte[] imgArr;
	public ImageInstance(String name, byte[] size, byte[] imgArr) {
		this.name = name;
		this.size = size;
		this.imgArr = imgArr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getSize() {
		return size;
	}
	public void setSize(byte[] size) {
		this.size = size;
	}
	public byte[] getImgArr() {
		return imgArr;
	}
	public void setImgArr(byte[] imgArr) {
		this.imgArr = imgArr;
	}
	
}
