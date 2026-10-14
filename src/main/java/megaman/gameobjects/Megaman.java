package megaman.gameobjects;

import java.awt.Graphics2D;
import java.awt.Rectangle;

import megaman.effect.Animation;
import megaman.effect.CacheDataLoader;

public class Megaman extends Human {

    public static final int RUNSPEED = 3;
    private static final float JUMP_SPEED = -5.5f;
    private static final long SHOOTING_TIME = 500_000_000L;
    private static final long LANDING_TIME = 150_000_000L;

    private final Animation runForwardAnim;
    private final Animation runBackAnim;
    private final Animation runShootingForwardAnim;
    private final Animation runShootingBackAnim;
    private final Animation idleForwardAnim;
    private final Animation idleBackAnim;
    private final Animation idleShootingForwardAnim;
    private final Animation idleShootingBackAnim;
    private final Animation dickForwardAnim;
    private final Animation dickBackAnim;
    private final Animation flyForwardAnim;
    private final Animation flyBackAnim;
    private final Animation flyShootingForwardAnim;
    private final Animation flyShootingBackAnim;
    private final Animation landingForwardAnim;
    private final Animation landingBackAnim;
    private final Animation climbForwardAnim;
    private final Animation climbBackAnim;

    private long lastShootingTime;
    private long landingStartTime;
    private boolean isShooting;

    public Megaman(float x, float y, GameWorld gameWorld) {
        super(x, y, 70, 90, 0.1f, 100, gameWorld);

        CacheDataLoader loader = CacheDataLoader.getInstance();
        runForwardAnim = loader.getAnimation("run");
        runBackAnim = loader.getAnimation("run");
        runBackAnim.flipAllImage();

        idleForwardAnim = loader.getAnimation("idle");
        idleBackAnim = loader.getAnimation("idle");
        idleBackAnim.flipAllImage();

        dickForwardAnim = loader.getAnimation("dick");
        dickBackAnim = loader.getAnimation("dick");
        dickBackAnim.flipAllImage();

        flyForwardAnim = loader.getAnimation("flyingup");
        flyForwardAnim.setIsRepeated(false);
        flyBackAnim = loader.getAnimation("flyingup");
        flyBackAnim.setIsRepeated(false);
        flyBackAnim.flipAllImage();

        landingForwardAnim = loader.getAnimation("landing");
        landingForwardAnim.setIsRepeated(false);
        landingBackAnim = loader.getAnimation("landing");
        landingBackAnim.setIsRepeated(false);
        landingBackAnim.flipAllImage();

        climbBackAnim = loader.getAnimation("clim_wall");
        climbForwardAnim = loader.getAnimation("clim_wall");
        climbForwardAnim.flipAllImage();

        behurtForwardAnim = loader.getAnimation("hurting");
        behurtBackAnim = loader.getAnimation("hurting");
        behurtBackAnim.flipAllImage();

        idleShootingForwardAnim = loader.getAnimation("idleshoot");
        idleShootingBackAnim = loader.getAnimation("idleshoot");
        idleShootingBackAnim.flipAllImage();

        runShootingForwardAnim = loader.getAnimation("runshoot");
        runShootingBackAnim = loader.getAnimation("runshoot");
        runShootingBackAnim.flipAllImage();

        flyShootingForwardAnim = loader.getAnimation("flyingupshoot");
        flyShootingForwardAnim.setIsRepeated(false);
        flyShootingBackAnim = loader.getAnimation("flyingupshoot");
        flyShootingBackAnim.setIsRepeated(false);
        flyShootingBackAnim.flipAllImage();

        setTeamType(LEAGUE_TEAM);
        setDirection(RIGHT_DIR);
        setTimeForNoBeHurt(2_000_000_000L);
    }

