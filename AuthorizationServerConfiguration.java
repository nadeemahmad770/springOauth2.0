
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	@Value("${security.jwt.client-id}")
	private String clientId;

	@Value("${security.jwt.client-secret}")
	private String clientSecret;

	@Value("${security.jwt.grant-type.password}")
	private String grantTypePassword;

	@Value("${security.jwt.grant-type.authorization_code}")
	private String authorizationCode;

	@Value("${security.jwt.grant-type.refresh_token}")
	private String refreshToken;

	@Value("${security.jwt.grant-type.implicit}")
	private String implicit;

	@Value("${security.jwt.scope-read}")
	private String scopeRead;

	@Value("${security.jwt.scope-write}")
	private String scopeWrite = "write";

	@Value("${security.jwt.resource-ids}")
	private String resourceIds;
	
	@Value("${security.jwt.token-expire-time}")
	private int tokenExpireTimeSecs;
	
	@Value("${security.jwt.refresh-token-expire-time}")
	private int refreshTokenExpireTimeDays;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private JwtAccessTokenConverter accessTokenConverter;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
		final int refreshTokenExpire = (86400*refreshTokenExpireTimeDays);
		configurer.inMemory().withClient(clientId).secret("{noop}" + clientSecret)
				.authorizedGrantTypes(grantTypePassword, authorizationCode, refreshToken, implicit)
				.scopes(scopeRead, scopeWrite).resourceIds(resourceIds).accessTokenValiditySeconds(tokenExpireTimeSecs)
				.refreshTokenValiditySeconds(refreshTokenExpire);
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
		enhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
		endpoints.tokenStore(tokenStore).accessTokenConverter(accessTokenConverter).tokenEnhancer(enhancerChain)
				.authenticationManager(authenticationManager);
	}

}
