package megaman.control;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class RectangleButton extends Button {
    public RectangleButton(String text, int posX, int posY, int width, int height,
            int paddingTextX, int paddingTextY, Color bgColor) {
        super(text, posX, posY, width, height, paddingTextX, paddingTextY, bgColor);
    }

    @Override
    public boolean isInButton(int x, int y) {
        return enabled && x >= posX && x <= posX + width && y >= posY && y <= posY + height;
    }

    @Override
    public void draw(Graphics graphics) {
        if (!enabled) {
            graphics.setColor(Color.GRAY);
        } else if (state == PRESSED) {
            graphics.setColor(pressedBgColor);
        } else if (state == HOVER) {
            graphics.setColor(hoverBgColor);
        } else {
            graphics.setColor(bgColor);
        }
        graphics.fillRect(posX, posY, width, height);
        graphics.setColor(Color.WHITE);
        graphics.drawRect(posX, posY, width, height);
        graphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        graphics.drawString(text, posX + paddingTextX, posY + paddingTextY);
    }
}
