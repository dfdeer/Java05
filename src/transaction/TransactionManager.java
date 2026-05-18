package transaction;

import java.util.ArrayList;

import account.BankAccount;

public class TransactionManager {

	private ArrayList<Transaction> transactionList = new ArrayList<>();

	// 입금
	void Deposit() {

	}

	// 출금
	void WithDraw() {

	}

	// 송금
	public boolean Transfer(
		BankAccount fromAccount,
		BankAccount toAccount,
		int amount,
		int date,
		String password,
		String memo
	) {

		// 입력값 검증
		if (fromAccount == null || toAccount == null || amount <= 0) {
			System.out.println("잘못된 입력입니다.");
			return false;
		}

		// 같은 계좌 송금 방지
		if (
			fromAccount.getAccountNumber()
				.equals(toAccount.getAccountNumber())
		) {
			System.out.println("같은 계좌로 송금할 수 없습니다.");
			return false;
		}

		// 출금자의 잔액 확인
		if (fromAccount.getBalance() < amount) {
			System.out.println("잔액이 부족합니다.");
			return false;
		}

		// 출금자 잔액 차감
		fromAccount.setBalance(
			fromAccount.getBalance() - amount
		);

		// 수취인 잔액 증가
		toAccount.setBalance(
			toAccount.getBalance() + amount
		);

		// 송금 기록 저장
		Transaction transaction = new Transaction(
			"송금",
			amount,
			fromAccount.getBalance()
		);

		transactionList.add(transaction);

		System.out.println("송금이 완료되었습니다.");

		return true;
	}

	// 거래 기록 조회
	public ArrayList<Transaction> getTransactionList() {
		return transactionList;
	}
}