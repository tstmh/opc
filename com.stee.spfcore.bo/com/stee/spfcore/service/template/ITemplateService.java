package com.stee.spfcore.service.template;

import java.util.List;

import com.stee.spfcore.model.template.Tag;
import com.stee.spfcore.model.template.Template;

public interface ITemplateService {
    public List< String > getModuleNames() throws TemplateException;

    public List< Template > getTemplatesByModuleName( String moduleName ) throws TemplateException;

    public List< String > getTemplateNamesByModuleName( String moduleName ) throws TemplateException;

    public Template getTemplateByName( String templateName ) throws TemplateException;

    public Tag getTagByName( String tagName ) throws TemplateException;

    public List< Template > getAllTemplates() throws TemplateException;

    public List< Tag > getAllTags() throws TemplateException;

    public void saveTemplate( Template template ) throws TemplateException;

    public void saveTag( Tag tag ) throws TemplateException;
}
