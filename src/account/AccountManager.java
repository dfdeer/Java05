package account;

import java.util.ArrayList;

public class AccountManager {
	
	private ArrayList<Account> accountList = new ArrayList<>();	// 은행 계좌 목록 변수
	private String accountNumber = "1000000";
	
	// 계좌 번호 업데이트
	private String accountCounter(){
			int countedAccount = Integer.parseInt(accountNumber);
			countedAccount++;
			accountNumber = String.valueOf(countedAccount);
			return accountNumber;
		}
		
	// 계좌 생성
	public boolean createBankAccount(String userId, Bank bank, long balance) {
		if (userId == null || userId.isEmpty()) {
			return false;
		}
		if (bank == null) {
			return false;
		}
		if (balance < 0) {
			System.out.println("잔액은 0원 이상이어야 합니다.");
			return false;
		}
		
		String accountNumber = accountCounter();
		Account account = new Account(userId, bank, accountNumber, balance);
		
		accountList.add(account);
		
		return true;
	}
	
	// 계정 제거
	public boolean deleteBankAccount(String accountNumber) {
		Account account = findBankAccount(accountNumber);
		if(account == null) return false;
		
		accountList.remove(account);
		
		return true;
	}
	
	// 현재 계좌 선택
	public Account findBankAccount(String inAccountNumber) {		
		for (Account account : accountList) {
			if (account.getAccountNumber().equals(inAccountNumber)) {
				return account;
			}
		}
		return null;
	}
	
	public ArrayList<Account> getAccountList() {
	    return accountList;
	}
}