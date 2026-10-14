package megaman.gameobjects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class ParticularObjectManager {
    protected final List<ParticularObject> particularObjects = new ArrayList<>();
    private final GameWorld gameWorld;

    public ParticularObjectManager(GameWorld world) { gameWorld = world; }
    public GameWorld getGameWorld() { return gameWorld; }
    public List<ParticularObject> getObjects() { return List.copyOf(particularObjects); }
    public int size() { return particularObjects.size(); }
    public void clear() { particularObjects.clear(); }

    public void addObject(ParticularObject object) {
        if (!particularObjects.contains(object)) particularObjects.add(object);
    }

    public void removeObject(ParticularObject object) { particularObjects.remove(object); }

    public ParticularObject getCollisionWidthEnemyObject(ParticularObject object) {
        for (ParticularObject candidate : particularObjects) {
            if (candidate != object && candidate.getTeamType() != object.getTeamType()
                    && candidate.getState() == ParticularObject.ALIVE
                    && object.getBoundForCollisionWithEnemy().intersects(candidate.getBoundForCollisionWithEnemy())) {
                return candidate;
            }
        }
        return null;
    }

    public void updateObjects() {
        for (ParticularObject object : List.copyOf(particularObjects)) {
            if (object == gameWorld.megaman || !object.isObjectOutOfCameraView()) object.update();
        }
        particularObjects.removeIf(object -> object.getState() == ParticularObject.DEATH);
    }

    public void draw(Graphics2D graphics) {
        for (ParticularObject object : particularObjects) {
            if (object.getState() != ParticularObject.DEATH && !object.isObjectOutOfCameraView()) {
                object.draw(graphics);
            }
        }
    }
}
