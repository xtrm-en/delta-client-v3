package me.xtrm.delta.client.gui.altmanager;

import java.util.Random;

public class AltAccount {
	
	private String username;
	private String email;
	private String password;
	
	public AltAccount(String email) {
		this.email = email;
		this.password = String.valueOf((new Random()).nextInt(420069));
	}
	
	public AltAccount(String email, String password) {
		this.email = email;
		this.password = password;
	}

}
