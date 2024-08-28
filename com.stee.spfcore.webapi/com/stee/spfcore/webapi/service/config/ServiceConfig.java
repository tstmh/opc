package com.stee.spfcore.webapi.service.config;
import java.util.HashMap;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;

import com.stee.spfcore.webapi.utils.EnvironmentUtils;

public class ServiceConfig {

//    private IMessagingConfig messagingConfig = null;
    private IMailServerConfig mailServerConfig = null;
    private ISmsServerConfig smsServerConfig = null;
    private IProcessConfig processConfig = null;
//    private IDAOSessionConfig daoConfig = null;
//    private IPortalUserConfig userConfig = null;
//    private IFileHandlingConfig fileConfig = null;
//    private IBPMEnvConfig bpmEnvConfig = null;
//    private IEnvConfig envConfig = null;
//    private ICodeConfig codeConfig = null;
//    private ISearchConfig searchConfig = null;
//    private ISecurityConfig securityConfig = null;
//    private ISystemStatusConfig systemStatusConfig = null;
//    private ITaskConfig taskConfig = null;
//    private IMarketingContentConfig marketingContentConfig = null;
//    private IAnnouncementConfig announcementConfig = null;
//    private IAccountingConfig acocuntingConfig = null;
//    private IWelfareCourseConfig welfareCourseConfig = null;
//    private IPlannerConfig plannerConfig = null;
//    private ISAGConfig sagConfig = null;
//    private ISSRSProxyConfig ssrsProxyConfig = null;
//    private IRateThisWebsiteConfig rateThisWebsiteConfig = null;
//    private ISupportConfig supportConfig = null;
//    private IStudyAwardConfig studyAwardConfig = null;
    
    private static ServiceConfig instance = null;

    public synchronized static ServiceConfig getInstance() {
        if ( instance == null ) {
            instance = new ServiceConfig();
        }

        return instance;
    }

    private ServiceConfig() {
        System.setProperty( "spfcore.config", EnvironmentUtils.getConfigPath() );
    }

//    public synchronized IMessagingConfig getMessagingConfig() {
//        if ( messagingConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isDevelopment() ) {
//                variables.put( "env", "dev" );
//            }
//            else if ( EnvironmentUtils.isProduction() ) {
//
//                if ( EnvironmentUtils.isInternet() ) {
//                    variables.put( "env", "feb" );
//                }
//                else if ( EnvironmentUtils.isIntranet() ) {
//                    variables.put( "env", "bpm" );
//                }
//            }
//
//            messagingConfig = ConfigFactory.create( IMessagingConfig.class, variables );
//        }
//
//        return messagingConfig;
//    }

    public synchronized IMailServerConfig getMailServerConfig() {
        if ( mailServerConfig == null ) {
            Map< String, String > variables = new HashMap< String, String >();

            if ( EnvironmentUtils.isDevelopment() ) {
                variables.put( "env", "dev" );
            }
            else if ( EnvironmentUtils.isProduction() ) {

                if ( EnvironmentUtils.isInternet() ) {
                    variables.put( "env", "feb" );
                }
                else if ( EnvironmentUtils.isIntranet() ) {
                    variables.put( "env", "bpm" );
                }
            }

            mailServerConfig = ConfigFactory.create( IMailServerConfig.class, variables );
        }

        return mailServerConfig;
    }

    public synchronized ISmsServerConfig getSmsServerConfig() {
        if ( smsServerConfig == null ) {
            Map< String, String > variables = new HashMap< String, String >();

            if ( EnvironmentUtils.isDevelopment() ) {
                variables.put( "env", "dev" );
            }
            else if ( EnvironmentUtils.isProduction() ) {

                if ( EnvironmentUtils.isInternet() ) {
                    variables.put( "env", "feb" );
                }
                else if ( EnvironmentUtils.isIntranet() ) {
                    variables.put( "env", "bpm" );
                }
            }

            smsServerConfig = ConfigFactory.create( ISmsServerConfig.class, variables );
        }

        return smsServerConfig;
    }

    public synchronized IProcessConfig getProcessConfig() {
        if ( processConfig == null ) {
            processConfig = ConfigFactory.create( IProcessConfig.class );
        }

        return processConfig;
    }

