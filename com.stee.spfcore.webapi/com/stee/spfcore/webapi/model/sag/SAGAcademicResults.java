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
@Table(name = "\"SAG_ACADEMIC_RESULTS\"", schema = "\"SPFCORE\"")
@XStreamAlias("SAGAcademicResults")
@Audited
@SequenceDef (name="SAGAcademicResults_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class SAGAcademicResults {

	@Id
	@GeneratedId
    @Column( name = "\"ID\"" )
	private String id;

	@Column(name = "\"SUBJECT\"", length = 60)
	private String subject;

	@Column(name = "\"GRADE\"", length = 20)
	private String grade;
	
	@Column(name = "\"OTHER_SUBJECT\"", length = 100)
	private String otherSubject;
	
	@Column(name = "\"SUBJECT_CATEGORY\"", length = 50)
	private String subjectCategory;

	public SAGAcademicResults() {
		super();
	}

	public SAGAcademicResults( String subject, String grade, String otherSubject, String subjectCategory ) {
		super();
		this.subject = subject;
		this.grade = grade;
		this.otherSubject = otherSubject;
		this.subjectCategory = subjectCategory;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject( String subject ) {
		this.subject = subject;
	}

	public String getOtherSubject() {
		return otherSubject;
	}

	public void setOtherSubject( String otherSubject ) {
		this.otherSubject = otherSubject;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade( String grade ) {
		this.grade = grade;
	}

	public String getId() {
		return id;
	}

	public String getSubjectCategory() {
		return subjectCategory;
	}

	public void setSubjectCategory( String subjectCategory ) {
		this.subjectCategory = subjectCategory;
	}
	
	public void preSave() {
		if ( otherSubject != null ) {
			otherSubject = otherSubject.toUpperCase();
		}

		if ( grade != null ) {
			grade = grade.toUpperCase();
		}
		
		if ( subjectCategory != null )
		{
			subjectCategory = subjectCategory.toUpperCase();
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
		SAGAcademicResults other = (SAGAcademicResults) obj;
		if ( id == null ) {
			if ( other.id != null )
				return false;
		} else if ( !id.equals( other.id ) )
			return false;
		return true;
	}

}
