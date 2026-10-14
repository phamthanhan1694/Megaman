package megaman.control;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Button {
    public static final int NONE = 0;
    public static final int PRESSED = 1;
    public static final int HOVER = 2;

    protected final String text;
    protected final int posX;
    protected final int posY;
    protected final int width;
    protected final int height;
    protected final int paddingTextX;
    protected final int paddingTextY;
    protected boolean enabled = true;
    protected int state;
    protected Color bgColor;
    protected Color pressedBgColor;
    protected Color hoverBgColor;

    public Button(String text, int posX, int posY, int width, int height,
            int paddingTextX, int paddingTextY, Color bgColor) {
        this.text = text;
        this.posX = posX;
        this.posY = posY;
        this.width = width;
        this.height = height;
        this.paddingTextX = paddingTextX;
        this.paddingTextY = paddingTextY;
        this.bgColor = bgColor;
        this.pressedBgColor = bgColor.darker();
        this.hoverBgColor = bgColor.brighter();
    }

    public void setEnable(boolean enabled) { this.enabled = enabled; }
    public void setState(int state) { this.state = state; }
    public void setBgColor(Color color) { bgColor = color; }
    public void setHoverBgColor(Color color) { hoverBgColor = color; }
    public void setPressedBgColor(Color color) { pressedBgColor = color; }

    public abstract boolean isInButton(int x, int y);
    public abstract void draw(Graphics graphics);
}
