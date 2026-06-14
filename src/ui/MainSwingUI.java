package ui;

import account.Account;
import account.AccountManager;
import account.Bank;
import account.User;
import account.UserManager;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import transaction.InterestCalculator;
import transaction.Transaction;
import transaction.TransactionManager;

public class MainSwingUI extends JFrame {

    private static final Color SIDEBAR_BG  = new Color(245, 247, 251);
    private static final Color PRIMARY     = new Color(24, 95, 165);
    private static final Color TEXT        = new Color(30, 41, 59);
    private static final Color MUTED       = new Color(100, 116, 139);
    private static final Color BORDER      = new Color(226, 232, 240);
    private static final Color SELECTED_BG = new Color(230, 241, 251);

    private final AccountManager am;
    private final TransactionManager tm;
    private final UserManager um;

    // 카드 레이아웃
    private CardLayout cardLayout;
    private JPanel contentPanel;

    // 사이드바 버튼
    private JButton btnAccount;
    private JButton btnTransaction;
    private JButton btnProfile;

    // 계좌 목록 패널
    private DefaultTableModel accountTableModel;

    // 계좌 상세 패널
    private JLabel detailBankLabel;
    private JLabel detailNumberLabel;
    private JLabel detailBalanceLabel;
    private DefaultTableModel txTableModel;
    private Account selectedAccount = null;

    // 거래 패널
    private JComboBox<AccountItem> depositCombo;
    private JComboBox<AccountItem> withdrawCombo;
    private JComboBox<AccountItem> transferFromCombo;
    private JComboBox<AccountItem> transferToCombo;
    private JComboBox<AccountItem> interestCombo;

    // 내 정보 패널
    private JLabel profileIdLabel;
    private JLabel profileNameLabel;
    private JLabel profilePhoneLabel;

    public MainSwingUI(AccountManager am, TransactionManager tm, UserManager um) {
        super("Java Bank");
        this.am = am;
        this.tm = tm;
        this.um = um;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(960, 660));
        setMinimumSize(new Dimension(860, 580));

