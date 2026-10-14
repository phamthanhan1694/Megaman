/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package megaman.gameobjects;

import megaman.gameobjects.GameWorld;
import megaman.effect.Animation;
import megaman.effect.CacheDataLoader;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author phamn
 */
public class RedEyeDevil extends ParticularObject {

    private Animation forwardAnim, backAnim;
    
    private long startTimeToShoot;
    
    private String shooting;
    
    public RedEyeDevil(float x, float y, GameWorld gameWorld) {
        super(x, y, 127, 89, 0, 100, gameWorld);
        backAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("redeye");
        forwardAnim.flipAllImage();
        startTimeToShoot = 0;
        setDamage(10);
        setTimeForNoBeHurt(300000000);
        shooting = "redeyeshooting";
    }

    @Override
    public void attack() {
    
        CacheDataLoader.getInstance().playSound(shooting);
        Bullet bullet = new RedEyeBullet(getPosX(), getPosY(), getGameWorld());
        if(getDirection() == LEFT_DIR) bullet.setSpeedX(-8);
        else bullet.setSpeedX(8);
        bullet.setTeamType(getTeamType());
        getGameWorld().bulletManager.addObject(bullet);
    
    }

    
    public void update(){
        super.update();
        if(getState() != ALIVE && getState() != NOBEHURT) return;
        if(System.nanoTime() - startTimeToShoot > 1_000_000_000L){
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
