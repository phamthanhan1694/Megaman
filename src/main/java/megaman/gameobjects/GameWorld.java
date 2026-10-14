package megaman.gameobjects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.List;

import megaman.effect.CacheDataLoader;
import megaman.effect.FrameImage;
import megaman.userinterface.GameFrame;

public class GameWorld {

    public enum Mode {
        START, INTRO, PLAYING, BOSS_INTRO, PAUSED, GAME_OVER, WON, MENU
    }

    private static final int PLAYER_START_X = 400;
    private static final int PLAYER_START_Y = 400;
    private static final int FINAL_BOSS_TRIGGER_X = 3600;
    private static final int FINAL_BOSS_X = 4300;
    private static final int FINAL_BOSS_Y = 460;
    private static final int ARENA_PLAYER_X = 3750;
    private static final int ARENA_CAMERA_X = 3600;
    private static final String[] STORY = {
            "We are heroes. Our mission is to protect our home, Earth.",
            "For ten years, a monster has held our world in fear.",
            "Now is the time to fight back and win our freedom!",
            "LET'S GO!"
    };

    public Megaman megaman;
    public PhysicalMap physicalMap;
    public Camera camera;
    public ParticularObjectManager particularObjectManager;
    public BulletManager bulletManager;

    private final CacheDataLoader loader = CacheDataLoader.getInstance();
    private final BufferedImage avatarImage;
    private final BufferedImage hearthImage;
    private final BufferedImage gameWinImage;

    private Mode mode = Mode.START;
    private Mode previousMode = Mode.PLAYING;
    private int lives = 3;
    private int storyPage;
    private int storyCharacters;
    private int introOpening;
    private int menuSelection;
    private boolean bossIntroReady;
    private boolean bossGateClosed;
    private boolean exitRequested;
    private FinalBoss boss;

    public GameWorld() {
        avatarImage = image("avatar");
        hearthImage = image("hearth");
        gameWinImage = image("gamewin");
        resetLevel();
        mode = Mode.START;
    }

    public Mode getMode() {
        return mode;
    }

    public int getLives() {
        return lives;
    }

    public FinalBoss getBoss() {
        return boss;
    }

    public boolean isExitRequested() {
        return exitRequested;
    }

    public void resetLevel() {
        loader.stopSound("bgmusic");
        physicalMap = new PhysicalMap(0, 0, this);
        camera = new Camera(0, 50, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, this);
        particularObjectManager = new ParticularObjectManager(this);
        bulletManager = new BulletManager(this);
        boss = null;
        bossGateClosed = false;
        bossIntroReady = false;
        lives = 3;
        exitRequested = false;
        createPlayer(PLAYER_START_X, PLAYER_START_Y);
        spawnConfiguredEnemies();
        storyPage = 0;
        storyCharacters = 0;
        introOpening = 0;
        menuSelection = 0;
        camera.update();
    }

    public void startGameplay() {
        if (mode != Mode.PLAYING) {
            mode = Mode.PLAYING;
            loader.playSound("bgmusic", true);
        }
    }

    public void selectMenu(int direction) {
        if (mode == Mode.MENU) {
            menuSelection = Math.floorMod(menuSelection + direction, 2);
        }
    }

    public void confirm() {
        switch (mode) {
            case START:
                beginIntro();
                break;
            case INTRO:
                advanceIntro();
                break;
            case BOSS_INTRO:
                if (bossIntroReady) startGameplay();
                break;
            case PAUSED:
                mode = previousMode;
                loader.playSound("bgmusic", true);
                break;
            case GAME_OVER:
            case WON:
                mode = Mode.MENU;
                loader.stopSound("bgmusic");
                break;
            case MENU:
                if (menuSelection == 0) {
                    resetLevel();
                    beginIntro();
                } else {
                    exitRequested = true;
                }
                break;
            default:
                break;
        }
    }

    public void togglePause() {
        if (mode == Mode.PLAYING || mode == Mode.BOSS_INTRO) {
            pause();
        } else if (mode == Mode.PAUSED) {
            mode = previousMode;
            loader.playSound("bgmusic", true);
        }
    }

