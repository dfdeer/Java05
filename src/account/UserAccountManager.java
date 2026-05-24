package account;

import java.util.ArrayList;

public class UserAccountManager {

    private ArrayList<UserAccount> userList = new ArrayList<>();	// 은행 계좌 목록 변수
    private UserAccount currentUser = null;			// 로그인 된 현재 계좌

    // userId 중복 확인
    public boolean isUserIdDuplicated(String userId) {
        for (UserAccount user : userList) {
            if (user.getUserId().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    // phoneNumber 중복 확인
    public boolean isPhoneNumberDuplicated(String phoneNumber) {
        for (UserAccount user : userList) {
            if (user.getPhoneNumber().equals(phoneNumber)) {
                return true;
            }
        }
        return false;
    }

    // 계정 생성
    public boolean createUserAccount(String userId, String password, String inName, String phoneNumber) {
        if (isUserIdDuplicated(userId)) {
            return false;
        }
        if (isPhoneNumberDuplicated(phoneNumber)) {
            return false;
        }

        UserAccount user = new UserAccount(userId, password, inName, phoneNumber);

        userList.add(user);

        return true;
    }

    // 계정 제거
    public void deleteUserAccount() {
        userList.remove(currentUser);
    }

    // 계정 수정 (비밀번호, 이름, 전화번호)
    public void editPassword(String password) {
        currentUser.setPassword(password);
        return;
    }

    public void editName(String name) {
        currentUser.setName(name);
        return;
    }

    public void editPhoneNumber(String phoneNumber) {
        currentUser.setPhoneNumber(phoneNumber);
        return;
    }

    // 로그인
    public boolean login(String userId, String password) {
        for (UserAccount user : userList) {
            if (user.getUserId().equals(userId) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    // 로그아웃
    public void logout() {
        currentUser = null;
    }
}
