package entities;

import static entities.PlayerData.*;
import static utilz.HelpMethods.*;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

import main.Game;
import utilz.LoadSave;

public class Player extends Entity {
	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 25;
	private int playerAction = IDLE;
	private boolean moving = false, attacking = false, death = false, shout = false, flame = false;
	private boolean left, up, right, down,jump,doubleJump, attack1=false, attack2=false, attack3=false, specialAttack=false;
	private float playerSpeed = 1.0f * Game.SCALE;
	private int[][] lvlData;
//	private float xDrawOffset = 21*Game.SCALE;
//	private float yDrawOffset = 4*Game.SCALE;
	private float xDrawOffset;
	private float yDrawOffset;
	
	private float airSpeed = 0f;
	private float gravity = 0.04f*Game.SCALE;
	private float jumpSpeed = -2.5f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;
	private boolean canDoubleJump = false;
	private static final long FLAMETIMES = 20000;
	private long setFlameTime = 0;
	private PlayerData playerData;
	private int flipX = 0;
	private int flipW =1;
	public boolean  inRight, inLeft;

	public Player(float x, float y, PlayerData playerData) {
		super(x, y,playerData.width,playerData.height);
		this.playerData = playerData;
		this.xDrawOffset = this.playerData.xOffset ;
		this.yDrawOffset = this.playerData.yOffset ;
		loadAnimations();
		initHitBox(x  , y , (int)(playerData.width / this.playerData.hitboxX * Game.SCALE), (int)(playerData.height / this.playerData.hitboxY * Game.SCALE));
	}
	

	public void update() {
		updatePos();
		updateAnimationTick();
		setAnimation();
		checkFlame();
		}

