package com.linyuan.oauth2config.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * @author: 林塬
 * @date: 2018/1/20
 * @description: OAuth2 授权服务器配置类
 */
@EnableAuthorizationServer
public abstract class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired(required = false)
    private JdbcClientDetailsService jdbcClientDetailsService;

    //令牌失效时间
    public int accessTokenValiditySeconds;

    //刷新令牌失效时间
    public int refreshTokenValiditySeconds;

    //是否可以重用刷新令牌
    public boolean isReuseRefreshToken;

    //是否支持刷新令牌
    public boolean isSupportRefreshToken;


    public  AuthServerConfig(int accessTokenValiditySeconds, int refreshTokenValiditySeconds, boolean isReuseRefreshToken, boolean isSupportRefreshToken) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
        this.isReuseRefreshToken = isReuseRefreshToken;
        this.isSupportRefreshToken = isSupportRefreshToken;
    }

    /**
     * 配置授权服务器端点，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //从BeanFactory获取所有 的TokenEnhancer的bean
        Collection<TokenEnhancer> tokenEnhancers = applicationContext.getBeansOfType(TokenEnhancer.class).values();
        // token增强调用链
        TokenEnhancerChain tokenEnhancerChain=new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(new ArrayList<>(tokenEnhancers));

        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        //是否允许刷新token
        defaultTokenServices.setReuseRefreshToken(isReuseRefreshToken);
        //是否支持刷新token
        defaultTokenServices.setSupportRefreshToken(isSupportRefreshToken);
        //添加token商店
        defaultTokenServices.setTokenStore(tokenStore);
        //token失效时间
        defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
        //token刷新时间
        defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
        //设置增强链
        defaultTokenServices.setTokenEnhancer(tokenEnhancerChain);

        //若通过 JDBC 存储令牌
        if (Objects.nonNull(jdbcClientDetailsService)){
            defaultTokenServices.setClientDetailsService(jdbcClientDetailsService);
        }


        endpoints
                // 设置当前授权认证管理器，可以自动注入
            .authenticationManager(authenticationManager)
                // 设置当前用户认证查询用户信息方式
            .userDetailsService(userDetailsService)
                // 设置token服务 可以设置token有效配置，刷新配置，增强设置，token的存储设置
            .tokenServices(defaultTokenServices);


    }


    /**
     * 配置授权服务器端点的安全
     * @param oauthServer
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

}
