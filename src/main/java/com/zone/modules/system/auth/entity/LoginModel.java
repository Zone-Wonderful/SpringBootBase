package com.zone.modules.system.auth.entity;

/**
 * 登录表单
 *
 * @author: ZoneWonderful
 * @date: 2020/5/1
 */
public class LoginModel {
    private String username;
    private String password;
    private String captcha;
    private String checkKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

	public String getCheckKey() {
		return checkKey;
	}

	public void setCheckKey(String checkKey) {
		this.checkKey = checkKey;
	}
    
}