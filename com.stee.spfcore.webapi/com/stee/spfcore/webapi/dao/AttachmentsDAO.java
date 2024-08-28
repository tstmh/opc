package com.stee.spfcore.webapi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stee.spfcore.webapi.model.attachments.Attachments;

@Repository
public interface AttachmentsDAO extends JpaRepository <Attachments, String> {

}
