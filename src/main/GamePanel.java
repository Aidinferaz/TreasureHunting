package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public int tileSize = originalTileSize * scale; // 48*48 tile
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; // 748 pixels
    public int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    public int worldWidth = tileSize * maxWorldCol;
    public int worldHeight = tileSize * maxWorldRow;

    // FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler(this);
    Sound sound = new Sound();
    public CollisionChecker cCheker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    Thread gameThread;

    // ENTITY & OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10];

    // SET PLAYER'S DEFAULT POSITION
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame(){
        aSetter.setObject();
        playMusic(0);
    }

    public void zoomInOut(int i){
        int oldWorldWidth = tileSize * maxWorldCol;
        tileSize += i;
        int newWorldWidth = tileSize * maxWorldCol;

        double multiplier = (double)newWorldWidth / oldWorldWidth;

        System.out.println("tileSize: " + tileSize);
        System.out.println("newWorldWidth: " + newWorldWidth);
        System.out.println("Player worldX: " + player.worldX);

        double newPlayerWorldX = player.worldX * multiplier;
        double newPlayerWorldY = player.worldY * multiplier;

        player.speed = (double)newWorldWidth/600;

        for (int o = 0; o < obj.length; o++) {
            if (obj[o] != null) {
                double newObjectWorldX = obj[o].worldX * multiplier;
                double newObjectWorldY = obj[o].worldY * multiplier;

                obj[o].worldX = (int) newObjectWorldX;
                obj[o].worldY = (int) newObjectWorldY;
            }
        }

        player.worldX = newPlayerWorldX;
        player.worldY = newPlayerWorldY;
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void run(){
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while (gameThread != null){

            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += currentTime - lastTime;
            lastTime = currentTime;

            if (delta >= 1){
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000){
                // System.out.println("FPS: " + FPS);
                drawCount = 0;
                timer = 0;
            }
        }
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Tile
        tileM.draw(g2);

        // Object
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // Player
        player.draw(g2);

        g2.dispose();
    }

    public void playMusic(int i){
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public void stopMusic(){
        sound.stop();
    }

    public void playSoundEffect(int i){
        sound.setFile(i);
        sound.play();
    }
}
