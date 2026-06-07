package ui;

import account.Account;
import account.AccountManager;
import account.Bank;
import account.User;
import account.UserManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import transaction.InterestCalculator;
import transaction.Transaction;
import transaction.TransactionManager;

public class MainSwingUI extends JFrame {

    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color CARD = Color.WHITE;
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color TEXT = new Color(30, 41, 59);
    private static final Color MUTED = new Color(100, 116, 139);

    private final AccountManager accountManager;
    private final TransactionManager transactionManager;
    private final UserManager userManager;

    private final DefaultTableModel accountTableModel = new DefaultTableModel(
            new Object[]{"은행", "계좌번호", "잔액"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final DefaultTableModel transactionTableModel = new DefaultTableModel(
            new Object[]{"거래 내역"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable accountTable;
    private JComboBox<AccountItem> sourceAccountCombo;
    private JComboBox<AccountItem> targetAccountCombo;
    private JLabel userTitleLabel;
    private JLabel balanceLabel;
    private JLabel profileLabel;

    public MainSwingUI(AccountManager accountManager, TransactionManager transactionManager, UserManager userManager) {
        super("Java Bank");
        this.accountManager = accountManager;
        this.transactionManager = transactionManager;
        this.userManager = userManager;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(960, 680));
        setMinimumSize(new Dimension(900, 620));
        setContentPane(createContent());
        refreshAll();
        pack();
        setLocationRelativeTo(null);
    }

    public static void show(AccountManager accountManager, TransactionManager transactionManager, UserManager userManager) {
        SwingUtilities.invokeLater(() -> new MainSwingUI(accountManager, transactionManager, userManager).setVisible(true));
    }

    private JPanel createContent() {
        JPanel root = new JPanel(new BorderLayout(0, 14));
        root.setBackground(BACKGROUND);
        root.setBorder(BorderFactory.createEmptyBorder(18, 22, 22, 22));

        root.add(createTopBar(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("계좌 관리", createAccountPanel());
        tabs.addTab("거래", createTransactionPanel());
        tabs.addTab("내 정보", createProfilePanel());
        root.add(tabs, BorderLayout.CENTER);

        return root;
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);

        userTitleLabel = new JLabel();
        userTitleLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        userTitleLabel.setForeground(TEXT);

        JButton logoutButton = createSecondaryButton("로그아웃");
        logoutButton.addActionListener(e -> {
            userManager.logout();
            dispose();
        });

        topBar.add(userTitleLabel, BorderLayout.WEST);
        topBar.add(logoutButton, BorderLayout.EAST);
        return topBar;
    }

    private JPanel createAccountPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);

        JPanel tableCard = createCard();
        tableCard.setLayout(new BorderLayout(0, 12));

        accountTable = new JTable(accountTableModel);
        accountTable.setRowHeight(28);
        accountTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableCard.add(sectionTitle("내 계좌"), BorderLayout.NORTH);
        tableCard.add(new JScrollPane(accountTable), BorderLayout.CENTER);

        JPanel formCard = createCard();
        formCard.setPreferredSize(new Dimension(280, 0));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        JComboBox<Bank> bankCombo = new JComboBox<>(visibleBanks());
        JTextField balanceField = createTextField();
        JButton createButton = createPrimaryButton("계좌 생성");
        JButton deleteButton = createSecondaryButton("선택 계좌 삭제");

        formCard.add(sectionTitle("새 계좌"));
        formCard.add(Box.createVerticalStrut(14));
        formCard.add(fieldBlock("은행", bankCombo));
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(fieldBlock("초기 금액", balanceField));
        formCard.add(Box.createVerticalStrut(16));
        formCard.add(createButton);
        formCard.add(Box.createVerticalStrut(10));
        formCard.add(deleteButton);

        createButton.addActionListener(e -> {
            User user = userManager.getCurrentUser();
            long balance = parseLong(balanceField.getText(), "초기 금액");
            Bank bank = (Bank) bankCombo.getSelectedItem();

            if (user == null || balance < 0 || bank == null) {
                return;
            }

            if (accountManager.createBankAccount(user.getUserId(), bank, balance)) {
                balanceField.setText("");
                refreshAll();
                JOptionPane.showMessageDialog(this, "계좌가 생성되었습니다.");
            } else {
                showWarning("계좌를 생성할 수 없습니다.");
            }
        });

        deleteButton.addActionListener(e -> {
            int row = accountTable.getSelectedRow();
            if (row < 0) {
                showWarning("삭제할 계좌를 선택해 주세요.");
                return;
            }

            String accountNumber = String.valueOf(accountTableModel.getValueAt(row, 1));
            int result = JOptionPane.showConfirmDialog(
                    this,
                    accountNumber + " 계좌를 삭제할까요?",
                    "계좌 삭제",
                    JOptionPane.YES_NO_OPTION
            );
            if (result == JOptionPane.YES_OPTION && accountManager.deleteBankAccount(accountNumber)) {
                refreshAll();
            }
        });

        panel.add(tableCard, BorderLayout.CENTER);
        panel.add(formCard, BorderLayout.EAST);
        return panel;
    }

    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(14, 0));
        panel.setOpaque(false);

        JPanel actionCard = createCard();
        actionCard.setPreferredSize(new Dimension(330, 0));
        actionCard.setLayout(new BoxLayout(actionCard, BoxLayout.Y_AXIS));

        sourceAccountCombo = new JComboBox<>();
        targetAccountCombo = new JComboBox<>();
        balanceLabel = new JLabel();
        balanceLabel.setForeground(MUTED);

        JTextField amountField = createTextField();
        JTextField memoField = createTextField();
        JTextField yearField = createTextField();

        JButton depositButton = createPrimaryButton("입금");
        JButton withdrawButton = createSecondaryButton("출금");
        JButton transferButton = createPrimaryButton("송금");
        JButton interestButton = createSecondaryButton("이자 적용");

        sourceAccountCombo.addActionListener(e -> refreshAccountCombos(false));

        actionCard.add(sectionTitle("거래 실행"));
        actionCard.add(Box.createVerticalStrut(14));
        actionCard.add(fieldBlock("사용 계좌", sourceAccountCombo));
        actionCard.add(Box.createVerticalStrut(6));
        actionCard.add(balanceLabel);
        actionCard.add(Box.createVerticalStrut(12));
        actionCard.add(fieldBlock("금액", amountField));
        actionCard.add(Box.createVerticalStrut(12));
        actionCard.add(buttonRow(depositButton, withdrawButton));
        actionCard.add(Box.createVerticalStrut(18));
        actionCard.add(fieldBlock("받는 계좌", targetAccountCombo));
        actionCard.add(Box.createVerticalStrut(10));
        actionCard.add(fieldBlock("메모", memoField));
        actionCard.add(Box.createVerticalStrut(12));
        actionCard.add(transferButton);
        actionCard.add(Box.createVerticalStrut(18));
        actionCard.add(fieldBlock("보유 기간(년)", yearField));
        actionCard.add(Box.createVerticalStrut(10));
        actionCard.add(interestButton);

        depositButton.addActionListener(e -> runAmountTransaction(
                amountField,
                (account, amount) -> transactionManager.deposit(account, amount),
                "입금에 실패했습니다."
        ));

        withdrawButton.addActionListener(e -> runAmountTransaction(
                amountField,
                (account, amount) -> transactionManager.withdraw(account, amount),
                "출금에 실패했습니다. 잔액을 확인해 주세요."
        ));

        transferButton.addActionListener(e -> {
            Account source = selectedSourceAccount();
            Account target = selectedTargetAccount();
            int amount = parseInt(amountField.getText(), "금액");

            if (source == null) {
                showWarning("송금할 계좌를 선택해 주세요.");
                return;
            }
            if (target == null) {
                showWarning("받는 계좌를 선택해 주세요.");
                return;
            }
            if (amount <= 0) {
                return;
            }

            if (transactionManager.transfer(source, target, amount, memoField.getText())) {
                amountField.setText("");
                memoField.setText("");
                refreshAll();
            } else {
                showWarning("송금에 실패했습니다. 계좌와 잔액을 확인해 주세요.");
            }
        });

        interestButton.addActionListener(e -> {
            Account account = selectedSourceAccount();
            int years = parseInt(yearField.getText(), "보유 기간");

            if (account == null) {
                showWarning("이자를 적용할 계좌를 선택해 주세요.");
                return;
            }
            if (years < 0) {
                return;
            }

            long before = account.getBalance();
            InterestCalculator.applyYearlyInterest(account, years);
            refreshAll();
            JOptionPane.showMessageDialog(this, "적용 이자: " + formatMoney(account.getBalance() - before));
        });

        JPanel historyCard = createCard();
        historyCard.setLayout(new BorderLayout(0, 12));

        JTable transactionTable = new JTable(transactionTableModel);
        transactionTable.setRowHeight(28);
        historyCard.add(sectionTitle("거래 내역"), BorderLayout.NORTH);
        historyCard.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        panel.add(actionCard, BorderLayout.WEST);
        panel.add(historyCard, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JPanel card = createCard();
        card.setPreferredSize(new Dimension(430, 0));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        profileLabel = new JLabel();
        profileLabel.setForeground(MUTED);
        profileLabel.setFont(new Font("Dialog", Font.PLAIN, 13));

        JTextField passwordField = createTextField();
        JTextField nameField = createTextField();
        JTextField phoneField = createTextField();

        JButton passwordButton = createSecondaryButton("비밀번호 변경");
        JButton nameButton = createSecondaryButton("이름 변경");
        JButton phoneButton = createSecondaryButton("전화번호 변경");

        card.add(sectionTitle("내 정보"));
        card.add(Box.createVerticalStrut(10));
        card.add(profileLabel);
        card.add(Box.createVerticalStrut(20));
        card.add(fieldBlock("새 비밀번호", passwordField));
        card.add(Box.createVerticalStrut(8));
        card.add(passwordButton);
        card.add(Box.createVerticalStrut(16));
        card.add(fieldBlock("새 이름", nameField));
        card.add(Box.createVerticalStrut(8));
        card.add(nameButton);
        card.add(Box.createVerticalStrut(16));
        card.add(fieldBlock("새 전화번호", phoneField));
        card.add(Box.createVerticalStrut(8));
        card.add(phoneButton);

        passwordButton.addActionListener(e -> {
            String password = passwordField.getText().trim();
            if (password.isEmpty()) {
                showWarning("새 비밀번호를 입력해 주세요.");
                return;
            }
            userManager.editPassword(password);
            passwordField.setText("");
            JOptionPane.showMessageDialog(this, "비밀번호가 변경되었습니다.");
        });

        nameButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                showWarning("새 이름을 입력해 주세요.");
                return;
            }
            userManager.editName(name);
            nameField.setText("");
            refreshAll();
        });

        phoneButton.addActionListener(e -> {
            String phone = phoneField.getText().trim();
            if (phone.isEmpty()) {
                showWarning("새 전화번호를 입력해 주세요.");
                return;
            }
            userManager.editPhoneNumber(phone);
            phoneField.setText("");
            refreshAll();
        });

        panel.add(card, BorderLayout.WEST);
        return panel;
    }

    private void runAmountTransaction(JTextField amountField, AccountTransaction transaction, String failureMessage) {
        Account account = selectedSourceAccount();
        int amount = parseInt(amountField.getText(), "금액");

        if (account == null) {
            showWarning("거래할 계좌를 선택해 주세요.");
            return;
        }
        if (amount <= 0) {
            return;
        }

        if (transaction.execute(account, amount)) {
            amountField.setText("");
            refreshAll();
        } else {
            showWarning(failureMessage);
        }
    }

    private void refreshAll() {
        refreshUser();
        refreshAccounts();
        refreshAccountCombos(true);
        refreshTransactions();
    }

    private void refreshUser() {
        User user = userManager.getCurrentUser();
        if (user == null) {
            userTitleLabel.setText("Java Bank");
            profileLabel.setText("로그인 정보가 없습니다.");
            return;
        }

        userTitleLabel.setText(user.getName() + "님, 안녕하세요");
        profileLabel.setText(String.format(
                "<html>아이디: %s<br>이름: %s<br>전화번호: %s</html>",
                user.getUserId(),
                user.getName(),
                user.getPhoneNumber()
        ));
    }

    private void refreshAccounts() {
        accountTableModel.setRowCount(0);
        for (Account account : currentUserAccounts()) {
            accountTableModel.addRow(new Object[]{
                    account.getBank(),
                    account.getAccountNumber(),
                    formatMoney(account.getBalance())
            });
        }
    }

    private void refreshAccountCombos(boolean rebuildSource) {
        Account selected = selectedSourceAccount();

        if (rebuildSource) {
            DefaultComboBoxModel<AccountItem> model = new DefaultComboBoxModel<>();
            for (Account account : currentUserAccounts()) {
                model.addElement(new AccountItem(account));
            }
            sourceAccountCombo.setModel(model);
            selectAccount(sourceAccountCombo, selected);
        }

        Account source = selectedSourceAccount();
        balanceLabel.setText(source == null ? "선택된 계좌가 없습니다." : "현재 잔액: " + formatMoney(source.getBalance()));

        DefaultComboBoxModel<AccountItem> targetModel = new DefaultComboBoxModel<>();
        for (Account account : accountManager.getAccountList()) {
            if (source == null || !source.getAccountNumber().equals(account.getAccountNumber())) {
                targetModel.addElement(new AccountItem(account));
            }
        }
        targetAccountCombo.setModel(targetModel);
    }

    private void refreshTransactions() {
        transactionTableModel.setRowCount(0);
        if (transactionManager.getTransactionList().isEmpty()) {
            transactionTableModel.addRow(new Object[]{"거래 내역이 없습니다."});
            return;
        }

        for (Transaction transaction : transactionManager.getTransactionList()) {
            transactionTableModel.addRow(new Object[]{transaction.toString()});
        }
    }

    private ArrayList<Account> currentUserAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        User user = userManager.getCurrentUser();
        if (user == null) {
            return accounts;
        }

        for (Account account : accountManager.getAccountList()) {
            if (user.getUserId().equals(account.getUserId())) {
                accounts.add(account);
            }
        }
        return accounts;
    }

    private Account selectedSourceAccount() {
        if (sourceAccountCombo == null) {
            return null;
        }
        AccountItem item = (AccountItem) sourceAccountCombo.getSelectedItem();
        return item == null ? null : item.account;
    }

    private Account selectedTargetAccount() {
        if (targetAccountCombo == null) {
            return null;
        }
        AccountItem item = (AccountItem) targetAccountCombo.getSelectedItem();
        return item == null ? null : item.account;
    }

    private void selectAccount(JComboBox<AccountItem> comboBox, Account account) {
        if (account == null) {
            return;
        }

        for (int i = 0; i < comboBox.getItemCount(); i++) {
            AccountItem item = comboBox.getItemAt(i);
            if (item.account.getAccountNumber().equals(account.getAccountNumber())) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private Bank[] visibleBanks() {
        ArrayList<Bank> banks = new ArrayList<>();
        for (Bank bank : Bank.values()) {
            if (bank != Bank.NULL) {
                banks.add(bank);
            }
        }
        return banks.toArray(new Bank[0]);
    }

    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(22, 24, 22, 24)
        ));
        return card;
    }

    private JPanel fieldBlock(String labelText, java.awt.Component field) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 64));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Dialog", Font.BOLD, 13));
        label.setForeground(TEXT);

        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buttonRow(JButton first, JButton second) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        panel.add(first);
        panel.add(second);
        return panel;
    }

    private JLabel sectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.BOLD, 18));
        label.setForeground(TEXT);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setPreferredSize(new Dimension(190, 34));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return field;
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(132, 38));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        styleButton(button);
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(132, 38));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        button.setBackground(Color.WHITE);
        button.setForeground(TEXT);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        styleButton(button);
        return button;
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setFont(new Font("Dialog", Font.BOLD, 13));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private int parseInt(String text, String label) {
        long value = parseLong(text, label);
        if (value > Integer.MAX_VALUE) {
            showWarning(label + "이 너무 큽니다.");
            return -1;
        }
        return (int) value;
    }

    private long parseLong(String text, String label) {
        String normalized = text.trim().replace(",", "");
        if (normalized.isEmpty()) {
            showWarning(label + "을 입력해 주세요.");
            return -1;
        }

        try {
            long value = Long.parseLong(normalized);
            if (value < 0) {
                showWarning(label + "은 0 이상이어야 합니다.");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            showWarning(label + "은 숫자로 입력해 주세요.");
            return -1;
        }
    }

    private String formatMoney(long amount) {
        return String.format("%,d원", amount);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "확인 필요", JOptionPane.WARNING_MESSAGE);
    }

    private static class AccountItem {
        private final Account account;

        AccountItem(Account account) {
            this.account = account;
        }

        @Override
        public String toString() {
            return account.getBank() + " " + account.getAccountNumber() + " (" + String.format("%,d원", account.getBalance()) + ")";
        }
    }

    private interface AccountTransaction {
        boolean execute(Account account, int amount);
    }
}
