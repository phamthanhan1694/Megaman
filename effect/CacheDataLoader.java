package megaman.effect;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;

// mẫu thiết kế Singleton
// Lớp CacheDataLoader này được thiết kế sao cho:
// + Chỉ được tạo ra một lần duy nhất (duy nhất một instance)
// + Có thể truy cập toàn cục thông qua phương thức getInstance()
// + Không thể tạo thêm đối tượng khác từ bên ngoài
public class CacheDataLoader {

    private static CacheDataLoader instance;

    private String framefile = "data/frame.txt";
    private String animationfile = "data/animation.txt";
    private String physmapfile = "data/phys_map.txt";

    private Hashtable<String, FrameImage> frameImages;
    private Hashtable<String, Animation> animations;

    // private CacheDataLoader() {
    // // private constructor to prevent instantiation
    // frameImages = new Hashtable<String, FrameImage>();
    // animation = new Hashtable<String, Animation>();
    // }

    private int[][] phys_map;

    private CacheDataLoader() {
    }

    public static CacheDataLoader getInstance() {
        if (instance == null) {
            instance = new CacheDataLoader();
        }
        return instance;
    }

    // dựa vào key lấy từ file text để tìm kiếm phần tử trong hashMap, lấy ra value
    // chính là một đối tượng Animation rồi khởi tạo đối tượng mới là copy của đối
    // tượng đó
    public Animation getAnimation(String name) {

        Animation animation = new Animation(instance.animations.get(name));
        return animation;

    }

    // tương tự với getAnimation
    public FrameImage getFrameImage(String name) {

        FrameImage frameImage = new FrameImage(instance.frameImages.get(name));
        return frameImage;

    }

    public int[][] getPhysicalMap() {
        return instance.phys_map;
    }

    public void loadFrame() throws IOException {
        // khởi tạo Hashtable để lưu các frame với tên làm key và đối tượng FrameImage
        // làm value
        frameImages = new Hashtable<String, FrameImage>();

        // đọc file, nếu là ảnh thì dùng ImageIO.read(new File(path))
        FileReader fr = new FileReader(framefile);
        BufferedReader br = new BufferedReader(fr);

        String line = null;

        if (br.readLine() == null) {
            System.out.println("No data");
            throw new IOException();
        } else {
            // đọc lại file từ đầu vì ban đàu đã kiểm tra br.readline() làm cho dòng đầu đã
            // bị đọc mất, do đó nếu không đọc lại sẽ mất dòng đầu tiên
            fr = new FileReader(framefile);
            br = new BufferedReader(fr);

            // 158

            // idle1
            // image data/megasprite.png
            // x 530
            // y 40
            // w 80
            // h 90
            //
            // lặp lại tương ứng
            while ((line = br.readLine()).equals(""))
                ;
            int n = Integer.parseInt(line);
            for (int i = 0; i < n; i++) {
                FrameImage frame = new FrameImage();
                while ((line = br.readLine()).equals(""))
                    ;
                frame.setName(line);

                while ((line = br.readLine()).equals(""))
                    ;
                String[] str = line.split(" ");
                String path = str[1];

                while ((line = br.readLine()).equals(""))
                    ;
                str = line.split(" ");
                int x = Integer.parseInt(str[1]);

                while ((line = br.readLine()).equals(""))
                    ;
                str = line.split(" ");
                int y = Integer.parseInt(str[1]);

                while ((line = br.readLine()).equals(""))
                    ;
                str = line.split(" ");
                int w = Integer.parseInt(str[1]);
                while ((line = br.readLine()).equals(""))
                    ;
                str = line.split(" ");
                int h = Integer.parseInt(str[1]);

                BufferedImage imageData = ImageIO.read(new File(path));
                BufferedImage image = imageData.getSubimage(x, y, w, h);
                frame.setImage(image);

                instance.frameImages.put(frame.getName(), frame); // name la key, doi tuong FrameImage la value
            }
        }
        br.close();
    }

    public void loadAnimation() throws IOException {
        animations = new Hashtable<String, Animation>();

        FileReader fr = new FileReader(animationfile);
        BufferedReader br = new BufferedReader(fr);

        String line = null;

        if (br.readLine() == null) {
            System.out.println("No data");
            throw new IOException();
        } else {
            fr = new FileReader(animationfile);
            br = new BufferedReader(fr);

            while ((line = br.readLine()).equals(""))
                ;
            int n = Integer.parseInt(line);
            for (int i = 0; i < n; i++) {
                Animation anim = new Animation();
                while ((line = br.readLine()).equals(""))
                    ;
                anim.setName(line);
                while ((line = br.readLine()).equals(""))
                    ;
                String[] str = line.split(" ");

                for (int j = 0; j < str.length; j += 2) {
                    anim.add(getFrameImage(str[j]), Double.parseDouble(str[j + 1]));
                }
                instance.animations.put(anim.getName(), anim);
            }
        }
        br.close();
    }

    // public int[][] getPhysicalMap(){
    // return instance.phys_map;
    // }

    public void loadPhysMap() throws IOException {

        FileReader fr = new FileReader(physmapfile);// trả về luồng dữ liệu
        BufferedReader br = new BufferedReader(fr); // đọc trên luồng dữ liệu

        String line = null;

        line = br.readLine();
        int numberOfRows = Integer.parseInt(line);
        line = br.readLine();
        int numberOfColumns = Integer.parseInt(line);

        instance.phys_map = new int[numberOfRows][numberOfColumns];

        for (int i = 0; i < numberOfRows; i++) {
            line = br.readLine();
            String[] str = line.split(" ");
            for (int j = 0; j < numberOfColumns; j++) {
                instance.phys_map[i][j] = Integer.parseInt(str[j]);
            }
        }
        // for (int i = 0; i < numberOfRows; i++) {
        // for (int j = 0; j < numberOfColumns; j++) {
        // System.err.println(" " + instance.phys_map[i][j]);
        // }
        // System.out.println();
        // }
        br.close();
    }

    public void loadData() throws IOException {
        loadFrame();
        loadAnimation();
        loadPhysMap();
    }
}
