package io.github.pengun;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {
    private Player player;
    private Texture snowballTexture;
    public Array<Snowball> snowballs = new Array<>();


    private float friction = 0.9f;
    private float snowballCooldown = 0.3f;

    private float cooldown = 0f;

    World(Texture playerTexture, Texture snowballTexture, float friction, float snowballCooldown)
    {
        player = new Player(playerTexture);
        this.snowballTexture = snowballTexture;
        this.friction = friction;
        this.snowballCooldown = snowballCooldown;
    }

    void inputHandler(int keyCode) {player.inputHandler(keyCode);}

    void touchHandler(Vector2 clickPos)
    {
        if (cooldown <= 0f) {
            Vector2 snowballPos = new Vector2(player.position.x + clickPos.x * 64f, player.position.y + clickPos.y * 64f);
            Snowball tmp = new Snowball(snowballTexture, snowballPos, clickPos);
            snowballs.add(tmp);
            cooldown = snowballCooldown;
        }
    }

    void update(float delta)
    {
        player.position = player.position.add(player.velocity);
        player.velocity = new Vector2(player.velocity.x * friction, player.velocity.y * friction);
        for (Snowball snowball: snowballs)
            snowball.position = new Vector2(snowball.position.x + snowball.velocity.x, snowball.position.y + snowball.velocity.y);
        if (cooldown > 0)
            cooldown -= delta;
    }

    void draw(SpriteBatch batch)
    {
        player.draw(batch);
        for (Snowball snowball: snowballs)
            snowball.draw(batch);
    }

    boolean isStageCleared()
    {
        return false;
    }

    Player getPlayer()
    {
        return player;
    }
}
