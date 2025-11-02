package io.github.pengun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
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
        GAME_RUNNING,
        GAME_PAUSED
    }

    private GameState state = GameState.GAME_RUNNING;
    private int stage = 0;

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture("penguin.png");
        snowballTexture = new Texture("snowball.png");
        fishTexture = new Texture("fish.png");
        gameWorld = new World(playerTexture, snowballTexture, 0.9f, 0.3f);

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
    }

    public void input()
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {state = (state == GameState.GAME_RUNNING ? GameState.GAME_PAUSED : GameState.GAME_RUNNING);}

        if (state == GameState.GAME_PAUSED) return;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {gameWorld.inputHandler(Input.Keys.W);}
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {gameWorld.inputHandler(Input.Keys.A);}
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {gameWorld.inputHandler(Input.Keys.S);}
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {gameWorld.inputHandler(Input.Keys.D);}

        if (Gdx.input.isTouched())
        {
            Vector2 mousePos = new Vector2(Gdx.input.getX()/1920f*2f-1, 1-Gdx.input.getY()/1080f*2f).nor();
            gameWorld.touchHandler(mousePos);
        };
    }
    public void update()
    {
        if (state == GameState.GAME_PAUSED) return;
        gameWorld.update(Gdx.graphics.getDeltaTime());
        if (gameWorld.isStageCleared())
        {

        }
    }

    public void draw()
    {
        ScreenUtils.clear(1f, 1f, 1f, 1f);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();


        if (state == GameState.GAME_RUNNING)
        {
            Vector2 cameraPos = new Vector2(
                gameWorld.getPlayer().position.x+(Gdx.input.getX()-960)/4f,
                gameWorld.getPlayer().position.y+(540-Gdx.input.getY())/4f);
            camera.position.set(cameraPos.x, cameraPos.y, 0);
            camera.update();
        }
        gameWorld.draw(batch);
        if (state == GameState.GAME_PAUSED)
        {
            Sprite tmp = new Sprite(new Texture("paused.png"));
            tmp.setCenter(camera.position.x, camera.position.y);
            tmp.draw(batch);
        }

        batch.end();
    }
}
