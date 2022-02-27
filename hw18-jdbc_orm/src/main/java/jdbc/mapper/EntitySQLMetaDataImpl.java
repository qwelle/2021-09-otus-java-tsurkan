package jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.selectAllSql = "select * from " + entityClassMetaData.getName();

        String fields = getFieldsInRow(entityClassMetaData.getAllFields());
        this.selectByIdSql = "select " + fields + " from " + entityClassMetaData.getName() + " where " + entityClassMetaData.getIdField().getName() + " = ?";

        List<Field> listFieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        String fieldsWithoutId = getFieldsInRow(listFieldsWithoutId);
        String questionMark = "?" + ",?".repeat(listFieldsWithoutId.size()-1);
        this.insertSql = "insert into " + entityClassMetaData.getName() + "(" + fieldsWithoutId + ") values (" + questionMark + ")";

        String fieldsInRow = "";
        for (int i = 0; i < listFieldsWithoutId.size(); i++) {
            if (i == listFieldsWithoutId.size() - 1) {
                fieldsInRow = fieldsInRow.concat(listFieldsWithoutId.get(i).getName() + " = ? ");
            } else {
                fieldsInRow = fieldsInRow.concat(listFieldsWithoutId.get(i).getName() + " = ?, ");
            }
        }
        this.updateSql = "update " + entityClassMetaData.getName() + " set " + fieldsInRow + " where id = ?";
    }

    private static String getFieldsInRow(List<Field> fields) {
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

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return this.updateSql;
    }
}
