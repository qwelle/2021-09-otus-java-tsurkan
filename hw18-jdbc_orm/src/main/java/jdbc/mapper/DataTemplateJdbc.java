package jdbc.mapper;

import core.repository.DataTemplate;
import core.repository.DataTemplateException;
import core.repository.executor.DbExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохратяет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

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
        log.debug(sqlSelectById);

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
        log.debug(sqlSelectAll);
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
        log.debug(sqlInsert);
        Class<?> clazz = client.getClass();
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> listObjects = new ArrayList<>();
        try {
            for(Field field : fieldsWithoutId) {
                Method method = clazz.getMethod("get" + field.getName().substring(0,1).toUpperCase() + field.getName().substring(1));
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
        log.debug(sqlUpdate);
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