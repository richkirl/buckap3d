package Menu;

public class CastamPair<E,T> {
	private int key;
	private String value;
	
	
	public CastamPair(eMenu x,String value) {
		this.key = x.getX();
		this.value = value;
	}
	
	public int getKey() {
		return key;
	}
	public void setKey(eMenu key) {
		this.key = key.getX();
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
