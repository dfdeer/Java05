package account;

import java.util.ArrayList;

public class UserAccountManager {

    private ArrayList<UserAccount> userList = new ArrayList<>();	// 은행 계좌 목록 변수
    private UserAccount currentUser = null;			// 로그인 된 현재 계좌

    // userId 중복 확인
    public boolean isUserIdDuplicated(String inUserId) {
        for (UserAccount user : userList) {
            if (user.getUserId().equals(inUserId)) {
                return true;
            }
        }
        return false;
    }

    // phoneNumber 중복 확인
    public boolean isPhoneNumberDuplicated(String inPhoneNumber) {
        for (UserAccount user : userList) {
            if (user.getPhoneNumber().equals(inPhoneNumber)) {
                return true;
            }
        }
        return false;
    }

    // 계정 생성
    public boolean createUserAccount(String inUserId, String inPassword, String inName, String inPhoneNumber) {
        if (isUserIdDuplicated(inUserId)) {
            return false;
        }
        if (isPhoneNumberDuplicated(inPhoneNumber)) {
            return false;
        }

        UserAccount user = new UserAccount(inUserId, inPassword, inName, inPhoneNumber);

        userList.add(user);

        return true;
    }

    // 계정 제거
    public void deleteUserAccount() {
        userList.remove(currentUser);
    }

    // 계정 수정 (비밀번호, 이름, 전화번호)
    public void editPassword(String newPassword) {
        currentUser.setPassword(newPassword);
        return;
    }

    public void editName(String newName) {
        currentUser.setName(newName);
        return;
    }

    public void editPhoneNumber(String newPhoneNumber) {
        currentUser.setPhoneNumber(newPhoneNumber);
        return;
    }

    // 로그인
    public boolean login(String inUserId, String inPassword) {
        for (UserAccount user : userList) {
            if (user.getUserId().equals(inUserId) && user.getPassword().equals(inPassword)) {
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
