package account;

public class UserAccount {

	private String userId;		// 아이디
	private String password;	// 비밀번호
	private String name;		// 이름
	private String phoneNumber;	// 전화번호
	
	public UserAccount(String userId, String password, String name, String phoneNumber){
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.phoneNumber = phoneNumber;
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
}
