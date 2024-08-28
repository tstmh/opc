package com.stee.spfcore.model.survey;

public enum QuestionType {
	
	TEXT_BOX ("Text Box"),
	TEXT_AREA ("Text Area"),
	RADIO_BUTTON ("Radio Button"),
	CHECK_BOX ("Check Box"),
	SLIDER ("Slider"),
	SINGLE_SELECT_LIST ("Single Select List"),
	MULTI_SELECT_LIST ("Multi Select List");
	
	private String value;
	
	private QuestionType (String value) {
		this.value = value;
	}

	@Override
	public String toString () {
		return this.value;
	}
	
	public static QuestionType getQuestionType (String type) {
		
		QuestionType [] types = QuestionType.values();
		
		for (QuestionType questionType : types) {
			if (questionType.value.equals(type)) {
				return questionType;
			}
		}
		
		return null;
	}

}
