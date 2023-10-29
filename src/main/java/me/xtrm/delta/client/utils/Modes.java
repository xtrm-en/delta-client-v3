package me.xtrm.delta.client.utils;

import java.util.ArrayList;

public class Modes {
	
	public static ArrayList<String> build(String... args) {
		ArrayList<String> lol = new ArrayList<String>();
		for(String s : args) {
			lol.add(s);
		}
		return lol;
	}

}
