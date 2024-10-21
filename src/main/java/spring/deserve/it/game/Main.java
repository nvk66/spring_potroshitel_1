package spring.deserve.it.game;

import spring.deserve.it.annotations.processors.InjectPropertyProcessor;
import spring.deserve.it.properties.PropertiesLoader;

public class Main {

    private static final String resourcePath = "application.properties";

    public static void main(String[] args) {
        PropertiesLoader.loadProperties(resourcePath);
        var paperSpider = new PaperSpider();
        InjectPropertyProcessor.injectProperty(paperSpider);
        paperSpider.isAlive();
        System.out.println("Starting game");
    }

}
