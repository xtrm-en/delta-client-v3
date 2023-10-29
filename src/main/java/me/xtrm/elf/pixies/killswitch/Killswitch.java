	package me.xtrm.elf.pixies.killswitch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import me.xtrm.delta.client.utils.Consts;
import me.xtrm.delta.client.utils.Wrapper;

public class Killswitch {
	
	public static boolean good = true;
	
	public Killswitch() {
		Logger log = LogManager.getLogger("Killswitch");
		
		checkOldVerStr();
		
		String text = "NULLASNIGGA";		
		try {
			String urll = new String("https://raw.githubusercontent.com/nkosmos/xdelta/master/" + Consts.VER_STR + "/killswitch");
	        URL url = new URL(urll);
	        URLConnection conn = url.openConnection();
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine;
	        StringBuilder sb = new StringBuilder();
	        while ((inputLine = br.readLine()) != null) 
	        	sb.append(inputLine + "\n");
	        text = sb.toString();
	        br.close();
		} catch(Exception e) {
			e.printStackTrace();
			good = false;
			Wrapper.exitGame();
			return;
		}
		
		if(text.equalsIgnoreCase("NULLASNIGGA") || text == "" || text.contains("404")) {
			good = false;
			Wrapper.exitGame();
			return;
		}
		if(text.toLowerCase().contains("false")) {
			return;
		}
		good = false;
	}
	
	private void checkOldVerStr() {
		Logger log = LogManager.getLogger("Killswitch");
		
		String text = "L";
		try {
			String urll = new String("https://raw.githubusercontent.com/nkosmos/xdelta/master/b2.5/killswitch");
	        URL url = new URL(urll);
	        URLConnection conn = url.openConnection();
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String inputLine;
	        StringBuilder sb = new StringBuilder();
	        while ((inputLine = br.readLine()) != null) 
	        	sb.append(inputLine + "\n");
	        text = sb.toString();
	        br.close();
		} catch(Exception e) {
			good = false;
			e.printStackTrace();
			Wrapper.exitGame();
			return;
		}
		
		if(text.equalsIgnoreCase("L") || text == "" || text.contains("404")) {
			good = false;
			Wrapper.exitGame();
			return;
		}
		if(!text.toLowerCase().contains("true")) {
			log.info("Ptdr t'essayes quoi là?");
			good = false;
			
			Wrapper.exitGame();
			return;
		}
	}
	
	public static boolean isKillswitched() {
		return !good;
	}

}
