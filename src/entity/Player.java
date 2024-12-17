package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    GamePanel gp;
    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    public int hasEgg = 0;
    int idleCounter = 0;
    public boolean dead = false;


    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp);

        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);

        solidArea = new Rectangle();
        solidArea.x = 12;
        solidArea.y = 16;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = gp.tileSize - (solidArea.x * 2);
        solidArea.height = gp.tileSize - ((solidArea.y / 2) * 2);

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues(){
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = gp.worldWidth / 600;
        direction = "down";

        // PLAYER STATUS
        maxLife = 6;
        life = maxLife;
    }

    public void getPlayerImage(){
        up1 = setup("/player/Up_1");
        up2 = setup("/player/Up_2");
        up3 = setup("/player/Up_3");
        up4 = setup("/player/Up_4");
        down1 = setup("/player/Down_1");
        down2 = setup("/player/Down_2");
        down3 = setup("/player/Down_3");
        down4 = setup("/player/Down_4");
        left1 = setup("/player/Left_1");
        left2 = setup("/player/Left_2");
        left3 = setup("/player/Left_3");
        left4 = setup("/player/Left_4");
        right1 = setup("/player/Right_1");
        right2 = setup("/player/Right_2");
        right3 = setup("/player/Right_3");
        right4 = setup("/player/Right_4");
    }

    public void update(){

        if (keyH.upPressed == true || keyH.downPressed == true || keyH.leftPressed == true || keyH.rightPressed == true){
            if (keyH.upPressed){
                direction = "up";
            }
            else if (keyH.downPressed){
                direction = "down";
            }
            else if (keyH.leftPressed) {
                direction = "left";
            }
            else if (keyH.rightPressed) {
                direction = "right";
            }

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cCheker.checkTile(this);

            // CHECK OBJECT COLLISION
            int objIndex = gp.cCheker.checkObject(this, true);
            pickUpObject(objIndex);

            // CHECK NPC COLLISION
            int npcIndex = gp.cCheker.checkEntity(this,gp.npc);
            interactNPC(npcIndex);

            // CHECK MONSTER COLLISION
            int monsterIndex = gp.cCheker.checkEntity(this,gp.monster);
            contactMonster(monsterIndex);

            // CHECK EVENT
            gp.eHandler.checkEvent();
            gp.keyH.ePressed = false;

            // IF COLLISION IS FALSE, PLAYER CAN MOVE
            if (collisionOn == false) {
                switch (direction){
                    case "up":
                        worldY -= speed;
                        break;
                        case "down":
                            worldY += speed;
                            break;
                            case "left":
                                worldX -= speed;
                                break;
                                case "right":
                                    worldX += speed;
                                    break;
                }
            }

            spriteCounter++;
            if (spriteCounter > 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;

                }
                spriteCounter = 0;
            }
        } else {
            idleCounter++;  // Increment idle counter
            if (idleCounter > 20) { // Idle animation starts after some delay
                idleCounter++;
                if (idleCounter > 30) {
                    spriteNum = spriteNum == 1 ? 2 : 1;
                    idleCounter = 0;
                }
            }
        }

        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        if (dead) {
            gp.ui.gameFinished = true;
        }
    }

    private void contactMonster(int monsterIndex) {
        if (monsterIndex != 999) {
            if (!invincible && life > 0) {
                life -= 1;
                invincible = true;
            } else if (life <= 0) {
                dead = true;
            }
        }
    }

    private void interactNPC(int i) {
        if (i != 999){
            if (gp.keyH.ePressed) {
                gp.gameState = gp.dialogState;
                gp.npc[i].speak();
            }
        }
    }

    public void pickUpObject(int i){
        if(i != 999){
            hasEgg += 1;
            gp.obj[i] = null;
            gp.playSoundEffect(1);

            if (hasEgg == gp.obj.length){
                gp.ui.gameFinished = true;
                gp.playSoundEffect(4);
            }
        }
    }

    public void draw(Graphics2D g2){
        BufferedImage image = null;

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed){
            switch(direction){
                case "up":
                    if (spriteNum == 1){
                        image = up3;
                    } else {
                        image = up4;
                    }
                    break;
                case "down":
                    if (spriteNum == 1){
                        image = down3;
                    } else {
                        image = down4;
                    }
                    break;
                case "left":
                    if (spriteNum == 1){
                        image = left3;
                    } else {
                        image = left4;
                    }
                    break;
                case "right":
                    if (spriteNum == 1){
                        image = right3;
                    } else {
                        image = right4;
                    }
                    break;
            }
        } else {
            switch(direction){
                case "up":
                    if (spriteNum == 1){
                        image = up1;
                    } else {
                        image = up2;
                    }
                    break;
                case "down":
                    if (spriteNum == 1){
                        image = down1;
                    } else {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1){
                        image = left1;
                    } else {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1){
                        image = right1;
                    } else {
                        image = right2;
                    }
                    break;
            }
        }



        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }
        g2.drawImage(image, screenX, screenY, null);

        // RESET ALPHA
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // CHECK HITBOXES
        if (keyH.checkHitBoxes) {
            g2.setColor(Color.RED);
            g2.drawRect(screenX + solidArea.x, screenY + solidArea.y, solidArea.width, solidArea.height);
        }
    }
}
