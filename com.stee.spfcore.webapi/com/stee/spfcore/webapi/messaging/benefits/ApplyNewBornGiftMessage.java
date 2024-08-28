package com.stee.spfcore.webapi.messaging.benefits;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.benefits.NewBornGift;

public class ApplyNewBornGiftMessage extends AbstractMessage {
	
	private NewBornGift newBornGift;
	
	public ApplyNewBornGiftMessage() {
		super(MessageId.APPLY_NEW_BORN_GIFT);
	}
	
	public ApplyNewBornGiftMessage(NewBornGift newBornGift) {
		super(MessageId.APPLY_NEW_BORN_GIFT);
		
		this.newBornGift = newBornGift;
	}

	public NewBornGift getNewBornGift() {
		return this.newBornGift;
	}
	
	@Override
	public String write(MessageStream stream) {
		return stream.write(this.newBornGift);
	}
	
	@Override
	public void read(MessageStream stream, String content) {
		this.newBornGift = (NewBornGift)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.newBornGift);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.newBornGift = (NewBornGift)stream.readObject();
	}
}
