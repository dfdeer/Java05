package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class signSystem {

    // 회원 정보가 저장되는 파일입니다.
    private static final Path MEMBERS_FILE = Path.of("members.txt");

    // 화면에서 반복해서 사용하는 색상과 폰트 값입니다.
    private static final Color BACKGROUND = new Color(245, 247, 251);
    private static final Color CARD = Color.WHITE;
    private static final Color PRIMARY = new Color(45, 105, 255);
    private static final Color TEXT = new Color(34, 40, 49);
    private static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 24);
    private static final Font LABEL_FONT = new Font("Dialog", Font.BOLD, 13);

    // 로그인 성공 여부를 다른 클래스에서 확인할 수 있도록 저장합니다.
    public static boolean login = false;

    // 회원가입 창을 열고 입력한 회원 정보를 파일에 저장합니다.
    public static void signUP_() {
        JFrame frame = createFrame("회원가입", 340, 500);

        JTextField nameField = createTextField();
        JTextField idField = createTextField();
        JPasswordField passwordField = createPasswordField();
        JButton registerButton = createPrimaryButton("가입하기");

        // 이름, 아이디, 비밀번호 입력칸을 한 줄씩 폼에 추가합니다.
        JPanel formPanel = createFormPanel();
        addFormRow(formPanel, 0, "이름", nameField);
        addFormRow(formPanel, 1, "아이디", idField);
        addFormRow(formPanel, 2, "비밀번호", passwordField);

        // 가입하기 버튼을 누르면 입력값 검사와 아이디 중복 검사를 한 뒤 저장합니다.
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (!isValidInput(name, id, password)) {
                JOptionPane.showMessageDialog(frame, "입력값을 확인해주세요.");
                return;
            }

            if (isIdDuplicated(id)) {
                JOptionPane.showMessageDialog(frame, "이미 존재하는 아이디입니다.");
                return;
            }

            // 비밀번호는 원문 대신 SHA-256 해시값으로 저장합니다.
            String hashedPassword = hashPassword(password);
            Member member = new Member(name, id, hashedPassword);
            saveMemberToFile(member);

            JOptionPane.showMessageDialog(frame, "회원가입이 완료되었습니다.");
            frame.dispose();
        });

        // 입력 폼과 버튼을 카드 형태로 묶어 화면에 보여줍니다.
        JPanel card = createCard("회원가입", "사용할 계정 정보를 입력해주세요.");
        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonArea(registerButton), BorderLayout.SOUTH);

        frame.add(wrapCard(card));
        frame.setVisible(true);
    }

    // 로그인 창을 열고 입력한 아이디와 비밀번호가 저장된 정보와 일치하는지 확인합니다.
    public static void signIN_() {
        JFrame loginFrame = createFrame("로그인", 340, 500);

        JTextField loginIdField = createTextField();
        JPasswordField loginPasswordField = createPasswordField();
        JButton loginButton = createPrimaryButton("로그인");

        JPanel formPanel = createFormPanel();
        addFormRow(formPanel, 0, "아이디", loginIdField);
        addFormRow(formPanel, 1, "비밀번호", loginPasswordField);

        // 로그인 버튼을 누르면 입력 비밀번호를 해시한 뒤 파일의 회원 정보와 비교합니다.
        loginButton.addActionListener(e -> {
            String id = loginIdField.getText().trim();
            String password = new String(loginPasswordField.getPassword());

            if (validateLogin(id, password)) {
                JOptionPane.showMessageDialog(loginFrame, "로그인 성공!");
                loginFrame.dispose();
                login = true;
            } else {
                JOptionPane.showMessageDialog(loginFrame, "로그인 실패! 아이디와 비밀번호를 확인해주세요.");
            }
        });

        JPanel card = createCard("로그인", "아이디와 비밀번호를 입력해주세요.");
        card.add(formPanel, BorderLayout.CENTER);
        card.add(createButtonArea(loginButton), BorderLayout.SOUTH);

        loginFrame.add(wrapCard(card));
        loginFrame.setVisible(true);
    }

    // 회원가입 창과 로그인 창이 공통으로 사용하는 JFrame 기본 설정입니다.
    private static JFrame createFrame(String title, int width, int height) {
        JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(BACKGROUND);
        return frame;
    }

    // 카드 패널을 화면 가운데에 배치하고 바깥 여백을 줍니다.
    private static JPanel wrapCard(JPanel card) {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BACKGROUND);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));
        wrapper.add(card);
        return wrapper;
    }

    // 제목, 설명, 내용 영역을 가진 흰색 카드 패널을 만듭니다.
    private static JPanel createCard(String title, String description) {
        JPanel card = new JPanel(new BorderLayout(0, 22));
        card.setBackground(CARD);
        card.setPreferredSize(new Dimension(340, 500));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(224, 229, 238)),
                new EmptyBorder(24, 28, 24, 28)
        ));

        JPanel header = new JPanel(new BorderLayout(0, 8));
        header.setOpaque(false);

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT);

        JLabel descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
        descriptionLabel.setForeground(new Color(108, 117, 125));

        header.add(titleLabel, BorderLayout.NORTH);
        header.add(descriptionLabel, BorderLayout.CENTER);
        card.add(header, BorderLayout.NORTH);

        return card;
    }

    // 입력 항목들을 GridBagLayout으로 정렬하기 위한 폼 패널입니다.
    private static JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        return formPanel;
    }

    // 라벨과 입력칸을 한 줄로 추가합니다. row 값으로 줄 위치를 정합니다.
    private static void addFormRow(JPanel panel, int row, String labelText, JTextField field) {
        GridBagConstraints labelConstraints = new GridBagConstraints();
        labelConstraints.gridx = 0;
        labelConstraints.gridy = row;
        labelConstraints.anchor = GridBagConstraints.WEST;
        labelConstraints.insets = new Insets(0, 0, 12, 14);

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

    // 버튼을 카드 아래쪽에 꽉 차게 배치하기 위한 패널입니다.
    private static JPanel createButtonArea(JButton button) {
        JPanel buttonArea = new JPanel(new BorderLayout());
        buttonArea.setOpaque(false);
        buttonArea.add(button, BorderLayout.CENTER);
        return buttonArea;
    }

    // 일반 텍스트 입력칸을 만들고 공통 스타일을 적용합니다.
    private static JTextField createTextField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    // 비밀번호 입력칸을 만들고 공통 스타일을 적용합니다.
    private static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        styleTextField(field);
        return field;
    }

    // 입력칸의 크기, 테두리, 글꼴을 통일합니다.
    private static void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(190, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218)),
                new EmptyBorder(6, 10, 6, 10)
        ));
        field.setFont(new Font("Dialog", Font.PLAIN, 13));
    }

    // 파란색 기본 버튼을 만들어 로그인/회원가입 버튼 스타일을 통일합니다.
    private static JButton createPrimaryButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 38));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // 입력한 아이디와 비밀번호가 members.txt에 저장된 정보와 일치하는지 확인합니다.
    private static boolean validateLogin(String id, String password) {
        String hashedPassword = hashPassword(password);

        try (BufferedReader br = Files.newBufferedReader(MEMBERS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("id=" + id + ",") && line.contains("password=" + hashedPassword)) {
                    return true;
                }
                if (line.contains("아이디: " + id + ",") && line.contains("비밀번호: " + hashedPassword)) {
                    return true;
                }
                if (line.contains(id) && line.contains(hashedPassword)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 회원가입한 회원 정보를 UTF-8 형식으로 members.txt 파일에 추가 저장합니다.
    private static void saveMemberToFile(Member member) {
        try (BufferedWriter bw = Files.newBufferedWriter(MEMBERS_FILE, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(member.toString());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 이미 같은 아이디가 저장되어 있는지 파일을 한 줄씩 읽으며 검사합니다.
    private static boolean isIdDuplicated(String id) {
        try (BufferedReader br = Files.newBufferedReader(MEMBERS_FILE, StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("id=" + id + ",") || line.contains("아이디: " + id + ",") || line.contains(id + ",")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 이름, 아이디, 비밀번호가 비어 있지 않고 아이디 형식이 올바른지 확인합니다.
    private static boolean isValidInput(String name, String id, String password) {
        if (name.isEmpty() || id.isEmpty() || password.isEmpty()) {
            return false;
        }

        return id.matches("[a-zA-Z][a-zA-Z0-9]*");
    }

    // SHA-256 알고리즘으로 비밀번호를 해시 문자열로 변환합니다.
    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // 파일에 저장할 회원 한 명의 정보를 담는 내부 클래스입니다.
    public static class Member {
        private String name;
        private String id;
        private String password;

        public Member(String name, String id, String password) {
            this.name = name;
            this.id = id;
            this.password = password;
        }

        @Override
        public String toString() {
            return "name=" + name + ", id=" + id + ", password=" + password;
        }
    }
}
