package megaman.userinterface;

import javax.imageio.ImageIO;
// JPanel là một thành phần GUI (giao diện) trong thư viện Swing của Java.
// GamePanel kế thừa JPanel để vẽ giao diện game, ví dụ: nền, nhân vật, v.v.
import javax.swing.JPanel;

import megaman.gameobjects.PhysicalMap;

import megaman.effect.CacheDataLoader;

import megaman.effect.FrameImage;
import megaman.effect.Animation;
import megaman.gameobjects.GameWorld;
import megaman.gameobjects.Megaman;

// Graphics là lớp đại diện cho bút vẽ (canvas).
// Nó cho phép bạn vẽ hình, chữ, màu… lên màn hình hoặc thành phần GUI như JPanel.
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
// Thư viện màu, ví dụ như Color.red, Color.blue, v.v.
import java.awt.Color;

//GamePanel kế thừa JPanel Điều này nghĩa là GamePanel là một loại JPanel có thể tuỳ chỉnh, ví dụ như tự vẽ nền, vẽ nhân vật.
public class GamePanel extends JPanel implements Runnable, KeyListener {

    private Thread thread;
    private boolean isRunning;
    private InputManager inputManager;
    // BufferedImage image;
    // BufferedImage subImage;
    // FrameImage frame1;
    // FrameImage frame2;
    // FrameImage frame3;

    Animation anim1;
    private BufferedImage bufImage;
    private Graphics2D bufG2D;

    // Megaman megaman = new Megaman(300, 300, 100, 100, 0.1f);
    // PhysicalMap physicalMap = new PhysicalMap(0, 0);
    public GameWorld gameWorld;