    public void pause() {
        if (mode == Mode.PLAYING || mode == Mode.BOSS_INTRO) {
            previousMode = mode;
            mode = Mode.PAUSED;
            megaman.stopRun();
            megaman.standUp();
            loader.stopSound("bgmusic");
        }
    }

    public boolean acceptsPlayerControl() {
        return mode == Mode.PLAYING;
    }

    public void update() {
        switch (mode) {
            case INTRO:
                updateIntro();
                break;
            case PLAYING:
                updatePlaying();
                break;
            case BOSS_INTRO:
                updateBossIntro();
                break;
            default:
                break;
        }
    }

    public void renderScene(Graphics2D g2) {
        Graphics2D mapGraphics = (Graphics2D) g2.create();
        try {
            mapGraphics.translate(-camera.getPosX(), -camera.getPosY());
            physicalMap.draw(mapGraphics);
        } finally {
            mapGraphics.dispose();
        }
        particularObjectManager.draw(g2);
        bulletManager.draw(g2);
    }

    public void render(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        renderScene(g2);
        drawHud(g2);

        switch (mode) {
            case START:
                drawStart(g2);
                break;
            case INTRO:
                drawIntro(g2);
                break;
            case BOSS_INTRO:
                drawBossIntro(g2);
                break;
            case PAUSED:
                drawCenterOverlay(g2, "PAUSED", "PRESS ENTER");
                break;
            case GAME_OVER:
                drawCenterOverlay(g2, "GAME OVER", "PRESS ENTER");
                break;
            case WON:
                drawWin(g2);
                break;
            case MENU:
                drawMenu(g2);
                break;
            default:
                break;
        }
    }

    private void beginIntro() {
        mode = Mode.INTRO;
        storyPage = 0;
        storyCharacters = 0;
        introOpening = 0;
        loader.playSound("bgmusic", true);
    }

    private void advanceIntro() {
        if (storyCharacters < STORY[storyPage].length()) {
            storyCharacters = STORY[storyPage].length();
            return;
        }
        storyPage++;
        storyCharacters = 0;
        if (storyPage >= STORY.length) {
            startGameplay();
        }
    }

    private void updateIntro() {
        introOpening = Math.min(160, introOpening + 2);
        if (storyPage < STORY.length && storyCharacters < STORY[storyPage].length()) {
            storyCharacters++;
        }
    }

    private void updatePlaying() {
        particularObjectManager.updateObjects();
        bulletManager.updateObjects();
        camera.update();
        handlePlayerLife();
        if (mode == Mode.PLAYING) {
            maybeStartBossIntro();
            if (boss != null && boss.getState() == ParticularObject.DEATH) {
                mode = Mode.WON;
                loader.stopSound("bgmusic");
            }
        }
    }

    private void updateBossIntro() {
        if (camera.getPosX() < ARENA_CAMERA_X) {
            camera.setPosX(Math.min(ARENA_CAMERA_X, camera.getPosX() + 2));
        }
        if (megaman.getPosX() < ARENA_PLAYER_X) {
            megaman.setDirection(ParticularObject.RIGHT_DIR);
            megaman.setSpeedX(Megaman.RUNSPEED);
            megaman.update();
        } else {
            megaman.stopRun();
        }
        camera.setPosY(50);
        bossIntroReady = camera.getPosX() >= ARENA_CAMERA_X && megaman.getPosX() >= ARENA_PLAYER_X;
    }

    private void maybeStartBossIntro() {
        if (boss == null && megaman.getPosX() > FINAL_BOSS_TRIGGER_X) {
            boss = new FinalBoss(FINAL_BOSS_X, FINAL_BOSS_Y, this);
            boss.setTeamType(ParticularObject.ENEMY_TEAM);
            boss.setDirection(ParticularObject.LEFT_DIR);
            particularObjectManager.addObject(boss);
            closeBossGate();
            camera.lock();
            mode = Mode.BOSS_INTRO;
            bossIntroReady = false;
        }
    }

    private void closeBossGate() {
        if (bossGateClosed) return;
        for (int row = 14; row <= 17; row++) {
            physicalMap.setTile(row, 120, 1, 17);
        }
        bossGateClosed = true;
    }

