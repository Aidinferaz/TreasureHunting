package object;

import main.GamePanel;

import javax.imageio.ImageIO;

public class OBJ_Heart extends SuperObject{
    GamePanel gp;

    public OBJ_Heart(GamePanel gp){
        this.gp = gp;
        name = "Heart";
        try{
            image3 = ImageIO.read(getClass().getResourceAsStream("/object/heart_blank.png"));
            image2 = ImageIO.read(getClass().getResourceAsStream("/object/heart_half.png"));
            image = ImageIO.read(getClass().getResourceAsStream("/object/heart_full.png"));
            image = uTool.scaleImage(image, gp.tileSize, gp.tileSize);
            image2 = uTool.scaleImage(image2, gp.tileSize, gp.tileSize);
            image3 = uTool.scaleImage(image3, gp.tileSize, gp.tileSize);
        } catch (Exception e){
            e.printStackTrace();
        }
        collision = true;
    }
}
