package jdbc.mapper;

import crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final String className;
    private Field fieldId;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId = new ArrayList<>();
    private final Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<T> clazz) throws ReflectiveOperationException {
        this.className = clazz.getSimpleName();
        this.allFields = List.of(clazz.getDeclaredFields());
        for(Field field : this.allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                this.fieldId = field;
                break;
            }
        }
        for (Field field : this.allFields) {
            if (!field.equals(this.fieldId))
            fieldsWithoutId.add(field);
        }
        this.constructor = clazz.getDeclaredConstructor();
    }

    @Override
    public String getName() {
       return this.className;
    }

    @Override
    public List<Field> getAllFields() {
        return this.allFields;
    }

    @Override
    public Field getIdField() {
        return this.fieldId;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return this.fieldsWithoutId;
    }

    @Override
    public Constructor<T> getConstructor() {
        return this.constructor;
    }
}
