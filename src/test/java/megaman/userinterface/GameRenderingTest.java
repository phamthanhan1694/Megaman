package megaman.userinterface;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import org.junit.BeforeClass;
import org.junit.Test;

import megaman.effect.CacheDataLoader;
import megaman.gameobjects.GameWorld;
import megaman.gameobjects.ParticularObject;

public class GameRenderingTest {
    @BeforeClass
    public static void loadVisualResources() throws Exception {
        CacheDataLoader loader = CacheDataLoader.getInstance();
        loader.loadFrame();
        loader.loadAnimation();
        loader.loadPhysMap();
        loader.loadBackgroundMap();
        loader.loadEnemySpawns();
    }

    @Test
    public void mapRendersIntoBufferWithoutExplicitClip() {
        GameWorld world = new GameWorld();
        BufferedImage image = newImage();
        Graphics2D graphics = image.createGraphics();
        try {
            assertNull(graphics.getClipBounds());
            world.physicalMap.draw(graphics);
        } finally {
            graphics.dispose();
        }
        assertTrue("Map must draw visible tiles", differences(image, newImage()) > 100_000);
    }

    @Test
    public void mapUsesTileCoordinatesFromFrameResource() {
        GameWorld world = new GameWorld();
        BufferedImage image = newImage();
        Graphics2D graphics = image.createGraphics();
        try {
            world.physicalMap.draw(graphics);
        } finally {
            graphics.dispose();
        }
        CacheDataLoader loader = CacheDataLoader.getInstance();
        int tileId = loader.getBackgroundMap()[0][0];
        BufferedImage tile = loader.getFrameImage("tiled" + tileId).getImage();
        for (int y = 0; y < tile.getHeight(); y++) {
            for (int x = 0; x < tile.getWidth(); x++) {
                assertEquals("Map tile must use its configured sprite coordinates",
                        tile.getRGB(x, y), image.getRGB(x, y));
            }
        }
    }

    @Test
    public void mapRespectsPartialClip() {
        GameWorld world = new GameWorld();
        BufferedImage full = newImage();
        BufferedImage clipped = newImage();
        Rectangle clip = new Rectangle(125, 95, 250, 180);
        Graphics2D graphics = full.createGraphics();
        try {
            graphics.setClip(0, 0, full.getWidth(), full.getHeight());
            world.physicalMap.draw(graphics);
        } finally {
            graphics.dispose();
        }
        graphics = clipped.createGraphics();
        try {
            graphics.setClip(clip);
            world.physicalMap.draw(graphics);
        } finally {
            graphics.dispose();
        }
        for (int y = 0; y < clipped.getHeight(); y++) {
            for (int x = 0; x < clipped.getWidth(); x++) {
                assertEquals(clip.contains(x, y) ? full.getRGB(x, y) : 0, clipped.getRGB(x, y));
            }
        }
    }

    @Test
    public void firstPaintAlreadyContainsMapAndPlayer() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel();
            assertEquals(GameWorld.Mode.START, panel.gameWorld.getMode());
            assertTrue("First paint must not be a blank white window",
                    differences(newWhiteImage(), paintPanel(panel)) > 100_000);

