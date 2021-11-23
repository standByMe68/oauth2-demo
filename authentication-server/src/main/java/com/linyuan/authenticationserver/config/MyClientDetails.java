package com.linyuan.authenticationserver.config;

import org.springframework.scheduling.concurrent.DefaultManagedAwareThreadFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

public class MyClientDetails  implements ClientDetailsService {

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return null;
    }
}
