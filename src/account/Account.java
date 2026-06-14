package account;

public class Account {
	private String userId;		// 아이디
	private Bank bank;	// 은행 이름
	private String accountNumber;	// 계좌 번호
	private long balance;		// 잔액
	
	public Account(String userId, Bank bank, String accountNumber, long balance){
		this.userId = userId;
		this.bank = bank;
		this.accountNumber = accountNumber;
		this.balance = balance;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public Bank getBank() {
		return bank;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public long getBalance() {
		return balance;
	}
	
	public void addBalance(long amount){
		this.balance += amount;
	}
}
