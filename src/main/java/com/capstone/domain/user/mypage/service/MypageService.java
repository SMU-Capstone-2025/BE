package com.capstone.domain.user.mypage.service;


import com.capstone.domain.project.repository.ProjectRepository;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.exception.UserFoundException;
import com.capstone.domain.user.exception.UserNotFoundException;
import com.capstone.domain.user.mypage.dto.UserDto;
import com.capstone.domain.user.mypage.exception.InvalidPasswordException;
import com.capstone.domain.user.repository.UserRepository;
import com.capstone.global.jwt.JwtUtil;
import com.capstone.global.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;



import static com.capstone.domain.user.message.UserMessages.*;
import static com.capstone.domain.user.mypage.message.MypageMessages.PASSWORD_MISMATCH;
import static com.capstone.domain.user.mypage.message.MypageMessages.PASSWORD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class MypageService
{
    private final JwtUtil jwtUtil;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final MailService mailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public UserDto.UserInfoDto getUser(String accessToken)
    {
        String email=jwtUtil.getEmail(accessToken);
        User user=userRepository.findUserByEmail(email);
        if(user==null)
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        return user.toDto();
    }

    public void modifyPassword(String accessToken, UserDto.UserPasswordDto userPasswordDto)
    {
        String email=jwtUtil.getEmail(accessToken);
        User user=userRepository.findUserByEmail(email);
        if(user==null)
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        String oldPassword=user.getPassword();

        //현재 비번이랑 입력한 비번 같은지 확인
        if(!bCryptPasswordEncoder.matches(userPasswordDto.getCurrentPassword(),oldPassword))
        {

            throw new InvalidPasswordException(PASSWORD_NOT_FOUND);
        }

        //새로 입력한 비번이랑 다시한번 입력하는 새로운 비번이랑 같은지 확인
        if(!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword()))
        {
            throw new InvalidPasswordException(PASSWORD_MISMATCH);
        }
        String password=bCryptPasswordEncoder.encode(userPasswordDto.getNewPassword());
        user.setPassword(password);
        userRepository.save(user);
    }

    public void modifyProfile(String accessToken, UserDto.UserProfileDto userProfileDto)
    {
        String email=jwtUtil.getEmail(accessToken);
        User user=userRepository.findUserByEmail(email);
        if(user==null)
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        user.setProfileImage(userProfileDto.getProfileImage());
        userRepository.save(user);
    }
    public void modifyEmail(String accessToken, UserDto.UserEmailDto userEmailDto) throws Exception {
        String email=jwtUtil.getEmail(accessToken);
        User user=userRepository.findUserByEmail(email);
        if(user==null)
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        if(!email.equals(userEmailDto.getCurrentEmail()))
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User newUser=userRepository.findUserByEmail(userEmailDto.getNewEmail());
        if(newUser!=null)
        {
            throw new UserFoundException(USER_FOUND);
        }
        user.setEmail(userEmailDto.getNewEmail());
        userRepository.save(user);
    }

    public void removeUser(String accessToken)
    {
        String email=jwtUtil.getEmail(accessToken);
        User user=userRepository.findUserByEmail(email);
        if(user==null)
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        userRepository.delete(user);
    }


}
