package jdbc.mapper;

import core.repository.DataTemplate;
import core.repository.DataTemplateException;
import core.repository.executor.DbExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        List<Field> allFields = entityClassMetaData.getAllFields();
        Constructor<T> contructor = entityClassMetaData.getConstructor();
        String sqlSelectById = entitySQLMetaData.getSelectByIdSql();
        System.out.println(sqlSelectById);

        return dbExecutor.executeSelect(connection, sqlSelectById, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    Object[] objectsArr = new Object[allFields.size()];
                    for (int i = 0; i < allFields.size(); i++) {
                        Object obj = rs.getObject(allFields.get(i).getName());
                        objectsArr[i] = obj;
                    }
                    return contructor.newInstance(objectsArr);
                }
                return null;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }});
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sqlSelectAll = entitySQLMetaData.getSelectAllSql();
        System.out.println(sqlSelectAll);
        List<Field> allFields = entityClassMetaData.getAllFields();
        Constructor<T> contructor = entityClassMetaData.getConstructor();

        return dbExecutor.executeSelect(connection, sqlSelectAll, Collections.emptyList(), rs -> {
            var clientList = new ArrayList<T>();
            try {
                while (rs.next()) {
                    Object[] objectsArr = new Object[allFields.size()];
                    for (int i = 0; i < allFields.size(); i++) {
                        Object obj = rs.getObject(allFields.get(i).getName());
                        objectsArr[i] = obj;
                    }
                    clientList.add(contructor.newInstance(objectsArr));
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        String sqlInsert = entitySQLMetaData.getInsertSql();
        System.out.println(sqlInsert);
        Class<?> clazz = client.getClass();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> listObjects = new ArrayList<>();
        try {
            for(Field field : fieldsWithoutId) {
                Method method = clazz.getMethod("get" + field.getName());
                var obj = method.invoke(client);
                listObjects.add(obj);
            }
            return dbExecutor.executeStatement(connection, sqlInsert, listObjects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        String sqlUpdate = entitySQLMetaData.getUpdateSql();
        System.out.println(sqlUpdate);
        Class<?> clazz = client.getClass();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> listObjects = new ArrayList<>();
        try {
            for(Field field : fieldsWithoutId) {
                Method method = clazz.getMethod("get" + field.getName());
                var obj = method.invoke(client);
                listObjects.add(obj);
            }
            Method methodGetId = clazz.getMethod("get" + entityClassMetaData.getIdField().getName());
            listObjects.add(methodGetId.invoke(client));
            dbExecutor.executeStatement(connection, sqlUpdate, listObjects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}