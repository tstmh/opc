package com.stee.spfcore.service.system;

import com.stee.spfcore.model.system.Heartbeat;

public interface ISystemService {

	/**
	 * Process system status
	 * @param
	 * @throws SystemServiceException
	 */
	public void process() throws SystemServiceException;

	/**
	 * Send heartbeat
	 * @param
	 * @throws SystemServiceException
	 */
	public void sendHeartbeat() throws SystemServiceException;

	/**
	 * Receive heartbeat
	 * @param
	 * @throws SystemServiceException
	 */
	public void receiveHeartbeat(Heartbeat heartbeat) throws SystemServiceException;

	/**
	 * Update system status
	 * @param
	 * @throws SystemServiceException
	 */
	public void updateSystemStatus() throws SystemServiceException;
}
