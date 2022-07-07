package Menu;

public enum eState {
	 MENU(1)
	,SELECT_LEVEL(2)
	,PLAYER_STAT(3)
	,PLAY(4)
	,EXIT(5);
	
	private int x;
	eState(int x){
		this.x = x;
	}
	public int getX() {
		return x;
	}
	
	
	
	
}
