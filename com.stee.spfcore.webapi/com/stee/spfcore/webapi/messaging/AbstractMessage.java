package com.stee.spfcore.webapi.messaging;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractMessage implements Cloneable {

	private static final Logger logger = Logger.getLogger(AbstractMessage.class.getName());

	private MessageId identity;

	protected AbstractMessage(MessageId identity) {
		this.identity = identity;
	}
	
	public MessageId getIdentity() {
		return this.identity;
	}

	public abstract String write(MessageStream stream);
	public abstract void read(MessageStream stream, String content);

	public abstract void write(ObjectOutputStream stream) throws IOException;
	public abstract void read(ObjectInputStream stream) throws ClassNotFoundException, IOException;
	
	public Object clone() {
		Object clone = null;
		
		try {
			clone = super.clone();
		}
		catch (CloneNotSupportedException exception) {		
			logger.log (Level.SEVERE, "Error cloning message [" + identity + "]");
			throw new RuntimeException();
		}
		
		return clone;
	}
}
