package com.stee.spfcore.service.database;

import java.util.List;

import com.stee.spfcore.vo.database.DbColumnDetail;

public interface IDatabaseService {
    public List< DbColumnDetail > getSpfcoreColumnDetails( String tableName ) throws DatabaseException;
}
