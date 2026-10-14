package megaman.userinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import megaman.effect.CacheDataLoader;
import megaman.gameobjects.GameWorld;

public class GamePanel extends JPanel {

    private final Timer timer;
    private final InputManager inputManager;
    private final BufferedImage bufImage;

    public final GameWorld gameWorld;

    public GamePanel() {
        gameWorld = new GameWorld();
        inputManager = new InputManager(gameWorld);
        setPreferredSize(new Dimension(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT));
        setFocusable(true);
        installKeyBindings();
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                inputManager.clear();
                gameWorld.pause();
            }
        });

        bufImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        timer = new Timer(13, e -> tick());
        renderGame();
    }

    @Override
    protected synchronized void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bufImage, 0, 0, this);
    }

    public void startGame() {
        requestFocusInWindow();
        if (!timer.isRunning()) timer.start();
    }

    public void stopGame() {
        timer.stop();
        inputManager.clear();
    }

    public void updateGame() {
        inputManager.update();
        gameWorld.update();
        if (gameWorld.isExitRequested()) {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) window.dispose();
        }
    }

    public synchronized void renderGame() {
        Graphics2D g2 = bufImage.createGraphics();
        try {
            g2.setClip(0, 0, bufImage.getWidth(), bufImage.getHeight());
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
            gameWorld.render(g2);
        } finally {
            g2.dispose();
        }
    }

    private void tick() {
        updateGame();
        renderGame();
        repaint();
    }

    private void installKeyBindings() {
        bind("leftPressed", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), KeyEvent.VK_LEFT, true);
        bind("leftReleased", KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), KeyEvent.VK_LEFT, false);
        bind("rightPressed", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), KeyEvent.VK_RIGHT, true);
        bind("rightReleased", KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), KeyEvent.VK_RIGHT, false);
        bind("upPressed", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), KeyEvent.VK_UP, true);
        bind("upReleased", KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), KeyEvent.VK_UP, false);
        bind("wPressed", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), KeyEvent.VK_W, true);
        bind("wReleased", KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), KeyEvent.VK_W, false);
        bind("spacePressed", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), KeyEvent.VK_SPACE, true);
        bind("spaceReleased", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), KeyEvent.VK_SPACE, false);
        bind("downPressed", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), KeyEvent.VK_DOWN, true);
        bind("downReleased", KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), KeyEvent.VK_DOWN, false);
        bind("attackPressed", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), KeyEvent.VK_A, true);
        bind("attackReleased", KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), KeyEvent.VK_A, false);
        bind("enterPressed", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), KeyEvent.VK_ENTER, true);
        bind("enterReleased", KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), KeyEvent.VK_ENTER, false);
        bind("escapePressed", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false), KeyEvent.VK_ESCAPE, true);
        bind("escapeReleased", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), KeyEvent.VK_ESCAPE, false);
    }

    private void bind(String name, KeyStroke keyStroke, int keyCode, boolean pressed) {
        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();
        inputMap.put(keyStroke, name);
        actionMap.put(name, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (pressed) {
                    inputManager.processKeyPressed(keyCode);
                } else {
                    inputManager.processKeyReleased(keyCode);
                }
            }
        });
    }
}
