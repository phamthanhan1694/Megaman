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
public class RobotR extends ParticularObject {

    private Animation forwardAnim, backAnim;
    
    private long startTimeToShoot;
    private float x1, x2, y1, y2;
    
    private String shooting;
    
    public RobotR(float x, float y, GameWorld gameWorld) {
        super(x, y, 127, 89, 0, 100, gameWorld);
        backAnim = CacheDataLoader.getInstance().getAnimation("robotR");
        forwardAnim = CacheDataLoader.getInstance().getAnimation("robotR");
        forwardAnim.flipAllImage();
        startTimeToShoot = 0;
        setTimeForNoBeHurt(300000000);
        setDamage(10);
        
        x1 = x - 100;
        x2 = x + 100;
        y1 = y - 50;
        y2 = y + 50;
        
        setSpeedX(1);
        setSpeedY(1);
        
        shooting = "robotRshooting";
    }

    @Override
    public void attack() {  
        
        CacheDataLoader.getInstance().playSound(shooting);
        Bullet bullet = new RobotRBullet(getPosX(), getPosY(), getGameWorld());
        
        if(getDirection()==LEFT_DIR)
            bullet.setSpeedX(5);
        else bullet.setSpeedX(-5);
        bullet.setTeamType(getTeamType());
        getGameWorld().bulletManager.addObject(bullet);

    }

    
    public void update(){
        super.update();
        if(getState() != ALIVE && getState() != NOBEHURT) return;
        
        if(getPosX() - getGameWorld().megaman.getPosX() > 0) setDirection(ParticularObject.RIGHT_DIR);
        else setDirection(ParticularObject.LEFT_DIR);
        
        if(getPosX() < x1)
            setSpeedX(1);
        else if(getPosX() > x2)
            setSpeedX(-1);
        setPosX(getPosX() + getSpeedX());
        
        if(getPosY() < y1)
            setSpeedY(1);
        else if(getPosY() > y2)
            setSpeedY(-1);
        setPosY(getPosY() + getSpeedY());
        
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
