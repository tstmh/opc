package com.stee.spfcore.webapi.messaging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.model.membership.Membership;

public class ApplyMembershipMessage extends AbstractMessage {
	
	private Membership membership;
	
	public ApplyMembershipMessage() {
		super(MessageId.APPLY_MEMBERSHIP);
	}
	
	public ApplyMembershipMessage(Membership membership) {
		super(MessageId.APPLY_MEMBERSHIP);
		
		this.membership = membership;
	}

	public Membership getMembership() {
		return this.membership;
	}
	
	@Override
	public String write(MessageStream stream) {
		return stream.write(this.membership);
	}
	
	@Override
	public void read(MessageStream stream, String content) {
		this.membership = (Membership)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.membership);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.membership = (Membership)stream.readObject();
	}
}
