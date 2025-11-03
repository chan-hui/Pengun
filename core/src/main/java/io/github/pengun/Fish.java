package io.github.pengun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Fish {
    public Vector2 position;
    public Vector2 velocity;
    public Sprite sprite;
    public int health;

    Fish(Texture fishTexture, Vector2 pos)
    {
        position = pos;
        velocity = new Vector2(0, 0);
        sprite = new Sprite(fishTexture);
        sprite.setScale(0.2f);
        health = 5;
    }

    void draw(SpriteBatch batch)
    {
        sprite.setCenter(position.x, position.y);
        sprite.draw(batch);
    }
}
