package transaction;

import java.util.ArrayList;

import account.BankAccount;

public class TransactionManager {

	private ArrayList<Transaction> transactionList = new ArrayList<>();

	// 입금
    public boolean Deposit(BankAccount account, int amount) {
        
        // 1. 입력값 검증
        if (account == null || amount <= 0) {
            System.out.println("잘못된 입력입니다. 입금 금액은 1원 이상이어야 합니다.");
            return false;
        }

        // 2. 오버플로우 방지
        long expectedBalance = account.getBalance() + amount;
        if (expectedBalance < 0) {
            System.out.println("계좌의 최대 보관 한도를 초과하여 입금할 수 없습니다.");
            return false;
        }

        // 3. 잔액 증가
        account.addBalance(amount);

        // 4. 거래 기록 저장
        Transaction transaction = new Transaction(
            "입금", 
            amount, 
            account.getBalance()
        );
        transactionList.add(transaction);

        // 5. 완료 메시지 출력
        System.out.println("입금이 완료되었습니다. 현재 잔액: " + account.getBalance() + "원");
        
        return true;
    }

	// 출금
	public boolean WithDraw(BankAccount account, int amount, String passward) {
        

        // 입력값 검증
        if (account == null || amount <= 0) {
            System.out.println("잘못된 입력입니다. 출금 금액은 1원 이상이어야 합니다.");
            return false;
        }

        // 잔액 부족 확인
        if (account.getBalance() < amount) {
            System.out.println("잔액이 부족합니다.");
            return false;
        }

        //잔액 차감
        account.minusBalance(amount);

        // 거래 기록 저장
        Transaction transaction = new Transaction(
            "출금",
            amount,
            account.getBalance()
        );

        transactionList.add(transaction);

        // 완료 메세지 출력
        System.out.println(
            "출금이 완료돠었습니다. 현재 잔액 : "
            + account.getBalance()
            + "원"
        );

        return true;
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
		String transferMemo = memo == null ? "" : memo.trim();
		Transaction transaction = new Transaction(
			"송금",
			amount,
			fromAccount.getBalance(),
			fromAccount.getAccountNumber(),
			toAccount.getAccountNumber(),
			transferMemo
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