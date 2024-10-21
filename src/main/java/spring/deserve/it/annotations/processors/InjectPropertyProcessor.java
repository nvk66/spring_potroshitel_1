package spring.deserve.it.annotations.processors;

import lombok.SneakyThrows;
import spring.deserve.it.annotations.InjectProperty;
import spring.deserve.it.properties.PropertiesLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class InjectPropertyProcessor {

    @SneakyThrows
    public static void injectProperty(Object object) {
        for (var field : getAllFields(object.getClass())) {
            if (field.isAnnotationPresent(InjectProperty.class)) {
                field.setAccessible(true);
                field.set(object, getProperty(field));
            }
        }
    }

    private static Object getProperty(Field field) {
        var type = field.getType();
        var annotationValue = field.getAnnotation(InjectProperty.class).value();
        return switch (type.getTypeName()) {
            case "int" -> PropertiesLoader.getIntProperty(annotationValue);
            case "long" -> PropertiesLoader.getLongProperty(annotationValue);
            case "boolean" -> PropertiesLoader.getBooleanProperty(annotationValue);
            case "java.lang.String" -> PropertiesLoader.getProperty(annotationValue);
            default -> throw new IllegalArgumentException("Unsupported type: " + type);
        }; // todo fix later
    }

    private static List<Field> getAllFields(Class clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        List<Field> filteredFields = List.of(clazz.getDeclaredFields());
        result.addAll(filteredFields);
        return result;
    }

    private static ArrayList<Class<?>> getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (directory.listFiles() == null) {
            return classes;
        }
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
