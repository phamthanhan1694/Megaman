package megaman.effect;

import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Animation {
    private String name;// tên để quản lý animation
    private long beginTime;
    private int currentFrame;
    private boolean isRepeated; // kiểm tra xem trạng thái có lặp lại
    private boolean drawRectFrame; // kiểm tra xem có vẽ khung hình hay không, để dễ quan sát độ rộng khung hình

    private ArrayList<FrameImage> frameImages;
    private ArrayList<Double> delayFrames;
    private ArrayList<Boolean> ignoreFrames;

    public Animation() {
        beginTime = 0;
        currentFrame = 0;
        isRepeated = true; // đa phần animation đều lặp lại
        drawRectFrame = false;

        frameImages = new ArrayList<FrameImage>();
        delayFrames = new ArrayList<Double>();
        ignoreFrames = new ArrayList<Boolean>();
    }

    public Animation(Animation animation) {
        beginTime = animation.beginTime;
        currentFrame = animation.currentFrame;
        isRepeated = animation.isRepeated;
        drawRectFrame = animation.drawRectFrame;

        frameImages = new ArrayList<FrameImage>();
        for (FrameImage x : animation.frameImages) {
            frameImages.add(new FrameImage(x));
        }
        delayFrames = new ArrayList<Double>();
        for (Double x : animation.delayFrames) {
            delayFrames.add(x);
        }
        ignoreFrames = new ArrayList<Boolean>();
        for (Boolean x : animation.ignoreFrames) {
            ignoreFrames.add(x);
        }

    }

    // getter setter của name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    //

    // getter, setter của beginTime
    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }
    //

    // getter, setter của currentFrame
    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame >= 0 && currentFrame < frameImages.size()) {
            this.currentFrame = currentFrame;
        } else {
            this.currentFrame = 0; // nếu không hợp lệ thì đặt về 0
        }

    }
    //

    // getter, setter của isRepeated
    public boolean getIsRepeated() {
        return isRepeated;
    }

    public void setIsRepeated(boolean isRepeated) {
        this.isRepeated = isRepeated;
    }
    //

    // getter, setter của drawRectFrame
    public boolean getDrawRectFrame() {
        return drawRectFrame;
    }

    public void setDrawRectFrame(boolean drawRectFrame) {
        this.drawRectFrame = drawRectFrame;
    }
    //

    // getter, setter của frameImages
    public ArrayList<FrameImage> getFrameImages() {
        return frameImages;
    }

    public void setFrameImages(ArrayList<FrameImage> frameImages) {
        this.frameImages = frameImages;
    }
    //

    // getter, setter của ignoreFrames
    public ArrayList<Boolean> getIgnoreFrames() {
        return ignoreFrames;
    }

    public void setIgnoreFrames(ArrayList<Boolean> ignoreFrames) {
        this.ignoreFrames = ignoreFrames;
    }
    //

    // getter, setter của delayFrames
    public ArrayList<Double> getDelayFrames() {
        return delayFrames;
    }

    public void setDelayFrames(ArrayList<Double> delayFrames) {
        this.delayFrames = delayFrames;
    }
    //

    // kiểm tra xem khung hình có bị bỏ qua hay không
    public boolean isIgnoreFrame(int id) {
        return ignoreFrames.get(id);
    }

    // bỏ qua khung hình có id
    public void setIgnoreFrame(int id) {
        if (id >= 0 && id < ignoreFrames.size()) {
            ignoreFrames.set(id, true);
        }
    }

    // nhận lại khung hình đã bỏ qua
    public void unIgnoreFrame(int id) {
        if (id >= 0 && id < ignoreFrames.size()) {
            ignoreFrames.set(id, false);
        }
    }

    // reset animation trở về trạng thái ban đầu
    public void reset() {
        currentFrame = 0;
        beginTime = 0;
        for (int i = 0; i < ignoreFrames.size(); i++) {
            ignoreFrames.set(i, false);
        }
    }

    // mỗi frame sẽ gồm 3 thuộc tính
    public void add(FrameImage frameImage, double timeToNextFrame) {
        ignoreFrames.add(false); // mặc định là không bỏ qua khung hình
        frameImages.add(frameImage);
        // autoboxing (Java tự động chuyển) từ double ->Double
        delayFrames.add(timeToNextFrame); // thời gian để chuyển sang khung hình tiếp theo
    }

    public BufferedImage getCurrentImage() {
        return frameImages.get(currentFrame).getImage();
    }

    private void nextFrame() {
        if (currentFrame >= frameImages.size() - 1) {
            if (isRepeated)
                currentFrame = 0; // nếu lặp lại thì quay về khung hình đầu tiên
        } else
            currentFrame++; // nếu không lặp lại thì chuyển sang khung hình tiếp theo
        if (ignoreFrames.get(currentFrame)) {
            nextFrame(); // nếu khung hình hiện tại bị bỏ qua thì chuyển sang khung hình tiếp theo
        }
    }

    public boolean isLastFrame() {
        if (currentFrame == frameImages.size() - 1)
            return true;
        else
            return false;
    }

    // nhay đến khung hình tiếp theo và cập nhật lại thời gian hiện tại nếu thời
    // gian đã đủ
    public void update(long currentTime) {
        if (beginTime == 0)
            beginTime = currentTime;
        else {
            if (currentTime - beginTime > delayFrames.get(currentFrame)) {
                nextFrame();
                beginTime = currentTime; // cập nhật thời gian bắt đầu cho khung hình mới
            }
        }
    }

    // Lật tất cả các ảnh trong danh sách frameImages theo chiều ngang (trái ↔
    // phải).
    public void flipAllImage() {
        for (int i = 0; i < frameImages.size(); i++) {
            // Lấy ảnh hiện tại (chỉ số i) từ danh sách frameImages
            BufferedImage image = frameImages.get(i).getImage();
            // Tạo một phép biến đổi (transform) để lật ảnh theo trục dọc (ngang):
            // -1 (X) lật trái–phải
            // 1 (Y) không thay đổi chiều dọc
            AffineTransform tx = AffineTransform.getScaleInstance(-1, 1); // Nhưng lúc này ảnh sẽ bị "đẩy lệch ra ngoài
                                                                          // khung", nên cần bước tiếp theo.

            // – Tịnh tiến ảnh ngược lại theo trục X đúng bằng chiều rộng ảnh
            // → Đưa ảnh lật quay trở lại đúng vị trí ban đầu
            tx.translate(-image.getWidth(), 0); // chưa thao tác lên ảnh mà chỉ nhận tham số để điều chỉnh

            // – Tạo một bộ xử lý biến đổi ảnh (AffineTransformOp) với phép lật đã định.
            // – TYPE_BILINEAR: kiểu nội suy để chất lượng ảnh mượt hơn sau khi biến đổi.
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            // Tham số thứ hai (destinationImage) là nơi để lưu kết quả sau khi biến đổi.
            // Nếu bạn truyền null, Java sẽ:
            // Tự động tạo một ảnh mới (BufferedImage) có kích thước phù hợp để chứa kết
            // quả.
            image = op.filter(image, null); // – Áp dụng phép biến đổi (op) lên ảnh và tạo ra ảnh mới đã được lật.
            frameImages.get(i).setImage(image); // cập nhật lại ảnh đã lật

            // Lưu ý khi dùng:
            // + Hàm này sẽ thay đổi vĩnh viễn ảnh gốc trong frameImages
            // + Nếu cần giữ cả bản gốc và bản lật, bạn nên copy ảnh trước khi lật
            // + Chỉ áp dụng với BufferedImage, không phải các loại Image thông thường
        }

    }

    public void draw(int x, int y, Graphics2D g2) {
        BufferedImage image = getCurrentImage();
        g2.drawImage(image, x - image.getWidth() / 2, y - image.getHeight() / 2, null);
        if (drawRectFrame) {
            g2.drawRect(x - image.getWidth() / 2, y - image.getHeight() / 2, image.getWidth(), image.getHeight());
        }
    }

}
