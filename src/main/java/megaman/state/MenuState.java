package megaman.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import megaman.control.Button;
import megaman.control.RectangleButton;
import megaman.userinterface.GameFrame;
import megaman.userinterface.GamePanel;

public class MenuState extends State {
    private static final int NUMBER_OF_BUTTONS = 2;

    private final BufferedImage bufferedImage;
    private final Button[] buttons = new Button[NUMBER_OF_BUTTONS];
    private int buttonSelected;

    public MenuState(GamePanel gamePanel) {
        super(gamePanel);
        bufferedImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        buttons[0] = new RectangleButton("NEW GAME", 430, 275, 140, 40, 27, 25, Color.ORANGE);
        buttons[1] = new RectangleButton("EXIT", 430, 325, 140, 40, 50, 25, Color.ORANGE);
        for (Button button : buttons) {
            button.setHoverBgColor(new Color(18, 150, 210));
            button.setPressedBgColor(new Color(30, 190, 80));
        }
    }

    @Override
    public void Update() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setState(i == buttonSelected ? Button.HOVER : Button.NONE);
        }
    }

    @Override
    public void Render() {
        Graphics2D graphics = bufferedImage.createGraphics();
        try {
            gamePanel.gameWorld.render(graphics);
            for (Button button : buttons) button.draw(graphics);
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
            case KeyEvent.VK_UP -> {
                buttonSelected = Math.floorMod(buttonSelected - 1, NUMBER_OF_BUTTONS);
                gamePanel.gameWorld.selectMenu(-1);
            }
            case KeyEvent.VK_DOWN -> {
                buttonSelected = Math.floorMod(buttonSelected + 1, NUMBER_OF_BUTTONS);
                gamePanel.gameWorld.selectMenu(1);
            }
            case KeyEvent.VK_ENTER -> gamePanel.gameWorld.confirm();
            default -> {
            }
        }
    }

    @Override
    public void setReleasedButton(int code) {
    }
}