    public synchronized IProcessConfig getProcessConfig( String process ) {
        Map< String, String > variables = new HashMap< String, String >();
        variables.put( "process", process );

        return ConfigFactory.create( IProcessConfig.class, variables );
    }

//    public synchronized IDAOSessionConfig getDaoSessionConfig() {
//        if ( daoConfig == null ) {
//            daoConfig = ConfigFactory.create( IDAOSessionConfig.class );
//        }
//
//        return daoConfig;
//    }
//
//    public synchronized IPortalUserConfig getPortalUserConfig() {
//        if ( userConfig == null ) {
//            userConfig = ConfigFactory.create( IPortalUserConfig.class );
//        }
//
//        return userConfig;
//    }

//    public synchronized IFileHandlingConfig getFileHandlingConfig() {
//        if ( fileConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isInternet() ) {
//                variables.put( "env", "feb" );
//            }
//            else {
//                variables.put( "env", "bpm" );
//            }
//
//            fileConfig = ConfigFactory.create( IFileHandlingConfig.class, variables );
//        }
//
//        return fileConfig;
//    }
//
//    public synchronized IHRInterfaceConfig getHRInterfaceConfig( String systemType ) {
//        Map< String, String > variables = new HashMap< String, String >();
//        variables.put( "system", systemType );
//
//        return ConfigFactory.create( IHRInterfaceConfig.class, variables );
//    }
//
//    public synchronized IHRInterfaceConfig getHRInterfaceConfig() {
//        return ConfigFactory.create( IHRInterfaceConfig.class );
//    }
//
    public synchronized ITemplateConfig getTemplateConfig() {
        return ConfigFactory.create( ITemplateConfig.class );
    }

    public synchronized IMailSenderConfig getMailSenderConfig( String module ) {
        Map< String, String > variables = new HashMap< String, String >();
        variables.put( "module", module );

        return ConfigFactory.create( IMailSenderConfig.class, variables );
    }
    
