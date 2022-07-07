package Menu;

public enum eMenu {
	START(1) 
	,PLAY(2)
	,SELECT_LEVEL(3)
	,PLAYER_STAT(4)
	,EXIT(5)
	,END(6);
	
	private int x;
	eMenu(int x){
		this.x = x;
	}
	public int getX() {
		return x;
	}
}
