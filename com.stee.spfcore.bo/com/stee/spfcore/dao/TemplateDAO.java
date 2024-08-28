package com.stee.spfcore.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.stee.spfcore.model.template.Tag;
import com.stee.spfcore.model.template.Template;

public class TemplateDAO {
    @SuppressWarnings( "unchecked" )
    public List< String > getModuleNames() {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select distinct t.moduleName from Template t" );
        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Template > getTemplatesByModuleName( String moduleName ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select t from Template t where t.moduleName = :moduleName" );
        query.setParameter( "moduleName", moduleName );
        return ( List< Template > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getTemplateNamesByModuleName( String moduleName ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select t.name from Template t where t.moduleName = :moduleName" );
        query.setParameter( "moduleName", moduleName );
        return query.list();
    }

    @SuppressWarnings( "unchecked" )
    public Template getTemplateByName( String templateName ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Template template = null;
        Query query = session.createQuery( "select t from Template t where t.name = :templateName" );
        query.setParameter( "templateName", templateName );
        List< Template > results = ( List< Template > ) query.list();
        if ( results != null && results.size() > 0) {
            template = results.get( 0 );
        }
        return template;
    }

    @SuppressWarnings( "unchecked" )
    public Tag getTagByName( String tagName ) {
        Session session = SessionFactoryUtil.getCurrentSession();

        Tag tag = null;
        Query query = session.createQuery( "select t from Tag t where t.name = :tagName" );
        query.setParameter( "tagName", tagName );
        List< Tag > results = ( List< Tag > ) query.list();
        if ( results != null && results.size() > 0 ) {
                tag = results.get( 0 );
        }
        return tag;
    }

    @SuppressWarnings( "unchecked" )
    public List< Template > getAllTemplates() {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select t from Template t" );
        return ( List< Template > ) query.list();
    }

    @SuppressWarnings( "unchecked" )
    public List< Tag > getAllTags() {
        Session session = SessionFactoryUtil.getCurrentSession();

        Query query = session.createQuery( "select t from Tag t" );
        return ( List< Tag > ) query.list();
    }

    public void saveTemplate( Template template ) {
        Session session = SessionFactoryUtil.getCurrentSession();
        session.saveOrUpdate( template );
        session.flush();
    }

    public void saveTag( Tag tag ) {
        Session session = SessionFactoryUtil.getCurrentSession();
        session.saveOrUpdate( tag );
        session.flush();
    }
}
