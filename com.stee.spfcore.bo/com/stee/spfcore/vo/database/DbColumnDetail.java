package com.stee.spfcore.vo.database;

public class DbColumnDetail {
    private String schemaName;
    private String tableName;
    private String columnName;
    private String dataType;
    private Integer charMaxLength;

    public DbColumnDetail() {
        // DO NOTHING
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName( String tableName ) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName( String columnName ) {
        this.columnName = columnName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName( String schemaName ) {
        this.schemaName = schemaName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType( String dataType ) {
        this.dataType = dataType;
    }

    public Integer getCharMaxLength() {
        return charMaxLength;
    }

    public void setCharMaxLength( Integer charMaxLength ) {
        this.charMaxLength = charMaxLength;
    }

    public String toString() {
        return String.format( "schemaName=%s, tableName=%s, columnName=%s, dataType=%s, charMaxLength=%s", this.schemaName, this.tableName, this.columnName, this.dataType, this.charMaxLength );
    }
}
