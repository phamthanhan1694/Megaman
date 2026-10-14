package megaman.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import megaman.gameobjects.BackgroundMap;
import megaman.gameobjects.BulletManager;
import megaman.gameobjects.Camera;
import megaman.gameobjects.GameWorld;
import megaman.gameobjects.Megaman;
import megaman.gameobjects.ParticularObjectManager;
import megaman.gameobjects.PhysicalMap;
import megaman.userinterface.GameFrame;
import megaman.userinterface.GamePanel;

public class GameWorldState extends State {
    public static final int finalBossX = 3600;
    public static final int INIT_GAME = 0;
    public static final int TUTORIAL = 1;
    public static final int GAMEPLAY = 2;
    public static final int GAMEOVER = 3;
    public static final int GAMEWIN = 4;
    public static final int PAUSEGAME = 5;
    public static final int INTROGAME = 0;
    public static final int MEETFINALBOSS = 1;

    private final BufferedImage bufferedImage;
    private final GameWorld world;

    public ParticularObjectManager particularObjectManager;
    public BulletManager bulletManager;
    public Megaman megaMan;
    public PhysicalMap physicalMap;
    public BackgroundMap backgroundMap;
    public Camera camera;

    public GameWorldState(GamePanel gamePanel) {
        super(gamePanel);
        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        world = gamePanel.gameWorld;
        refreshAliases();
    }

    @Override
    public void Update() {
        world.update();
        refreshAliases();
    }

    @Override
    public void Render() {
        Graphics2D graphics = bufferedImage.createGraphics();
        try {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
            world.render(graphics);
        } finally {
            graphics.dispose();
        }
    }

    @Override
    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    @Override
    public void setPressedButton(int code) {
        switch (code) {
            case KeyEvent.VK_ENTER -> world.confirm();
            case KeyEvent.VK_ESCAPE -> world.togglePause();
            case KeyEvent.VK_SPACE, KeyEvent.VK_UP -> {
                if (world.acceptsPlayerControl()) world.megaman.jump();
            }
            case KeyEvent.VK_DOWN -> {
                if (world.acceptsPlayerControl()) world.megaman.dick();
            }
            case KeyEvent.VK_RIGHT -> {
                if (world.acceptsPlayerControl()) {
                    world.megaman.setDirection(Megaman.RIGHT_DIR);
                    world.megaman.run();
                }
            }
            case KeyEvent.VK_LEFT -> {
                if (world.acceptsPlayerControl()) {
                    world.megaman.setDirection(Megaman.LEFT_DIR);
                    world.megaman.run();
                }
            }
            case KeyEvent.VK_A -> {
                if (world.acceptsPlayerControl()) world.megaman.attack();
            }
            default -> {
            }
        }
    }

    @Override
    public void setReleasedButton(int code) {
        switch (code) {
            case KeyEvent.VK_DOWN -> world.megaman.standUp();
            case KeyEvent.VK_RIGHT -> {
                if (world.megaman.getSpeedX() > 0) world.megaman.stopRun();
            }
            case KeyEvent.VK_LEFT -> {
                if (world.megaman.getSpeedX() < 0) world.megaman.stopRun();
            }
            default -> {
            }
        }
    }

    private void refreshAliases() {
        particularObjectManager = world.particularObjectManager;
        bulletManager = world.bulletManager;
        megaMan = world.megaman;
        physicalMap = world.physicalMap;
        backgroundMap = new BackgroundMap(0, 0, world);
        camera = world.camera;
    }
}
