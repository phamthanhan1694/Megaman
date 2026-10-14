package megaman.userinterface;

import java.io.IOException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import megaman.effect.CacheDataLoader;

public class GameFrame extends JFrame {
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 600;

    private final GamePanel gamePanel;

    public GameFrame() {
        setTitle("Megaman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        try {
            CacheDataLoader.getInstance().loadData();
        } catch (IOException e) {
            throw new IllegalStateException("Cannot load game data", e);
        }

        gamePanel = new GamePanel();
        add(gamePanel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                gamePanel.stopGame();
                CacheDataLoader.getInstance().closeSounds();
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    public void startGame() {
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                GameFrame gameFrame = new GameFrame();
                gameFrame.setVisible(true);
                gameFrame.startGame();
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Megaman", JOptionPane.ERROR_MESSAGE);
                throw e;
            }
        });
    }
}