    private void handlePlayerLife() {
        if (megaman.getPosY() > physicalMap.getMapHeight() + 200) {
            megaman.setBlood(0);
            megaman.setState(ParticularObject.DEATH);
        }
        if (megaman.getState() != ParticularObject.DEATH) return;

        lives--;
        if (lives <= 0) {
            mode = Mode.GAME_OVER;
            loader.stopSound("bgmusic");
            return;
        }

        float x = bossGateClosed ? ARENA_PLAYER_X : PLAYER_START_X;
        float y = bossGateClosed ? 400 : PLAYER_START_Y;
        respawnPlayer(x, y);
    }

    private void respawnPlayer(float x, float y) {
        particularObjectManager.removeObject(megaman);
        bulletManager.clear();
        createPlayer(x, y);
        megaman.setState(ParticularObject.NOBEHURT);
        camera.update();
    }

    private void createPlayer(float x, float y) {
        megaman = new Megaman(x, y, this);
        snapPlayerToGround(megaman);
        particularObjectManager.addObject(megaman);
    }

    private void snapPlayerToGround(Megaman player) {
        int tileSize = physicalMap.getTileSize();
        int[][] map = physicalMap.phys_map;
        int leftColumn = Math.max(0, (int) (player.getPosX() - player.getWidth() / 2) / tileSize);
        int rightColumn = Math.min(map[0].length - 1, (int) (player.getPosX() + player.getWidth() / 2) / tileSize);
        int startRow = Math.max(0, (int) (player.getPosY() + player.getHeight() / 2) / tileSize);
        for (int row = startRow; row < map.length; row++) {
            for (int column = leftColumn; column <= rightColumn; column++) {
                if (map[row][column] == 1) {
                    player.setPosY(row * tileSize - player.getHeight() / 2 - 1);
                    player.setSpeedY(0);
                    player.setIsJumping(false);
                    player.setIsLanding(false);
                    return;
                }
            }
        }
    }

    private void spawnConfiguredEnemies() {
        for (CacheDataLoader.EnemySpawn spawn : loader.getEnemySpawns()) {
            ParticularObject enemy = createEnemy(spawn);
            if (enemy != null) {
                enemy.setTeamType(ParticularObject.ENEMY_TEAM);
                enemy.setDirection("left".equalsIgnoreCase(spawn.direction())
                        ? ParticularObject.LEFT_DIR : ParticularObject.RIGHT_DIR);
                particularObjectManager.addObject(enemy);
            }
        }
    }

    private ParticularObject createEnemy(CacheDataLoader.EnemySpawn spawn) {
        return switch (spawn.type()) {
            case "redeye" -> new RedEyeDevil(spawn.x(), spawn.y(), this);
            case "robotR" -> new RobotR(spawn.x(), spawn.y(), this);
            case "smallredgun" -> new SmallRedGun(spawn.x(), spawn.y(), this);
            case "darkraise" -> new DarkRaise(spawn.x(), spawn.y(), this);
            default -> null;
        };
    }

    private void drawHud(Graphics2D g2) {
        if (mode == Mode.START || mode == Mode.INTRO || mode == Mode.MENU) return;
        g2.setColor(new Color(0, 0, 0, 130));
        g2.fillRoundRect(18, 16, 168, 48, 8, 8);
        for (int i = 0; i < lives; i++) {
            g2.drawImage(hearthImage, 30 + i * 32, 28, null);
        }
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(30, 68, 130, 12);
        g2.setColor(new Color(225, 54, 54));
        g2.fillRect(30, 68, Math.max(0, megaman.getBlood()) * 130 / 100, 12);
        g2.setColor(Color.WHITE);
        g2.drawRect(30, 68, 130, 12);

        if (boss != null && boss.getState() != ParticularObject.DEATH) {
            g2.setColor(Color.DARK_GRAY);
            g2.fillRect(740, 28, 220, 14);
            g2.setColor(new Color(241, 190, 62));
            g2.fillRect(740, 28, Math.max(0, boss.getBlood()) * 220 / 100, 14);
            g2.setColor(Color.WHITE);
            g2.drawRect(740, 28, 220, 14);
        }
    }

    private void drawStart(Graphics2D g2) {
        drawShade(g2, 160);
        setFont(g2, Font.BOLD, 54);
        drawCentered(g2, "MEGAMAN", 250, Color.WHITE);
        setFont(g2, Font.BOLD, 24);
        drawCentered(g2, "PRESS ENTER", 318, new Color(100, 230, 255));
    }

