package Menu;

import java.util.ArrayList;
import java.util.List;
public class MenuState {

	private eState estate;
	private eMenu emenu;
	private int mSelectLevelIndex;
	private eState mState= eState.MENU;
	private eMenu mCurrentMenu;
	
	
	public void init() {
		
		
		List<CastamPair<eMenu,String>> mvMenu;
		mvMenu = new ArrayList<>();
		
		mvMenu.add(new CastamPair<>(emenu.PLAY,"PLAY"));
		mvMenu.add(new CastamPair<>(emenu.SELECT_LEVEL,"SELECT_LEVEL"));
		mvMenu.add(new CastamPair<>(emenu.PLAYER_STAT,"PLAYER_STAT"));
		mvMenu.add(new CastamPair<>(emenu.EXIT,"EXIT"));
		
	}
	
	
	public void processing() {
		switch(mState.getX()) {
		case 1:
		{
			break;
		}
		case 2:
		{
			break;
		}
		case 3:
		{
			break;
		}
		case 4:
		{
			break;
		}
		case 5:
		{
			close();
			break;
		}
		}
	}
	private void close() {
		// TODO Auto-generated method stub
		
	}
	public void render() {
		switch(mState.getX()) {
		case 1:
		{
			break;
		}
		case 2:
		{
			break;
		}
		case 3:
		{
			break;
		}
		case 4:
		{
			break;
		}
		case 5:
		{
			close();
			break;
		}
		}
	}

}




//mvMenu.add(emenu.PLAY,"PLAY");
		//mvMenu.add(eMenu.SELECT_LEVEL,"PLAY");
		//mvMenu.add(eMenu.PLAYER_STAT,"STAT");
		//mvMenu.add(eMenu.EXIT,"PLAY");
//enum eState {
//MENU
//,SELECT_LEVEL
//,PLAYER_STAT
//,PLAY
//,EXIT
//}
//enum eMenu {
//START
//,PLAY
//,SELECT_LEVEL
//,EXIT
//,END
//}