            panel.gameWorld.startGameplay();
            panel.renderGame();
            assertPlayerVisible(panel);
        });
    }

    @Test
    public void movementAndJumpKeepRenderingPlayer() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel();
            panel.gameWorld.startGameplay();
            float startX = panel.gameWorld.megaman.getPosX();
            sendKey(panel, KeyEvent.VK_RIGHT, false);
            for (int i = 0; i < 10; i++) {
                panel.updateGame();
                panel.renderGame();
            }
            assertTrue(panel.gameWorld.megaman.getPosX() > startX);
            assertPlayerVisible(panel);
            sendKey(panel, KeyEvent.VK_RIGHT, true);
            assertEquals(0, panel.gameWorld.megaman.getSpeedX(), 0);

            sendKey(panel, KeyEvent.VK_LEFT, false);
            float rightX = panel.gameWorld.megaman.getPosX();
            panel.updateGame();
            panel.renderGame();
            assertTrue(panel.gameWorld.megaman.getPosX() < rightX);
            assertPlayerVisible(panel);
            sendKey(panel, KeyEvent.VK_LEFT, true);

            for (int i = 0; i < 150; i++) {
                panel.updateGame();
                panel.renderGame();
            }
            float groundY = panel.gameWorld.megaman.getPosY();
            panel.gameWorld.megaman.setIsJumping(false);
            panel.gameWorld.megaman.setIsLanding(false);
            panel.gameWorld.megaman.setSpeedY(0);
            sendKey(panel, KeyEvent.VK_SPACE, false);
            assertTrue(panel.gameWorld.megaman.getSpeedY() < 0);
            panel.updateGame();
            panel.renderGame();
            assertTrue(panel.gameWorld.megaman.getPosY() <= groundY);
            assertPlayerVisible(panel);
        });
    }

    @Test
    public void playerAttackCreatesBulletFromResourceAnimation() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel();
            panel.gameWorld.startGameplay();
            assertEquals(0, panel.gameWorld.bulletManager.size());
            sendKey(panel, KeyEvent.VK_A, false);
            assertEquals(1, panel.gameWorld.bulletManager.size());
            panel.renderGame();
            assertPlayerVisible(panel);
        });
    }

    @Test
    public void playerCanJumpImmediatelyWhenGameplayStarts() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel();
            panel.gameWorld.startGameplay();
            assertEquals(false, panel.gameWorld.megaman.getIsJumping());
            sendKey(panel, KeyEvent.VK_SPACE, false);
            assertTrue("Jump should start immediately after entering gameplay",
                    panel.gameWorld.megaman.getSpeedY() < 0);
        });
    }

    @Test
    public void enemiesAndBossFlowComeFromConfiguredResources() {
        GameWorld world = new GameWorld();
        assertEquals("Twelve configured enemies plus Megaman", 13, world.particularObjectManager.size());
        world.startGameplay();
        world.megaman.setPosX(3610);
        world.megaman.setPosY(400);
        world.update();
        assertEquals(GameWorld.Mode.BOSS_INTRO, world.getMode());
        assertTrue(world.camera.isLocked());
        assertTrue(world.getBoss() != null && world.getBoss().getTeamType() == ParticularObject.ENEMY_TEAM);
        assertEquals(1, world.physicalMap.phys_map[14][120]);
    }

    @Test
    public void menuIntroPauseAndResumeTransitionsWork() {
        GameWorld world = new GameWorld();
        assertEquals(GameWorld.Mode.START, world.getMode());
        world.confirm();
        assertEquals(GameWorld.Mode.INTRO, world.getMode());
        for (int i = 0; i < 8; i++) {
            world.confirm();
        }
        assertEquals(GameWorld.Mode.PLAYING, world.getMode());
        world.togglePause();
        assertEquals(GameWorld.Mode.PAUSED, world.getMode());
        world.confirm();
        assertEquals(GameWorld.Mode.PLAYING, world.getMode());
    }

    @Test
    public void enterKeyCanAdvancePastIntroAfterRelease() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GamePanel panel = new GamePanel();
            assertEquals(GameWorld.Mode.START, panel.gameWorld.getMode());
            sendKey(panel, KeyEvent.VK_ENTER, false);
            sendKey(panel, KeyEvent.VK_ENTER, true);
            assertEquals(GameWorld.Mode.INTRO, panel.gameWorld.getMode());
            for (int i = 0; i < 8; i++) {
                sendKey(panel, KeyEvent.VK_ENTER, false);
                sendKey(panel, KeyEvent.VK_ENTER, true);
            }
            assertEquals(GameWorld.Mode.PLAYING, panel.gameWorld.getMode());
        });
    }

    private static void sendKey(GamePanel panel, int code, boolean released) {
        Object action = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW)
                .get(KeyStroke.getKeyStroke(code, 0, released));
        panel.getActionMap().get(action).actionPerformed(new ActionEvent(panel, 0, "test"));
    }

    private static void assertPlayerVisible(GamePanel panel) {
        BufferedImage mapOnly = newImage();
        Graphics2D graphics = mapOnly.createGraphics();
        try {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, mapOnly.getWidth(), mapOnly.getHeight());
            graphics.setClip(0, 0, mapOnly.getWidth(), mapOnly.getHeight());
            graphics.translate(-panel.gameWorld.camera.getPosX(), -panel.gameWorld.camera.getPosY());
            panel.gameWorld.physicalMap.draw(graphics);
        } finally {
            graphics.dispose();
        }
        panel.renderGame();
        BufferedImage painted = paintPanel(panel);
        int playerX = (int) (panel.gameWorld.megaman.getPosX() - panel.gameWorld.camera.getPosX());
        int playerY = (int) (panel.gameWorld.megaman.getPosY() - panel.gameWorld.camera.getPosY());
        int changed = differencesInRegion(mapOnly, painted, playerX - 70, playerY - 90, 140, 170);
        assertTrue("Player sprite must be visible over the map; changed pixels: " + changed,
                changed > 100);
    }

    private static BufferedImage paintPanel(GamePanel panel) {
        panel.setSize(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
        BufferedImage image = newImage();
        Graphics2D graphics = image.createGraphics();
        try {
            panel.paint(graphics);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static BufferedImage newImage() {
        return new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    private static BufferedImage newWhiteImage() {
        BufferedImage image = newImage();
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static int differences(BufferedImage first, BufferedImage second) {
        int changed = 0;
        for (int y = 0; y < first.getHeight(); y++) {
            for (int x = 0; x < first.getWidth(); x++) {
                if (first.getRGB(x, y) != second.getRGB(x, y)) {
                    changed++;
                }
            }
        }
        return changed;
    }

    private static int differencesInRegion(BufferedImage first, BufferedImage second,
            int x, int y, int width, int height) {
        int changed = 0;
        int minX = Math.max(0, x);
        int minY = Math.max(0, y);
        int maxX = Math.min(first.getWidth(), x + width);
        int maxY = Math.min(first.getHeight(), y + height);
        for (int row = minY; row < maxY; row++) {
            for (int col = minX; col < maxX; col++) {
                if (first.getRGB(col, row) != second.getRGB(col, row)) {
                    changed++;
                }
            }
        }
        return changed;
    }
}