	public void render(Graphics g, int lvlOffset) {
		if(flame&&!death&&(playerAction != SHOUT))
			g.drawImage(animations[playerAction+11][aniIndex], (int)(hitbox.x-xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y-yDrawOffset), width*flipW, height, null);
		else
		g.drawImage(animations[playerAction][aniIndex], (int)(hitbox.x-xDrawOffset) - lvlOffset + flipX, (int)(hitbox.y-yDrawOffset), width*flipW, height, null);
//		drawHitBox(g);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if(aniIndex>=GetSpriteAmount(playerData, playerAction)-1) {
				if((playerAction == SPECIAL_ATTACK)&&(flame==true)) {
					aniIndex = 0;
					attacking = false;
					specialAttack = false;
				}	
				else if (aniIndex >= GetSpriteAmount(this.playerData,playerAction)) {
						aniIndex = 0;
						attacking = false;
						attack1 = false;
						attack2 = false;
						attack3 = false;
						specialAttack = false;
						shout = false;
					}
			}
		}
	}
	public void setFalme() {
		if(setFlameTime == 0 || (System.currentTimeMillis() - setFlameTime >= 3 * FLAMETIMES )) {
		flame = true;
		shout = true;
		setFlameTime = System.currentTimeMillis();		
		}
	}
	private void checkFlame() {
		if(flame)
		if(System.currentTimeMillis() - setFlameTime>FLAMETIMES)
			flame = false;
	}

	private void setAnimation() {
		int startAni = playerAction;

		if (moving)
			playerAction = RUNNING;
		else
			playerAction = IDLE;
		if(inAir) {
			if(airSpeed <0)
				playerAction = JUMP;
			else {
				playerAction = FALLING;
			}
		}

		if (attack1)
			playerAction = ATTACK_1;
		if (attack2)
			playerAction = ATTACK_2;
		if (attack3)
			playerAction = ATTACK_3;
		if (specialAttack)
			playerAction = SPECIAL_ATTACK;
		if(shout)
			playerAction = SHOUT;

		if (startAni != playerAction)
			resetAniTick();
	}

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		float xSpedd = 0;
		if(inLeft) {
			if(playerData == WOLF_SAMURAI) {
				flipX = 0;
				flipW = 1;
			}
			else {
			flipX = width;
			flipW = -1;
			}
		}
		if(inRight) {
			if(playerData == WOLF_SAMURAI) {
				flipX = width;
				flipW = -1;
			}
			else {
			flipX = 0;
			flipW = 1;
			}
		}
		if (left) 
			xSpedd -= playerSpeed;
		
		if (right) 
			xSpedd += playerSpeed;

		if(playerAction==SPECIAL_ATTACK&&aniIndex>=6&&(playerData == WOLF_SAMURAI&&aniIndex<10||playerData == SAMURAI_2&&aniIndex<9)) {
			if(inRight)
				xSpedd += 1.5 *playerSpeed;
			else
				xSpedd -= 1.5 *playerSpeed;

			if(!inAir) 
				if(!IsEntityOnFloor(hitbox, lvlData)) {
					inAir = true;		
					}
			if(inAir) {
				if(CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, lvlData)) {
					hitbox.y += airSpeed;
					airSpeed += gravity;
					updateXPos(xSpedd);
				} else {
					hitbox.y = GetEntityYPosUnderRoofOrAboveFlorr(hitbox, airSpeed);
					if(airSpeed > 0)
						resetInAir();
					else 
						airSpeed = fallSpeedAfterCollision;
					updateXPos(xSpedd);
				}
			} 
				updateXPos(xSpedd);
		}
		
		if(jump) 
			jump();
		
		if(doubleJump)
			doubleJump();
		if(!inAir && (attacking||shout))
			return;
		
		if(!left&&!right&&!inAir)
			return;
		
		if(!inAir) 
			if(!IsEntityOnFloor(hitbox, lvlData)) {
				inAir = true;		
				}
		if(inAir) {
			if(CanMoveHere(hitbox.x, hitbox.y+airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpedd);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFlorr(hitbox, airSpeed);
				if(airSpeed > 0)
					resetInAir();
				else 
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpedd);
			}
		} else 
			updateXPos(xSpedd);
		moving = true;

	}

	private void doubleJump() {
		if(!canDoubleJump)
			return;
		airSpeed = jumpSpeed;
		canDoubleJump = false;
	}


	private void jump() {
		if(inAir)
			return;
		canDoubleJump = true;
		inAir = true;
		airSpeed = jumpSpeed;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
	}

	private void updateXPos(float xSpedd) {
		if(CanMoveHere(hitbox.x+xSpedd, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
		hitbox.x += xSpedd;
	    } else {
		     hitbox.x = GetEntityXPosNextToWall(hitbox, xSpedd);
	    }
    }

	private void loadAnimations() {
	
			BufferedImage img = LoadSave.GetSpriteAtlas(this.playerData.img);

			animations = new BufferedImage[this.playerData.rowNum][this.playerData.columnNum];
			for (int j = 0; j < animations.length; j++)
				for (int i = 0; i < animations[j].length; i++)
					animations[j][i] = img.getSubimage(i * this.playerData.imgWidth, j * this.playerData.imgHeight, playerData.imgWidth, this.playerData.imgHeight);

	}
	
	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if(!IsEntityOnFloor(hitbox, lvlData))
			inAir = true ;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

//	public void setAttacking(boolean attacking) {
//		this.attacking = attacking;
//	}
	public void setAttack1(boolean attack1) {
		this.attack1 = attack1;
		if(this.attack1)
			attacking = true;
	}
	public void setAttack2(boolean attack2) {
		this.attack2 = attack2;
		if(this.attack2)
			attacking = true;
	}
	public void setAttack3(boolean attack3) {
		this.attack3 = attack3;
		if(this.attack3)
			attacking = true;
	}
	public void setSpecialAttack(boolean specialAttack) {
		this.specialAttack = specialAttack;
		if(this.specialAttack)
			attacking = true;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
		if(left) {
			inLeft = true;
			inRight = false;
		}
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
		if(right)
			inRight = true;
			inLeft = false;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}
	public void setDoubleJump(boolean doubleJump) {
		this.doubleJump = doubleJump;
	}

	public void setJump(boolean jump) {
		this.jump = jump;		
	}
	public boolean checkInAir() {
		return inAir;
	}

}