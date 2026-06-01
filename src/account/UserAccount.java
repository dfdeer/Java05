package account;

public class UserAccount {

	private String userId;		// 아이디
	private String password;	// 비밀번호
	private String name;		// 이름
	private String phoneNumber;	// 전화번호
	private int passwordFailCount;
	private boolean locked;
	
	public UserAccount(String userId, String password, String name, String phoneNumber){
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.passwordFailCount = 0;
		this.locked = false;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserId() {
		return userId;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public int getPasswordFailCount() {
    	return passwordFailCount;
	}

	public void setPasswordFailCount(int passwordFailCount) {
    	this.passwordFailCount = passwordFailCount;
	}

	public boolean isLocked() {
    	return locked;
	}

	public void setLocked(boolean locked) {
    	this.locked = locked;
	}
}
