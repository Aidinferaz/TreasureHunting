package tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;
        tile = new Tile[50];
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
        setupTileImage();
        loadMap("/maps/tilesmapcoba.txt");
    }

    public void getTileImage(){
            setup(0, "grass00", false);
            setup(1, "grass00", false);
            setup(2, "grass00", false);
            setup(3, "grass00", false);
            setup(4, "grass00", false);
            setup(5, "grass00", false);
            setup(6, "grass00", false);
            setup(7, "grass00", false);
            setup(8, "grass00", false);
            setup(9, "grass00", false);
            setup(10, "grass00", false);
            setup(11, "grass01", false);
            setup(12, "water00", true);
            setup(13, "water01", true);
            setup(14, "water02", true);
            setup(15, "water03", true);
            setup(16, "water04", true);
            setup(17, "water05", true);
            setup(18, "water06", true);
            setup(19, "water07", true);
            setup(20, "water08", true);
            setup(21, "water09", true);
            setup(22, "water10", true);
            setup(23, "water11", true);
            setup(24, "water12", true);
            setup(25, "water13", true);
            setup(26, "road00", false);
            setup(27, "road01", false);
            setup(28, "road02", false);
            setup(29, "road03", false);
            setup(30, "road04", false);
            setup(31, "road05", false);
            setup(32, "road06", false);
            setup(33, "road07", false);
            setup(34, "road08", false);
            setup(35, "road09", false);
            setup(36, "road10", false);
            setup(37, "road11", false);
            setup(38, "road12", false);
            setup(39, "earth", false);
            setup(40, "wall", true);
            setup(41, "tree", true);
    }

    public void setupTileImage() {
        setup(0, "Tilesets/000", false);
        setup(1, "Tilesets/001", false);
        setup(2, "Tilesets/002", false);
        setup(3, "Tilesets/003", false);
        setup(4, "Tilesets/004", false);
        setup(5, "Tilesets/005", false);
        setup(6, "Tilesets/006", false);
        setup(7, "Tilesets/007", false);
        setup(8, "Tilesets/008", false);
        setup(9, "Tilesets/009", false);
        setup(10, "Tilesets/010", false);
        setup(11, "Tilesets/011", false);
        setup(12, "Tilesets/012", false);
        setup(13, "Tilesets/013", false);
        setup(14, "Tilesets/014", false);
        setup(15, "Tilesets/015", false);
        setup(16, "Tilesets/016", true);
        setup(17, "Tilesets/017", false);
        setup(18, "Tilesets/018", true);
        setup(19, "Tilesets/019", true);
        setup(20, "Tilesets/020", true);
        setup(21, "Tilesets/021", true);
        setup(22, "Tilesets/022", true);
        setup(23, "Tilesets/023", true);
        setup(24, "Tilesets/024", true);
        setup(25, "Tilesets/025", true);
        setup(26, "Tilesets/026", true);
        setup(27, "Tilesets/027", true);
        setup(28, "Tilesets/028", true);
        setup(29, "Tilesets/029", true);
        setup(30, "Tilesets/030", true);
        setup(31, "Tilesets/031", true);
        setup(32, "Tilesets/032", true);
        setup(33, "Tilesets/033", true);
        setup(34, "Tilesets/034", true);
        setup(35, "Tilesets/035", false);
        setup(36, "Tilesets/036", false);
        setup(37, "Tilesets/037", false);
    }

    public void setup(int index, String imageName, boolean collision){
        UtilityTool uTool = new UtilityTool();
        try{
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" +imageName +".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, gp.tileSize, gp.tileSize);
            tile[index].collision = collision;
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            int col = 0;
            int row = 0;

            while (col < gp.maxWorldCol && row < gp.maxWorldRow){
                String line = br.readLine();

                while (col < gp.maxWorldCol) {
                    String[] numbers = line.split(" ");

                    int num = Integer.parseInt(numbers[col]);

                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gp.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            br.close();
        } catch (Exception e) {}
    }

    public void draw(Graphics2D g2){
        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow){
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gp.tileSize;
            int worldY = worldRow * gp.tileSize;
            double screenX = worldX - gp.player.worldX + gp.player.screenX;
            double screenY = worldY - gp.player.worldY + gp.player.screenY;

            if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
                g2.drawImage(tile[tileNum].image, (int)screenX, (int)screenY,null);
            }
            worldCol++;

            if (worldCol == gp.maxWorldCol){
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
