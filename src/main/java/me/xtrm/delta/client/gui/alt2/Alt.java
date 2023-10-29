package me.xtrm.delta.client.gui.alt2;

import me.xtrm.delta.client.utils.auth.AuthenticationType;

public final class Alt {
	private String displayName;
	private String username, password;
	private AuthenticationType auth;
	
    public Alt(String displayName, String username, String password, AuthenticationType authType) {
    	this.displayName = displayName;
    	this.username = username;
    	this.password = password;
    	this.auth = authType;
    }
    
    public boolean auth() {
    	try {
    		auth.authenticate(this);
    		return true;
    	} catch(Exception e) {
    		return false;
    	}
    }

    public AuthenticationType getAuthenticationType() {
    	return auth;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setDisplayName(String mask) {
        this.displayName = mask;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

