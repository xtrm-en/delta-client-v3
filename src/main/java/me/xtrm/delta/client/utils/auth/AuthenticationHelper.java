package me.xtrm.delta.client.utils.auth;

import java.net.Proxy;
import java.util.UUID;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;

import me.xtrm.delta.client.gui.alt2.Alt;
import me.xtrm.delta.client.utils.reflect.ReflectedClass;
import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.Session;

public class AuthenticationHelper {
	
	private static TheAlteningAuthentication authentication = TheAlteningAuthentication.mojang();
	
	public static void loginOffline(Alt alt) {
		authentication.updateService(AlteningServiceType.MOJANG);
		Session session = new Session(alt.getUsername(), UUID.nameUUIDFromBytes(("OfflinePlayer" + alt.getUsername()).getBytes()).toString(), "", "mojang");
		setSession(session);
	}
	
	public static void loginMojang(Alt alt) throws AuthenticationException {
		authentication.updateService(AlteningServiceType.MOJANG);
		Session session = createSession(alt);
		alt.setDisplayName(session.getUsername());
		setSession(session);	
	}
	
	public static void loginAltening(Alt alt) throws AuthenticationException {
		authentication.updateService(AlteningServiceType.THEALTENING);
		Session session = createSession(alt);
		alt.setDisplayName(session.getUsername());
		setSession(session);
	}
	
	public static Session createSession(Alt alt) throws AuthenticationException {
		YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
		YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
		
		auth.setUsername(alt.getUsername());
		auth.setPassword(alt.getPassword());
		
		auth.logIn();
		return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
	}
	
	public static void setSession(Session session) {
		try {
        	boolean obf = !((Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment"));
            ReflectedClass rc = ReflectedClass.of(Minecraft.getMinecraft());
            rc.set0(obf ? "field_71449_j" : "session", session);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
	}
}