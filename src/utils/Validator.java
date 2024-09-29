package src.utils;

import java.lang.reflect.Field;

public class Validator {
    public static void validate(Object o) throws IllegalAccessError, IllegalAccessException {
        Class<?> clazz = o.getClass();
        Field[]  fields = clazz.getFields();

        for (Field f : fields) {
            f.setAccessible(true);
            Object value = f.get(o);

            if(value instanceof String) {
                if (((String) value).isBlank()) {
                    throw new IllegalArgumentException((f.getName() + " no puede estar vacio"));
                }
            }
            if(value instanceof Number) {
                if(((Number) value).doubleValue() == 0) {
                    throw new IllegalArgumentException((f.getName() + " no puede ser cero"));
                }
            }
        }
    }
}
