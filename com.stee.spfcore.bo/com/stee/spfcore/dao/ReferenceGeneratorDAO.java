package com.stee.spfcore.dao;

import org.hibernate.Session;

import com.stee.spfcore.model.ReferenceGenerator;
import com.stee.spfcore.model.internal.ApplicationType;
import com.stee.spfcore.service.benefits.BenefitsServiceException;

public class ReferenceGeneratorDAO {

	/**
	 * Get the reference generator by application type
	 * 
	 * @param applicationType
	 * @return ReferenceGenerator or null
	 * @throws BenefitsServiceException
	 */
	public ReferenceGenerator getReferenceGenerator(
			ApplicationType applicationType) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		return (ReferenceGenerator) session.get(ReferenceGenerator.class,
				applicationType.toString());
	}

	/**
	 * Add new reference generator.
	 * 
	 * @param referenceGenerator
	 *            the reference generator detail to be added.
	 * @throws BenefitsServiceException
	 *             Exception while adding the reference generator.
	 */
	public void addReferenceGenerator(ReferenceGenerator referenceGenerator) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		session.save(referenceGenerator);

		session.flush();
	}

	/**
	 * Updating reference generator detail
	 * 
	 * @param referenceGenerator
	 *            the reference generator detail to be updated.
	 * @throws BenefitsServiceException
	 *             Exception while updating the reference generator detail.
	 */
	public void updateReferenceGenerator(ReferenceGenerator referenceGenerator) {

		Session session = SessionFactoryUtil.getInstance().getCurrentSession();

		session.merge(referenceGenerator);

		session.flush();
	}

}
