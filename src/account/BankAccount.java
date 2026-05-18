package account;

public class BankAccount {
	private String userId;		// 아이디
	private BankList bankName;	// 은행 이름
	private String accountNumber;	// 계좌 번호
	private int balance;		// 잔액

	// 초기 금액 미설정 시 0원으로 자동 설정
	public BankAccount(String inUserId, BankList inBankName, String inAccountNumber){
		this(inUserId, inBankName, inAccountNumber, 0);
	}
	
	public BankAccount(String inUserId, BankList inBankName, String inAccountNumber, int inBalance){
		userId = inUserId;
		bankName = inBankName;
		accountNumber = inAccountNumber;
		balance = inBalance;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public BankList getBankName() {
		return bankName;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public int getBalance() {
		return balance;
	}
}
