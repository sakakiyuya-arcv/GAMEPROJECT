package gamestates;

import java.security.PublicKey;

public enum Gamestate {

	PLAYING, MENU, OPTION, QUIT;
	
	public static Gamestate  state = MENU;
}
