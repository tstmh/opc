package com.stee.spfcore.webapi.model.attachments;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table( name = "\"ATTACHMENTS\"", schema = "\"SPFCORE\"" )
@XStreamAlias( "Attachments" )
public class Attachments {
	@Id
    @GeneratedValue( strategy = GenerationType.IDENTITY )
    @Column(name="\"ID\"")
    private long id;

    @Temporal( TemporalType.TIMESTAMP )
    @Column( name = "UPLOAD_TIME" )
    private Date uploadTime;
    
    @Lob
    @Column(name = "CONTENT", nullable = false, length=4194304)
    private byte[] data;

    @Column(name="\"FILE_NAME\"", length=200)
    private String fileName;
    
    @Column(name="\"FILE_TYPE\"", length=50)
    private String fileType;
    
	public Attachments() {
		super();
	}

    public Attachments(Date uploadTime, byte[] data, String fileName, String fileType) {
		super();
		this.uploadTime = uploadTime;
		this.data = data;
		this.fileName = fileName;
		this.fileType = fileType;
	}


	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
    
}