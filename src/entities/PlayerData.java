package entities;



import main.Game;
import utilz.LoadSave;

public class PlayerData {
	
	public String img;
	public int width;
	public int height;
	public int rowNum;
	public int columnNum;
	public int imgWidth;
	public int imgHeight;
	public int xOffset;
	public int yOffset;
	public float hitboxX, hitboxY;
	
	public PlayerData(String string, int width, int height, int a, int b, int c, int d, int e, int f, float hitboxX, float hitboxY) {
		this.img = string;
		this.width = width;
		this.height = height;
		this.rowNum = a;
		this.columnNum = b;
		this.imgWidth = c;
		this.imgHeight = d;
		this.xOffset = e;
		this.yOffset = f;
		this.hitboxX = hitboxX;
		this.hitboxY = hitboxY;
	}
	public static final PlayerData DEMON_SAMURAI = new PlayerData(LoadSave.DEMON_SAMURAI_ATLAS,(int)(64*1.43*Game.SCALE),(int)(54*1.43*Game.SCALE), 20, 26, 128, 108,(int)(41*1.43),(int)(38*1.43),9f,3.6f);
	public static final PlayerData WOLF_SAMURAI = new PlayerData(LoadSave.WOLF_SAMURAI_ATLAS, (int)(96*1.43*Game.SCALE),(int)(32*1.43*Game.SCALE), 10, 12, 192, 64,(int)(57*1.43),(int)(21*1.43),7.2f,3.6f);
	public static final PlayerData SAMURAI_2 = new PlayerData(LoadSave.SAMURAI_2_ATLAS, (int)(80*1.43*Game.SCALE),(int)(34*1.43*Game.SCALE), 10, 11, 250, 110,(int)(53*1.43),(int)(18*1.43),12f,2.275f);

	public static final int IDLE = 0;
	public static final int RUNNING = 1;
	public static final int JUMP = 2;
	public static final int FALLING = 3;
	public static final int HIT = 4;
	public static final int ATTACK_1 = 5;
	public static final int ATTACK_2 = 6;
	public static final int ATTACK_3 = 7;
	public static final int SPECIAL_ATTACK =8;
	public static final int DEATH = 9;
	public static final int SHOUT = 10;

	public static int GetSpriteAmount(PlayerData playerData, int playerAction) {
		if(playerData == DEMON_SAMURAI)
			switch (playerAction) {
		case IDLE:
			return 6;
		case RUNNING:
			return 8;
		case JUMP:
			return 3;
		case HIT:
			return 4;
		case ATTACK_1:
		case ATTACK_3:
			return 7;
		case ATTACK_2:
			return 5;
		case SPECIAL_ATTACK:
			return 12;
		case DEATH:
			return 26;
		case SHOUT:
			return 17;
		case FALLING:
			return 1;
		}
		if(playerData == WOLF_SAMURAI)
			switch (playerAction) {
		case IDLE:
		case RUNNING:
		case ATTACK_1:
		case HIT:
			return 6;
		case JUMP:
			return 3;
		case ATTACK_2:
			return 4;
		case ATTACK_3:
			return 5;
		case SPECIAL_ATTACK:
			return 12;
		case DEATH:
			return 8;
		case FALLING:
			return 1;
		}
		if(playerData == SAMURAI_2)
			switch (playerAction) {
			case IDLE:
				return 5;
			case RUNNING:
				return 7;
			case JUMP:
				return 3;
			case HIT:
				return 3;
			case ATTACK_1:
			case ATTACK_2:
				return 5;
			case ATTACK_3:
				return 10;
			case SPECIAL_ATTACK:
				return 11;
			case DEATH:
				return 10;
			case FALLING:
				return 1;
		}
		return 1;
	}	
}
