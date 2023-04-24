package org.xbmlz.next.security;

import org.springframework.security.core.AuthenticationException;

/**
 * @author chenxc
 */
public class UserNotActivatedException extends AuthenticationException {
    private static final long serialVersionUID = 1L;

    public UserNotActivatedException(String message) {
        super(message);
    }

    public UserNotActivatedException(String message, Throwable t) {
        super(message, t);
    }
}
