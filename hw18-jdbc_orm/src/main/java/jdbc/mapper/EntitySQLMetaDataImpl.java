package jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName();
    }

    @Override
    public String getSelectByIdSql() {
        String fields = getFieldsInRow(entityClassMetaData.getAllFields());
        return "select " + fields + " from " + entityClassMetaData.getName() + " where " + entityClassMetaData.getIdField().getName() + " = ?";
    }

    @Override
    public String getInsertSql() {
        List<Field> listField = entityClassMetaData.getFieldsWithoutId();
        String fields = getFieldsInRow(listField);
        String questionMark = "?" + ",?".repeat(listField.size()-1);
        return "insert into " + entityClassMetaData.getName() + "(" + fields + ") values (" + questionMark + ")";
    }

    @Override
    public String getUpdateSql() {
        List<Field> fields = entityClassMetaData.getFieldsWithoutId();
        String fieldsInRow = "";
        for (int i = 0; i < fields.size(); i++) {
            if (i == fields.size() - 1) {
                fieldsInRow.concat(fields.get(i).getName() + " = ? ");
            } else {
                fieldsInRow.concat(fields.get(i).getName() + " = ?, ");
            }
        }
        return "update " + entityClassMetaData.getName() + " set " + fieldsInRow + " where id = ?";
    }

    private String getFieldsInRow(List<Field> fields) {
        String fieldsInRow = "";
        for (int i = 0; i < fields.size(); i++) {
            String stringField = fields.get(i).getName();
            if (i == 0) {
                fieldsInRow += stringField;
            } else {
                fieldsInRow += ", " + stringField;
            }
        }
        return fieldsInRow;
    }
}
