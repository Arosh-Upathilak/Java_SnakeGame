# 🐍 Java Snake Game

This is a simple Snake Game built using Java. It demonstrates basic GUI development, game loops, and event handling in Java.

---

## 📁 Project Structure

SnakeGame/  
├── src/  
│   ├── Main.java  
│   ├── GamePanel.java  
│   └── assets/  
│       ├── snaketitle.jpg  
│       ├── enemy.png  
│       ├── snakeimage.png  
│       ├── upmouth.png  
│       ├── downmouth.png  
│       ├── leftmouth.png  
│       └── rightmouth.png  
├── SnakeGame.bat  
├── SnakeGame.iml  
├── README.md

---

## ▶️ How to Run

### 🔧 Requirements
- Java JDK (version 8 or higher)
- A Java IDE (e.g., IntelliJ, Eclipse) or command-line terminal

### 🏃 Run via Terminal

cd path/to/SnakeGame/src  
javac Main.java  
java Main

### 🖱️ Run via .bat file (Windows)

Create the `.bat` file to run the program in the background:

echo java Main > SnakeGame.bat

Then create a `.vbs` file to run the batch file silently:

1. Run:

echo > SnakeGame.vbs  
notepad SnakeGame.vbs

2. Paste this into Notepad:

Set WshShell = CreateObject("WScript.Shell")  
WshShell.Run "SnakeGame.bat", 0, False

Save and close the file. Now double-click the `.vbs` file to run the game without opening a terminal window.

---

## 🎮 Controls

- Arrow keys: Control the snake
- Eat food to grow
- Avoid walls and yourself

---

## 🛠️ Features

- Smooth gameplay
- Score tracking
- Game over logic
- Custom graphics

---

## 🖼️ Assets

All game images are located in the `src/assets/` folder.

---

## 🧑‍💻 Author

**Arosh Upathilak**  
GitHub: [Arosh-Upathilak](https://github.com/Arosh-Upathilak)
