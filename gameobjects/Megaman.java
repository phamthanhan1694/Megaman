package megaman.gameobjects;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import megaman.effect.Animation;
import megaman.effect.CacheDataLoader;
// import rockman-master.rockman-master.src.com.gamestudio.gameobject.BlueFire;
// import rockman-master.rockman-master.src.com.gamestudio.gameobject.Bullet;

public class Megaman extends Human {

    // private float posX;
    // private float posY;

    // private float width;
    // private float height;
    // private float mass;

    // public static int DIR_LEFT;
    // public static int DIR_RIGHT;

    // private float speedX;
    // private float speedY;

    // private int direction;

    // GameWorld gameWorld;
    public static final int RUNSPEED = 3;

    private Animation runForwardAnim, runBackAnim, runShootingForwardAnim, runShootingBackAnim;
    private Animation idleForwardAnim, idleBackAnim, idleShootingForwardAnim, idleShootingBackAnim;
    private Animation dickForwardAnim, dickBackAnim;
    private Animation flyForwardAnim, flyBackAnim, flyShootingForwardAnim, flyShootingBackAnim;
    private Animation landingForwardAnim, landingBackAnim;

    private Animation climWallForward, climWallBack;

    private long lastShootingTime;
    private boolean isShooting = false;

    // private AudioClip hurtingSound;
    // private AudioClip shooting1;

    // public Megaman(float posX, float posY, float width, float height, float mass,
    // GameWorld gameWorld) {
    // this.posX = posX;
    // this.posY = posY;
    // this.width = width;
    // this.height = height;
    // this.mass = mass;
    // this.gameWorld = gameWorld;
    // }

    public Megaman(float x, float y, GameWorld gameWorld) {
        super(x, y, 70, 90, 0.1f, 100, gameWorld);

        // shooting1 = CacheDataLoader.getInstance().getSound("bluefireshooting");
        // hurtingSound = CacheDataLoader.getInstance().getSound("megamanhurt");

        setTeamType(LEAGUE_TEAM);

        setTimeForNoBeHurt(2000000000);

        // runForwardAnim = CacheDataLoader.getInstance().getAnimation("run");
        // runBackAnim = CacheDataLoader.getInstance().getAnimation("run");
        // runBackAnim.flipAllImage();

        // idleForwardAnim = CacheDataLoader.getInstance().getAnimation("idle");
        // idleBackAnim = CacheDataLoader.getInstance().getAnimation("idle");
        // idleBackAnim.flipAllImage();

        // dickForwardAnim = CacheDataLoader.getInstance().getAnimation("dick");
        // dickBackAnim = CacheDataLoader.getInstance().getAnimation("dick");
        // dickBackAnim.flipAllImage();

        // flyForwardAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        // flyForwardAnim.setIsRepeated(false);
        // flyBackAnim = CacheDataLoader.getInstance().getAnimation("flyingup");
        // flyBackAnim.setIsRepeated(false);
        // flyBackAnim.flipAllImage();

        // landingForwardAnim = CacheDataLoader.getInstance().getAnimation("landing");
        // landingBackAnim = CacheDataLoader.getInstance().getAnimation("landing");
        // landingBackAnim.flipAllImage();

        // climWallBack = CacheDataLoader.getInstance().getAnimation("clim_wall");
        // climWallForward = CacheDataLoader.getInstance().getAnimation("clim_wall");
        // climWallForward.flipAllImage();

        // behurtForwardAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        // behurtBackAnim = CacheDataLoader.getInstance().getAnimation("hurting");
        // behurtBackAnim.flipAllImage();

        // idleShootingForwardAnim =
        // CacheDataLoader.getInstance().getAnimation("idleshoot");
        // idleShootingBackAnim =
        // CacheDataLoader.getInstance().getAnimation("idleshoot");
        // idleBackAnim.flipAllImage();

        // runShootingForwardAnim =
        // CacheDataLoader.getInstance().getAnimation("runshoot");
        // runShootingBackAnim = CacheDataLoader.getInstance().getAnimation("runshoot");
        // runShootingBackAnim.flipAllImage();

        // flyShootingForwardAnim =
        // CacheDataLoader.getInstance().getAnimation("flytinupshoot");
        // flyShootingBackAnim =
        // CacheDataLoader.getInstance().getAnimation("flyingupshoot");
        // flyShootingBackAnim.flipAllImage();

    }

