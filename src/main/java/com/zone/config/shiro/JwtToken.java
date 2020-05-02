package com.zone.config.shiro;
 
import org.apache.shiro.authc.AuthenticationToken;

/**
 * @author: ZoneWonderful
 * @date: 2020/5/2
 * @desc
 **/
public class JwtToken implements AuthenticationToken {
	
	private static final long serialVersionUID = 1L;
	private String token;
 
    public JwtToken(String token) {
        this.token = token;
    }
 
    @Override
    public Object getPrincipal() {
        return token;
    }
 
    @Override
    public Object getCredentials() {
        return token;
    }
}
