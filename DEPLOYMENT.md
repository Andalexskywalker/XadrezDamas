# Deployment and Portfolio Guide

## 1. GitHub Repository
Your GitHub repository should contain the **source code**, not the compiled files.

### What to Commit
- `src/` folder (your Java code)
- `art/` folder (images/icons if any)
- `README.md` (Project description)
- `build_jar.bat` (Build script)
- `.gitignore` (Configuration to ignore unnecessary files)

### What NOT to Commit
- `bin/` folder (Compiled classes)
- `XadrezDamas.jar` (The executable - upload this to "Releases" instead)

### Steps to Upload
1. Initialize Git: `git init`
2. Add files: `git add .`
3. Commit: `git commit -m "Initial commit of Chess/Checkers game"`
4. Create a repo on GitHub.
5. Push your code:
   ```bash
   git remote add origin <your-github-repo-url>
   git branch -M main
   git push -u origin main
   ```

## 2. Creating a Release (The JAR File)
To let people download and play your game without compiling it:

1. Go to your GitHub repository page.
2. Click on **Releases** (usually on the right sidebar).
3. Click **Draft a new release**.
4. Tag version: `v1.0`.
5. Title: `Release v1.0`.
6. **Attach binaries**:
   - It is highly recommended to **ZIP** both `XadrezDamas.jar` and `play.bat` together into a file named `XadrezDamas_v1.0.zip`.
   - Upload this `.zip` file. This ensures users get the launcher script.
7. Click **Publish release**.

Now, anyone can download the zip, extract it, and double-click `play.bat` to play!

## 3. Portfolio Presentation
To make this look good on your portfolio:

### Screenshots & Video
- **Take Screenshots**: Capture the Main Menu, a Chess game in progress, and a Checkers game.
- **Record a Demo**: Use a tool like OBS or Loom to record a 30-second video showing:
    - Opening the game.
    - Selecting a mode (1 Player vs CPU).
    - Making a few moves.
    - Showing the "Game Over" or other features.

### Description
Write a short paragraph explaining what you built:
> "A Java-based desktop application featuring both Chess and Checkers. Implements Object-Oriented Programming principles, custom game logic, and a Swing-based GUI. Features include a 2-player mode and a basic CPU opponent."

## 4. Troubleshooting "Java Exception"
If double-clicking the JAR gives an error, it's often because Windows hasn't associated `.jar` files with the Java Runtime correctly.

**Solution:**
- Users should have Java installed.
- You can provide a simple `play.bat` file for them alongside the JAR:
  ```bat
  @echo off
  start javaw -jar XadrezDamas.jar
  exit
  ```
