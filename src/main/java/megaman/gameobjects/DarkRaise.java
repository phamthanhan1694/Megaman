/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package megaman.gameobjects;

import megaman.gameobjects.GameWorld;
import megaman.effect.Animation;
import megaman.effect.CacheDataLoader;
import static megaman.gameobjects.ParticularObject.LEFT_DIR;
import static megaman.gameobjects.ParticularObject.NOBEHURT;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author phamn
 */
public class DarkRaise extends ParticularObject{

    private Animation forwardAnim, backAnim;
    
    private long startTimeToShoot;
    private float x1, x2;
    
    public DarkRaise(float x, float y, GameWorld gameWorld) {
        super(x, y, 127, 89, 0, 100, gameWorld);
        backAnim = CacheDataLoader.getInstance().getAnimation("darkraise");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("darkraise");
        forwardAnim.flipAllImage();
        startTimeToShoot = 0;
        setTimeForNoBeHurt(300000000);
        
        x1 = x - 100;
        x2 = x + 100;
        setSpeedX(1);
        
        setDamage(10);
    }

    @Override
    public void attack() {
    
        float megamanX = getGameWorld().megaman.getPosX();
        float megamanY = getGameWorld().megaman.getPosY();
        
        float deltaX = megamanX - getPosX();
        float deltaY = megamanY - getPosY();
        
        float speed = 3;
        float distance = (float) Math.hypot(deltaX, deltaY);
        if (distance == 0) distance = 1;
        float speedX = speed * Math.abs(deltaX) / distance;
        float speedY = speed * deltaY / distance;
        
        
        
        Bullet bullet = new DarkRaiseBullet(getPosX(), getPosY(), getGameWorld());
        
        if(deltaX < 0)
            bullet.setSpeedX(-speedX);
        else bullet.setSpeedX(speedX);
        bullet.setSpeedY(speedY);
        bullet.setTeamType(getTeamType());
        getGameWorld().bulletManager.addObject(bullet);

    }

    
    public void update(){
        super.update();
        if(getState() != ALIVE && getState() != NOBEHURT) return;
        if(getPosX() < x1)
            setSpeedX(1);
        else if(getPosX() > x2)
            setSpeedX(-1);
        setPosX(getPosX() + getSpeedX());
        
        if(System.nanoTime() - startTimeToShoot > 1_500_000_000L){
            attack();
            startTimeToShoot = System.nanoTime();
        }
    }
    
    @Override
    public Rectangle getBoundForCollisionWithEnemy() {
        Rectangle rect = getBoundForCollisionWithMap();
        rect.x += 20;
        rect.width -= 40;
        
        return rect;
    }

    @Override
    public void draw(Graphics2D g2) {
        if(!isObjectOutOfCameraView()){
            if(getState() == NOBEHURT && (System.nanoTime()/10000000)%2!=1){
                // plash...
            }else{
                if(getDirection() == LEFT_DIR){
                    backAnim.update(System.nanoTime());
                    backAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                            (int)(getPosY() - getGameWorld().camera.getPosY()), g2);
                }else{
                    forwardAnim.update(System.nanoTime());
                    forwardAnim.draw((int) (getPosX() - getGameWorld().camera.getPosX()), 
                            (int)(getPosY() - getGameWorld().camera.getPosY()), g2);
                }
            }
        }
        //drawBoundForCollisionWithEnemy(g2);
    }
    
}