    public GamePanel() {
        gameWorld = new GameWorld();
        inputManager = new InputManager(gameWorld);
        // System.out.println("Working directory: " + new File(".").getAbsolutePath());
        // System.out.println("Full image path: " + new
        // File("data/megasprite.png").getAbsolutePath());

        // try {
        // image = ImageIO.read(new File("data/megasprite.png"));
        // subImage = image.getSubimage(2, 5, 30, 100);
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // frame1 = CacheDataLoader.getInstance().getFrameImage("idleshoot1");
        // anim1 = CacheDataLoader.getInstance().getAnimation("robotRbullet");
        // anim1.flipAllImage();

        // lưu trữ hình ảnh trong bộ nhớ mà bạn có thể vẽ lên, đọc/ghi pixel, hoặc sử
        // dụng để hiển thị trên màn hình.
        // Đây là dạng ảnh mà bạn có thể làm các thao tác như drawImage(),
        // getSubimage(), getRGB()...
        bufImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        // tạo ra một ảnh mới trên bộ nhớ (buffered image) với kích thước cố định (chiều
        // rộng và chiều cao) và kiểu màu hỗ trợ ARGB (Alpha, Red, Green, Blue).

        try {
            BufferedImage image = ImageIO.read(new File("data/megasprite.png"));
            // BufferedImage image1 = image.getSubimage(529, 38, 100, 100);
            // frame1 = new FrameImage("frame1", image1);
            // BufferedImage image2 = image.getSubimage(616, 38, 100, 100);
            // frame2 = new FrameImage("frame2", image2);
            // BufferedImage image3 = image.getSubimage(704, 38, 100, 100);
            // frame3 = new FrameImage("frame3", image3);

            // anim = new Animation();
            // anim.add(frame1, 200 * 1000000); // 500ms
            // anim.add(frame2, 200 * 1000000);
            // anim.add(frame3, 200 * 1000000);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override // ghi đè phương thức paint của JPanel
    public void paint(Graphics g) {
        // g.setColor(Color.WHITE);
        // // Vì cả GameFrame và GamePanel đều nằm trong package megaman.userinterface,
        // bạn
        // // không cần import GameFrame.
        // // lấy thuộc tính static của GameFrame
        // g.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
        // -> Vẽ một hình chữ nhật đỏ từ góc trái trên (0,0) với kích thước bằng kích
        // thước của cửa sổ GameFrame.

        // g.drawImage(image, 10, 10, this); // vẽ hình ảnh lên GamePanel tại vị trí
        // (10, 10)
        // vẽ ảnh đó ở vị trí bất kỳ trên cửa sổ – như dán mảnh giấy đó vào 1 nơi trên
        // bảng
        // Graphics2D g2 = (Graphics2D) g; // ép kiểu Graphics sang Graphics2D để sử
        // dụng các phương thức vẽ nâng cao
        // anim.update(System.nanoTime());
        // anim.draw(100, 130, g2); // vẽ ảnh tại vị trí (100, 130) trên GamePanel

        // frame1.draw(g2, 130, 130);
        // anim1.draw(300, 300, g2);
        g.drawImage(bufImage, 0, 0, this);
    }

    public void startGame() {
        if (thread == null) {
            isRunning = true; // đặt trạng thái isRunning thành true để bắt đầu trò chơi
            thread = new Thread(this); // this đại diện cho đối tượng hiện tại của GamePanel, tức là đối tượng đang
                                       // chạy.
            // Tạo một luồng mới để chạy phương thức run() của GamePanel.
            thread.start(); // bắt đầu luồng mới, start thì sẽ gọi phương thức run() của GamePanel
        }
    }

    public void updateGame() {
        // megaman.update();
        gameWorld.update();
    }

    public void renderGame() {
        if (bufImage == null) {
            bufImage = new BufferedImage(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
        if (bufImage != null) {
            bufG2D = (Graphics2D) bufImage.getGraphics();
        }
        if (bufG2D != null) {
            bufG2D.setColor(Color.WHITE);
            bufG2D.fillRect(0, 0, GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT);
            // bufG2D.setColor(Color.RED);
            // bufG2D.fillRect(40, 50, 100, 100);

            // megaman.draw(bufG2D);
            // physicalMap.draw(bufG2D);
            gameWorld.render(bufG2D);
        }
    }

    @Override // ghi đè phương thức run() của interface Runnable
    // viết thành THREAD, vì trong quá trình chạy cần sleep
    // giới hạn số frame trong 1s => để game chạy với tốc độ ổn định
    // giảm cpu chạy nhiều lần gây hao phí ko cần thiết
    public void run() {
        // System.out.println("Game is running...");
        long FPS = 80;// giới hạn số frame trong 1 giây
        long period = 1000 * 1000000 / FPS; // vì đang sử dụng nano second
        // 80 FPS thì period(chu kì) là 1s/80 = 12.5ms
        long beginTime;
        long sleepTime;

        // int a = 1;
        beginTime = System.nanoTime(); // lấy thời gian hiện tại theo nano giây
        while (isRunning) {
            // gọi lại phương thức
            // System.out.println("a= " + a++);

            // giới hạn tốc độ này vì cpu quá nhanh
            // Update game
            // render game
            // chu kì của 1 frame là T gồm update và render và thời gian còn lại sẽ sleep
            // anim1.update(System.nanoTime());
            updateGame();
            renderGame();
            repaint(); // gọi lại phương thức paint() để vẽ lại GamePanel

            long deltaTime = System.nanoTime() - beginTime; // tính thời gian đã trôi qua kể từ khi bắt đầu
            // deltaTime là thời gian đã trôi qua kể từ khi bắt đầu vòng lặp
            sleepTime = period - deltaTime; // tính thời gian cần ngủ để đạt được tốc độ FPS mong muốn
            try {
                // Thread.sleep(100);
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime / 1000000); // chuyển từ nano giây sang mili giây
                } else
                    Thread.sleep(period / 2000000);
                // Khi hệ thống bị quá tải, sleepTime sẽ < 0 → tức là không còn thời gian để
                // sleep đúng theo lịch.
                // Nhưng nếu không sleep, vòng lặp sẽ chạy liên tục → gây hao CPU 100%.
                // Vì thế, bạn vẫn cố gắng nghỉ một chút (6ms) để tránh "đốt CPU", ngay cả khi
                // đã trễ.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            beginTime = System.nanoTime(); // cập nhật thời gian bắt đầu cho vòng lặp tiếp theo
        }
    }

    // callback method là phương thức được lớp khác gọi
    // ở đây là làm việc với bàn phím sẽ gọi
    @Override // ghi đè phương thức keyTyped của KeyListener
    public void keyTyped(java.awt.event.KeyEvent e) {// Xử lý sự kiện khi người dùng gõ phím
    }

    @Override // ghi đè phương thức keyPressed của KeyListener
    public void keyPressed(java.awt.event.KeyEvent e) {// Xử lý sự kiện khi người dùng nhấn phím
        // System.out.println("Key Pressed: " + e.getKeyChar()); // In ra ký tự của phím
        // đã nhấn
        // if(e.getKeyCode()==KeyEvent.VK_A){
        // System.out.println("Key A Pressed"); // Nếu phím A được nhấn, in ra thông báo
        // }
        inputManager.processKeyPressed(e.getKeyCode()); // Gọi phương thức processKeyPressed của InputManager để xử lý
                                                        // phím nhấn
    }

    @Override // ghi đè phương thức keyReleased của KeyListener
    public void keyReleased(java.awt.event.KeyEvent e) {// Xử lý sự kiện khi người dùng thả phím
        // System.out.println("Key Released: " + e.getKeyChar()); // In ra ký tự của
        // phím đã thả
        inputManager.processKeyReleased(e.getKeyCode());
    }

}
