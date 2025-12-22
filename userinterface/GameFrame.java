package megaman.userinterface;

import javax.swing.JFrame;

import megaman.effect.CacheDataLoader;

import java.awt.Toolkit;
import java.io.IOException;
import java.awt.Dimension;

public class GameFrame extends JFrame {
    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 600;

    GamePanel gamePanel;

    public GameFrame() {
        // dat frame o toa do nao, kich thuoc
        Toolkit toolkit = this.getToolkit();
        Dimension dimension = toolkit.getScreenSize();
        this.setBounds((dimension.width - SCREEN_WIDTH) / 2,
                (dimension.height - SCREEN_HEIGHT) / 2,
                SCREEN_WIDTH,
                SCREEN_HEIGHT);
        // đến đây thì dù có thoát màn hình toolkit thì chương trình vẫn chạy
        // do đó thêm dòng này để thoát chương trình khi đóng cửa sổ
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        try {
            CacheDataLoader.getInstance().loadData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gamePanel = new GamePanel(); // khởi tạo đối tượng
        add(gamePanel);
        // this.
        addKeyListener(gamePanel); // thêm lắng nghe bàn phím vào GamePanel để xử lý sự kiện bàn phím
    }

    public void startGame() {
        gamePanel.startGame(); // gọi phương thức startGame của GamePanel để bắt đầu trò chơi
    }

    public static void main(String args[]) {
        GameFrame gameFrame = new GameFrame();
        gameFrame.setVisible(true);
        gameFrame.startGame(); // bắt đầu trò chơi khi khởi tạo GameFrame
    }
}
