package megaman.gameobjects;

import megaman.gameobjects.GameWorld;
import megaman.effect.Animation;
import megaman.effect.CacheDataLoader;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class RedEyeBullet extends Bullet{
	
    private Animation forwardBulletAnim, backBulletAnim;
    
    public RedEyeBullet(float x, float y, GameWorld gameWorld) {
            super(x, y, 30, 30, 1.0f, 10, gameWorld);
            forwardBulletAnim = CacheDataLoader.getInstance().getAnimation("redeyebullet");
            backBulletAnim = CacheDataLoader.getInstance().getAnimation("redeyebullet");
            backBulletAnim.flipAllImage();
    }

    
    
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
            // TODO Auto-generated method stub
            return getBoundForCollisionWithMap();
    }

    @Override
    public void draw(Graphics2D g2) {
            // TODO Auto-generated method stub
        if(getSpeedX() > 0){          
            forwardBulletAnim.update(System.nanoTime());
            forwardBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }else{
            backBulletAnim.update(System.nanoTime());
            backBulletAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), (int) getPosY() - (int) getGameWorld().camera.getPosY(), g2);
        }
        //drawBoundForCollisionWithEnemy(g2);
    }

    @Override
    public void update() {
            // TODO Auto-generated method stub
        super.update();
    }

    @Override
    public void attack() {}

}
