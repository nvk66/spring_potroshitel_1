package spring.deserve.it.core;

import lombok.Getter;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import spring.deserve.it.annotations.processors.Configurator;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;


public final class ObjectFactory {

    private final static ObjectFactory instance = new ObjectFactory();
    private final List<Configurator> configurators;
    private final Reflections reflections = new Reflections("spring.deserve.it");

    private ObjectFactory() {
        configurators = reflections.getSubTypesOf(Configurator.class)
                .stream()
                .map(this::createSimpleObject)
                .collect(Collectors.toList());
    }

    public static ObjectFactory getInstance() {
        return instance;
    }


    public <T> T createObject(Class<T> clazz) {
        T simpleObject = createSimpleObject(clazz);
        configurators.forEach(configurator -> configurator.configure(simpleObject));
        return simpleObject;
    }

    @SneakyThrows
    private <T> T createSimpleObject(Class<T> clazz) {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getGenericParameterTypes().length == 0) {
                return (T) constructor.newInstance();
            }
        }
        throw new IllegalArgumentException();
    }

}
