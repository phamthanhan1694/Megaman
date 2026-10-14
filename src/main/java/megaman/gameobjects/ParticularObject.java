package megaman.gameobjects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import megaman.effect.Animation;

public abstract class ParticularObject extends GameObject {

    // public ParticularObject(float posX, float posY, GameWorld gameWorld) {
    public ParticularObject(float posX, float posY, float width, float height, float mass, int blood,
            GameWorld gameWorld) {
        super(posX, posY, gameWorld);
        // x, y, width, height, mass, blood, gameWorld
        this.width = width;
        this.height = height;
        this.mass = mass;
        this.blood = blood;
        this.direction = RIGHT_DIR;
        // TODO Auto-generated constructor stub
    }

    public static final int LEAGUE_TEAM = 1;
    public static final int ENEMY_TEAM = 2;

    public static final int LEFT_DIR = 0;
    public static final int RIGHT_DIR = 1;

    public static final int ALIVE = 0;
    public static final int BEHURT = 1;
    public static final int FEY = 2;
    public static final int DEATH = 3;
    public static final int NOBEHURT = 4;
    private int state = ALIVE;

    private float width;
    private float height;
    private float mass;
    private float speedX;
    private float speedY;
    private int blood;

    private int damage;

    private int direction;
    protected Animation behurtForwardAnim, behurtBackAnim;

    private int teamType;

    private long startTimeNoBeHurt;
    private long timeForNoBeHurt;

    public static int getLeagueTeam() {
        return LEAGUE_TEAM;
    }

    public static int getEnemyTeam() {
        return ENEMY_TEAM;
    }

    public static int getLeftDir() {
        return LEFT_DIR;
    }

    public static int getRightDir() {
        return RIGHT_DIR;
    }

    public static int getAlive() {
        return ALIVE;
    }

    public static int getBehurt() {
        return BEHURT;
    }

    public static int getFey() {
        return FEY;
    }

    public static int getDeath() {
        return DEATH;
    }

    public static int getNobehurt() {
        return NOBEHURT;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        if (state == NOBEHURT) startTimeNoBeHurt = System.nanoTime();
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public int getBlood() {
        return blood;
    }

    public void setBlood(int blood) {
        this.blood = Math.max(0, blood);
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public Animation getBehurtForwardAnim() {
        return behurtForwardAnim;
    }

    public void setBehurtForwardAnim(Animation behurtForwardAnim) {
        this.behurtForwardAnim = behurtForwardAnim;
    }

    public Animation getBehurtBackAnim() {
        return behurtBackAnim;
    }

    public void setBehurtBackAnim(Animation behurtBackAnim) {
        this.behurtBackAnim = behurtBackAnim;
    }

    public int getTeamType() {
        return teamType;
    }

    public void setTeamType(int teamType) {
        this.teamType = teamType;
    }

    public long getStartTimeNoBeHurt() {
        return startTimeNoBeHurt;
    }

    public void setStartTimeNoBeHurt(long startTimeNoBeHurt) {
        this.startTimeNoBeHurt = startTimeNoBeHurt;
    }

    public long getTimeForNoBeHurt() {
        return timeForNoBeHurt;
    }

    public void setTimeForNoBeHurt(long timeForNoBeHurt) {
        this.timeForNoBeHurt = timeForNoBeHurt;
    }

    @Override
    public void update() {
        switch (state) {
            case ALIVE:
                ParticularObject enemy = getGameWorld().particularObjectManager.getCollisionWidthEnemyObject(this);
                if (enemy != null && enemy.getDamage() > 0) beHurt(enemy.getDamage());
                if (getBlood() == 0) state = FEY;
                break;
            case BEHURT:
                if (behurtBackAnim == null) {
                    state = NOBEHURT;
                    startTimeNoBeHurt = System.nanoTime();
                    if (getBlood() == 0)
                        state = FEY;
                } else {
                    behurtForwardAnim.update(System.nanoTime());
                    if (behurtForwardAnim.isLastFrame()) {
                        behurtForwardAnim.reset();
                        state = NOBEHURT;
                        if (getBlood() == 0)
                            state = FEY;
                        startTimeNoBeHurt = System.nanoTime();
                    }
                }
                break;
            case FEY:
                state = DEATH;
                break;
            case DEATH:
                break;
            case NOBEHURT:
                if (System.nanoTime() - startTimeNoBeHurt > timeForNoBeHurt)
                    state = ALIVE;
                break;
        }
    }

    public abstract Rectangle getBoundForCollisionWithEnemy();

    public abstract void draw(Graphics2D g2);

    public void hurtingCallback() {
    };

    public abstract void attack();

    public Rectangle getBoundForCollisionWithMap() {
        Rectangle bound = new Rectangle();
        bound.x = (int) (getPosX() - (getWidth() / 2));
        bound.y = (int) (getPosY() - (getHeight() / 2));
        bound.width = (int) getWidth();
        bound.height = (int) getHeight();
        return bound;
    }

    public void beHurt(int damageEat) {
        if (state != ALIVE || damageEat <= 0) return;
        setBlood(getBlood() - damageEat);
        state = BEHURT;
        if (behurtForwardAnim != null) behurtForwardAnim.reset();
        if (behurtBackAnim != null) behurtBackAnim.reset();
        hurtingCallback();
    }

    public boolean isObjectOutOfCameraView() {
        Camera camera = getGameWorld().camera;
        return getPosX() + getWidth() < camera.getPosX()
                || getPosX() - getWidth() > camera.getPosX() + camera.getWidthView()
                || getPosY() + getHeight() < camera.getPosY()
                || getPosY() - getHeight() > camera.getPosY() + camera.getHeightView();
    }

    public void drawBoundForCollisionWithMap(Graphics2D g2) {
        Rectangle rect = getBoundForCollisionWithMap();
        g2.setColor(Color.blue);
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    public void drawBoundForCollisionWithEnemy(Graphics2D g2) {
        Rectangle rect = getBoundForCollisionWithEnemy();
        g2.setColor(Color.red);
        g2.drawRect(rect.x, rect.y, rect.width, rect.height);
    }
}
