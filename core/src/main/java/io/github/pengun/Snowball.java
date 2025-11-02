package io.github.pengun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Snowball {
    public Vector2 position;
    public Vector2 velocity;
    public Sprite sprite;
    public float shotSpeed = 15f;

    Snowball(Texture tex, Vector2 pos, Vector2 vel)
    {
        sprite = new Sprite(tex);
        sprite.setCenter(pos.x, pos.y);
        sprite.setScale(0.3f);
        position = pos;
        velocity = new Vector2(vel.x*shotSpeed,vel.y*shotSpeed);
    }

    void draw(SpriteBatch batch)
    {
        sprite.setCenter(position.x, position.y);
        sprite.draw(batch);
    }

}
