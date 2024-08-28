package com.stee.spfcore.webapi.messaging.course;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.stee.spfcore.webapi.messaging.AbstractMessage;
import com.stee.spfcore.webapi.messaging.MessageId;
import com.stee.spfcore.webapi.messaging.MessageStream;
import com.stee.spfcore.webapi.model.course.CourseParticipant;

public class WithdrawCourseMessage extends AbstractMessage {

	private CourseParticipant participant;

	public WithdrawCourseMessage () {
		super(MessageId.WITHDRAW_COURSE);
	}
	
	public WithdrawCourseMessage (CourseParticipant participant) {
		super (MessageId.WITHDRAW_COURSE);
		this.participant = participant;
	}

	public CourseParticipant getCourseParticipant() {
		return participant;
	}

	@Override
	public String write(MessageStream stream) {
		return stream.write(participant);
	}

	@Override
	public void read(MessageStream stream, String content) {
		this.participant = (CourseParticipant)stream.read(content);
	}

	@Override
	public void write(ObjectOutputStream stream) throws IOException {
		stream.writeObject(this.participant);
	}

	@Override
	public void read(ObjectInputStream stream) throws ClassNotFoundException, IOException {
		this.participant = (CourseParticipant)stream.readObject();
	}
}