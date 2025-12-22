package megaman.gameobjects;

import java.awt.Graphics2D;

public class GameWorld {

    public Megaman megaman;
    public PhysicalMap physicalMap;

    public GameWorld() {
        // megaman = new Megaman(300, 300, 100, 100, 0.1f, this);
        megaman = new Megaman(300, 300, this);
        physicalMap = new PhysicalMap(0, 0, this);
    }

    public void update() {
        megaman.update();

    }

    public void render(Graphics2D g2) {
        physicalMap.draw(g2);
        megaman.draw(g2);
    }
}