    private void drawIntro(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, Math.max(30, 170 - introOpening));
        g2.fillRect(0, GameFrame.SCREEN_HEIGHT - Math.max(30, 170 - introOpening),
                GameFrame.SCREEN_WIDTH, Math.max(30, 170 - introOpening));
        g2.setColor(new Color(0, 0, 0, 205));
        g2.fillRect(0, 430, GameFrame.SCREEN_WIDTH, 170);
        g2.drawImage(avatarImage, 44, 448, null);
        setFont(g2, Font.PLAIN, 23);
        g2.setColor(Color.WHITE);
        String text = STORY[storyPage].substring(0, Math.min(storyCharacters, STORY[storyPage].length()));
        drawWrapped(g2, text, 170, 462, 760, 32);
    }

    private void drawBossIntro(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, 70);
        g2.fillRect(0, GameFrame.SCREEN_HEIGHT - 70, GameFrame.SCREEN_WIDTH, 70);
        setFont(g2, Font.BOLD, 28);
        drawCentered(g2, bossIntroReady ? "FINAL BOSS" : "WARNING", 45,
                bossIntroReady ? new Color(241, 190, 62) : Color.RED);
        if (bossIntroReady) {
            setFont(g2, Font.BOLD, 20);
            drawCentered(g2, "PRESS ENTER", GameFrame.SCREEN_HEIGHT - 28, Color.WHITE);
        }
    }

    private void drawCenterOverlay(Graphics2D g2, String title, String subtitle) {
        drawShade(g2, 175);
        setFont(g2, Font.BOLD, 48);
        drawCentered(g2, title, 270, Color.WHITE);
        setFont(g2, Font.BOLD, 22);
        drawCentered(g2, subtitle, 320, new Color(100, 230, 255));
    }

    private void drawWin(Graphics2D g2) {
        drawShade(g2, 120);
        g2.drawImage(gameWinImage, GameFrame.SCREEN_WIDTH / 2 - gameWinImage.getWidth() / 2, 180, null);
        setFont(g2, Font.BOLD, 23);
        drawCentered(g2, "PRESS ENTER", 390, Color.WHITE);
    }

    private void drawMenu(Graphics2D g2) {
        drawShade(g2, 190);
        setFont(g2, Font.BOLD, 42);
        drawCentered(g2, "MEGAMAN", 210, Color.WHITE);
        setFont(g2, Font.BOLD, 27);
        drawCentered(g2, (menuSelection == 0 ? "> " : "  ") + "NEW GAME", 292,
                menuSelection == 0 ? new Color(100, 230, 255) : Color.WHITE);
        drawCentered(g2, (menuSelection == 1 ? "> " : "  ") + "EXIT", 336,
                menuSelection == 1 ? new Color(100, 230, 255) : Color.WHITE);
    }

    private void drawShade(Graphics2D g2, int alpha) {
        g2.setComposite(AlphaComposite.SrcOver.derive(alpha / 255f));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
        g2.setComposite(AlphaComposite.SrcOver);
    }

    private void drawWrapped(Graphics2D g2, String text, int x, int y, int width, int lineHeight) {
        FontMetrics metrics = g2.getFontMetrics();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            String trial = line.isEmpty() ? word : line + " " + word;
            if (metrics.stringWidth(trial) > width && !line.isEmpty()) {
                g2.drawString(line.toString(), x, y);
                y += lineHeight;
                line = new StringBuilder(word);
            } else {
                line = new StringBuilder(trial);
            }
        }
        if (!line.isEmpty()) g2.drawString(line.toString(), x, y);
    }

    private void drawCentered(Graphics2D g2, String text, int y, Color color) {
        g2.setColor(color);
        FontMetrics metrics = g2.getFontMetrics();
        g2.drawString(text, (GameFrame.SCREEN_WIDTH - metrics.stringWidth(text)) / 2, y);
    }

    private void setFont(Graphics2D g2, int style, int size) {
        g2.setFont(new Font(Font.SANS_SERIF, style, size));
    }

    private BufferedImage image(String frameName) {
        FrameImage frame = loader.getFrameImage(frameName);
        return frame.getImage();
    }
}
