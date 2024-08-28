package com.stee.spfcore.webapi.model.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Define the sequence to be use by the model class.
 * 
 * It support formatting of the sequence output string.
 * The order of the arguments pass to the format is as followings:
 * <ol>
 * 	<li>sequence (long)</li>
 * 	<li>date<li>
 * </ol>
 * 
 * E.g.,
 * if the intranetFormat is set to "NB-%2$tYM-%1$04d" and 
 * the sequence value is 25, today date is '23-12-2016',
 * then the output will be "NB-2016M-0025"
 * 
 * if the interanetFormat is set to "CTO-FIN-%2$tY-%2$tm-%1$04d"
 * and the sequence value is 25, today date is '23-12-2016',
 * then the output will be "CTO-FIN-2016-12-0025"
 * 
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface SequenceDef {

	/**
	 * Name of the sequence. Default is the name of the class that 
	 * contains the annotation. 
	 * @return
	 */
	String name () default "";
	
	/**
	 * Name of the schema. Default is SPFCORE.
	 * @return
	 */
	String schema () default "SPFCORE";
	
	/**
	 * Format string to be used to format the sequence in
	 * BPM environment.
	 * @return
	 */
	String intranetFormat () default "";
	
	/**
	 * Format string to be used to format the sequence in
	 * FEB environment.
	 * @return
	 */
	String internetFormat () default "";
	
	/**
	 * The initial value of the sequence
	 * @return
	 */
	String initialValue () default "0";
	
	/**
	 * Max value of the sequence
	 * @return
	 */
	String maxValue () default "";
	
	/**
	 * Whether the sequence will cycle the value when 
	 * max is reached. 
	 * @return
	 */
	boolean cycle () default false;
}
