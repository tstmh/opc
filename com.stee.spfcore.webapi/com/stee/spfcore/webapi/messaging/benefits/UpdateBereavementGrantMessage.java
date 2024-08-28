package com.stee.spfcore.webapi.messaging.benefits;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.benefits.BereavementGrant;

public class UpdateBereavementGrantMessage extends AbstractMessage {
	
	private BereavementGrant bereavementGrant;
	
	public UpdateBereavementGrantMessage() {
		super(MessageId.UPDATE_BEREAVEMENT_GRANT);
	}
	
	public UpdateBereavementGrantMessage(BereavementGrant bereavementGrant) {
		super(MessageId.UPDATE_BEREAVEMENT_GRANT);
		
		this.bereavementGrant = bereavementGrant;
	}

	public BereavementGrant getBereavementGrant() {
		return this.bereavementGrant;
	}
	
	@Override
	public String write(MessageStream stream) {
		return stream.write(this.bereavementGrant);
	}
	
	@Override
	public void read(MessageStream stream, String content) {
		this.bereavementGrant = (BereavementGrant)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.bereavementGrant);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.bereavementGrant = (BereavementGrant)stream.readObject();
	}
}
