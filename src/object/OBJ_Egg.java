package object;

import entity.Entity;
import main.GamePanel;

import java.util.Random;

public class OBJ_Egg extends Entity {
    public OBJ_Egg(GamePanel gp) {
        super(gp);
        name = "Egg";
        down1 = setup("/object/egg/egg_low");
        down2 = setup("/object/egg/egg_mid");
        up1 = setup("/object/egg/egg_low");
        up2 = setup("/object/egg/egg_mid");
        left1 = setup("/object/egg/egg_low");
        left2 = setup("/object/egg/egg_mid");
        right1 = setup("/object/egg/egg_low");
        right2 = setup("/object/egg/egg_mid");
    }

    @Override
    public void setAction() {
        actionLockCounter ++;

        if (actionLockCounter == 240){
            Random random = new Random();
            int i = random.nextInt(100) + 1; // pick up a number form 1 to 100

            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75) {
                direction = "right";
            }

            actionLockCounter = 0;
        }
    }
}
