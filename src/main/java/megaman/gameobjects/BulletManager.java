package megaman.gameobjects;

public class BulletManager extends ParticularObjectManager {
    public BulletManager(GameWorld world) { super(world); }

    @Override
    public void updateObjects() {
        super.updateObjects();
        particularObjects.removeIf(ParticularObject::isObjectOutOfCameraView);
    }
}