    @Override
    public void update() {
        super.update();

        long now = System.nanoTime();
        if (isShooting && now - lastShootingTime > SHOOTING_TIME) {
            isShooting = false;
        }

        if (getIsLanding()) {
            if (landingStartTime == 0) {
                landingStartTime = now;
                landingForwardAnim.reset();
                landingBackAnim.reset();
            } else if (now - landingStartTime > LANDING_TIME) {
                setIsLanding(false);
                landingStartTime = 0;
            }
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

    @Override
    public void draw(Graphics2D g2) {
        long now = System.nanoTime();

        switch (getState()) {
            case ALIVE:
            case NOBEHURT:
                if (getState() == NOBEHURT && (now / 10_000_000) % 2 != 1) {
                    return;
                }
                drawAlive(now, g2);
                break;
            case BEHURT:
                drawAnimation(getDirection() == RIGHT_DIR ? behurtForwardAnim : behurtBackAnim, now, g2);
                break;
            default:
                break;
        }
    }

    private void drawAlive(long now, Graphics2D g2) {
        if (getIsLanding()) {
            drawAnimation(getDirection() == RIGHT_DIR ? landingForwardAnim : landingBackAnim, now, g2);
        } else if (getIsJumping()) {
            Rectangle rectRightWall = getBoundForCollisionWithMap();
            rectRightWall.x += 1;
            Rectangle rectLeftWall = getBoundForCollisionWithMap();
            rectLeftWall.x -= 1;
            boolean onRightWall = getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall) != null;
            boolean onLeftWall = getGameWorld().physicalMap.haveCollisionWithLeftWall(rectLeftWall) != null;
            if (!isShooting && ((onRightWall && getSpeedX() > 0) || (onLeftWall && getSpeedX() < 0))) {
                drawAnimation(getDirection() == RIGHT_DIR ? climbForwardAnim : climbBackAnim, now, g2);
                return;
            }
            Animation animation = getDirection() == RIGHT_DIR
                    ? (isShooting ? flyShootingForwardAnim : flyForwardAnim)
                    : (isShooting ? flyShootingBackAnim : flyBackAnim);
            drawAnimation(animation, now, g2);
        } else if (getIsDicking()) {
            drawAnimation(getDirection() == RIGHT_DIR ? dickForwardAnim : dickBackAnim, now, g2);
        } else if (getSpeedX() > 0) {
            drawAnimation(isShooting ? runShootingForwardAnim : runForwardAnim, now, g2);
        } else if (getSpeedX() < 0) {
            drawAnimation(isShooting ? runShootingBackAnim : runBackAnim, now, g2);
        } else if (getDirection() == RIGHT_DIR) {
            drawAnimation(isShooting ? idleShootingForwardAnim : idleForwardAnim, now, g2);
        } else {
            drawAnimation(isShooting ? idleShootingBackAnim : idleBackAnim, now, g2);
        }
    }

    private void drawAnimation(Animation animation, long now, Graphics2D g2) {
        animation.update(now);
        int drawX = (int) (getPosX() - getGameWorld().camera.getPosX());
        int drawY = (int) (getPosY() - getGameWorld().camera.getPosY());
        animation.draw(drawX, drawY, g2);
    }

    @Override
    public void run() {
        setSpeedX(getDirection() == LEFT_DIR ? -RUNSPEED : RUNSPEED);
    }

    @Override
    public void jump() {
        if (!getIsJumping()) {
            setIsJumping(true);
            setIsLanding(false);
            landingStartTime = 0;
            setSpeedY(JUMP_SPEED);
            flyForwardAnim.reset();
            flyBackAnim.reset();
            flyShootingForwardAnim.reset();
            flyShootingBackAnim.reset();
            return;
        }

        Rectangle rectRightWall = getBoundForCollisionWithMap();
        rectRightWall.x += 1;
        Rectangle rectLeftWall = getBoundForCollisionWithMap();
        rectLeftWall.x -= 1;
        if (getGameWorld().physicalMap.haveCollisionWithRightWall(rectRightWall) != null && getSpeedX() > 0) {
            setSpeedY(JUMP_SPEED);
        } else if (getGameWorld().physicalMap.haveCollisionWithLeftWall(rectLeftWall) != null && getSpeedX() < 0) {
            setSpeedY(JUMP_SPEED);
        }
    }

    @Override
    public void dick() {
        if (!getIsJumping()) {
            setIsDicking(true);
        }
    }

    @Override
    public void standUp() {
        setIsDicking(false);
        idleForwardAnim.reset();
        idleBackAnim.reset();
        dickForwardAnim.reset();
        dickBackAnim.reset();
    }

    @Override
    public void stopRun() {
        setSpeedX(0);
        runForwardAnim.reset();
        runBackAnim.reset();
        runShootingForwardAnim.reset();
        runShootingBackAnim.reset();
    }

    @Override
    public void attack() {
        if ((getState() == ALIVE || getState() == NOBEHURT) && !isShooting && !getIsDicking()) {
            CacheDataLoader.getInstance().playSound("bluefireshooting");
            BlueFire blueFire = new BlueFire(getPosX() + (getDirection() == RIGHT_DIR ? 40 : -40),
                    getPosY() - (getIsJumping() ? 20 : 0), getGameWorld());
            blueFire.setTeamType(getTeamType());
            blueFire.setSpeedX(getDirection() == RIGHT_DIR ? 10 : -10);
            getGameWorld().bulletManager.addObject(blueFire);
            lastShootingTime = System.nanoTime();
            isShooting = true;
        }
    }

    @Override
    public void hurtingCallback() {
        CacheDataLoader.getInstance().playSound("megamanhurt");
    }
}
