package com.stee.spfcore.webapi.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stee.spfcore.webapi.model.template.Tag;
import com.stee.spfcore.webapi.model.template.Template;

@Repository
public class TemplateDAO {
	
	private EntityManager entityManager;
	private static final Logger logger = LoggerFactory.getLogger(TemplateDAO.class);
	
	@Autowired
	public TemplateDAO(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
    @SuppressWarnings( "unchecked" )
    public List< String > getModuleNames() {
    	Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "select distinct t.moduleName from Template t" );
        List< String > results = ( List< String > ) query.list();
        return results;
    }

    @SuppressWarnings( "unchecked" )
    public List< Template > getTemplatesByModuleName( String moduleName ) {
    	Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "select t from Template t where t.moduleName = :moduleName" );
        query.setParameter( "moduleName", moduleName );
        List< Template > results = ( List< Template > ) query.list();
        return results;
    }

    @SuppressWarnings( "unchecked" )
    public List< String > getTemplateNamesByModuleName( String moduleName ) {
    	Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "select t.name from Template t where t.moduleName = :moduleName" );
        query.setParameter( "moduleName", moduleName );
        List< String > results = ( List< String > ) query.list();
        return results;
    }

    @SuppressWarnings( "unchecked" )
    public Template getTemplateByName( String templateName ) {
    	logger.info("getTemplateByName called");
    	Session session = entityManager.unwrap(Session.class);

        Template template = null;
        Query query = session.createQuery( "select t from Template t where t.name = :templateName" );
        logger.info(query.toString());
        query.setParameter( "templateName", templateName );
        List< Template > results = ( List< Template > ) query.list();
        if ( results != null ) {
            if ( results.size() > 0 ) {
                template = results.get( 0 );
            }
        }
        return template;
    }

    @SuppressWarnings( "unchecked" )
    public Tag getTagByName( String tagName ) {
    	Session session = entityManager.unwrap(Session.class);

        Tag tag = null;
        Query query = session.createQuery( "select t from Tag t where t.name = :tagName" );
        query.setParameter( "tagName", tagName );
        List< Tag > results = ( List< Tag > ) query.list();
        if ( results != null ) {
            if ( results.size() > 0 ) {
                tag = results.get( 0 );
            }
        }
        return tag;
    }

    @SuppressWarnings( "unchecked" )
    public List< Template > getAllTemplates() {
    	Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "select t from Template t" );
        List< Template > results = ( List< Template > ) query.list();
        return results;
    }

    @SuppressWarnings( "unchecked" )
    public List< Tag > getAllTags() {
    	Session session = entityManager.unwrap(Session.class);

        Query query = session.createQuery( "select t from Tag t" );
        List< Tag > results = ( List< Tag > ) query.list();
        return results;
    }

    public void saveTemplate( Template template ) {
    	Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate( template );
        session.flush();
    }

    public void saveTag( Tag tag ) {
    	Session session = entityManager.unwrap(Session.class);
        session.saveOrUpdate( tag );
        session.flush();
    }
}
