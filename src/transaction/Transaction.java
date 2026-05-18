package transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
	private String transactionType;	// 거래 종류 (출금, 입금, 송금 등)
	private long amount;	// 거래 금액
	private LocalDateTime transactionDate;	// 거래일시
	private long AfterBalance; // 거래 후 잔액

	public Transaction(String transactionType, long amount, long AfterBalance) {
		this.transactionDate = LocalDateTime.now();
		this.transactionType = transactionType;
		this.amount = amount;
		this.AfterBalance = AfterBalance;
	}

	public String toString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("2026-MM-dd HH:mm:ss");
		return String.format("[s] %s |금액: %,d원 | 잔액: %,d", transactionDate.format(formatter), transactionType, amount, AfterBalance);
	}
}
