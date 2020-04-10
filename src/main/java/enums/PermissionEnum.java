package enums;

public enum PermissionEnum {
	

	ADD_PROJECT("ADD_PROJECT", 1), 
	EDIT_PROJECT("EDIT_PROJECT", 2), 
	ASSIGN_PROJECT("ASSIGN_PROJECT", 3), 
	CONTROLE_PROJECT("CONTROLE_PROJECT", 4), 
	VALIDATE_PROJECT("VALIDATE_PROJECT", 5), 
	DELETE_PROJECT("DELETE_PROJECT", 6),
	VIEW_USERS("VIEW_USERS", 10),
	ADD_USER("ADD_USER", 11),
	EDIT_USER("EDIT_USER", 12),
	DELETE_USER("DELETE_USER", 13),
	VIEW_CONVENTION("VIEW_CONVENTION", 20),
	VIEW_LOCATION("VIEW_LOCATION", 30);
	
	private final String key;
	private final Integer value;
	
	PermissionEnum(String key, Integer value) {
	    this.key = key;
	    this.value = value;
	}
	
	public String getKey() {
	    return key;
	}
	public Integer getValue() {
	    return value;
	}

}
