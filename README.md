# Flappy Bird Clone-LibGDX
This is a simple Flappy Bird-style game built using **LibGDX**. It was developed as a learning project to explore game loops, physics, and collision handling in 2D. 

The game runs on Android via the LibGDX Android ;auncher and was tested using an Android emulator in Android Studio


---

### 🎮 Overview
The gameplay follows the classic Flappy Bird formula: 
- Tap to flap
- Avoid the incoming pipes
- Try not to crash and survive as long as possible

### Features
- Two‑frame animated bird sprite
- Gravity-based movement and jump physics
- Randomized obstacle(tube) generation
- Collision detection using LibGDX geometry utilities
- Score tracking system
- Three game states:
    - Ready
    - Playing
    - Game Over

---

### 🧱 Project structure
LibGDX organizes projects into modules. The relevant ones here are:

```core/```       → Main game logic (```FlappyBirdGame.java```)
```android/```   → Android launcher that initializes the core module
```assets/```     → Game textures and other resources

The android module is what the emulator runs. It loads the ```FlappyBirdGame``` class from the **core** module and hands control over to LibGDX’s lifecycle ```(create()```, ```render()```, ```dispose()```).

---

### ⚙️GamePlay system
The game uses the below state system
- **Ready** - waiting for the user input
- **Playing** - active gameplay (movement, scoring, collisions)
- **Game Over** - displays a restart prompt

---

### Bird movement
The bird's movement uses a simple velocity model:
- A tap applies an upward velocity
- Gravity increases downward velocity each frame
- Position updates continuously
- The sprite alternates between two textures to simulate flapping

---

### Obstacle generation/creation
- Four tube pairs move across the screen at a constant speed. 
- Each pair:
  - Has a fixed horizontal spacing
  - Receives a random vertical offset
- Tubes recycle when leaving the screen to maintain continuous gameplay

---

### Collision detector
- Bird → circle collider
- Tubes → rectangle colliders
Collisions are checked using ```Intersector.overlaps()``` method.

If the bird collides with a tube or goes out of bounds, the game transitions to the **Game Over** state.

---

### Scoring
- Score increases when the bird passes a tube
- A ```scoringTube``` index ensures each ostacle is counted once.

--- 

### 🧪 Status

This project was created as a learning exercise and is not actively maintained.
It may require an older LibGDX setup to run.

--- 

### 🛠 Tech Stack
Java
LibGDX
Android Studio
