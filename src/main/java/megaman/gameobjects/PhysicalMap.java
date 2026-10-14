package megaman.gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import megaman.effect.CacheDataLoader;

public class PhysicalMap extends GameObject {
    public int[][] phys_map;
    private final int[][] backgroundMap;
    private final int tileSize;
    private final Map<Integer, BufferedImage> tiles = new HashMap<>();

    public PhysicalMap(float x, float y, GameWorld gameWorld) {
        super(x, y, gameWorld);
        this.tileSize = 30;
        CacheDataLoader loader = CacheDataLoader.getInstance();
        this.phys_map = loader.getPhysicalMap();
        this.backgroundMap = loader.getBackgroundMap();
        for (int[] row : backgroundMap) {
            for (int tileId : row) {
                if (tileId > 0) {
                    tiles.computeIfAbsent(tileId, id -> loader.getFrameImage("tiled" + id).getImage());
                }
            }
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMapWidth() { return phys_map[0].length * tileSize; }
    public int getMapHeight() { return phys_map.length * tileSize; }

    public void setTile(int row, int column, int collision, int tileId) {
        phys_map[row][column] = collision;
        backgroundMap[row][column] = tileId;
        if (tileId > 0) tiles.computeIfAbsent(tileId,
                id -> CacheDataLoader.getInstance().getFrameImage("tiled" + id).getImage());
    }

    public void draw(Graphics2D g2) {
        Rectangle clip = g2.getClipBounds();
        if (clip == null) {
            // Offscreen image graphics have no explicit clip by default.
            clip = new Rectangle((int) getPosX(), (int) getPosY(),
                    backgroundMap[0].length * tileSize, backgroundMap.length * tileSize);
        }
        int startRow = Math.max(0, (clip.y - (int) getPosY()) / tileSize);
        int endRow = Math.min(backgroundMap.length - 1, (clip.y + clip.height - (int) getPosY()) / tileSize + 1);
        int startCol = Math.max(0, (clip.x - (int) getPosX()) / tileSize);
        int endCol = Math.min(backgroundMap[0].length - 1, (clip.x + clip.width - (int) getPosX()) / tileSize + 1);

        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int tileId = backgroundMap[row][col];
                if (tileId <= 0) {
                    continue;
                }

                int dx = (int) getPosX() + col * tileSize;
                int dy = (int) getPosY() + row * tileSize;
                g2.drawImage(tiles.get(tileId), dx, dy, tileSize, tileSize, null);
            }
        }
    }

    public Rectangle haveCollisionWithLand(Rectangle rect) {
        int posX1 = rect.x / tileSize - 2;
        int posX2 = (rect.x + rect.width) / tileSize + 2;
        int posY1 = Math.max(0, (rect.y + rect.height) / tileSize);

        posX1 = Math.max(0, posX1);
        posX2 = Math.min(phys_map[0].length - 1, posX2);

        for (int y = posY1; y < phys_map.length; y++) {
            for (int x = posX1; x <= posX2; x++) {
                if (phys_map[y][x] == 1) {
                    Rectangle r = tileRectangle(x, y);
                    if (rect.intersects(r)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public Rectangle haveCollisionWithTop(Rectangle rect) {
        int posX1 = rect.x / tileSize - 2;
        int posX2 = (rect.x + rect.width) / tileSize + 2;
        int posY1 = Math.min(phys_map.length - 1, rect.y / tileSize);

        posX1 = Math.max(0, posX1);
        posX2 = Math.min(phys_map[0].length - 1, posX2);

        for (int y = posY1; y >= 0; y--) {
            for (int x = posX1; x <= posX2; x++) {
                if (phys_map[y][x] == 1) {
                    Rectangle r = tileRectangle(x, y);
                    if (rect.intersects(r)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public Rectangle haveCollisionWithRightWall(Rectangle rect) {
        int posY1 = rect.y / tileSize - 2;
        int posY2 = (rect.y + rect.height) / tileSize + 2;
        int posX1 = (rect.x + rect.width) / tileSize;
        int posX2 = posX1 + 3;

        posY1 = Math.max(0, posY1);
        posY2 = Math.min(phys_map.length - 1, posY2);
        posX1 = Math.max(0, posX1);
        posX2 = Math.min(phys_map[0].length - 1, posX2);

        for (int x = posX1; x <= posX2; x++) {
            for (int y = posY1; y <= posY2; y++) {
                if (phys_map[y][x] == 1) {
                    Rectangle r = tileRectangle(x, y);
                    if (r.y < rect.y + rect.height - 1 && rect.intersects(r)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public Rectangle haveCollisionWithLeftWall(Rectangle rect) {
        int posY1 = rect.y / tileSize - 2;
        int posY2 = (rect.y + rect.height) / tileSize + 2;
        int posX1 = rect.x / tileSize;
        int posX2 = posX1 - 3;

        posY1 = Math.max(0, posY1);
        posY2 = Math.min(phys_map.length - 1, posY2);
        posX1 = Math.min(phys_map[0].length - 1, Math.max(0, posX1));
        posX2 = Math.max(0, posX2);

        for (int x = posX1; x >= posX2; x--) {
            for (int y = posY1; y <= posY2; y++) {
                if (phys_map[y][x] == 1) {
                    Rectangle r = tileRectangle(x, y);
                    if (r.y < rect.y + rect.height - 1 && rect.intersects(r)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void update() {
    }

    private Rectangle tileRectangle(int x, int y) {
        return new Rectangle((int) getPosX() + x * tileSize, (int) getPosY() + y * tileSize, tileSize, tileSize);
    }
}
