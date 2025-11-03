package io.github.pengun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    public Vector2 position;
    public Vector2 velocity;
    public Sprite sprite;
    public boolean isFacingLeft = true;

    public int health;

    Player(Texture playerTexture)
    {
        position = new Vector2(0, 0);
        velocity = new Vector2(0, 0);
        sprite = new Sprite(playerTexture);
        sprite.setScale(0.3f);
        health = 100;
    }

    void inputHandler(int keyCode)
    {
        if (keyCode == Input.Keys.W) {velocity = velocity.add(new Vector2(0, 1));}
        if (keyCode == Input.Keys.S) {velocity = velocity.add(new Vector2(0, -1));}
        if (keyCode == Input.Keys.A) {velocity = velocity.add(new Vector2(-1, 0));}
        if (keyCode == Input.Keys.D) {velocity = velocity.add(new Vector2(1, 0));}
    }

    void draw(SpriteBatch batch)
    {
        sprite.setCenter(position.x, position.y);
        if ((velocity.x < 0 && !isFacingLeft) || (velocity.x > 0 && isFacingLeft)) {
            sprite.flip(true, false);
            isFacingLeft = !isFacingLeft;
        }
        sprite.draw(batch);
    }
}
