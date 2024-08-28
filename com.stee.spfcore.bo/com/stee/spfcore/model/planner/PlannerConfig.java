package com.stee.spfcore.model.planner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table( name = "PLANNER_CONFIG", schema = "SPFCORE" )
@XStreamAlias( "PlannerConfig" )
public class PlannerConfig {
    @Id
    @Column( name = "ID" )
    @XStreamOmitField
    private int id = 0;

    @Column( name = "ACCEPTED_COUNT_LIMIT" )
    private Integer acceptedCountLimit = 100;

    @Column( name = "ALLOW_SOLVE_DURATION_SEC" )
    private Integer allowableSolveDuration = 120;

    @Column( name = "IS_EXPORT_ENABLED" )
    private Boolean isExportEnabled = Boolean.FALSE;

    @Column( name = "EXPORT_PATH" )
    private String exportPath = "c:\\spfcore\\planner\\output";

    @Column( name = "IS_DEBUG_ENABLED" )
    private Boolean isDebugEnabled = Boolean.FALSE;

    @Column( name = "SEED" )
    private Long seed = 1L;

    @Column( name = "NUM_SOLVE_ATTEMPTS" )
    private Integer numSolveAttempts = 1;

    public PlannerConfig() {
        // DO NOTHING
    }

    public void fillNullsWithDefaultValues() {
        PlannerConfig defaultConfig = new PlannerConfig();
        if ( this.acceptedCountLimit == null ) {
            this.acceptedCountLimit = defaultConfig.getAcceptedCountLimit();
        }
        if ( this.allowableSolveDuration == null ) {
            this.allowableSolveDuration = defaultConfig.getAllowableSolveDuration();
        }
        if ( this.isExportEnabled == null ) {
            this.isExportEnabled = defaultConfig.getIsExportEnabled();
        }
        if ( this.exportPath == null ) {
            this.exportPath = defaultConfig.getExportPath();
        }
        if ( this.isDebugEnabled == null ) {
            this.isDebugEnabled = defaultConfig.getIsDebugEnabled();
        }
        if ( this.seed == null ) {
            this.seed = defaultConfig.getSeed();
        }
        if ( this.numSolveAttempts == null ) {
            this.numSolveAttempts = defaultConfig.getNumSolveAttempts();
        }
    }

    public String toString() {
        return String.format( "planner config acceptedCountLimit=%s, allowableSolveDuration=%s, isExportEnabled=%s, exportPath=%s, isDebugEnabled=%s, seed=%s, numSolveAttempts=%s", this.acceptedCountLimit, this.allowableSolveDuration,
                this.isExportEnabled, this.exportPath, this.isDebugEnabled, this.seed, this.numSolveAttempts );
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    public Integer getAcceptedCountLimit() {
        return acceptedCountLimit;
    }

    public void setAcceptedCountLimit( Integer acceptedCountLimit ) {
        this.acceptedCountLimit = acceptedCountLimit;
    }

    public Boolean getIsExportEnabled() {
        return isExportEnabled;
    }

    public void setIsExportEnabled( Boolean isExportEnabled ) {
        this.isExportEnabled = isExportEnabled;
    }

    public String getExportPath() {
        return exportPath;
    }

    public void setExportPath( String exportPath ) {
        this.exportPath = exportPath;
    }

    public Boolean getIsDebugEnabled() {
        return isDebugEnabled;
    }

    public void setIsDebugEnabled( Boolean isDebugEnabled ) {
        this.isDebugEnabled = isDebugEnabled;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed( Long seed ) {
        this.seed = seed;
    }

    public Integer getAllowableSolveDuration() {
        return allowableSolveDuration;
    }

    public void setAllowableSolveDuration( Integer allowableSolveDuration ) {
        this.allowableSolveDuration = allowableSolveDuration;
    }

    public Integer getNumSolveAttempts() {
        return numSolveAttempts;
    }

    public void setNumSolveAttempts( Integer numSolveAttempts ) {
        this.numSolveAttempts = numSolveAttempts;
    }
}
