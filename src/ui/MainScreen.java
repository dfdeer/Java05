package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainScreen extends JFrame {

    // 프로그램에서 메인 화면을 열 때 사용하는 시작 메서드입니다.
    public static void run() {
        MainScreen m = new MainScreen();
        m.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public MainScreen() {
        super("Java bank");
        setSize(450, 800);
        setLayout(new BorderLayout());

        // 메인 화면 상단에 보여줄 로고 이미지를 불러오고 크기를 조정합니다.
        ImageIcon icon = new ImageIcon("image/javabank.png");
        icon = imageSetSize(icon, 384, 216);
        JLabel imageLabel = new JLabel(icon, JLabel.CENTER);

        JButton loginButton = new JButton("로그인");
        JButton registerButton = new JButton("회원가입");

        // 로그인/회원가입 버튼을 이미지 아래에 나란히 배치합니다.
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // 이미지와 버튼을 한 패널에 담아 화면 위쪽에 배치합니다.
        JPanel contentPanel = new JPanel(new BorderLayout(0, 20));
        contentPanel.setBorder(new EmptyBorder(200, 20, 20, 20));
        contentPanel.add(imageLabel, BorderLayout.NORTH);
        contentPanel.add(buttonPanel, BorderLayout.CENTER);

        // 로그인 버튼 클릭 시 로그인 창을 엽니다.
        class LoginActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                //JOptionPane.showMessageDialog(menu.this, "xtestx");
                signSystem Sign = new signSystem();
                Sign.signIN_();
            }
        }

        loginButton.addActionListener(new LoginActionListener());

        // 회원가입 버튼 클릭 시 회원가입 창을 엽니다.
        class RegisterActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                signSystem Sign = new signSystem();
                Sign.signUP_();
            }
        }

        registerButton.addActionListener(new RegisterActionListener());

        add(contentPanel, BorderLayout.NORTH);
        setVisible(true);
    }

    // ImageIcon을 원하는 크기로 줄여서 화면에 맞게 보여줍니다.
    ImageIcon imageSetSize(ImageIcon icon, int width, int height) {
        java.awt.Image img = icon.getImage();
        java.awt.Image resizedImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
