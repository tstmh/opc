package com.stee.spfcore.model.sag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.model.annotation.GeneratedId;
import com.stee.spfcore.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_ANNOUNCEMENT_CONFIG\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGAnnouncementConfig")
@Audited
@SequenceDef (name="SAGAnnouncementConfig_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGAnnouncementConfig {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;
	
	@Column( name = "\"FINANCIAL_YEAR\"", length=10 )
	private String financialYear;
	
	@Column( name = "\"ANNOUNCEMENT_ID\"", length=100 )
	private String announcementId;

	public SAGAnnouncementConfig() {
		super();
	}

	public SAGAnnouncementConfig( String financialYear, String announcementId ) {
		super();
		this.financialYear = financialYear;
		this.announcementId = announcementId;
	}

	public String getId() {
		return id;
	}

	public void setId( String id ) {
		this.id = id;
	}

	public String getFinancialYear() {
		return financialYear;
	}

	public void setFinancialYear( String financialYear ) {
		this.financialYear = financialYear;
	}

	public String getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId( String announcementId ) {
		this.announcementId = announcementId;
	}
}
