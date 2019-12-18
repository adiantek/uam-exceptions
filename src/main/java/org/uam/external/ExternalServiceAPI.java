package org.uam.external;

import org.uam.dataload.TestSubject;

import javax.naming.ServiceUnavailableException;
import java.util.List;

public class ExternalServiceAPI {
    public void send(List<TestSubject> testObjects) throws ServiceUnavailableException {
        throw new ServiceUnavailableException();
    }
}
