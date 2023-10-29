package me.xtrm.delta.client.gui.alt2;

import me.xtrm.delta.client.utils.Wrapper;
import me.xtrm.delta.client.utils.auth.AuthenticationType;

public final class AltLoginThread extends Thread {
	
    private final String password;
    private final String username;
    private final AuthenticationType auth;
    
    private boolean addAlt;
    private LoginStatus status;
    
    public AltLoginThread(String username, String password, AuthenticationType authType) {
        this(username, password, authType, false);
    }
    
    public AltLoginThread(String username, String password, AuthenticationType authType, boolean addAlt) {
        super("Alt Login Thread");
        
        this.username = username;
        this.password = password;
        this.auth = authType;        
        this.addAlt = addAlt;
        this.status = LoginStatus.WAITING;
    }

    public LoginStatus getStatus() {
        return this.status;
    }

    @Override
    public void run() {
    	String sessionId = Wrapper.mc.getSession().getSessionID();
    	
    	this.status = LoginStatus.LOGGINGIN;
    	
    	Alt alt = new Alt(this.username, this.username, this.password, this.auth);
    	boolean auth = alt.auth();
    	
    	if(sessionId == Wrapper.mc.getSession().getSessionID()) {
    		if(this.auth.usesToken()) {
    			this.status = LoginStatus.FAILED;
    		}else {
    			this.status = LoginStatus.FAILED;
    		}
    	}else {
    		this.status = LoginStatus.LOGGEDIN;
    	}
    	
    	if(addAlt) {
    		AltManager.registry.add(alt);
    	}
    }
}