package com.minlia.cloud.security.credential;

/**
 * Created by will on 8/14/17.
 */
public interface WithEmailCredential extends WithGuidCredential {

    /**
     * 邮箱
     * @return
     */
    public String getEmail();

}
