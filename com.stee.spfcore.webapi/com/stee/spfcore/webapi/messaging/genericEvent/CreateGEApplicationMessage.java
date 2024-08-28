package com.stee.spfcore.webapi.messaging.genericEvent;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.genericEvent.GenericEventApplication;

public class CreateGEApplicationMessage extends AbstractMessage {
	
	private GenericEventApplication application;
	
	public CreateGEApplicationMessage() {
		super(MessageId.CREATE_GENERIC_EVENT_APPLICATION);
	}
	
	public CreateGEApplicationMessage (GenericEventApplication application) {
		super (MessageId.CREATE_GENERIC_EVENT_APPLICATION);
		this.application = application;
	}

	public GenericEventApplication getEventDetailApplication () {
		return application;
	}

	@Override
	public String write(MessageStream stream) {
		return stream.write(application);
	}

	@Override
	public void read(MessageStream stream, String content) {
		this.application = (GenericEventApplication)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.application);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.application  = (GenericEventApplication)stream.readObject();
	}

}
