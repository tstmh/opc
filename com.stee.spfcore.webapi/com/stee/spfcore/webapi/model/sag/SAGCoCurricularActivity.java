package com.stee.spfcore.webapi.model.sag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "\"SAG_COCURRICULAR_ACTIVITY\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGCoCurricularActivity")
@Audited
@SequenceDef (name="SAGCoCurricularActivity_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGCoCurricularActivity {

	@Id
    @GeneratedId
    @Column( name = "\"ID\"" )
	private String id;

	@Column(name = "\"CCA_NAME\"", length = 60)
	private String ccaName;

	@Column(name = "\"APPOINTMENT\"", length = 60)
	private String appointment;

	@Column(name = "\"RESULTS\"", length = 20)
	private String results;
	
	public SAGCoCurricularActivity() {
		super();
	}

	public SAGCoCurricularActivity( String ccaName, String appointment,
			String results ) {
		super();
		this.ccaName = ccaName;
		this.appointment = appointment;
		this.results = results;
	}

	public String getCcaName() {
		return ccaName;
	}

	public void setCcaName( String ccaName ) {
		this.ccaName = ccaName;
	}

	public String getAppointment() {
		return appointment;
	}

	public void setAppointment( String appointment ) {
		this.appointment = appointment;
	}

	public String getResults() {
		return results;
	}

	public void setResults( String results ) {
		this.results = results;
	}

	public String getId() {
		return id;
	}

	public void preSave() {
		if ( ccaName != null ) {
			ccaName = ccaName.toUpperCase();
		}

		if ( appointment != null ) {
			appointment = appointment.toUpperCase();
		}
		
		if ( results != null ) {
			results = results.toUpperCase();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass() != obj.getClass() )
			return false;
		SAGCoCurricularActivity other = (SAGCoCurricularActivity) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}

}
