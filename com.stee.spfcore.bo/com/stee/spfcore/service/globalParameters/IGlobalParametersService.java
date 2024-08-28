package com.stee.spfcore.service.globalParameters;

import com.stee.spfcore.model.globalParameters.GlobalParameters;

public interface IGlobalParametersService {
    public GlobalParameters getGlobalParameters() throws GlobalParametersException;

    public void addOrUpdateGlobalParameters( GlobalParameters globalParameters ) throws GlobalParametersException;
    
    public GlobalParameters getGlobalParametersByUnit (String unit, String subunit) throws GlobalParametersException;
    
    public String getNextSequenceId () throws GlobalParametersException;
}
