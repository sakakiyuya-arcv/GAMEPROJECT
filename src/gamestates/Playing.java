package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import entities.Player;
import entities.PlayerData;
import levels.LevelManager;
import main.Game;
import ui.PauseOverlay;
import utilz.LoadSave;
import utilz.Constants;


public class Playing extends State implements Statemethods{
	private Player player1, player2;
	private LevelManager levelManager;
	private PauseOverlay pauseOverlay;
	private boolean paused = false;
	
	private int xLvlOffset;
	private int lefBorder = (int)(0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int)(0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	
	private BufferedImage backgroundImg, treesImg,bigCloudsImg,smallCloudsImg,backgroundImg1,backgroundImg2,backgroundImg3;
	public PlayerData player1Data, player2Data;
	
	public Playing(Game game) {
		super(game);
		player1Data = PlayerData.WOLF_SAMURAI;
		player2Data = PlayerData.DEMON_SAMURAI;
		initClasses();
		
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);
		backgroundImg1 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG1);
		backgroundImg2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG2);
		backgroundImg3 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND_IMG3);
		treesImg = LoadSave.GetSpriteAtlas(LoadSave.TREES);
		bigCloudsImg = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		smallCloudsImg = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);

	}
	
	private void initClasses() {
		levelManager = new LevelManager(game);
		player1 = new Player(300, 200, player1Data);
		player2 = new Player(1100, 200,player2Data);
		player1.loadLvlData(levelManager.getCurrentLevel().getLevlData());
		player2.loadLvlData(levelManager.getCurrentLevel().getLevlData());
		player1.inRight = true;
		player2.inLeft = true;
		pauseOverlay = new PauseOverlay(this);
	}
	
	@Override
	public void update() {
		if(!paused) {
			levelManager.update();
			player1.update();
			player2.update();
			checkCloseToBorder();
		} else {
			pauseOverlay.update();
		}
	}

	private void checkCloseToBorder() {
		int playerX = (int) player1.getHibox().x;
		int diff = playerX - xLvlOffset;
		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < lefBorder)
			xLvlOffset += diff - lefBorder;
		if(xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if( xLvlOffset < 0)
			xLvlOffset = 0;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
		g.drawImage(backgroundImg3, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
		g.drawImage(backgroundImg2, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
		g.drawImage(backgroundImg1, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT , null);
//		g.drawImage(bigCloudsImg, 0, 330,(int)(Game.GAME_WIDTH*0.5), (int)(Game.GAME_HEIGHT*0.25) , null);
//		g.drawImage(bigCloudsImg, (int)(Game.GAME_WIDTH*0.5), 330,(int)(Game.GAME_WIDTH*0.5), (int)(Game.GAME_HEIGHT*0.25) , null);
//		g.drawImage(smallCloudsImg, 942, 264,(int)(Game.GAME_WIDTH*0.1), (int)(Game.GAME_HEIGHT*0.05) , null);
//		g.drawImage(smallCloudsImg, 36, 247,(int)(Game.GAME_WIDTH*0.1), (int)(Game.GAME_HEIGHT*0.05) , null);
//		g.drawImage(smallCloudsImg, 550, 180,(int)(Game.GAME_WIDTH*0.1), (int)(Game.GAME_HEIGHT*0.05) , null);

		levelManager.draw(g, xLvlOffset);
//		g.drawImage(treesImg, 400-xLvlOffset, 372, 240, 60, null);
		g.drawImage(treesImg, 50-xLvlOffset, (int)(354 * Game.SCALE), 360, 120, null);
		g.drawImage(treesImg, 1050-xLvlOffset, (int)(354 * Game.SCALE), 360, 120, null);
		player1.render(g, xLvlOffset);
		player2.render(g, xLvlOffset);

		if(paused) {
			 g.setColor(new Color(0,0,0,150));
			 g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(paused)
			pauseOverlay.mousePressed(e);
		
	}
	public void mouseDragged(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseDragged(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseReleased(e);
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(paused)
			pauseOverlay.mouseMoved(e);
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player1.setLeft(true);
			break;
		case KeyEvent.VK_D:
			player1.setRight(true);
			break;
		case KeyEvent.VK_W:
			if(player1.checkInAir()) {
			player1.setDoubleJump(true);
			} else {
				player1.setJump(true);
			}
			break;	
		case KeyEvent.VK_S:
			if(player1Data== PlayerData.DEMON_SAMURAI)
			player1.setFalme();
			break;
		case KeyEvent.VK_J:
			player1.setAttack1(true);
			break;
		case KeyEvent.VK_K:
			player1.setAttack2(true);
			break;
		case KeyEvent.VK_L:
			player1.setAttack3(true);
			break;
		case KeyEvent.VK_I:
			player1.setSpecialAttack(true);
			break;	
			
		case KeyEvent.VK_ESCAPE:
			paused = !paused;
			break;
			
		case KeyEvent.VK_LEFT:
			player2.setLeft(true);
			break;
		case KeyEvent.VK_RIGHT:
			player2.setRight(true);
			break;
		case KeyEvent.VK_UP:
			if(player2.checkInAir()) {
				player2.setDoubleJump(true);
				} else {
					player2.setJump(true);
				}
			break;	
		case KeyEvent.VK_DOWN:
			if(player2Data== PlayerData.DEMON_SAMURAI)
			player2.setFalme();
			break;
		case KeyEvent.VK_NUMPAD1:
			player2.setAttack1(true);
			break;	
		case KeyEvent.VK_NUMPAD2:
			player2.setAttack2(true);
			break;	
		case KeyEvent.VK_NUMPAD3:
			player2.setAttack3(true);
			break;	
		case KeyEvent.VK_NUMPAD0:
			player2.setSpecialAttack(true);
			break;	
		
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_A:
			player1.setLeft(false);
			break;
		case KeyEvent.VK_D:
			player1.setRight(false);
			break;
		case KeyEvent.VK_W:{
			player1.setJump(false);
			player1.setDoubleJump(false);
		}
			break;
			
		case KeyEvent.VK_LEFT:
			player2.setLeft(false);
			break;
		case KeyEvent.VK_RIGHT:
			player2.setRight(false);
			break;
		case KeyEvent.VK_UP:{
			player2.setJump(false);
			player2.setDoubleJump(false);
		}
			break;
	}

		
	}
	public void windowFocusLost() {
		player1.resetDirBooleans();
		player2.resetDirBooleans();

	}

	public Player getPlayer1() {
		return player1;
	}
	public Player getPlayer2() {
		return player2;
	}
	public void unpauseGame() {
		paused = false;
	}
}
