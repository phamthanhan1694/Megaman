package megaman.gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import megaman.effect.CacheDataLoader;
import megaman.userinterface.GameFrame;

public class BackgroundMap extends GameObject {
    public final int[][] map;
    private final int tileSize = 30;
    private final Map<Integer, BufferedImage> tiles = new HashMap<>();

    public BackgroundMap(float x, float y, GameWorld gameWorld) {
        super(x, y, gameWorld);
        CacheDataLoader loader = CacheDataLoader.getInstance();
        map = loader.getBackgroundMap();
        for (int[] row : map) {
            for (int tileId : row) {
                if (tileId > 0) {
                    tiles.computeIfAbsent(tileId, id -> loader.getFrameImage("tiled" + id).getImage());
                }
            }
        }
    }

    @Override
    public void update() {
    }

    public void draw(Graphics2D graphics) {
        Camera camera = getGameWorld().camera;
        Rectangle view = new Rectangle((int) camera.getPosX(), (int) camera.getPosY(),
                GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
        int startRow = Math.max(0, view.y / tileSize);
        int endRow = Math.min(map.length - 1, (view.y + view.height) / tileSize + 1);
        int startCol = Math.max(0, view.x / tileSize);
        int endCol = Math.min(map[0].length - 1, (view.x + view.width) / tileSize + 1);
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                int tileId = map[row][col];
                if (tileId > 0) {
                    graphics.drawImage(tiles.get(tileId),
                            (int) getPosX() + col * tileSize - (int) camera.getPosX(),
                            (int) getPosY() + row * tileSize - (int) camera.getPosY(),
                            tileSize, tileSize, null);
                }
            }
        }
    }
}
