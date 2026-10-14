package megaman.gameobjects;

public class Camera extends GameObject {
    private final float widthView;
    private final float heightView;
    private boolean locked;

    public Camera(float x, float y, float width, float height, GameWorld world) {
        super(x, y, world);
        widthView = width;
        heightView = height;
    }

    public void lock() { locked = true; }
    public void unlock() { locked = false; }
    public boolean isLocked() { return locked; }
    public float getWidthView() { return widthView; }
    public float getHeightView() { return heightView; }

    @Override
    public void update() {
        if (locked) return;
        Megaman player = getGameWorld().megaman;
        float x = player.getPosX() - getPosX();
        float y = player.getPosY() - getPosY();
        if (x > 400) setPosX(player.getPosX() - 400);
        if (x < 200) setPosX(player.getPosX() - 200);
        if (y > 400) setPosY(player.getPosY() - 400);
        if (y < 250) setPosY(player.getPosY() - 250);
        setPosX(Math.max(0, Math.min(getPosX(), getGameWorld().physicalMap.getMapWidth() - widthView)));
        setPosY(Math.max(0, Math.min(getPosY(), getGameWorld().physicalMap.getMapHeight() - heightView)));
    }
}
