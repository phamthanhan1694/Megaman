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
public class SmallRedGun extends ParticularObject{

    private Animation forwardAnim, backAnim;
    
    private long startTimeToShoot;
    
    public SmallRedGun(float x, float y, GameWorld gameWorld) {
        super(x, y, 127, 89, 0, 100, gameWorld);
        backAnim = CacheDataLoader.getInstance().getAnimation("smallredgun");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("smallredgun");
        forwardAnim.flipAllImage();
        startTimeToShoot = 0;
        setTimeForNoBeHurt(300000000);
    }

    @Override
    public void attack() {
    
        Bullet bullet = new YellowFlowerBullet(getPosX(), getPosY(), getGameWorld());
        bullet.setSpeedX(-3);
        bullet.setSpeedY(3);
        bullet.setTeamType(getTeamType());
        getGameWorld().bulletManager.addObject(bullet);
        
        bullet = new YellowFlowerBullet(getPosX(), getPosY(), getGameWorld());
        bullet.setSpeedX(3);
        bullet.setSpeedY(3);
        bullet.setTeamType(getTeamType());
         getGameWorld().bulletManager.addObject(bullet);
    }

    
    public void update(){
        super.update();
        if(getState() != ALIVE && getState() != NOBEHURT) return;
        if(System.nanoTime() - startTimeToShoot > 2_000_000_000L){
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
