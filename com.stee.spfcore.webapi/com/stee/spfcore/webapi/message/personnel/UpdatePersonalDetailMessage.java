package com.stee.spfcore.webapi.message.personnel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.*;
import com.stee.spfcore.webapi.model.personnel.PersonalDetail;

public class UpdatePersonalDetailMessage extends AbstractMessage {
	
	private PersonalDetail personal;
	
	public UpdatePersonalDetailMessage() {
		super(MessageId.UPDATE_PERSONAL_DETAIL);
	}

	public UpdatePersonalDetailMessage(PersonalDetail personal) {
		super(MessageId.UPDATE_PERSONAL_DETAIL);
		
		this.personal = personal;
	}
	
	public PersonalDetail getPersonalDetail() {
		return this.personal;
	}
	
	@Override
	public String write(MessageStream stream) {
		return stream.write(this.personal);
	}
	
	@Override
	public void read(MessageStream stream, String content) {
		this.personal = (PersonalDetail)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.personal);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.personal = (PersonalDetail)stream.readObject();
	}
}
