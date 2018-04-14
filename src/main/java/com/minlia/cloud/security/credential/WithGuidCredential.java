package com.minlia.cloud.security.credential;

/**
 * Created by will on 8/14/17.
 */
public interface WithGuidCredential extends Credential {

    /**
     * Global User Identity
     *
     * @return
     */
    public String getGuid();


}
