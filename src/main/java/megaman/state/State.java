package megaman.state;

import java.awt.image.BufferedImage;

import megaman.userinterface.GamePanel;

public abstract class State {
    protected final GamePanel gamePanel;

    protected State(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public abstract void Update();
    public abstract void Render();
    public abstract BufferedImage getBufferedImage();
    public abstract void setPressedButton(int code);
    public abstract void setReleasedButton(int code);
}
