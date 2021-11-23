package com.linyuan.authenticationserver.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyTokenEnhancer implements TokenEnhancer {
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

        Map<String, Object> map = new HashMap<>();
        map.put("task", "jack");
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(map);

        Collection<? extends GrantedAuthority> authorities = authentication.getUserAuthentication().getAuthorities();
        authorities.forEach(x->{
            String authority = x.getAuthority();
            System.out.println(authority);
        });

        ((DefaultOAuth2AccessToken) accessToken).setValue("123123123");
        ((DefaultOAuth2AccessToken) accessToken).setRefreshToken(new DefaultExpiringOAuth2RefreshToken("q12321312321",new Date()));

        return accessToken;
    }
}
