package io.github.pengun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;


    private Texture playerTexture;
    private Texture snowballTexture;
    private Texture fishTexture;

    private World gameWorld;
    private OrthographicCamera camera;
    private Viewport viewport;

    enum GameState
    {
        GAME_STARTED,
        GAME_RUNNING,
        GAME_PAUSED,
        GAME_ENDED
    }

    private GameState state = GameState.GAME_STARTED;

    private Sound snowballSFX;
    private Sound hitSFX;
    private Sound stageClearSFX;

    enum StageConfig {
        STAGE1(0.9f, 0.15f, 20f, 3f, 3, new Color(1f, 1f, 1f, 1f)),
        STAGE2(0.9f, 0.15f, 20f, 3f, 5, new Color(0.9f, 0.9f, 1f, 1f)),
        STAGE3(0.92f, 0.2f, 15f, 3f, 5, new Color(0.85f, 0.85f, 1f, 1f)),
        STAGE4(0.95f, 0.2f, 15f, 2f, 7, new Color(0.8f, 0.8f, 1f, 1f)),
        STAGE5(0.99f, 0.3f, 15f, 2f, 10, new Color(0.7f, 0.7f, 1f, 1f));

        private final float friction;
        private final float snowballCooldown;
        private final float snowballShotspeed;
        private final float fishSpawnRate;
        private final int requiredFishesToKill;
        private final Color bgColor;

        StageConfig(float friction, float snowballCooldown, float snowballShotspeed,
                    float fishSpawnRate, int requiredFishesToKill, Color bgColor)
        {
            this.friction = friction;
            this.snowballCooldown = snowballCooldown;
            this.snowballShotspeed = snowballShotspeed;
            this.fishSpawnRate = fishSpawnRate;
            this.requiredFishesToKill = requiredFishesToKill;
            this.bgColor = bgColor;
        }
        public float getFriction() {return friction;}
        public float getSnowballCooldown() {return snowballCooldown;}
        public float getSnowballShotspeed() {return snowballShotspeed;}
        public float getFishSpawnRate() {return fishSpawnRate;}
        public int getRequiredFishesToKill() {return requiredFishesToKill;}
        public Color getBackgroundColor() {return bgColor;}
    }
    private int stage = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture("penguin.png");
        snowballTexture = new Texture("snowball.png");
        fishTexture = new Texture("fish.png");
        snowballSFX = Gdx.audio.newSound(Gdx.files.internal("snowball.wav"));
        hitSFX = Gdx.audio.newSound(Gdx.files.internal("hit.mp3"));
        stageClearSFX = Gdx.audio.newSound(Gdx.files.internal("stageCleared.wav"));
        gameWorld = null;

        camera = new OrthographicCamera();
        viewport = new FitViewport(1920,1080, camera);
        camera.setToOrtho(false,1920,1080);
    }

    @Override
    public void render() {
        input();
        update();
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
        snowballTexture.dispose();
        fishTexture.dispose();
    }

    void advanceStage()
    {
        stage++;
        if (stage > StageConfig.values().length)
            state = GameState.GAME_ENDED;
        else {
            StageConfig currentStage = StageConfig.valueOf("STAGE"+stage);
            gameWorld = new World(playerTexture, snowballTexture, fishTexture,
                currentStage.getFriction(), currentStage.getSnowballCooldown(), currentStage.getSnowballShotspeed(),
                currentStage.getFishSpawnRate(), currentStage.getRequiredFishesToKill(),
                snowballSFX, hitSFX);
        }
    }

    public void input()
    {
        if (state == GameState.GAME_STARTED)
        {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))
            {
                state = GameState.GAME_RUNNING;
                advanceStage();
            }
        }
        else if (state == GameState.GAME_ENDED)
        {
            if (Gdx.input.isKeyPressed(Input.Keys.R)) {
                stage = 0;
                state = GameState.GAME_STARTED;
            }
        }
        else {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                state = (state == GameState.GAME_RUNNING ? GameState.GAME_PAUSED : GameState.GAME_RUNNING);
            }

            if (state == GameState.GAME_PAUSED) return;

            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                gameWorld.inputHandler(Input.Keys.W);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                gameWorld.inputHandler(Input.Keys.A);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                gameWorld.inputHandler(Input.Keys.S);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                gameWorld.inputHandler(Input.Keys.D);
            }

            if (Gdx.input.isTouched()) {
                Vector2 mousePos = new Vector2(Gdx.input.getX() / 1920f * 2f - 1, 1 - Gdx.input.getY() / 1080f * 2f).nor();
                gameWorld.touchHandler(mousePos);
            }
        }
    }
    public void update()
    {
        if (state != GameState.GAME_RUNNING) return;

        gameWorld.update(Gdx.graphics.getDeltaTime());
        if (gameWorld.isStageCleared()) {stageClearSFX.play(0.5f); advanceStage();}
        if (gameWorld.isGameOver()) {stage = 0; state = GameState.GAME_STARTED;}
    }

    public void draw()
    {
        if (stage > 0 && stage <= StageConfig.values().length)
            ScreenUtils.clear(StageConfig.valueOf("STAGE"+stage).getBackgroundColor());
        else
            ScreenUtils.clear(1f, 1f, 1f, 1f);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        if (state == GameState.GAME_STARTED)
        {
            Sprite tmp = new Sprite(new Texture("title.png"));
            tmp.setCenter(camera.position.x, camera.position.y);
            tmp.draw(batch);
        }
        else if (state == GameState.GAME_ENDED)
        {
            Sprite tmp = new Sprite(new Texture("end.png"));
            tmp.setCenter(camera.position.x, camera.position.y);
            tmp.draw(batch);
        }
        else {
            if (state == GameState.GAME_RUNNING) {
                Vector2 cameraPos = new Vector2(
                    gameWorld.getPlayer().position.x + (Gdx.input.getX() - 960) / 4f,
                    gameWorld.getPlayer().position.y + (540 - Gdx.input.getY()) / 4f);
                camera.position.set(cameraPos.x, cameraPos.y, 0);
                camera.update();
            }
            gameWorld.draw(batch);
            if (state != GameState.GAME_STARTED && state != GameState.GAME_ENDED)
            {
                Sprite stageIndicator = new Sprite(new Texture(String.format("stage%d.png", stage)));
                stageIndicator.setScale(0.8f);
                stageIndicator.setCenter(camera.position.x-620, camera.position.y+420);
                stageIndicator.draw(batch);
            }
            if (state == GameState.GAME_PAUSED) {
                Sprite tmp = new Sprite(new Texture("paused.png"));
                tmp.setCenter(camera.position.x, camera.position.y);
                tmp.draw(batch);
            }
        }

        batch.end();
    }
}
