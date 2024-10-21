package spring.deserve.it.game;

import spring.deserve.it.annotations.processors.InjectPropertyConfigurator;
import spring.deserve.it.core.ObjectFactory;
import spring.deserve.it.properties.PropertiesLoader;

public class Main {

    private static final String resourcePath = "application.properties";

    public static void main(String[] args) {
        PropertiesLoader.loadProperties(resourcePath);
        PaperSpider spider = ObjectFactory.getInstance().createObject(PaperSpider.class);
        spider.isAlive();
        System.out.println("Starting game");
    }

}
