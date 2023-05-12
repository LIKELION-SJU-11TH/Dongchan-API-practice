package com.dan.api_example.service;

import com.dan.api_example.entity.User;
import com.dan.api_example.model.GetUserRes;
import com.dan.api_example.model.SignUpUserReq;
import com.dan.api_example.repository.UserRepository;
import com.dan.api_example.util.SHA256;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 유저 생성
     * @param signUpUserReq
     */
    public void createUser(SignUpUserReq signUpUserReq) throws Exception {
        String plainPw = signUpUserReq.getPassword(); // 유저가 입력한 비밀번호 (ex. ldc1104)
        String encryptPw = SHA256.encrypt(plainPw); // 암호화된 비밀번호 (ex. d34eda6...)

        // 논리적 Validation 부분
        Optional<User> optionalUser = userRepository.findByEmail(signUpUserReq.getEmail()); // email로 DB에서 유저 검색. 유저가 없으면 optionalUser는 null
        if (optionalUser.isPresent()) { // 만약 유저가 null이 아니라 있으면 에러를 발생시킴.
            Exception e = new Exception("해당 이메일로 이미 가입하셨습니다."); //에러 생성(만들기).
            throw e;
        }
        // 논리적 Validation 끝

        User user = User.builder()
                .age(signUpUserReq.getAge())
                .name(signUpUserReq.getName())
                .email(signUpUserReq.getEmail())
                .password(encryptPw)
                .build();

        userRepository.save(user);
    }

    /**
     * 유저 전체 조회
     */
    public List<GetUserRes> getUsers() {
        List<User> userList = userRepository.findAll(); // DB에서 모든 User를 가져와 리스트에 넣기.


        List<GetUserRes> userRes = new ArrayList<>(); // 반환해줄 배열을 생성, 배열을 구성할 객체는 GetUserRes.
        for (User user : userList) {
            GetUserRes getUserEntity = new GetUserRes(user); // GetUserRes.class의 생성자를 통하여 User를 GetUserRes로 변환할 수 있게 구현. (GetUserRes.class에서 확인해보세요.)
            userRes.add(getUserEntity); //배열에 객체 추가
        }

        /*
        간단한 방법.
        List<GetUserRes> userRes = userList.stream().map(GetUserRes::new).collect(Collectors.toList());
        */

        return userRes;
    }


    /**
     * 인덱스로 유저 조회
     * 한번 구현해 보세요
     * <p>
     * 1. user_id로 user 조회 (UserRepository의 findById 이용)
     * 2. User를 GetUserRes로 변환
     * 3. GetUserRes 반환
     * <p>
     * findById로 User 조회 시 Optional<User>로 반환 -> User를 빼오는 방법 찾아보세요.
     */
    public GetUserRes getUserById(Long userId) throws Exception {
        Optional<User> optionalUser = userRepository.findById(userId); //아이디로 유저 조회, 만약 해당 아이디의 유저가 없으면 optionalUser는 null.

        if (optionalUser.isEmpty()) { // 만약 유저 아이디에 따른 유저가 없다면 예외 발생
            Exception e = new Exception("해당 Id의 유저가 존재하지 않습니다.");
            throw e;
        }

        User user = optionalUser.get(); //optionalUser가 null이 아니라면 get()메서드를 통해 User를 가져올 수 있음.
        GetUserRes getUserRes = new GetUserRes(user); // GetUserRes.class의 생성자를 통하여 User를 GetUserRes로 변환할 수 있게 구현. (GetUserRes.class에서 확인해보세요.)

        return getUserRes;
    }
}
