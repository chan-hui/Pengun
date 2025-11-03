package io.github.pengun;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class World {
    private Player player;
    private Texture snowballTexture;
    private Texture fishTexture;
    public Array<Snowball> snowballs = new Array<>();
    public Array<Fish> fishes = new Array<>();


    private float friction;
    private float snowballCooldown;
    private float snowballShotspeed;
    private float fishSpawnRate;
    private int requiredFishesToKill;

    private float cooldown = 0f;
    private float fishTimer = 0f;
    private int currentFishKilled = 0;

    private Sound snowballSFX;
    private Sound hitSFX;

    World(Texture playerTexture, Texture snowballTexture, Texture fishTexture,
          float friction, float snowballCooldown, float snowballShotspeed, float fishSpawnRate, int requiredFishesToKill,
          Sound snowballSFX, Sound hitSFX)
    {
        player = new Player(playerTexture);
        this.snowballTexture = snowballTexture;
        this.fishTexture = fishTexture;
        this.friction = friction;
        this.snowballCooldown = snowballCooldown;
        this.snowballShotspeed = snowballShotspeed;
        this.fishSpawnRate = fishSpawnRate;
        this.requiredFishesToKill = requiredFishesToKill;
        this.snowballSFX = snowballSFX;
        this.hitSFX = hitSFX;
    }

    void inputHandler(int keyCode) {player.inputHandler(keyCode);}

    void touchHandler(Vector2 clickPos)
    {
        if (cooldown <= 0f) {
            Vector2 snowballPos = new Vector2(player.position.x + clickPos.x * 64f, player.position.y + clickPos.y * 64f);
            Snowball tmp = new Snowball(snowballTexture, snowballPos, clickPos, snowballShotspeed);
            snowballs.add(tmp);
            cooldown = snowballCooldown;
            snowballSFX.play(0.25f);
        }
    }

    void update(float delta)
    {
        float normedDelta = delta * 60f;
        player.position = new Vector2(player.position.x + player.velocity.x * normedDelta, player.position.y + player.velocity.y * normedDelta);
        player.velocity.scl(friction);
        for (Snowball snowball: snowballs)
            snowball.position = new Vector2(snowball.position.x + snowball.velocity.x * normedDelta, snowball.position.y + snowball.velocity.y * normedDelta);
        for (Fish fish: fishes)
        {
            fish.velocity.add(new Vector2(player.position.x-fish.position.x, player.position.y-fish.position.y).nor().scl(0.5f));
            fish.position = new Vector2(fish.position.x + fish.velocity.x * normedDelta, fish.position.y + fish.velocity.y * normedDelta);
            fish.velocity.scl(friction);
        }

        fishTimer += delta;
        cooldown -= delta;

        if (fishTimer >= fishSpawnRate)
        {
            if (fishes.size < 15) {
                Vector2 randomDir = new Vector2(1000f, 0).rotateDeg(MathUtils.random(360));
                Fish tmp = new Fish(fishTexture, new Vector2(player.position.x + randomDir.x, player.position.y + randomDir.y));
                fishes.add(tmp);
                fishTimer = 0f;
            }
        }

        for (Fish fish: fishes)
        {
            for (Snowball snowball: snowballs){
                if (snowball.sprite.getBoundingRectangle().overlaps(fish.sprite.getBoundingRectangle()))
                {
                    hitSFX.play();
                    fish.velocity.add(snowball.velocity);
                    fish.health -= 1;
                    if (fish.health <= 0) {
                        fishes.removeValue(fish, false);
                        currentFishKilled++;
                    }
                    snowballs.removeValue(snowball, false);
                }
            }
            if (player.sprite.getBoundingRectangle().overlaps(fish.sprite.getBoundingRectangle()))
            {
                player.velocity.add(new Vector2(player.position.x-fish.position.x, player.position.y-fish.position.y).nor().scl(3f));
                fish.velocity.add(new Vector2(fish.position.x-player.position.x, fish.position.y-player.position.y).nor().scl(3f));
                player.health -= 1;
            }
        }
    }

    void draw(SpriteBatch batch)
    {
        player.draw(batch);
        for (Snowball snowball: snowballs)
            snowball.draw(batch);
        for (Fish fish: fishes)
            fish.draw(batch);
    }

    boolean isStageCleared()
    {
        return (currentFishKilled >= requiredFishesToKill);
    }
    boolean isGameOver()
    {
        return (player.health <= 0);
    }

    Player getPlayer()
    {
        return player;
    }
}
