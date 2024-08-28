package com.stee.spfcore.service.extSysInterface;

import com.stee.spfcore.service.extSysInterface.impl.ExtInterfacerCommon;

public class ExtInterfacerServiceFactory {

	private ExtInterfacerServiceFactory(){}
	private static IExtInterfacer extInterfaceService;
	
	public static synchronized IExtInterfacer getExtInterfacer () {
		if (extInterfaceService == null) {
			extInterfaceService = new ExtInterfacerCommon();
		}

		return extInterfaceService;
	}
}
