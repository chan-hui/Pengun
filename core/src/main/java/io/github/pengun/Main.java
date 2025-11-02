package io.github.pengun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;


    private Texture playerTexture;

    private World gameWorld;

    enum gameState
    {
        GAME_RUNNING,
        GAME_PAUSED
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        playerTexture = new Texture("penguin.png");
        gameWorld = new World();
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
    }

    public void input()
    {
        if (Gdx.input.isKeyPressed(Input.Keys.W))
        {
            gameWorld
        }
    }
    public void update()
    {

    }

    public void draw()
    {
        ScreenUtils.clear(1f, 1f, 1f, 1f);
        batch.begin();
        batch.draw(playerTexture, 140, 210);
        batch.end();
    }
}
