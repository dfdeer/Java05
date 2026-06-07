package ui;

import account.UserManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.reflect.InvocationTargetException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class AuthSwingUI {

    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color CARD = Color.WHITE;
    private static final Color PRIMARY = new Color(37, 99, 235);
    private static final Color TEXT = new Color(30, 41, 59);
    private static final Color MUTED = new Color(100, 116, 139);
    private static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Dialog", Font.BOLD, 13);

    private final UserManager userManager;
    private boolean loggedIn;

    public AuthSwingUI(UserManager userManager) {
        this.userManager = userManager;
    }

    public boolean run() {
        loggedIn = false;
        userManager.createUserAccount("admin", "admin", "관리자", "01012345678");

        if (SwingUtilities.isEventDispatchThread()) {
            createDialog().setVisible(true);
            return loggedIn;
        }

        try {
            SwingUtilities.invokeAndWait(() -> createDialog().setVisible(true));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (InvocationTargetException e) {
            JOptionPane.showMessageDialog(
                    null,
                    "로그인 화면을 여는 중 오류가 발생했습니다.",
                    "오류",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return loggedIn;
    }

    private JDialog createDialog() {
        JDialog dialog = new JDialog((java.awt.Frame) null, "Java Bank 로그인", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setContentPane(createContent(dialog));
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        return dialog;
    }

    private JPanel createContent(JDialog dialog) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setPreferredSize(new Dimension(420, 520));
        wrapper.setBackground(BACKGROUND);
        wrapper.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JPanel card = createCard();
        card.setLayout(new BorderLayout(0, 22));
        card.add(createHeader(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("로그인", createLoginPanel(dialog));
        tabs.addTab("회원가입", createSignUpPanel(dialog));
        card.add(tabs, BorderLayout.CENTER);

        wrapper.add(card);
        return wrapper;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(0, 8));
        header.setOpaque(false);

        JLabel title = new JLabel("Java Bank", SwingConstants.CENTER);
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT);

        JLabel subtitle = new JLabel("로그인하거나 새 계정을 만들어 주세요.", SwingConstants.CENTER);
        subtitle.setFont(new Font("Dialog", Font.PLAIN, 13));
        subtitle.setForeground(MUTED);

        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.CENTER);
        return header;
    }

    private JPanel createLoginPanel(JDialog dialog) {
        JPanel formPanel = createFormPanel();
        JTextField idField = createTextField();
        JPasswordField passwordField = createPasswordField();
        JButton loginButton = createPrimaryButton("로그인");

        addFormRow(formPanel, 0, "아이디", idField);
        addFormRow(formPanel, 1, "비밀번호", passwordField);

        loginButton.addActionListener(e -> {
            String userId = idField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (isBlank(userId) || isBlank(password)) {
                showWarning(dialog, "아이디와 비밀번호를 모두 입력해 주세요.");
                return;
            }

            if (userManager.login(userId, password)) {
                loggedIn = true;
                JOptionPane.showMessageDialog(dialog, "로그인 성공!");
                dialog.dispose();
            } else {
                showWarning(dialog, "로그인 실패! 아이디 또는 비밀번호를 확인해 주세요.");
                passwordField.setText("");
            }
        });

        return wrapWithButton(formPanel, loginButton);
    }

    private JPanel createSignUpPanel(JDialog dialog) {
        JPanel formPanel = createFormPanel();
        JTextField idField = createTextField();
        JPasswordField passwordField = createPasswordField();
        JTextField nameField = createTextField();
        JTextField phoneField = createTextField();
        JButton signUpButton = createPrimaryButton("회원가입");

        addFormRow(formPanel, 0, "아이디", idField);
        addFormRow(formPanel, 1, "비밀번호", passwordField);
        addFormRow(formPanel, 2, "이름", nameField);
        addFormRow(formPanel, 3, "전화번호", phoneField);

        signUpButton.addActionListener(e -> {
            String userId = idField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String name = nameField.getText().trim();
            String phoneNumber = phoneField.getText().trim();

            if (isBlank(userId) || isBlank(password) || isBlank(name) || isBlank(phoneNumber)) {
                showWarning(dialog, "모든 항목을 입력해 주세요.");
                return;
            }

            if (userManager.createUserAccount(userId, password, name, phoneNumber)) {
                JOptionPane.showMessageDialog(dialog, "회원가입이 완료되었습니다. 로그인해 주세요.");
                idField.setText("");
                passwordField.setText("");
                nameField.setText("");
                phoneField.setText("");
            } else {
                showWarning(dialog, "회원가입 실패! 아이디 또는 전화번호가 이미 존재합니다.");
            }
        });

        return wrapWithButton(formPanel, signUpButton);
    }

    private JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD);
        card.setPreferredSize(new Dimension(360, 450));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(24, 26, 24, 26)
        ));
        return card;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        return panel;
    }

    private JPanel wrapWithButton(JPanel formPanel, JButton button) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(18, 0, 0, 0));
        buttonPanel.add(button, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void addFormRow(JPanel panel, int row, String labelText, JTextField field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 12, 12);

        JLabel label = new JLabel(labelText);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT);
        panel.add(label, labelConstraints);

        GridBagConstraints fieldConstraints = new GridBagConstraints();
        fieldConstraints.gridx = 1;
        fieldConstraints.gridy = row;
        fieldConstraints.weightx = 1;
        fieldConstraints.fill = GridBagConstraints.HORIZONTAL;
        fieldConstraints.insets = new Insets(0, 0, 12, 0);
        panel.add(field, fieldConstraints);
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleTextField(field);
        return field;
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(190, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225)),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        field.setFont(new Font("Dialog", Font.PLAIN, 13));
    }

    private JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 38));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showWarning(java.awt.Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "확인 필요", JOptionPane.WARNING_MESSAGE);
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}
