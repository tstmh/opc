package com.stee.spfcore.webapi.message.corporateCard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.corporateCard.CardBooking;

public class UpdateCardBookingMessage extends AbstractMessage {

    private CardBooking cardBooking;

    public UpdateCardBookingMessage() {
        super( MessageId.UPDATE_CARD_BOOKING );
    }

    public UpdateCardBookingMessage( CardBooking cardBooking ) {
        super( MessageId.UPDATE_CARD_BOOKING );
        this.cardBooking = cardBooking;
    }

    public CardBooking getCardBooking() {
        return this.cardBooking;
    }

    @Override
    public String write( MessageStream stream ) {
        return stream.write( this.cardBooking );
    }

    @Override
    public void read( MessageStream stream, String content ) {
        this.cardBooking = ( CardBooking ) stream.read( content );
    }

    @Override
    public void write( ObjectOutputStream stream ) throws IOException {
        stream.writeObject( this.cardBooking );
    }

    @Override
    public void read( ObjectInputStream stream ) throws ClassNotFoundException, IOException {
        this.cardBooking = ( CardBooking ) stream.readObject();
    }
}
