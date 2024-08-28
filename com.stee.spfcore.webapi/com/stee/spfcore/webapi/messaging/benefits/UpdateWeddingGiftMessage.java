package com.stee.spfcore.webapi.messaging.benefits;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.benefits.WeddingGift;

public class UpdateWeddingGiftMessage extends AbstractMessage {
	
	private WeddingGift weddingGift;
	
	public UpdateWeddingGiftMessage() {
		super(MessageId.UPDATE_WEDDING_GIFT);
	}
	
	public UpdateWeddingGiftMessage(WeddingGift weddingGift) {
		super(MessageId.UPDATE_WEDDING_GIFT);
		
		this.weddingGift = weddingGift;
	}

	public WeddingGift getWeddingGift() {
		return this.weddingGift;
	}
	
	@Override
	public String write(MessageStream stream) {
		return stream.write(this.weddingGift);
	}
	
	@Override
	public void read(MessageStream stream, String content) {
		this.weddingGift = (WeddingGift)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.weddingGift);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.weddingGift = (WeddingGift)stream.readObject();
	}
}
