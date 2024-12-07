package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Entity {
    GamePanel gp;

    public double worldX, worldY;
    public double speed;

    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionLockCounter = 0;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    String dialogue[] = new String[20];
    int dialogueIndex = 0;

    // CHARACTER STATUS
    public int maxLife;
    public int life;

    public Entity(GamePanel gp){
        this.gp = gp;
    }

    public void setAction(){}
    public void speak(){
        if (dialogue[dialogueIndex] == null){
            dialogueIndex = 0;
        }
        gp.ui.currentDialog = dialogue[dialogueIndex];
        dialogueIndex++;

        switch (gp.player.direction) {
            case "up":
                direction = "down";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
            case "down":
                direction = "up";
                break;
        }
    }

    public void update(){
        setAction();

        collisionOn = false;
        gp.cCheker.checkTile(this);
        gp.cCheker.checkObject(this, false);
        gp.cCheker.checkPlayer(this);

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
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;
        double screenX = worldX - gp.player.worldX + gp.player.screenX;
        double screenY = worldY - gp.player.worldY + gp.player.screenY;

        if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

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
            if (image != null) {
                g2.drawImage(image, (int)screenX, (int)screenY, gp.tileSize, gp.tileSize, null);
            } else {
                System.err.println("Image tidak diinisialisasi untuk direction: " + direction + " di Entity: " + this);
            }
        }
    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        String fullPath = imagePath + ".png";
        System.out.println("Mencoba memuat gambar dari: " + fullPath);
        InputStream is = getClass().getResourceAsStream(fullPath);
        if (is == null) {
            System.err.println("Gagal menemukan resource: " + fullPath);
            return null;
        }

        try {
            image = ImageIO.read(is);
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
        } catch (IOException e){
            e.printStackTrace();
        }

        return image;
    }

}