        setContentPane(buildFrame());
        pack();
        setLocationRelativeTo(null);
        refreshAll();
    }

    // ── 전체 프레임 ────────────────────────────────────────
    private JPanel buildFrame() {
        JPanel frame = new JPanel(new BorderLayout());
        frame.add(buildSidebar(), BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(buildAccountListPanel(), "account");
        contentPanel.add(buildAccountDetailPanel(), "detail");
        contentPanel.add(buildTransactionPanel(), "transaction");
        contentPanel.add(buildProfilePanel(), "profile");
        frame.add(contentPanel, BorderLayout.CENTER);

        return frame;
    }

    // ── 사이드바 ────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER),
            new EmptyBorder(20, 0, 20, 0)
        ));
        sidebar.setPreferredSize(new Dimension(160, 0));

        JLabel title = new JLabel("Java Bank");
        title.setFont(new Font("Dialog", Font.BOLD, 16));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        title.setBorder(new EmptyBorder(0, 18, 16, 18));
        sidebar.add(title);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(BORDER);
        sidebar.add(sep);
        sidebar.add(Box.createVerticalStrut(8));

        btnAccount = navButton("계좌 관리", true);
        btnTransaction = navButton("거래", false);
        btnProfile = navButton("내 정보", false);

        btnAccount.addActionListener(e -> switchPage("account"));
        btnTransaction.addActionListener(e -> switchPage("transaction"));
        btnProfile.addActionListener(e -> switchPage("profile"));

        sidebar.add(btnAccount);
        sidebar.add(btnTransaction);
        sidebar.add(btnProfile);
        sidebar.add(Box.createVerticalGlue());

        JSeparator sep2 = new JSeparator();
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setForeground(BORDER);
        sidebar.add(sep2);
        sidebar.add(Box.createVerticalStrut(8));

        JButton btnLogout = navButton("로그아웃", false);
        btnLogout.setForeground(new Color(163, 45, 45));
        btnLogout.addActionListener(e -> {
            um.logout();
            dispose();
            AuthSwingUI authUI = new AuthSwingUI(um);
            if (authUI.run()) {
                new MainSwingUI(am, tm, um).setVisible(true);
            }
        });
        sidebar.add(btnLogout);

        return sidebar;
    }

    private JButton navButton(String text, boolean active) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Dialog", Font.PLAIN, 13));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        setNavActive(btn, active);
        return btn;
    }

    private void setNavActive(JButton btn, boolean active) {
        if (active) {
            btn.setBackground(SELECTED_BG);
            btn.setForeground(PRIMARY);
            btn.setFont(new Font("Dialog", Font.BOLD, 13));
            btn.setOpaque(true);
        } else {
            btn.setBackground(SIDEBAR_BG);
            btn.setForeground(MUTED);
            btn.setFont(new Font("Dialog", Font.PLAIN, 13));
            btn.setOpaque(true);
        }
    }

    private void switchPage(String page) {
        setNavActive(btnAccount, page.equals("account"));
        setNavActive(btnTransaction, page.equals("transaction"));
        setNavActive(btnProfile, page.equals("profile"));

        if (page.equals("transaction")) refreshCombos();
        if (page.equals("profile")) refreshProfile();

        cardLayout.show(contentPanel, page);
    }

    // ── 계좌 목록 패널 ──────────────────────────────────────
    private JPanel buildAccountListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setBackground(Color.WHITE);

        // 헤더
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel title = pageTitle("계좌 관리");
        JButton createBtn = primaryButton("+ 계좌 생성");
        createBtn.addActionListener(e -> showCreateAccountDialog());

        header.add(title, BorderLayout.WEST);
        header.add(createBtn, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // 테이블
        accountTableModel = new DefaultTableModel(new Object[]{"은행", "계좌번호", "잔액"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(accountTableModel);
        table.setRowHeight(32);
        table.setFont(new Font("Dialog", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row < 0) return;
                    String accNum = (String) accountTableModel.getValueAt(row, 1);
                    selectedAccount = am.findBankAccount(accNum);
                    if (selectedAccount != null) {
                        refreshDetail();
                        cardLayout.show(contentPanel, "detail");
                    }
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        panel.add(scroll, BorderLayout.CENTER);

        JLabel hint = new JLabel("계좌를 더블클릭하면 상세 정보를 볼 수 있습니다.");
        hint.setFont(new Font("Dialog", Font.PLAIN, 11));
        hint.setForeground(MUTED);
        hint.setBorder(new EmptyBorder(8, 0, 0, 0));
        panel.add(hint, BorderLayout.SOUTH);

        return panel;
    }

    private void showCreateAccountDialog() {
        JDialog dialog = new JDialog(this, "계좌 생성", true);
        dialog.setSize(300, 220);
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JComboBox<Bank> bankCombo = new JComboBox<>(Bank.values());
        JTextField balanceField = new JTextField("0");

        panel.add(fieldBlock("은행", bankCombo));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("초기 금액", balanceField));
        panel.add(Box.createVerticalStrut(16));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton cancel = secondaryButton("취소");
        JButton confirm = primaryButton("생성");

        cancel.addActionListener(e -> dialog.dispose());
        confirm.addActionListener(e -> {
            Bank bank = (Bank) bankCombo.getSelectedItem();
            long balance;
            try {
                balance = Long.parseLong(balanceField.getText().trim());
                if (balance < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "올바른 금액을 입력하세요.");
                return;
            }
            String userId = um.getCurrentUser().getUserId();
            if (am.createBankAccount(userId, bank, balance)) {
                refreshAccounts();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "계좌 생성에 실패했습니다.");
            }
        });

        btns.add(cancel);
        btns.add(confirm);
        panel.add(btns);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ── 계좌 상세 패널 ──────────────────────────────────────
    private JPanel buildAccountDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 16));
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setBackground(Color.WHITE);

        // 헤더
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        JButton backBtn = secondaryButton("← 뒤로");
        backBtn.addActionListener(e -> cardLayout.show(contentPanel, "account"));

        JButton deleteBtn = new JButton("계좌 삭제");
        deleteBtn.setFont(new Font("Dialog", Font.BOLD, 13));
        deleteBtn.setForeground(new Color(163, 45, 45));
        deleteBtn.setBackground(new Color(252, 235, 235));
        deleteBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(240, 149, 149)),
            new EmptyBorder(6, 14, 6, 14)
        ));
        deleteBtn.setFocusPainted(false);
        deleteBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        deleteBtn.addActionListener(e -> {
            if (selectedAccount == null) return;
            int res = JOptionPane.showConfirmDialog(this, "계좌를 삭제할까요?", "계좌 삭제", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                am.deleteBankAccount(selectedAccount.getAccountNumber());
                selectedAccount = null;
                refreshAccounts();
                cardLayout.show(contentPanel, "account");
            }
        });

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        headerRight.setOpaque(false);
        headerRight.add(deleteBtn);

        header.add(backBtn, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);

        // 계좌 정보 카드
        JPanel infoCard = new JPanel(new GridLayout(3, 2, 0, 8));
        infoCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(16, 20, 16, 20)
        ));
        infoCard.setBackground(new Color(248, 250, 252));

        detailBankLabel   = infoLabel("");
        detailNumberLabel = infoLabel("");
        detailBalanceLabel = infoLabel("");
        detailBalanceLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        detailBalanceLabel.setForeground(PRIMARY);

        infoCard.add(muted("은행"));      infoCard.add(detailBankLabel);
        infoCard.add(muted("계좌번호"));   infoCard.add(detailNumberLabel);
        infoCard.add(muted("잔액"));      infoCard.add(detailBalanceLabel);

        JPanel northPanel = new JPanel(new BorderLayout(0, 12));
        northPanel.setOpaque(false);
        northPanel.add(header, BorderLayout.NORTH);
        northPanel.add(infoCard, BorderLayout.CENTER);
        panel.add(northPanel, BorderLayout.NORTH);

        // 거래내역 테이블
        txTableModel = new DefaultTableModel(new Object[]{"일시", "종류", "금액", "잔액", "메모"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable txTable = new JTable(txTableModel);
        txTable.setRowHeight(28);
        txTable.setFont(new Font("Dialog", Font.PLAIN, 12));
        txTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        txTable.setShowGrid(false);

        JPanel txPanel = new JPanel(new BorderLayout(0, 8));
        txPanel.setOpaque(false);
        txPanel.add(sectionTitle("거래 내역"), BorderLayout.NORTH);
        txPanel.add(new JScrollPane(txTable), BorderLayout.CENTER);
        panel.add(txPanel, BorderLayout.CENTER);

        return panel;
    }

    private void refreshDetail() {
        if (selectedAccount == null) return;
        detailBankLabel.setText(selectedAccount.getBank().name());
        detailNumberLabel.setText(selectedAccount.getAccountNumber());
        detailBalanceLabel.setText(String.format("%,d원", selectedAccount.getBalance()));
 
        txTableModel.setRowCount(0);
        for (Transaction tx : tm.getTransactionList(selectedAccount.getAccountNumber())) {
            // 출금이면 fromAccount가 내 계좌일 때만 표시
            if (tx.getType().equals("출금") && !selectedAccount.getAccountNumber().equals(tx.getFromAccountNumber())) continue;
            // 입금이면 toAccount가 내 계좌일 때만 표시
            if (tx.getType().equals("입금") && !selectedAccount.getAccountNumber().equals(tx.getToAccountNumber())) continue;

            txTableModel.addRow(new Object[]{
                tx.getDate(), tx.getType(),
                String.format("%,d원", tx.getAmount()),
                String.format("%,d원", tx.getAfterBalance()),
                tx.getMemo()
            });
        }
    }

    // ── 거래 패널 ───────────────────────────────────────────
    private JPanel buildTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setBackground(Color.WHITE);

        panel.add(pageTitle("거래"), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setBorder(new EmptyBorder(12, 0, 0, 0));
        tabs.setFont(new Font("Dialog", Font.PLAIN, 13));

        tabs.addTab("입금", buildDepositTab());
        tabs.addTab("출금", buildWithdrawTab());
        tabs.addTab("송금", buildTransferTab());
        tabs.addTab("이자", buildInterestTab());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDepositTab() {
        JPanel panel = tabPanel();
        depositCombo = accountCombo();
        JTextField amountField = new JTextField();
        JButton btn = primaryButton("입금");

        btn.addActionListener(e -> {
            AccountItem item = (AccountItem) depositCombo.getSelectedItem();
            if (item == null) { showWarning("계좌를 선택하세요."); return; }
            int amount = parseAmount(amountField.getText());
            if (amount <= 0) return;
            if (tm.deposit(item.account, amount)) {
                amountField.setText("");
                refreshAll();
                JOptionPane.showMessageDialog(this, "입금이 완료되었습니다.");
            }
        });

        panel.add(fieldBlock("계좌 선택", depositCombo));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("입금 금액", amountField));
        panel.add(Box.createVerticalStrut(16));
        panel.add(btn);
        return panel;
    }

    private JPanel buildWithdrawTab() {
        JPanel panel = tabPanel();
        withdrawCombo = accountCombo();
        JTextField amountField = new JTextField();
        JButton btn = primaryButton("출금");

        btn.addActionListener(e -> {
            AccountItem item = (AccountItem) withdrawCombo.getSelectedItem();
            if (item == null) { showWarning("계좌를 선택하세요."); return; }
            int amount = parseAmount(amountField.getText());
            if (amount <= 0) return;
            if (tm.withdraw(item.account, amount)) {
                amountField.setText("");
                refreshAll();
                JOptionPane.showMessageDialog(this, "출금이 완료되었습니다.");
            }
        });

        panel.add(fieldBlock("계좌 선택", withdrawCombo));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("출금 금액", amountField));
        panel.add(Box.createVerticalStrut(16));
        panel.add(btn);
        return panel;
    }

    private JPanel buildTransferTab() {
        JPanel panel = tabPanel();
        transferFromCombo = accountCombo();
        transferToCombo   = accountCombo();
        JTextField amountField = new JTextField();
        JTextField memoField   = new JTextField();
        JButton btn = primaryButton("송금");

        transferFromCombo.addActionListener(e -> {
            AccountItem from = (AccountItem) transferFromCombo.getSelectedItem();
            transferToCombo.removeAllItems();
            for (Account acc : am.getAccountList()) {
                if (from == null || !acc.getAccountNumber().equals(from.account.getAccountNumber())) {
                    transferToCombo.addItem(new AccountItem(acc));
                }
            }
        });
        
        btn.addActionListener(e -> {
            AccountItem from = (AccountItem) transferFromCombo.getSelectedItem();
            AccountItem to   = (AccountItem) transferToCombo.getSelectedItem();
            if (from == null || to == null) { showWarning("계좌를 선택하세요."); return; }
            int amount = parseAmount(amountField.getText());
            if (amount <= 0) return;
            if (tm.transfer(from.account, to.account, amount, memoField.getText())) {
                amountField.setText("");
                memoField.setText("");
                refreshAll();
                JOptionPane.showMessageDialog(this, "송금이 완료되었습니다.");
            }else{JOptionPane.showMessageDialog(this, "잔액이 부족합니다.");}
        });

        panel.add(fieldBlock("보내는 계좌", transferFromCombo));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("받는 계좌", transferToCombo));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("금액", amountField));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("메모 (선택)", memoField));
        panel.add(Box.createVerticalStrut(16));
        panel.add(btn);
        return panel;
    }

    private JPanel buildInterestTab() {
        JPanel panel = tabPanel();
        interestCombo = accountCombo();
        JTextField yearField = new JTextField();
        JButton btn = primaryButton("이자 적용");

        btn.addActionListener(e -> {
            AccountItem item = (AccountItem) interestCombo.getSelectedItem();
            if (item == null) { showWarning("계좌를 선택하세요."); return; }
            int years;
            try { years = Integer.parseInt(yearField.getText().trim()); }
            catch (NumberFormatException ex) { showWarning("보유 기간을 숫자로 입력하세요."); return; }
            long before = item.account.getBalance();
            InterestCalculator.applyYearlyInterest(item.account, years);
            long interest = item.account.getBalance() - before;
            yearField.setText("");
            refreshAll();
            JOptionPane.showMessageDialog(this, String.format("이자 %,d원이 적용되었습니다.", interest));
        });

        panel.add(fieldBlock("계좌 선택", interestCombo));
        panel.add(Box.createVerticalStrut(10));
        panel.add(fieldBlock("보유 기간 (년)", yearField));
        panel.add(Box.createVerticalStrut(16));
        panel.add(btn);
        return panel;
    }

    boolean okSign = false;
    // ── 내 정보 패널 ────────────────────────────────────────
    private JPanel buildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(24, 24, 24, 24));
        panel.setBackground(Color.WHITE);

        panel.add(pageTitle("내 정보"), BorderLayout.NORTH);

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(20, 24, 20, 24)
        ));
        card.setBackground(new Color(248, 250, 252));
        card.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));

        profileIdLabel    = infoLabel("");
        profileNameLabel  = infoLabel("");
        profilePhoneLabel = infoLabel("");

        card.add(infoRow("아이디", profileIdLabel));
        card.add(Box.createVerticalStrut(8));
        card.add(infoRow("이름", profileNameLabel));
        card.add(Box.createVerticalStrut(8));
        card.add(infoRow("전화번호", profilePhoneLabel));
        card.add(Box.createVerticalStrut(20));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        btns.setOpaque(false);
        JButton editName  = secondaryButton("이름 변경");
        JButton editPhone = secondaryButton("전화번호 변경");
        JButton editPw    = secondaryButton("비밀번호 변경");

        editName.addActionListener(e -> showEditDialog("이름 변경", "새 이름", false,
            val -> { um.editName(val); refreshProfile(); }));

        editPhone.addActionListener(e -> showEditDialog("전화번호 변경", "새 전화번호", false,
            val -> { 
                if(!val.matches("\\d+")){
                    JOptionPane.showMessageDialog(this, "전화번호는 숫자만 입력해야 합니다.");
                    return;
                }
                if(val.length() != 11 || !val.startsWith("010")){
                    JOptionPane.showMessageDialog(this, "올바른 전화번호 형식이 아닙니다.");
                }else{
                    if(val.equals(profilePhoneLabel.getText())){
                        JOptionPane.showMessageDialog(this, "전화번호가 현재와 같습니다.");
                    } else {
                        okSign = true;
                        um.editPhoneNumber(val);
                        showComplete();
                        refreshProfile();
                    }
                }
            }));

        editPw.addActionListener(e -> showEditDialog("비밀번호 변경", "새 비밀번호", true,
            val -> {
                if(!isValidPassword(val)){
                    JOptionPane.showMessageDialog(this, "비밀번호는 영어, 숫자, 기호를 각각 1개 이상 포함해야 합니다.");
                    return;
                }
                if(val.length() < 8){
                    JOptionPane.showMessageDialog(this, "비밀번호는 최소 8자 이상이어야 합니다.");
                    return;
                }
                if(val.equals(um.getCurrentUser().getPassword())){
                    JOptionPane.showMessageDialog(this, "비밀번호가 현재와 같습니다.");
                } else {
                    okSign = true;
                    um.editPassword(val);
                    showComplete();
                    refreshProfile(); }
            }));

        btns.add(editName);
        btns.add(editPhone);
        btns.add(editPw);
        card.add(btns);

        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.add(card);
        panel.add(wrapper, BorderLayout.CENTER);

        return panel;
    }
    private void showComplete() {
        JOptionPane.showMessageDialog(this, "변경되었습니다.");
    }

    private boolean isValidPassword(String password) {
        return password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*\\p{Punct})[A-Za-z\\d\\p{Punct}]+$");
    }

    private void showEditDialog(String title, String label, boolean isPassword, java.util.function.Consumer<String> onConfirm) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(280, isPassword ? 190 : 160);

        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        if (isPassword) {
            panel.add(fieldBlock("영어, 숫자, 기호를 각각 1개 포함하시오", field));
        }
        panel.add(fieldBlock(label, field));
        panel.add(Box.createVerticalStrut(16));

        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);
        JButton cancel  = secondaryButton("취소");
        JButton confirm = primaryButton("변경");
        field.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirm.doClick();
                }
            }
        });
        cancel.addActionListener(e -> dialog.dispose());
        confirm.addActionListener(e -> {
            String val = field.getText().trim();
            if (val.isEmpty()) { JOptionPane.showMessageDialog(dialog, label + "을 입력하세요."); return; }
            onConfirm.accept(val);
            if(okSign) {
                dialog.dispose();
                //JOptionPane.showMessageDialog(this, "변경되었습니다.");
                okSign = false;
            }
        });

        btns.add(cancel);
        btns.add(confirm);
        panel.add(btns);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ── 새로고침 ────────────────────────────────────────────
    private void refreshAll() {
        refreshAccounts();
        refreshCombos();
        if (selectedAccount != null) refreshDetail();
    }

    private void refreshAccounts() {
        if (accountTableModel == null) return;
        accountTableModel.setRowCount(0);
        User user = um.getCurrentUser();
        if (user == null) return;
        for (Account acc : am.getAccountList()) {
            if (acc.getUserId().equals(user.getUserId())) {
                accountTableModel.addRow(new Object[]{
                    acc.getBank().name(),
                    acc.getAccountNumber(),
                    String.format("%,d원", acc.getBalance())
                });
            }
        }
    }

    private void refreshCombos() {
        ArrayList<Account> userAccounts = new ArrayList<>();
        User user = um.getCurrentUser();
        if (user != null) {
            for (Account acc : am.getAccountList()) {
                if (acc.getUserId().equals(user.getUserId())) userAccounts.add(acc);
            }
        }

        refillCombo(depositCombo, userAccounts);
        refillCombo(withdrawCombo, userAccounts);
        refillCombo(transferFromCombo, userAccounts);
        refillCombo(interestCombo, userAccounts);
    }

    private void refillCombo(JComboBox<AccountItem> combo, java.util.List<Account> list) {
        if (combo == null) return;
        combo.removeAllItems();
        for (Account acc : list) combo.addItem(new AccountItem(acc));
    }

    private void refreshProfile() {
        User user = um.getCurrentUser();
        if (user == null) return;
        profileIdLabel.setText(user.getUserId());
        profileNameLabel.setText(user.getName());
        profilePhoneLabel.setText(user.getPhoneNumber());
    }

    // ── 유틸 ────────────────────────────────────────────────
    private JComboBox<AccountItem> accountCombo() {
        JComboBox<AccountItem> combo = new JComboBox<>();
        combo.setFont(new Font("Dialog", Font.PLAIN, 13));
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 32));
        return combo;
    }

    private JPanel tabPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(20, 4, 20, 4));
        p.setOpaque(false);
        return p;
    }

    private JPanel fieldBlock(String labelText, Component field) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Dialog", Font.BOLD, 12));
        lbl.setForeground(MUTED);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private JPanel infoRow(String labelText, JLabel valueLabel) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 13));
        lbl.setForeground(MUTED);
        lbl.setPreferredSize(new Dimension(80, 0));
        row.add(lbl, BorderLayout.WEST);
        row.add(valueLabel, BorderLayout.CENTER);
        return row;
    }

    private JLabel infoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Dialog", Font.BOLD, 13));
        lbl.setForeground(TEXT);
        return lbl;
    }

    private JLabel muted(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Dialog", Font.PLAIN, 13));
        lbl.setForeground(MUTED);
        return lbl;
    }

    private JLabel pageTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Dialog", Font.BOLD, 20));
        lbl.setForeground(TEXT);
        lbl.setBorder(new EmptyBorder(0, 0, 16, 0));
        return lbl;
    }

    private JLabel sectionTitle(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Dialog", Font.BOLD, 14));
        lbl.setForeground(TEXT);
        return lbl;
    }

    private JButton primaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Dialog", Font.BOLD, 13));
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private JButton secondaryButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Dialog", Font.PLAIN, 13));
        btn.setBackground(Color.WHITE);
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(6, 14, 6, 14)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        return btn;
    }

    private int parseAmount(String text) {
        try {
            int val = Integer.parseInt(text.trim().replace(",", ""));
            if (val <= 0) { showWarning("금액은 1원 이상이어야 합니다."); return -1; }
            return val;
        } catch (NumberFormatException e) {
            showWarning("금액을 숫자로 입력하세요.");
            return -1;
        }
    }

    private void showWarning(String msg) {
        JOptionPane.showMessageDialog(this, msg, "확인 필요", JOptionPane.WARNING_MESSAGE);
    }

    private static class AccountItem {
        final Account account;
        AccountItem(Account account) { this.account = account; }
        public String toString() {
            return String.format("%s %s (%,d원)",
                account.getBank().name(),
                account.getAccountNumber(),
                account.getBalance());
        }
    }
}
