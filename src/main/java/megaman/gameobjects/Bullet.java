package megaman.gameobjects;

public abstract class Bullet extends ParticularObject {
    public Bullet(float x, float y, float width, float height, float mass, int damage, GameWorld world) {
        super(x, y, width, height, mass, 1, world);
        setDamage(damage);
    }

    @Override
    public void update() {
        if (getState() == DEATH) return;
        setPosX(getPosX() + getSpeedX());
        setPosY(getPosY() + getSpeedY());
        ParticularObject target = getGameWorld().particularObjectManager.getCollisionWidthEnemyObject(this);
        if (target != null) {
            target.beHurt(getDamage());
            setState(DEATH);
        }
    }
}
