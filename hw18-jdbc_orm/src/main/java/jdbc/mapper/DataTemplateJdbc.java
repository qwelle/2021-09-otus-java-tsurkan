package jdbc.mapper;

import core.repository.DataTemplate;
import core.repository.DataTemplateException;
import core.repository.executor.DbExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
public class DataTemplateJdbc<T> implements DataTemplate<T> {
    private static final Logger log = LoggerFactory.getLogger(DataTemplateJdbc.class);

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData entitySQLMetaData, EntityClassMetaData<T> entityClassMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sqlSelectById = entitySQLMetaData.getSelectByIdSql();
        log.debug(sqlSelectById);
        List<Field> allFields = entityClassMetaData.getAllFields();
        Constructor<T> contructor = entityClassMetaData.getConstructor();

        return dbExecutor.executeSelect(connection, sqlSelectById, List.of(id), rs -> {
            try {
                if (rs.next()) {
                    T obj = contructor.newInstance();
                    for (Field field : allFields) {
                        field.setAccessible(true);
                        field.set(obj, rs.getObject(field.getName()));
                    }
                    return obj;
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
                    T obj = contructor.newInstance();
                    for (Field field : allFields) {
                        field.setAccessible(true);
                        field.set(obj, rs.getObject(field.getName()));
                    }
                    clientList.add(obj);
                }
                return clientList;
            } catch (Exception e) {
                throw new DataTemplateException(e);
            }
        }).orElseThrow(() -> new DataTemplateException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        String sqlInsert = entitySQLMetaData.getInsertSql();
        log.debug(sqlInsert);
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> listObjects = new ArrayList<>();
        try {
            for(Field field : fieldsWithoutId) {
                field.setAccessible(true);
                listObjects.add(field.get(client));
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
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        List<Object> listObjects = new ArrayList<>();
        try {
            for(Field field : fieldsWithoutId) {
                field.setAccessible(true);
                listObjects.add(field.get(client));
            }
            Field fieldId = entityClassMetaData.getIdField();
            fieldId.setAccessible(true);
            listObjects.add(fieldId.get(client));
            dbExecutor.executeStatement(connection, sqlUpdate, listObjects);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }
}