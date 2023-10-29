package me.xtrm.delta.client.management.friend;

import java.util.ArrayList;
import java.util.List;

import me.xtrm.delta.client.api.friend.IFriendManager;

public class FriendManager implements IFriendManager {
	
	private final List<String> friends;
	
	public FriendManager() {
		friends = new ArrayList<>();
	}
	
	@Override
	public List<String> getFriends(){
		return friends;
	}

}
