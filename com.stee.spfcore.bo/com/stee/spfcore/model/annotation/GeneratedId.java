package com.stee.spfcore.model.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD})
@Retention(RUNTIME)
public @interface GeneratedId {

	/**
	 * Class that contains the sequence definition to be used 
	 * for the id generation.
	 * @return
	 */
	Class<?> sequenceDefinitionClass () default void.class;
	
}
