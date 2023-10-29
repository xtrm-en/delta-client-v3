package me.xtrm.delta.client.utils.auth;

import java.util.function.Consumer;

import com.mojang.authlib.exceptions.AuthenticationException;

import me.xtrm.delta.client.gui.alt2.Alt;
import me.xtrm.delta.client.utils.CachedResource;

public enum AuthenticationType {
	
	OFFLINE("Cracked", AuthenticationHelper::loginOffline, false, false, new CachedResource("https://nkosmos.github.io/assets/mojang.png")),
	MOJANG("Mojang", t -> {
		try {
			AuthenticationHelper.loginMojang(t);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}, false, true, new CachedResource("https://nkosmos.github.io/assets/mojang.png")),
	THEALTENING("TheAltening", t -> {
		try {
			AuthenticationHelper.loginAltening(t);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		}
	}, true, false, new CachedResource("https://nkosmos.github.io/assets/thealtening.png"));
	
	private String name;
	
	private Consumer<Alt> authenticator;
	
	private boolean useToken;
	private boolean usePassword;
	
	private CachedResource icon;
	
	AuthenticationType(String name, Consumer<Alt> consumer, boolean useToken, boolean usePassword, CachedResource icon) {
		this.name = name;
		this.authenticator = consumer;
		this.icon = icon;
		this.useToken = useToken;
		this.usePassword = usePassword;
	}
	
	public String getName() {
		return name;
	}
	
	public void authenticate(Alt alt) {
		this.authenticator.accept(alt);
	}
	
	public CachedResource getIcon() {
		return icon;
	}
	
	public boolean usesToken() {
		return useToken;
	}
	
	public boolean usesPassword() {
		return usePassword;
	}

}
