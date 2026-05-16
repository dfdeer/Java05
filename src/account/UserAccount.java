package account;

public class UserAccount {

	private String userId;		// 아이디
	private String password;	// 비밀번호
	private String name;		// 이름
	private String phoneNumber;	// 전화번호
	
	public UserAccount(){
		userId = "user";
		password = "1111";
		name = "익명";
		phoneNumber = "01011111111";
	}
	
	public UserAccount(String inUserId, String inPassword, String inName, String inPhoneNumber){
		userId = inUserId;
		password = inPassword;
		name = inName;
		phoneNumber = inPhoneNumber;
	}
	
	public void setUserId(String inUserId) {
		userId = inUserId;
	}
	public String getUserId() {
		return userId;
	}
	
	public void setPassword(String inPassword) {
		password = inPassword;
	}
	public String getPassword() {
		return password;
	}
	
	public void setName(String inName) {
		name = inName;
	}
	public String getName() {
		return name;
	}
	
	public void setPhoneNumber(String inPhoneNumber) {
		phoneNumber = inPhoneNumber;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
}
