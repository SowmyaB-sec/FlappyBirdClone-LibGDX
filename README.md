# FlappyBirdClone-LibGDX
This is a simple Flappy Bird-style game built using **LibGDX**. It runs on Android through the LibGDX Android launcher and was tested on an Android emulator in Android Studio. The goal was to implement the core mechanics of a side‑scrolling obstacle game while keeping the codebase readable and aligned with LibGDX's structure.

---

### 🎮 Overview
The gameplay is exactly what you’d expect: tap to flap, avoid the pipes, try not to crash.
The game includes:
- A two‑frame animated bird sprite
- Gravity and jump physics
- Randomized tube generation
- Collision detection using LibGDX's geometry utilities
- A basic scoring system
- Three game states: Ready, Playing, and Game Over

It is intentionally lightweight, but it covers the essential components of a functional 2D game loop.

Supporting images from the internet can be added here to illustrate the gameplay or mechanics.

### Project structure
LibGDX organizes projects into modules. The relevant ones here are:
```Code
core/       → Main game logic (FlappyBirdGame.java)
android/    → Android launcher that initializes the core module
assets/     → Game textures and other resources
```
The android module is what the emulator runs. It loads the **FlappyBirdGame** class from the **core** module and hands control over to LibGDX’s lifecycle **(create(), render(), dispose()**).

### GamePlay system
The game uses the below state system
- Ready - waiting for the first screen tap
- Playing - physics, movement, and scoring active
- Game Over - displays a restart prompt

### Bird movement
The bird's movement is based on a simple velocity model:
- A tap applies an upward impulse
- Gravity increases velocity each frame
- Position updates accordingly
- The sprite alternates between two textures to simulate flapping

The physics are intentionally minimal, but they create the familiar feel of the original game.

### Obstacle generation/creation
Four tube pairs move across the screen at a constant speed. Each pair:
- Has a fixed horizontal spacing
- Receives a random vertical offset
- Wraps around to the right once it exits the screen

This keeps the gameplay continuous without allocating new objects during runtime.

### Collision detector
The bird uses a circle collider, and each tube uses a rectangle.
Collisions are checked using ```Intersector.overlaps()```.

If the bird intersects with a tube or goes out of bounds, the game transitions to the Game Over state.

### Scoring
The score increments when the bird passes the midpoint of the screen relative to the next tube. A ```scoringTube``` index ensures each tube contributes only once.
