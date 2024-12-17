package main;

import entity.Entity;
import object.OBJ_Egg;
import object.OBJ_Heart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    public boolean gameFinished = false;
    GamePanel gp;
    Graphics2D g2;
    Font maruMonica, purisaB;

    BufferedImage heart_full, heart_half, heart_blank;
    BufferedImage egg;

    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    public String currentDialog = "";
    public int commandNum = 0;

    public UI(GamePanel gp) {
        this.gp = gp;
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            purisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // CREATE HUD OBJECT
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_half = heart.image2;
        heart_blank = heart.image3;

        // CREATE EGG HUD
        Entity egg_obj = new OBJ_Egg(gp);
        egg = egg_obj.down2;
    }

    public void showMessage(String text){
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {
        this.g2 = g2;

        g2.setFont(maruMonica);
        g2.setColor(Color.WHITE);

        // TITLE STATE
        if (gp.gameState == gp.titleState){
            drawTitleScreen();
        }

        // PLAY STATE
        if (gp.gameState == gp.playState) {
            drawPlayerLife();
            drawEgg();
        }
        // PAUSE STATE
        if (gp.gameState == gp.pauseState) {
            // Do pauseState
            drawPauseScreen();
        }
        // DIALOG STATE
        if (gp.gameState == gp.dialogState){
            drawDialogScreen();
        }

        // WIN / LOSE STATE
        if (gp.gameState == gp.transitionState){
            drawWinScreen();
        }
    }

    private void drawEgg() {
        int x = gp.tileSize * 13;
        int y = gp.tileSize / 2;
        int i = 0;

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45f));
        String text = "x " + gp.player.hasEgg;
        g2.drawImage(egg, x, y, null);
        g2.drawString(text, (int) (gp.tileSize * 14.2), (int) (gp.tileSize * 1.25));
    }

    private void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        // DRAW MAX HEALTH
        while (i < gp.player.maxLife/2) {
            g2.drawImage(heart_blank, x, y, null);
            i++;
            x += gp.tileSize;
        }

        // RESET
        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        // DRAW CURRENT LIFE
        while (i < gp.player.life) {
            g2.drawImage(heart_half, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gp.tileSize;
        }

    }

    private void drawTitleScreen() {

        // TITLE BACKGROUND
        g2.setColor(new Color(0,0,0));
        g2.fillRect(0,0,gp.screenWidth,gp.screenHeight);

        // TITLE NAME
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 96f));
        String text = "Keputih Ranch";
        int x = getCenteredTextX(text);
        int y = gp.tileSize * 3;

        // SHADOW
        g2.setColor(Color.GRAY);
        int xOffset = 5;
        int yOffset = 5;
        g2.drawString(text,x + xOffset,y + yOffset);

        // MAIN COLOR
        g2.setColor(Color.WHITE);
        g2.drawString(text, x, y);

        // BLUE BOY IMAGE
        x = gp.screenWidth / 2 - ((gp.tileSize * 2) / 2);
        y += gp.tileSize * 2;
        g2.drawImage(gp.player.down1, x, y, gp.tileSize * 2, gp.tileSize * 2, null);

        // MENU
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));

        text = "NEW GAME";
        x = getCenteredTextX(text);
        y += gp.tileSize * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - gp.tileSize, y);
        }

        text = "QUIT";
        x = getCenteredTextX(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        if (commandNum == 2) {
            g2.drawString(">", x - gp.tileSize, y);
        }
    }

    private void drawDialogScreen() {
        // WINDOW
        int x = gp.tileSize * 2;
        int y = gp.tileSize / 2;
        int width = gp.screenWidth - (gp.tileSize * 4);
        int height = gp.tileSize * 4;

        drawSubWindow(x, y, width, height, false);

        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32));
        x += gp.tileSize;
        y += gp.tileSize;

        for (String line : currentDialog.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height, boolean transition) {
        if (transition) {
            Color c = new Color(0, 0, 0, 225);
            g2.setColor(c);
            g2.fillRoundRect(x, y, width, height, 35, 35);

            c = new Color(243, 243, 243);
            g2.setStroke(new BasicStroke(8));
            g2.setColor(c);
            g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 35, 35);
        }

        else {
            Color c = new Color(0, 0, 0, 220);
            g2.setColor(c);
            g2.fillRoundRect(x, y, width, height, 35, 35);

            c = new Color(255, 255, 255);
            g2.setStroke(new BasicStroke(5));
            g2.setColor(c);
            g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 35, 35);
        }
    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
        String text = "PAUSED";
        int x = getCenteredTextX(text);
        int y = gp.screenHeight/2 - 48;

        g2.drawString(text, x, y);
    }

    public void drawWinScreen() {
        // WINDOW
        int x = gp.screenWidth / 2 - ((gp.tileSize * 5) / 2);
        int y = gp.screenHeight / 2 - ((gp.tileSize * 8) / 2);
        int width = gp.tileSize * 5;
        int height = gp.tileSize * 8;

        drawSubWindow(x, y, width, height, true);

        if (gp.player.dead) {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32));
            String text = "You Lose";
            x = getCenteredTextX(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);

            String text1 = "RESTART GAME";
            x = getCenteredTextX(text1);
            y += gp.tileSize * 4;
            g2.setColor(Color.WHITE);
            g2.drawString(text1, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize / 2, y);
            }
        }

        else {
            g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32));
            String text = "WIN";
            x = getCenteredTextX(text);
            y += gp.tileSize * 2;
            g2.drawString(text, x, y);

            String text1 = "RESTART GAME";
            x = getCenteredTextX(text1);
            y += gp.tileSize * 4;
            g2.setColor(Color.WHITE);
            g2.drawString(text1, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - gp.tileSize / 2, y);
            }
        }
    }

    public int getCenteredTextX(String text){
        int length = (int)g2.getFontMetrics().getStringBounds(text,g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
}
