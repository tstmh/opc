package com.stee.spfcore.model.calendar;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

@Entity
@Table(name = "\"CALENDAR_PUBLIC_HOLIDAYS\"", schema = "\"SPFCORE\"")
@XStreamAlias("PublicHoliday")
@Audited
public class PublicHoliday {

	@Id
	@GenericGenerator(name = "PublicHolidayIdGenerator", strategy = "com.stee.spfcore.dao.utils.StringIdHibernateGenerator")
	@GeneratedValue(generator = "PublicHolidayIdGenerator")
	@Column(name = "\"ID\"")
	@XStreamOmitField
	private String id;

	@Column(name = "\"NAME\"")
	private String name;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"START_DATE\"")
	private Date startDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "\"END_DATE\"")
	private Date endDate;

	public PublicHoliday() {
		super();
	}

	public PublicHoliday(String id, String name, Date startDate, Date endDate) {
		super();
		this.id = id;
		this.name = name;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
