package com.mygdx.flappybirdgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBirdGame extends ApplicationAdapter {

    // Rendering
    private SpriteBatch batch;

    // Textures
    private Texture background;
    private Texture bird1, bird2;
    private Texture[] birds;
    private Texture topTube, bottomTube;
    private Texture gameOver;

    // Bird physics
    private float birdY;
    private float velocity = 0;

    // Constants
    private static final float JUMP_VELOCITY = -30f;
    private static final float GRAVITY = 2f;
    private static final float GAP = 400f;
    private static final float TUBE_VELOCITY = 4f;

    // Tubes
    private static final int NUMBER_OF_TUBES = 4;
    private float distanceBetweenTubes;
    private float[] tubeX;
    private float[] offset;

    // Collision shapes
    private Circle birdCircle;
    private Rectangle[] topRectangles;
    private Rectangle[] bottomRectangles;

    // Game state
    private int flapState = 0;
    private int gameState = 0; // 0 = ready, 1 = playing, 2 = game over
    private int score = 0;
    private int scoringTube = 0;

    // Random generator
    private Random random;

    // Font
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        random = new Random();

        // Load textures
        background = new Texture("bg.png");
        bird1 = new Texture("bird.png");
        bird2 = new Texture("bird2.png");
        birds = new Texture[]{bird1, bird2};

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        gameOver = new Texture("gameover.png");

        // Bird starting position
        birdY = Gdx.graphics.getHeight() / 2f;

        // Tube setup
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3f / 4f;
        tubeX = new float[NUMBER_OF_TUBES];
        offset = new float[NUMBER_OF_TUBES];

        topRectangles = new Rectangle[NUMBER_OF_TUBES];
        bottomRectangles = new Rectangle[NUMBER_OF_TUBES];

        birdCircle = new Circle();

        // Score font
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        // Initialize tubes
        for (int i = 0; i < NUMBER_OF_TUBES; i++) {
            offset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - GAP - 200);
            tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topRectangles[i] = new Rectangle();
            bottomRectangles[i] = new Rectangle();
        }
    }

    private void resetGame() {
        birdY = Gdx.graphics.getHeight() / 2f;
        velocity = 0;
        score = 0;
        scoringTube = 0;

        for (int i = 0; i < NUMBER_OF_TUBES; i++) {
            offset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - GAP - 200);
            tubeX[i] = Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            topRectangles[i].set(0, 0, 0, 0);
            bottomRectangles[i].set(0, 0, 0, 0);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // READY STATE
        if (gameState == 0) {
            if (Gdx.input.justTouched()) {
                gameState = 1;
            }
        }

        // PLAYING STATE
        else if (gameState == 1) {

            // Jump
            if (Gdx.input.justTouched()) {
                velocity = JUMP_VELOCITY;
            }

            // Bird animation
            flapState = flapState == 0 ? 1 : 0;

            // Gravity
            velocity += GRAVITY;
            birdY -= velocity;

            // Floor / ceiling collision
            if (birdY <= 0 || birdY >= Gdx.graphics.getHeight() - birds[flapState].getHeight()) {
                gameState = 2;
            }

            // Scoring
            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2f) {
                score++;
                scoringTube = (scoringTube + 1) % NUMBER_OF_TUBES;
            }

            // Move tubes
            for (int i = 0; i < NUMBER_OF_TUBES; i++) {

                if (tubeX[i] < -topTube.getWidth()) {
                    tubeX[i] += NUMBER_OF_TUBES * distanceBetweenTubes;
                    offset[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - GAP - 200);
                } else {
                    tubeX[i] -= TUBE_VELOCITY;
                }

                float tubeTopY = Gdx.graphics.getHeight() / 2f + GAP / 2f + offset[i];
                float tubeBottomY = Gdx.graphics.getHeight() / 2f - bottomTube.getHeight() - GAP / 2f + offset[i];

                batch.draw(topTube, tubeX[i], tubeTopY);
                batch.draw(bottomTube, tubeX[i], tubeBottomY);

                topRectangles[i].set(tubeX[i], tubeTopY, topTube.getWidth(), topTube.getHeight());
                bottomRectangles[i].set(tubeX[i], tubeBottomY, bottomTube.getWidth(), bottomTube.getHeight());
            }
        }

        // GAME OVER STATE
        else if (gameState == 2) {

            batch.draw(
                    gameOver,
                    Gdx.graphics.getWidth() / 2f - gameOver.getWidth() / 2f,
                    Gdx.graphics.getHeight() / 2f - gameOver.getHeight() / 2f
            );

            if (Gdx.input.justTouched()) {
                resetGame();
                gameState = 1;
            }

            // Draw tubes in last position
            for (int i = 0; i < NUMBER_OF_TUBES; i++) {
                float tubeTopY = Gdx.graphics.getHeight() / 2f + GAP / 2f + offset[i];
                float tubeBottomY = Gdx.graphics.getHeight() / 2f - bottomTube.getHeight() - GAP / 2f + offset[i];

                batch.draw(topTube, tubeX[i], tubeTopY);
                batch.draw(bottomTube, tubeX[i], tubeBottomY);
            }
        }

        // Draw bird
        float birdX = Gdx.graphics.getWidth() / 2f - birds[flapState].getWidth() / 2f;
        batch.draw(birds[flapState], birdX, birdY);

        // Update collision circle
        birdCircle.set(
                Gdx.graphics.getWidth() / 2f,
                birdY + birds[flapState].getHeight() / 2f,
                birds[flapState].getWidth() / 2f
        );

        // Collision detection
        if (gameState == 1) {
            for (int i = 0; i < NUMBER_OF_TUBES; i++) {
                if (Intersector.overlaps(birdCircle, topRectangles[i]) ||
                        Intersector.overlaps(birdCircle, bottomRectangles[i])) {
                    gameState = 2;
                }
            }
        }

        // Draw score
        font.draw(batch, String.valueOf(score), 200, 200);

        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        background.dispose();
        bird1.dispose();
        bird2.dispose();
        topTube.dispose();
        bottomTube.dispose();
        gameOver.dispose();
        font.dispose();
    }
}