    public synchronized IMailRecipientConfig getMailRecipientConfig( String module ) {
      Map< String, String > variables = new HashMap< String, String >();
      variables.put( "module", module );

      return ConfigFactory.create( IMailRecipientConfig.class, variables );
    }
//
//    public synchronized IBPMEnvConfig getBpmEnvConfig() {
//        if ( bpmEnvConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isDevelopment() ) {
//                variables.put( "env", "dev" );
//            }
//            else if ( EnvironmentUtils.isProduction() ) {
//                variables.put( "env", "prod" );
//            }
//            //Add conditional Statements for other environments in Future
//
//            bpmEnvConfig = ConfigFactory.create( IBPMEnvConfig.class, variables );
//        }
//
//        return bpmEnvConfig;
//    }

//    public synchronized IEnvConfig getEnvConfig() {
//        if ( envConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isDevelopment() ) {
//                variables.put( "env", "dev" );
//            }
//            else if ( EnvironmentUtils.isProduction() ) {
//                variables.put( "env", "prod" );
//            }
//            //Add conditional Statements for other environments in Future
//
//            envConfig = ConfigFactory.create( IEnvConfig.class, variables );
//        }
//
//        return envConfig;
//    }

//    public synchronized ICodeConfig getCodeConfig() {
//        if ( codeConfig == null ) {
//            codeConfig = ConfigFactory.create( ICodeConfig.class );
//        }
//
//        return codeConfig;
//    }

/*    public synchronized ISearchConfig getSearchConfig() {
        if ( searchConfig == null ) {
        	searchConfig = ConfigFactory.create( ISearchConfig.class );
        }

        return searchConfig;
    }*/
    
//    public synchronized ISecurityConfig getSecurityConfig() {
//        if ( securityConfig == null ) {
//            securityConfig = ConfigFactory.create( ISecurityConfig.class );
//        }
//
//        return securityConfig;
//    }

//    public synchronized ISystemStatusConfig getSystemStatusConfig() {
//        if ( systemStatusConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isDevelopment() ) {
//                variables.put( "env", "dev" );
//            }
//            else if ( EnvironmentUtils.isProduction() ) {
//
//                if ( EnvironmentUtils.isInternet() ) {
//                    variables.put( "env", "feb" );
//                }
//                else if ( EnvironmentUtils.isIntranet() ) {
//                    variables.put( "env", "bpm" );
//                }
//            }
//            systemStatusConfig = ConfigFactory.create( ISystemStatusConfig.class, variables );
//        }
//
//        return systemStatusConfig;
//    }

//    public synchronized ITaskConfig getTaskConfig() {
//        if ( taskConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isInternet() ) {
//                variables.put( "env", "feb" );
//            }
//            else {
//                variables.put( "env", "bpm" );
//            }
//
//            taskConfig = ConfigFactory.create( ITaskConfig.class, variables );
//        }
//
//        return taskConfig;
//    }

//    public synchronized IMarketingContentConfig getMarketingContentConfig() {
//        if ( marketingContentConfig == null ) {
//            marketingContentConfig = ConfigFactory.create( IMarketingContentConfig.class );
//        }
//
//        return marketingContentConfig;
//    }

//    public synchronized IAnnouncementConfig getAnnouncementConfig() {
//        if ( announcementConfig == null ) {
//            announcementConfig = ConfigFactory.create( IAnnouncementConfig.class );
//        }
//
//        return announcementConfig;
//    }

//    public synchronized IAccountingConfig getAccountingConfig() {
//        if ( acocuntingConfig == null ) {
//            acocuntingConfig = ConfigFactory.create( IAccountingConfig.class );
//        }
//        return acocuntingConfig;
//    }

//    public synchronized IWelfareCourseConfig getWelfareCourseConfig() {
//        if ( welfareCourseConfig == null ) {
//            welfareCourseConfig = ConfigFactory.create( IWelfareCourseConfig.class );
//        }
//        return welfareCourseConfig;
//    }

//    public synchronized IPlannerConfig getPlannerConfig() {
//        if ( plannerConfig == null ) {
//            plannerConfig = ConfigFactory.create( IPlannerConfig.class );
//        }
//        return plannerConfig;
//    }

//    public synchronized ISAGConfig getSAGConfig() {
//        if ( sagConfig == null ) {
//            sagConfig = ConfigFactory.create( ISAGConfig.class );
//        }
//        return sagConfig;
//    }
//    
//    public synchronized ISSRSProxyConfig getSSRSProxyConfig () {
//    	if ( ssrsProxyConfig == null) {
//    		ssrsProxyConfig = ConfigFactory.create(ISSRSProxyConfig.class);
//    	}
//    	return ssrsProxyConfig;
//    }
//    
//    public synchronized IRateThisWebsiteConfig getRateThisWebsiteConfig() {
//        if ( rateThisWebsiteConfig == null ) {
//        	rateThisWebsiteConfig = ConfigFactory.create( IRateThisWebsiteConfig.class );
//        }
//
//        return rateThisWebsiteConfig;
//    }
    
//    public synchronized ISupportConfig getSupportConfig() {
//        if ( supportConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isInternet() ) {
//                variables.put( "env", "feb" );
//            }
//            else {
//                variables.put( "env", "bpm" );
//            }
//
//            supportConfig = ConfigFactory.create( ISupportConfig.class, variables );
//        }
//
//        return supportConfig;
//    }
    
//    public synchronized IStudyAwardConfig getStudyAwardConfig() {
//        if ( studyAwardConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isInternet() ) {
//                variables.put( "env", "feb" );
//            }
//            else {
//                variables.put( "env", "bpm" );
//            }
//
//            studyAwardConfig = ConfigFactory.create( IStudyAwardConfig.class, variables );
//        }
//
//        return studyAwardConfig;
//    }
//    public synchronized ISearchConfig getSearchConfig() {
//        if ( searchConfig == null ) {
//            Map< String, String > variables = new HashMap< String, String >();
//
//            if ( EnvironmentUtils.isInternet() ) {
//                variables.put( "env", "feb" );
//            }
//            else {
//                variables.put( "env", "bpm" );
//            }
//
//            searchConfig = ConfigFactory.create( ISearchConfig.class, variables );
//        }
//
//        return searchConfig;
//    }
}
