package megaman.userinterface;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import megaman.gameobjects.GameWorld;
import megaman.gameobjects.Megaman;

public class InputManager {

    private final GameWorld gameWorld;
    private final Set<Integer> pressedKeys = new HashSet<>();

    public InputManager(GameWorld gameWorld) {
        this.gameWorld = gameWorld;
    }

    public void processKeyPressed(int keyCode) {
        boolean firstPress = pressedKeys.add(keyCode);
        if (!firstPress) return;

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                if (gameWorld.getMode() == GameWorld.Mode.MENU) {
                    gameWorld.selectMenu(-1);
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (gameWorld.getMode() == GameWorld.Mode.MENU) {
                    gameWorld.selectMenu(1);
                }
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
                if (gameWorld.getMode() == GameWorld.Mode.MENU) {
                    gameWorld.selectMenu(-1);
                } else if (gameWorld.acceptsPlayerControl()) {
                    gameWorld.megaman.jump();
                }
                break;
            case KeyEvent.VK_SPACE:
                if (gameWorld.acceptsPlayerControl()) gameWorld.megaman.jump();
                break;
            case KeyEvent.VK_DOWN:
                if (gameWorld.getMode() == GameWorld.Mode.MENU) {
                    gameWorld.selectMenu(1);
                } else if (gameWorld.acceptsPlayerControl()) {
                    gameWorld.megaman.dick();
                }
                break;
            case KeyEvent.VK_A:
                if (gameWorld.acceptsPlayerControl()) gameWorld.megaman.attack();
                break;
            case KeyEvent.VK_ENTER:
                gameWorld.confirm();
                break;
            case KeyEvent.VK_ESCAPE:
                gameWorld.togglePause();
                break;
            default:
                break;
        }
    }

    public void processKeyReleased(int keyCode) {
        pressedKeys.remove(keyCode);
        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                updateHorizontalMovement();
                break;
            case KeyEvent.VK_RIGHT:
                updateHorizontalMovement();
                break;
            case KeyEvent.VK_DOWN:
                gameWorld.megaman.standUp();
                break;
            default:
                break;
        }
    }

    public void update() {
        if (!gameWorld.acceptsPlayerControl()) {
            gameWorld.megaman.stopRun();
            gameWorld.megaman.standUp();
            return;
        }
        updateHorizontalMovement();
        if (pressedKeys.contains(KeyEvent.VK_DOWN)) gameWorld.megaman.dick();
        if (pressedKeys.contains(KeyEvent.VK_A)) gameWorld.megaman.attack();
    }

    public void clear() {
        pressedKeys.clear();
        gameWorld.megaman.stopRun();
        gameWorld.megaman.standUp();
    }

    private void updateHorizontalMovement() {
        if (!gameWorld.acceptsPlayerControl()) return;
        boolean left = pressedKeys.contains(KeyEvent.VK_LEFT);
        boolean right = pressedKeys.contains(KeyEvent.VK_RIGHT);
        if (left == right) {
            gameWorld.megaman.stopRun();
        } else if (left) {
            gameWorld.megaman.setDirection(Megaman.LEFT_DIR);
            gameWorld.megaman.run();
        } else {
            gameWorld.megaman.setDirection(Megaman.RIGHT_DIR);
            gameWorld.megaman.run();
        }
    }
}
