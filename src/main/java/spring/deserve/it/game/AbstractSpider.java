package spring.deserve.it.game;

import lombok.Getter;
import lombok.Setter;
import spring.deserve.it.annotations.InjectProperty;
import spring.deserve.it.api.Spider;

@Getter
@Setter
public abstract class AbstractSpider implements Spider {

    @InjectProperty("spider.default.lives")
    private int lives;

    public boolean isAlive() {
        System.out.println(lives);
        return lives > 0;
    }
    public void loseLife() {
        if (lives > 0) {
            lives--;
        }
    }
}

