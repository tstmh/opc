package com.stee.spfcore.webapi.model.accounting;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.envers.Audited;

import com.stee.spfcore.webapi.model.annotation.GeneratedId;
import com.stee.spfcore.webapi.model.annotation.SequenceDef;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@Entity
@Table(name = "BANK_INFORMATION", schema = "SPFCORE")
@XStreamAlias("BankInformation")
@Audited
@SequenceDef (name="BANK_INFORMATION_SEQ", schema = "SPFCORE", internetFormat="FEB-%d", intranetFormat="BPM-%d")
public class BankInformation {
	
	@Id
	@GeneratedId
	@Column(name = "\"ID\"")
	private String id;
	
	@Column(name = "\"BANK_CODE\"")
	private String bankCode;
	
	@Column(name = "\"BANK_NAME\"")
	private String bankName;
	
	@Column(name = "\"SWIFT_BIC_CODE\"")
	private String swiftBicCode;
	
	public BankInformation() {
		super();
	}

	public BankInformation(String id, String bankCode, String bankName,
			String swiftBicCode) {
		super();
		this.id = id;
		this.bankCode = bankCode;
		this.bankName = bankName;
		this.swiftBicCode = swiftBicCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getSwiftBicCode() {
		return swiftBicCode;
	}

	public void setSwiftBicCode(String swiftBicCode) {
		this.swiftBicCode = swiftBicCode;
	}
	
}

