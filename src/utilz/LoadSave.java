package utilz;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import main.Game;

public class LoadSave {
//	public static final String PLAYER_ATLAS = "player_sprites.png";
//	public static final String PLAYER_ATLAS1 = "player.png";
	public static final String DEMON_SAMURAI_ATLAS = "demon_samurai.png";
	public static final String WOLF_SAMURAI_ATLAS = "wolf_samurai.png";
	public static final String SAMURAI_2_ATLAS = "samurai_2.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
//	public static final String LEVEL_ATLAS = "Tileset.png";
//	public static final String LEVEL_ONE_DATA = "level_one_data.png";
//	public static final String LEVEL_ONE_DATA = "level_one_data_long.png";
	public static final String LEVEL_ONE_DATA = "new_level_one_data.png";

	public static final String MENU_BUTTON = "button_atlas.png";
	public static final String MENU_BACKGROUND = "menu_background.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTON = "sound_button.png";
	public static final String URM_BUTTON = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String MENU_BACKGROUND_IMG = "background_menu.png";
//	public static final String PLAYING_BG_IMG = "playing_bg_img.png";
	public static final String PLAYING_BG_IMG = "Preview.png";
	public static final String TREES= "Trees.png";
	public static final String BIG_CLOUDS= "big_clouds.png";
	public static final String SMALL_CLOUDS= "small_clouds.png";
	public static final String BACKGROUND_IMG1 = "1.png";
	public static final String BACKGROUND_IMG2 = "2.png";
	public static final String BACKGROUND_IMG3= "3.png";




	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img=null;
		InputStream is = LoadSave.class.getResourceAsStream("/"+fileName);
		try {
			 img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	public static int[][] GetLevelData(){
		
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		
		for(int j=0; j< img.getHeight();j++)
			for(int i=0; i< img.getWidth();i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if(value>=48)
					value=0;
				lvlData[j][i]=value;
			}
		return lvlData;
	}
}
