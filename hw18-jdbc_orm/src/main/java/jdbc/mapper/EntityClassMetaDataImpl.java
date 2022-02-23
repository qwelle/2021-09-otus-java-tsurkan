package jdbc.mapper;

import crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<?> clazz;

    public EntityClassMetaDataImpl(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public String getName() {
       return clazz.getSimpleName();
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public Field getIdField() {
        Field fieldId = null;
        for(Field field : getAllFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldId = field;
                break;
            }
        }
        return fieldId;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> listFields = getAllFields();
        listFields.remove(getIdField());
        return listFields;
    }

    @Override
    public Constructor<T> getConstructor() {
        Constructor<T> requiredContrs = null;
        for(Constructor<?> constructor : clazz.getConstructors()) {
            //ищем конструктор, где кол-во параметров соответствует кол-ву полей объекта
            if (constructor.getParameterTypes().length == getAllFields().size()) {
                requiredContrs = (Constructor<T>) constructor;
                break;
            }
        }
        return requiredContrs;
    }
}
