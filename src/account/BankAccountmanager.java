package account;

import java.util.ArrayList;

public class BankAccountmanager {
	
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
	public boolean createBankAccount(String inUserId, BankList inBankName, int inBalance) {
		String inAccountNumber = accountCounter();
		BankAccount account = new BankAccount(inUserId, inBankName, inAccountNumber, inBalance);
		
		accountList.add(account);
		
		return true;
	}
	
	// 계정 제거
	public void deleteBankAccount() {
		accountList.remove(currentAccount);
	}
	
	// 현재 계좌 선택
	public void selectBankAccount(String inAccountNumber) {		
		for (BankAccount account : accountList) {
			if (account.getAccountNumber() == inAccountNumber) {
				currentAccount = account;
			}
		}
	}
}
