# Java Chess & Checkers Engine

A robust desktop game engine featuring both Chess and Checkers (Damas), built with Java Swing. This project demonstrates object-oriented programming principles, game logic implementation, and custom UI rendering.

## Features

-   **Two Game Modes:**
    -   **1 Player (vs CPU):** Challenge a computer opponent with random move logic.
    -   **2 Players:** Play locally against a friend.
-   **Chess (Xadrez):** Complete implementation of standard chess rules (movement, capture, promotion).
-   **Checkers (Damas):**
    -   Mandatory capture enforcement (highlighted moves).
    -   "Dama Dupla" (King) movement logic.
    -   Multi-jump logic.
-   **Interactive UI:**
    -   Smooth piece dragging and dropping.
    -   **Animated CPU Moves:** Visual feedback for computer actions.
    -   Valid move indicators (Green/Red highlights).
    -   Game Over detection.

## Technologies Used

-   **Language:** Java (JDK 21)
-   **GUI Framework:** Swing (JFrame, JPanel, Graphics2D)
-   **Concepts:** OOP, Inheritance, Polymorphism, Event Handling, Game Loop/Timer.

## How to Run

### From Release (Recommended)
1.  Go to the **Releases** section of this repository.
2.  Download the latest `.zip` file (e.g., `XadrezDamas_v1.0.zip`).
3.  Extract the zip file.
4.  Double-click `play.bat` to start the game.

### From Source (Eclipse)
1.  Import the project into Eclipse.
2.  Run `src/pds/menu/MenuJogo.java` as a Java Application.

### From Command Line
```bash
javac -d bin -sourcepath src src/pds/menu/MenuJogo.java
java -cp bin pds.menu.MenuJogo
```

## Project Structure

-   `pds.menu`: Main menu and game selection.
-   `pds.xadrez`: Chess game logic and rendering.
-   `pds.damas`: Checkers game logic, including mandatory capture rules.
-   `pds.peca`: Base classes for pieces and the board (`Tabuleiro`).
-   `art/`: Game assets (images).

## Author

[Your Name/Portfolio Link]
