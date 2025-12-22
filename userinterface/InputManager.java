package megaman.userinterface;

import java.awt.event.KeyEvent;

import megaman.gameobjects.Megaman;
import megaman.gameobjects.GameWorld;

public class InputManager {

    private GamePanel gamePanel;
    private GameWorld gameWorld;

    // public InputManager(GamePanel gamePanel) {
    // this.gamePanel = gamePanel;
    // }
    public InputManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void processKeyPressed(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                System.out.println("Up key pressed");
                // gameWorld.physicalMap.y += 3;
                // gameWorld.megaman.setSpeedY(-3);
                // gameWorld.megaman.setPosY(gamePanel.gameWorld.megaman.getPosY() - 3);
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("Down key pressed");
                // gameWorld.physicalMap.y -= 3;
                // gameWorld.megaman.setSpeedY(3);
                // gameWorld.megaman.setPosY(gamePanel.gameWorld.megaman.getPosY() + 3);
                break;
            case KeyEvent.VK_LEFT:
                // System.out.println("Left key pressed");
                // gameWorld.megaman.setDirection(Megaman.DIR_LEFT);
                // gameWorld.megaman.setSpeedX(-5);
                gameWorld.megaman.setDirection(Megaman.LEFT_DIR);
                gameWorld.megaman.run();
                // gameWorld.physicalMap.x += 3;
                break;
            case KeyEvent.VK_RIGHT:
                // System.out.println("Right key pressed");
                // gameWorld.megaman.setDirection(Megaman.DIR_RIGHT);
                // gameWorld.megaman.setSpeedX(5);
                gameWorld.megaman.setDirection(Megaman.RIGHT_DIR);
                gameWorld.megaman.run();
                // gameWorld.physicalMap.x -= 3;
                break;
            case KeyEvent.VK_ENTER:
                System.out.println("Enter key pressed");
                break;
            case KeyEvent.VK_SPACE:
                // System.out.println("Space key pressed");
                gameWorld.megaman.setSpeedY(-3);
                // gameWorld.megaman.setPosY(gamePanel.gameWorld.megaman.getPosY() - 3);
                gameWorld.megaman.setPosY(gameWorld.megaman.getPosY() - 3);
                break;
            case KeyEvent.VK_A:
                break;
        }
    }

    public void processKeyReleased(int keyCode) {
        switch (keyCode) {
            case KeyEvent.VK_UP:
                System.out.println("Up key released");
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("Down key released");

                break;
            case KeyEvent.VK_LEFT:
                System.out.println("Left key released");
                // gamePanel.gameWorld.megaman.setSpeedX(0);
                gameWorld.megaman.setSpeedX(0);
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("Right key released");
                gameWorld.megaman.setSpeedX(0);
                break;
            case KeyEvent.VK_ENTER:
                System.out.println("Enter key released");
                break;
            case KeyEvent.VK_SPACE:
                System.out.println("Space key released");
                break;
            case KeyEvent.VK_A:
                break;
        }
    }
}
