package com.stee.spfcore.webapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.stee.spfcore.webapi.utils.EnvironmentType;

@Configuration
@PropertySource("classpath:environment.properties")
public class EnvironmentConfig {
	
	@Value("${env.environmentType}")
	private String environmentType;
	
	public EnvironmentConfig () {
		super();
	}
	
	public boolean isIntranet() {
		return EnvironmentType.get(this.environmentType) == EnvironmentType.INTRANET;
	}
	
	public boolean isInternet() {
		return EnvironmentType.get(this.environmentType) == EnvironmentType.INTERNET;
	}
	
	public EnvironmentType getEnvironmentType() {
		return EnvironmentType.get(this.environmentType);
	}
}
