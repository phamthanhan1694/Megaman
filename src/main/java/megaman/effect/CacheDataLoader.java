package megaman.effect;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class CacheDataLoader {

    private static CacheDataLoader instance;

    private final String framefile = "data/frame.txt";
    private final String animationfile = "data/animation.txt";
    private final String physmapfile = "data/phys_map.txt";
    private final String backgroundMapFile = "data/background_map.txt";
    private final String soundfile = "data/sounds.txt";

    private Hashtable<String, FrameImage> frameImages;
    private Hashtable<String, Animation> animations;
    private Hashtable<String, Clip> sounds = new Hashtable<>();
    private List<EnemySpawn> enemySpawns = List.of();
    private int[][] physMap;
    private int[][] backgroundMap;

    private CacheDataLoader() {
    }

    public static CacheDataLoader getInstance() {
        if (instance == null) {
            instance = new CacheDataLoader();
        }
        return instance;
    }

    public Animation getAnimation(String name) {
        Animation animation = animations.get(name);
        if (animation == null) {
            throw new IllegalArgumentException("Animation not found: " + name);
        }
        return new Animation(animation);
    }

    public FrameImage getFrameImage(String name) {
        FrameImage frameImage = frameImages.get(name);
        if (frameImage == null) {
            throw new IllegalArgumentException("Frame image not found: " + name);
        }
        return new FrameImage(frameImage);
    }

    public Clip getSound(String name) {
        return sounds.get(name);
    }

    public int[][] getPhysicalMap() {
        return copyMap(physMap);
    }

    public int[][] getBackgroundMap() {
        return copyMap(backgroundMap);
    }

    private int[][] copyMap(int[][] source) {
        int[][] copy = new int[source.length][];
        for (int i = 0; i < source.length; i++) copy[i] = source[i].clone();
        return copy;
    }

    public record EnemySpawn(String type, float x, float y, String direction) {}

    public List<EnemySpawn> getEnemySpawns() { return enemySpawns; }

    public void loadEnemySpawns() throws IOException {
        List<EnemySpawn> spawns = new ArrayList<>();
        try (BufferedReader reader = readText("data/enemies.txt")) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                String[] values = line.split("\\s+");
                if (values.length != 4) throw new IOException("Invalid enemy entry: " + line);
                spawns.add(new EnemySpawn(values[0], Float.parseFloat(values[1]),
                        Float.parseFloat(values[2]), values[3]));
            }
        }
        enemySpawns = List.copyOf(spawns);
    }

    public void stopSound(String name) {
        Clip clip = getSound(name);
        if (clip != null) clip.stop();
    }

    public void closeSounds() {
        for (Clip clip : sounds.values()) clip.close();
        sounds.clear();
    }

    public BufferedImage getImage(String path) throws IOException {
        return readImage(path);
    }

    public void playSound(String name) {
        playSound(name, false);
    }

    public void playSound(String name, boolean loop) {
        Clip clip = getSound(name);
        if (clip == null) {
            return;
        }
        if (clip.isRunning()) {
            clip.stop();
        }
        clip.setFramePosition(0);
        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }

    public void loadData() throws IOException {
        loadFrame();
        loadAnimation();
        loadPhysMap();
        loadBackgroundMap();
        loadEnemySpawns();
        loadSounds();
    }

    public void loadFrame() throws IOException {
        frameImages = new Hashtable<>();
        Hashtable<String, BufferedImage> sheets = new Hashtable<>();

        try (BufferedReader br = readText(framefile)) {
            int numberOfFrames = Integer.parseInt(readNonEmptyLine(br));
            for (int i = 0; i < numberOfFrames; i++) {
                FrameImage frame = new FrameImage();
                frame.setName(readNonEmptyLine(br));

                String path = readNonEmptyLine(br).split("\\s+")[1];
                int x = Integer.parseInt(readNonEmptyLine(br).split("\\s+")[1]);
                int y = Integer.parseInt(readNonEmptyLine(br).split("\\s+")[1]);
                int width = Integer.parseInt(readNonEmptyLine(br).split("\\s+")[1]);
                int height = Integer.parseInt(readNonEmptyLine(br).split("\\s+")[1]);

                BufferedImage imageData = sheets.get(path);
                if (imageData == null) {
                    imageData = readImage(path);
                    sheets.put(path, imageData);
                }
                frame.setImage(imageData.getSubimage(x, y, width, height));
                frameImages.put(frame.getName(), frame);
            }
        }
    }

    public void loadAnimation() throws IOException {
        animations = new Hashtable<>();

        try (BufferedReader br = readText(animationfile)) {
            int numberOfAnimations = Integer.parseInt(readNonEmptyLine(br));
            for (int i = 0; i < numberOfAnimations; i++) {
                Animation animation = new Animation();
                animation.setName(readNonEmptyLine(br));

                String[] parts = readNonEmptyLine(br).split("\\s+");
                for (int j = 0; j < parts.length; j += 2) {
                    animation.add(getFrameImage(parts[j]), Double.parseDouble(parts[j + 1]));
                }
                animations.put(animation.getName(), animation);
            }
        }
    }

    public void loadPhysMap() throws IOException {
        physMap = readIntMap(physmapfile);
    }

    public void loadBackgroundMap() throws IOException {
        backgroundMap = readIntMap(backgroundMapFile);
    }

    public void loadSounds() throws IOException {
        closeSounds();
        sounds = new Hashtable<>();

        try (BufferedReader br = readText(soundfile)) {
            int numberOfSounds = Integer.parseInt(readNonEmptyLine(br));
            for (int i = 0; i < numberOfSounds; i++) {
                String[] parts = readNonEmptyLine(br).split("\\s+");
                try {
                    Clip clip = loadClip(parts[1]);
                    if (clip != null) {
                        sounds.put(parts[0], clip);
                    }
                } catch (LineUnavailableException | UnsupportedAudioFileException | IllegalArgumentException e) {
                    System.err.println("Cannot load sound " + parts[0] + ": " + e.getMessage());
                }
            }
        }
    }

    private int[][] readIntMap(String path) throws IOException {
        try (BufferedReader br = readText(path)) {
            int numberOfRows = Integer.parseInt(readNonEmptyLine(br));
            int numberOfColumns = Integer.parseInt(readNonEmptyLine(br));
            int[][] map = new int[numberOfRows][numberOfColumns];

            for (int i = 0; i < numberOfRows; i++) {
                String[] values = readNonEmptyLine(br).split("\\s+");
                for (int j = 0; j < numberOfColumns; j++) {
                    map[i][j] = Integer.parseInt(values[j]);
                }
            }
            return map;
        }
    }

    private Clip loadClip(String path) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        InputStream inputStream = openResource(path);
        if (inputStream == null) {
            return null;
        }

        try (InputStream buffered = new BufferedInputStream(inputStream);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(buffered);
                AudioInputStream decodedStream = decodeAudio(audioInputStream)) {
            Clip clip = AudioSystem.getClip();
            clip.open(decodedStream);
            return clip;
        }
    }

    private AudioInputStream decodeAudio(AudioInputStream sourceStream) {
        AudioFormat sourceFormat = sourceStream.getFormat();
        if (AudioFormat.Encoding.PCM_SIGNED.equals(sourceFormat.getEncoding())) {
            return sourceStream;
        }

        AudioFormat decodedFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                sourceFormat.getSampleRate(),
                16,
                sourceFormat.getChannels(),
                sourceFormat.getChannels() * 2,
                sourceFormat.getSampleRate(),
                false);
        return AudioSystem.getAudioInputStream(decodedFormat, sourceStream);
    }

    private BufferedReader readText(String path) throws IOException {
        InputStream inputStream = openResource(path);
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private BufferedImage readImage(String path) throws IOException {
        InputStream inputStream = openResource(path);
        try (InputStream in = inputStream) {
            return ImageIO.read(in);
        }
    }

    private InputStream openResource(String path) throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
        if (inputStream != null) {
            return inputStream;
        }
        throw new FileNotFoundException("Resource not found: " + path);
    }

    private String readNonEmptyLine(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        throw new IOException("Unexpected end of data file");
    }
}
