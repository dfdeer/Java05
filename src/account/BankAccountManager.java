package account;

import java.util.ArrayList;

public class BankAccountManager {
	
	private ArrayList<BankAccount> accountList = new ArrayList<>();	// 은행 계좌 목록 변수
	private BankAccount currentAccount = null;
	private String accountNumber = "1000000";
	
	// 계좌 번호 업데이트
	public String accountCounter(){
			int countedAccount = Integer.parseInt(accountNumber);
			countedAccount++;
			accountNumber = String.valueOf(countedAccount);
			return accountNumber;
		}
		
	// 계좌 생성
	public boolean createBankAccount(String userId, Bank bank, long balance) {
		String accountNumber = accountCounter();
		BankAccount account = new BankAccount(userId, bank, accountNumber, balance);
		
		accountList.add(account);
		
		return true;
	}
	
	// 계정 제거
	public void deleteBankAccount() {
		accountList.remove(currentAccount);
	}
	
	// 현재 계좌 선택
	public void selectBankAccount(String accountNumber) {		
		for (BankAccount account : accountList) {
			if (account.getAccountNumber().equals(accountNumber)) {
				currentAccount = account;
			}
		}
	}
}
