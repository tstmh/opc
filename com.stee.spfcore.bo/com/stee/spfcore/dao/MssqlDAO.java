package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;

import com.stee.spfcore.vo.database.DbColumnDetail;

public class MssqlDAO {
    @SuppressWarnings( "unchecked" )
    public List< DbColumnDetail > getColumnDetails( List< String > schemaNames, List< String > tableNames, List< String > columnNames ) {
        Session session = SessionFactoryUtil.getInstance().getCurrentSession();
        StringBuilder queryString = new StringBuilder();
        queryString.append( "select " );
        queryString.append( "table_schema as schemaName, " );
        queryString.append( "table_name as tableName, " );
        queryString.append( "column_name as columnName, " );
        queryString.append( "data_type as dataType, " );
        queryString.append( "character_maximum_length as charMaxLength " );
        queryString.append( "from information_schema.columns where 1=1 and " );
        if ( schemaNames != null ) {
            queryString.append( "table_schema in :schemaNames and " );
        }
        if ( tableNames != null ) {
            queryString.append( "table_name in :tableNames and " );
        }
        if ( columnNames != null ) {
            queryString.append( "column_name in :columnNames and " );
        }
        queryString.append( "1=1 order by table_schema, table_name, column_name" );
        SQLQuery query = session.createSQLQuery( queryString.toString() );
        if ( schemaNames != null ) {
            query.setParameterList( "schemaNames", schemaNames );
        }
        if ( tableNames != null ) {
            query.setParameterList( "tableNames", tableNames );
        }
        if ( columnNames != null ) {
            query.setParameterList( "columnNames", columnNames );
        }
        query.addScalar( "schemaName", StandardBasicTypes.STRING );
        query.addScalar( "tableName", StandardBasicTypes.STRING );
        query.addScalar( "columnName", StandardBasicTypes.STRING );
        query.addScalar( "dataType", StandardBasicTypes.STRING );
        query.addScalar( "charMaxLength", StandardBasicTypes.INTEGER );
        query.setResultTransformer( Transformers.aliasToBean( DbColumnDetail.class ) );
        return ( List< DbColumnDetail > ) query.list();
    }
}
