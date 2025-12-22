package megaman.effect;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class FrameImage {
    private String name;
    private BufferedImage image;

    public FrameImage() {
        // this.
        this.name = null;
        this.image = null;
    }

    public FrameImage(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }

    // Đây là constructor nhận vào một đối tượng FrameImage khác → mục tiêu là tạo
    // bản sao của nó.
    public FrameImage(FrameImage frameImage) {
        // Mục đích: cấp phát bộ nhớ cho ảnh mới, chứ không dùng lại ảnh gốc (tránh
        // shared reference).
        // tạo ra một BufferedImage mới, hoàn toàn trống, không chứa hình ảnh gốc.
        // Nó chỉ có đúng:
        // Chiều rộng, chiều cao giống ảnh cũ
        // Kiểu ảnh giống (ARGB, RGB,...)
        image = new BufferedImage(
                frameImage.getImageWidth(),
                frameImage.getImageHeight(),
                frameImage.getImage().getType());
        // Tạo một đối tượng Graphics để vẽ lên ảnh mới (image).
        // Graphics hoạt động như "bút vẽ" để ta có thể vẽ nội dung từ ảnh gốc sang ảnh
        // mới.
        // Khi bạn gọi image.getGraphics(), Java cấp cho bạn một bút vẽ (Graphics) để vẽ
        // trực tiếp lên ảnh đó.
        Graphics g = image.getGraphics();
        g.drawImage(frameImage.getImage(), 0, 0, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getImageWidth() {
        return image.getWidth();
    }

    public int getImageHeight() {
        return image.getHeight();
    }

    // vẽ hình ảnh (image) tại tọa độ (x, y) sao cho tâm của ảnh nằm tại vị trí (x,
    // y) chứ không phải góc trên bên trái như thông thường.
    // ba tham so:
    // thamso1: ảnh cần vẽ
    // thamso2, thamso3: vị trí vẽ (góc trên trái của ảnh)
    // thamso4(observer): một đối tượng thực hiện giao diện ImageObserver — được
    // dùng để theo dõi tiến trình tải ảnh (nếu ảnh chưa được tải xong), ở đây đặt
    // là null là không quan tâm tới quá trình tải ảnh.
    public void draw(Graphics2D g2, int x, int y) {
        // g2.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2,
        // null);
        g2.drawImage(image, x - getImageWidth() / 2, y - getImageHeight() / 2, null);
    }
}
