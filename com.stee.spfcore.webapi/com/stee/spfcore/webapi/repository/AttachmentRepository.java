package com.stee.spfcore.webapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.stee.spfcore.webapi.model.attachments.Attachments;

@Repository
public interface AttachmentRepository extends JpaRepository <Attachments, Long> {

}
