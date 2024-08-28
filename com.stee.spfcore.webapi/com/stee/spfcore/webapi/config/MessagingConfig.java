package com.stee.spfcore.webapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:messaging.properties")
public class MessagingConfig {
		
	@Value("${mq.hostname}")
	private String hostname;
	
	@Value("${mq.disabled}")
	private boolean disabled;
	
	@Value("${mq.port}")
	private int port;
	
	@Value("${mq.channel}")
	private String channel;
	
	@Value("${mq.queue.manager}")
	private String manager;
	
	@Value("${mq.remote.queue}")
	private String queue;
	
	@Value("${mq.ssl.enabled}")
	private boolean sslEnabled;
	
	@Value("${mq.ssl.cipher.suite}")
	private String sslCipherSuite;
	
	@Value("${mq.ssl.trust.store.path}")
	private String sslTrustStorePath;
	
	@Value("${mq.ssl.key.store.path}")
	private String sslKeyStorePath;
	
	@Value("${mq.ssl.password}")
	private String sslPassword;
	
	@Value("${mq.rf.header.mail.id}")
	private Integer mailId;
	
	@Value("${mq.rf.header.request.number}")
    private Integer requestNumber;

    @Value("${mq.rf.header.mail.number}")
    private Integer mailNumber;

    @Value("${mq.rf.header.subject.id}")
    private Integer subjectId;

    @Value("${mq.rf.header.source.system.id}")
    private Integer sourceSystemId;
    
    @Value("${unhandled.msgs.log.enabled}")
    private Boolean unhandledMsgsLogEnabled;

    @Value("{unhandled.msgs.log.path}")
    private String unhandledMsgsLogPath;
    
	public MessagingConfig() {
		super();
	}

	public MessagingConfig(String hostname, boolean disabled, int port, String channel, String manager, String queue,
			boolean sslEnabled, String sslCipherSuite, String sslTrustStorePath, String sslKeyStorePath,
			String sslPassword, Integer mailId, Integer requestNumber, Integer mailNumber, Integer subjectId,
			Integer sourceSystemId, Boolean unhandledMsgsLogEnabled, String unhandledMsgsLogPath) {
		super();
		this.hostname = hostname;
		this.disabled = disabled;
		this.port = port;
		this.channel = channel;
		this.manager = manager;
		this.queue = queue;
		this.sslEnabled = sslEnabled;
		this.sslCipherSuite = sslCipherSuite;
		this.sslTrustStorePath = sslTrustStorePath;
		this.sslKeyStorePath = sslKeyStorePath;
		this.sslPassword = sslPassword;
		this.mailId = mailId;
		this.requestNumber = requestNumber;
		this.mailNumber = mailNumber;
		this.subjectId = subjectId;
		this.sourceSystemId = sourceSystemId;
		this.unhandledMsgsLogEnabled = unhandledMsgsLogEnabled;
		this.unhandledMsgsLogPath = unhandledMsgsLogPath;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getQueue() {
		return queue;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public boolean isSslEnabled() {
		return sslEnabled;
	}

	public void setSslEnabled(boolean sslEnabled) {
		this.sslEnabled = sslEnabled;
	}

	public String getSslCipherSuite() {
		return sslCipherSuite;
	}

	public void setSslCipherSuite(String sslCipherSuite) {
		this.sslCipherSuite = sslCipherSuite;
	}

	public String getSslTrustStorePath() {
		return sslTrustStorePath;
	}

	public void setSslTrustStorePath(String sslTrustStorePath) {
		this.sslTrustStorePath = sslTrustStorePath;
	}

	public String getSslKeyStorePath() {
		return sslKeyStorePath;
	}

	public void setSslKeyStorePath(String sslKeyStorePath) {
		this.sslKeyStorePath = sslKeyStorePath;
	}

	public String getSslPassword() {
		return sslPassword;
	}

	public void setSslPassword(String sslPassword) {
		this.sslPassword = sslPassword;
	}

	public Integer getMailId() {
		return mailId;
	}

	public void setMailId(Integer mailId) {
		this.mailId = mailId;
	}

	public Integer getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(Integer requestNumber) {
		this.requestNumber = requestNumber;
	}

	public Integer getMailNumber() {
		return mailNumber;
	}

	public void setMailNumber(Integer mailNumber) {
		this.mailNumber = mailNumber;
	}

	public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getSourceSystemId() {
		return sourceSystemId;
	}

	public void setSourceSystemId(Integer sourceSystemId) {
		this.sourceSystemId = sourceSystemId;
	}

	public Boolean getUnhandledMsgsLogEnabled() {
		return unhandledMsgsLogEnabled;
	}

	public void setUnhandledMsgsLogEnabled(Boolean unhandledMsgsLogEnabled) {
		this.unhandledMsgsLogEnabled = unhandledMsgsLogEnabled;
	}

	public String getUnhandledMsgsLogPath() {
		return unhandledMsgsLogPath;
	}

	public void setUnhandledMsgsLogPath(String unhandledMsgsLogPath) {
		this.unhandledMsgsLogPath = unhandledMsgsLogPath;
	}

	@Override
	public String toString() {
		return "MessagingConfig [hostname=" + hostname + ", disabled=" + disabled + ", port=" + port + ", channel="
				+ channel + ", manager=" + manager + ", queue=" + queue + ", sslEnabled=" + sslEnabled
				+ ", sslCipherSuite=" + sslCipherSuite + ", sslTrustStorePath=" + sslTrustStorePath
				+ ", sslKeyStorePath=" + sslKeyStorePath + ", sslPassword=" + sslPassword + ", mailId=" + mailId
				+ ", requestNumber=" + requestNumber + ", mailNumber=" + mailNumber + ", subjectId=" + subjectId
				+ ", sourceSystemId=" + sourceSystemId + ", unhandledMsgsLogEnabled=" + unhandledMsgsLogEnabled
				+ ", unhandledMsgsLogPath=" + unhandledMsgsLogPath + "]";
	}
}
