# Megaman

Java Swing Megaman demo.

## Build and Run

Use Maven:

```powershell
mvn clean package
java -jar target/megaman-1.0.0.jar
```

Or on Windows:

```powershell
.\run.bat
```

The Windows launcher rebuilds the game before starting it. Use `build.bat build-only`
to compile and package `target/megaman-1.0.0.jar` without opening the game.

Run rendering and movement regression checks with `mvn test`.

## Controls

- Enter: start, advance intro, resume, confirm menu
- Escape: pause
- Left/Right: move
- Up/W/Space: jump
- Down: duck
- A: shoot

The player only moves after the intro has advanced into gameplay.

Images, sounds, animations, enemy spawns, and map text files are loaded from
`src/main/resources/data`. Map tile IDs refer to the `tiled1`, `tiled2`, etc.
regions defined in `frame.txt`; enemy placement is in `enemies.txt`.