    @Override
    public void update() {
        super.update();
        if (isShooting) {
            if (System.nanoTime() - lastShootingTime > 500 * 1000000) {
                isShooting = false;
            }
        }
        if (getIsLanding()) {
            // landingBackAnim.update(System.nanoTime());
            // if (landingBackAnim.isLastFrame()) {
            // setIsLanding(false);
            // // landingBackAnim.reset();
            // // runForwardAnim.reset();
            // // runBackAnim.reset();
            // }
        }
    }

    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        if (getIsDicking()) {
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 20;
            rect.width = 44;
            rect.height = 65;
        } else {
            rect.x = (int) getPosX() - 22;
            rect.y = (int) getPosY() - 40;
            rect.width = 44;
            rect.height = 80;
        }
        return rect;
    }

    public void draw(Graphics2D g2) {
        // g2.setColor(Color.red);
        // g2.fillRect((int) (posX - (getWidth() / 2)), (int) (posY - (getHeight() /
        // 2)), (int) width, (int) height);
        drawBoundForCollisionWithMap(g2);
    }

    // public float getPosX() {
    // return posX;
    // }

    // public void setPosX(float posX) {
    // this.posX = posX;
    // }

    // public float getPosY() {
    // return posY;
    // }

    // public void setPosY(float posY) {
    // this.posY = posY;
    // }

    // public float getWidth() {
    // return width;
    // }

    // public void setWidth(float width) {
    // this.width = width;
    // }

    // public float getHeight() {
    // return height;
    // }

    // public void setHeight(float height) {
    // this.height = height;
    // }

    // public float getMass() {
    // return mass;
    // }

    // public void setMass(float mass) {
    // this.mass = mass;
    // }

    // public float getSpeedX() {
    // return speedX;
    // }

    // public void setSpeedX(float speedX) {
    // this.speedX = speedX;
    // }

    // public float getSpeedY() {
    // return speedY;
    // }

    // public void setSpeedY(float speedY) {
    // this.speedY = speedY;
    // }

    // public int getDirection() {
    // return direction;
    // }

    // public void setDirection(int direction) {
    // this.direction = direction;
    // }

    // public Rectangle getBoundForCollisionWithMap() {
    // Rectangle bound = new Rectangle();
    // bound.x = (int) (getPosX() - (getWidth() / 2));
    // bound.y = (int) (getPosY() - (getHeight() / 2));
    // bound.width = (int) getWidth();
    // bound.height = (int) getHeight();
    // return bound;
    // }

    // @Override
    // public void update() {
    // super.update();
    // setPosX(getPosX() + getSpeedX());
    // setPosY(getPosY() + getSpeedY());

    // setSpeedY(getSpeedY() + getMass());
    // if (getPosY() >= 400)
    // setPosY(400);
    // Rectangle futureRect = getBoundForCollisionWithMap();
    // futureRect.y += getSpeedY();
    // Rectangle rectLand =
    // getGameWorld().physicalMap.haveCollisionWithLand(futureRect);

    // if (rectLand != null)
    // setPosY(rectLand.y - getHeight() / 2);
    // else {
    // setPosY(getPosY() + getSpeedY());
    // setSpeedY(getSpeedY() + getMass());
    // }
    // }

    // @Override
    // public void draw(Graphics2D g2) {
    // switch (getState()) {
    // case ALIVE:
    // case NOBEHURT:
    // if (getState() == NOBEHURT && (System.nanoTime() / 10000000) % 2 != 1) {
    // System.out.println("Plash");
    // } else {
    // if (getIsLanding()) {
    // if (getDirection() == RIGHT_DIR) {
    // landingForwardAnim.setCurrentFrame(landingBackAnim.getCurrentFrame());
    // landingForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY()
    // + (getBoundForCollisionWithMap().height / 2 -
    // landingForwardAnim.getCurrentImage().getHeight() / 2),
    // g2);
    // } else {
    // landingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY()
    // + landingBackAnim.getCurrentImage().getHeight() / 2,
    // g2);
    // }
    // } else if (getIsJumping()) {
    // if (getDirection() == RIGHT_DIR) {
    // flyForwardAnim.update(System.nanoTime());
    // if (isShooting) {
    // flyShootingForwardAnim.setCurrentFrame(flyForwardAnim.getCurrentFrame());
    // flyShootingForwardAnim.draw((int) (getPosX() -
    // getGameWorld().camera.getPosX()) - 10,
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // flyForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // } else {
    // flyBackAnim.update(System.nanoTime());
    // if (isShooting) {
    // flyShootingBackAnim.setCurrentFrame(flyBackAnim.getCurrentFrame());
    // flyShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX())
    // - 10,
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // flyBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // }

    // } else if (getIsDicking()) {
    // if (getDirection() == RIGHT_DIR) {
    // dickForwardAnim.update(System.nanoTime());
    // dickForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY()
    // + (getBoundForCollisionWithMap().height / 2
    // - dickForwardAnim.getCurrentImage().getHeight() / 2),
    // g2);
    // } else {
    // dickBackAnim.update(System.nanoTime());
    // dickBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY()
    // + (getBoundForCollisionWithMap().height / 2
    // - dickBackAnim.getCurrentImage().getHeight() / 2),
    // g2);
    // }
    // } else {
    // if (getSpeedX() > 0) {
    // runForwardAnim.update(System.nanoTime());
    // if (isShooting) {
    // runShootingForwardAnim.setCurrentFrame(runForwardAnim.getCurrentFrame() - 1);
    // runShootingForwardAnim.draw((int) (getPosX() -
    // getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // runForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // if (runForwardAnim.getCurrentFrame() == 1)
    // runForwardAnim.setIgnoreFrame(0);
    // } else if (getSpeedX() < 0) {
    // runBackAnim.update(System.nanoTime());
    // if (isShooting) {
    // runShootingBackAnim.setCurrentFrame(runBackAnim.getCurrentFrame() - 1);
    // runShootingBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // runBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // if (runBackAnim.getCurrentFrame() == 1)
    // runBackAnim.setIgnoreFrame(0);
    // } else {
    // if (getDirection() == RIGHT_DIR) {
    // if (isShooting) {
    // idleShootingForwardAnim.update(System.nanoTime());
    // idleShootingForwardAnim.draw((int) (getPosX() -
    // getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // idleForwardAnim.update(System.nanoTime());
    // idleForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // } else {
    // if (isShooting) {
    // idleShootingBackAnim.update(System.nanoTime());
    // idleShootingBackAnim.draw((int) (getPosX() -
    // getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // idleBackAnim.update(System.nanoTime());
    // idleBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // }
    // }
    // }
    // }

    // break;
    // case BEHURT:
    // if (getDirection() == RIGHT_DIR) {
    // behurtForwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // } else {
    // behurtBackAnim.setCurrentFrame(behurtForwardAnim.getCurrentFrame());
    // behurtBackAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()),
    // (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
    // }
    // break;
    // case FEY:
    // break;
    // }
    // }

    // @Override
    // public void run() {
    // if (getDirection() == LEFT_DIR)
    // setSpeedX(-3);
    // else
    // setSpeedX(3);
    // }
    @Override
    public void run() {
        if (getDirection() == LEFT_DIR) {
            setSpeedX(-3);
        } else {
            setSpeedX(3);
        }
        System.out.println("SpeedX after run: " + getSpeedX());
    }

    @Override
    public void jump() {
        // if (!getIsJumping()) {
        // setIsJumping(true);
        // setSpeedY(-5); // Điều chỉnh tốc độ nhảy tùy ý
        // }
        if (!getIsJumping()) {
            setIsJumping(true);
            setSpeedY(-0.5f);
            // flyBackAnim.reset();
            // flyForwardAnim.reset();
        } else {
            Rectangle rectRightWall = getBoundForCollisionWithMap();
            rectRightWall.x += 1;
            Rectangle rectLeftWall = getBoundForCollisionWithMap();
            rectLeftWall.x -= 1;
            if (getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall) != null && getSpeedX() > 0) {
                setSpeedY(-5.0f);
                // flyBackAnim.reset();
                // flyForwardAnim.reset();
            } else if (getGameWorld().physicalMap.haveCollisionWithLeftWall(rectLeftWall) != null && getSpeedX() < 0) {
                setSpeedY(-5.0f);
                // flyBackAnim.reset();
                // flyForwardAnim.reset();
            }
        }
        // if(getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall) !=
        // null)
    }

    @Override
    public void dick() {
        if (!getIsJumping())
            setIsDicking(true);
    }

    @Override
    public void standUp() {
        setIsDicking(false);
        // idleForwardAnim.reset();
        // idleBackAnim.reset();
        // dickForwardAnim.reset();
        // dickBackAnim.reset();
    }

    @Override
    public void stopRun() {
        setSpeedX(0);
        // runForwardAnim.reset();
        // runBackAnim.reset();
        // runForwardAnim.unIgnoreFrame(0);
        // runBackAnim.unIgnoreFrame(0);
    }

    @Override
    public void attack() {

        // if (!isShooting && !getIsDicking()) {

        // shooting1.play();

        // Bullet bullet = new BlueFire(getPosX(), getPosY(), getGameWorld());
        // if (getDirection() == LEFT_DIR) {
        // bullet.setSpeedX(-10);
        // bullet.setPosX(bullet.getPosX() - 40);
        // if (getSpeedX() != 0 && getSpeedY() == 0) {
        // bullet.setPosX(bullet.getPosX() - 10);
        // bullet.setPosY(bullet.getPosY() - 5);
        // }
        // } else {
        // bullet.setSpeedX(10);
        // bullet.setPosX(bullet.getPosX() + 40);
        // if (getSpeedX() != 0 && getSpeedY() == 0) {
        // bullet.setPosX(bullet.getPosX() + 10);
        // bullet.setPosY(bullet.getPosY() - 5);
        // }
        // }
        // if (getIsJumping())
        // bullet.setPosY(bullet.getPosY() - 20);

        // bullet.setTeamType(getTeamType());
        // getGameWorld().bulletManager.addObject(bullet);

        // lastShootingTime = System.nanoTime();
        // isShooting = true;

        // }

    }

    @Override
    public void hurtingCallback() {
        // System.out.println("Call back hurting");
        // hurtingSound.play();
    }

}
