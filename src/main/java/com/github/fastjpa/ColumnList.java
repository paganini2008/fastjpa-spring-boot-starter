package com.github.fastjpa;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Description: ColumnList
 * @Author: Fred Feng
 * @Date: 07/10/2024
 * @Version 1.0.0
 */
public class ColumnList extends ArrayList<Column> {

    private static final long serialVersionUID = -1066600234659676707L;

    public ColumnList() {}

    public ColumnList(Column... columns) {
        super(List.of(columns));
    }

    public ColumnList(String[] attributeNames) {
        if (attributeNames != null && attributeNames.length > 0) {
            for (String attributeName : attributeNames) {
                add(Column.forName(attributeName));
            }
        }
    }

    @SafeVarargs
    public <X> ColumnList(SerializedFunction<X, ?>... functions) {
        if (functions != null && functions.length > 0) {
            for (SerializedFunction<X, ?> function : functions) {
                addColumn(function);
            }
        }
    }

    @SafeVarargs
    public ColumnList(Field<?>... fields) {
        if (fields != null && fields.length > 0) {
            for (Field<?> field : fields) {
                addColumn(field, field.toString());
            }
        }
    }

    public ColumnList(String alias, String[] attributeNames) {
        if (attributeNames != null && attributeNames.length > 0) {
            for (String attributeName : attributeNames) {
                add(Column.forName(alias, attributeName));
            }
        }
    }

    public ColumnList addColumn(String attributeName) {
        add(Column.forName(attributeName));
        return this;
    }

    public ColumnList addColumn(String attributeName, Class<?> requiredType) {
        add(Column.forName(attributeName, requiredType));
        return this;
    }

    public ColumnList addColumn(String alias, String attributeName, Class<?> requiredType) {
        add(Column.forName(alias, attributeName, requiredType));
        return this;
    }

    public ColumnList addColumn(String alias, String attributeName) {
        add(Column.forName(alias, attributeName));
        return this;
    }

    public ColumnList addColumn(Column column) {
        add(column);
        return this;
    }

    public <X> ColumnList addColumn(SerializedFunction<X, ?> function) {
        add(Column.forName(function, null));
        return this;
    }

    public ColumnList addColumn(Field<?> field, String alias) {
        add(field.as(alias));
        return this;
    }

}
