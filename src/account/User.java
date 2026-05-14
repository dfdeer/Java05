package account;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    // 회원 정보가 저장되는 파일입니다.
    private static final Path MEMBERS_FILE = Path.of("members.txt");

	private String Id;		// 아이디
	private String password;	// 비밀번호
	private String name;		// 이름
	private int phoneNumber;	// 전화번호

	// 로그인 시 입력한 아이디와 비밀번호가 저장된 회원 정보와 일치하는지 확인하는 메서드입니다.
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
	@Override
        public String toString() {
            return "name=" + name + ", id=" + Id + ", password=" + password;
    }
}
