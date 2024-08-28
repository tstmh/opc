package com.stee.spfcore.service.template.impl;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.stee.spfcore.dao.SessionFactoryUtil;
import com.stee.spfcore.dao.TemplateDAO;
import com.stee.spfcore.model.template.Tag;
import com.stee.spfcore.model.template.Template;
import com.stee.spfcore.service.template.ITemplateService;
import com.stee.spfcore.service.template.TemplateException;

public class TemplateService implements ITemplateService {

    static {
        Logger.getLogger(TemplateService.class.getName());
    }

    private TemplateDAO dao;

    public TemplateService() {
        dao = new TemplateDAO();
    }

    @Override
    public List< String > getModuleNames() throws TemplateException {
        List< String > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getModuleNames();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getModuleNames() failed.", e );
        }

        return results;
    }

    @Override
    public List< Template > getTemplatesByModuleName( String moduleName ) throws TemplateException {
        List< Template > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getTemplatesByModuleName( moduleName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getTemplatesByModuleName() failed.", e );
        }

        return results;
    }

    @Override
    public List< String > getTemplateNamesByModuleName( String moduleName ) throws TemplateException {
        List< String > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getTemplateNamesByModuleName( moduleName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getTemplateNamesByModuleName() failed.", e );
        }

        return results;
    }

    @Override
    public Template getTemplateByName( String templateName ) throws TemplateException {
        Template results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getTemplateByName( templateName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getTemplateByName() failed.", e );
        }

        return results;
    }

    @Override
    public Tag getTagByName( String tagName ) throws TemplateException {
        Tag results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getTagByName( tagName );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getTagByName() failed.", e );
        }

        return results;
    }

    @Override
    public List< Template > getAllTemplates() throws TemplateException {
        List< Template > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getAllTemplates();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getAllTemplates() failed.", e );
        }

        return results;
    }

    @Override
    public List< Tag > getAllTags() throws TemplateException {
        List< Tag > results = null;

        try {
            SessionFactoryUtil.beginTransaction();

            results = dao.getAllTags();

            SessionFactoryUtil.commitTransaction();
        }
        catch ( RuntimeException e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "getAllTags() failed.", e );
        }

        return results;
    }

    @Override
    public void saveTemplate( Template template ) throws TemplateException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveTemplate( template );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "saveTemplate()", e );
        }
    }

    @Override
    public void saveTag( Tag tag ) throws TemplateException {
        try {
            SessionFactoryUtil.beginTransaction();

            dao.saveTag( tag );

            SessionFactoryUtil.commitTransaction();
        }
        catch ( Exception e ) {
            SessionFactoryUtil.rollbackTransaction();
            throw new TemplateException( "saveTag()", e );
        }
    }
}
