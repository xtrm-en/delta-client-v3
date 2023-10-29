package me.xtrm.delta.client.gui.alt2;

import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.utils.auth.AuthenticationType;

public class AltManager {
    public static Alt lastAlt;
    public static List<Alt> registry;
    
    public static AuthenticationType authType;

    static {
    	authType = AuthenticationType.MOJANG;
        registry = new ArrayList<Alt>();
    }

    public List<Alt> getRegistry() {
        return registry;
    }

    public void setLastAlt(Alt alt2) {
        lastAlt = alt2;
    }
}

