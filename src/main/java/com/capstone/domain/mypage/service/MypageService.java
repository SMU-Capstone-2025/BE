package com.capstone.domain.mypage.service;


import com.capstone.domain.mypage.dto.CalendarTaskDto;
import com.capstone.domain.mypage.dto.UserDto;
import com.capstone.domain.mypage.exception.InvalidPasswordException;
import com.capstone.domain.payment.entity.PaymentEntity;
import com.capstone.domain.payment.repository.PaymentRepository;
import com.capstone.domain.project.entity.Project;
import com.capstone.domain.project.exception.ProjectNotFoundException;

import com.capstone.domain.task.entity.Task;
import com.capstone.domain.task.repository.TaskRepository;
import com.capstone.domain.user.entity.ProjectUser;
import com.capstone.domain.user.entity.User;
import com.capstone.domain.user.exception.UserFoundException;
import com.capstone.domain.user.exception.UserNotFoundException;
import com.capstone.domain.user.repository.ProjectUserRepository;
import com.capstone.domain.user.repository.UserRepository;
import com.capstone.global.response.exception.GlobalException;
import com.capstone.global.response.status.ErrorStatus;
import com.capstone.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.capstone.domain.mypage.message.MypageMessages.PASSWORD_MISMATCH;
import static com.capstone.domain.user.message.UserMessages.USER_FOUND;
import static com.capstone.domain.user.message.UserMessages.USER_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
public class MypageService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PaymentRepository paymentRepository;
    private final ProjectUserRepository projectUserRepository;


    public UserDto.UserInfoDto getUser(CustomUserDetails userDetails)
    {

        String email=userDetails.getEmail();
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        return user.get().toDto();
    }
    public boolean checkEmail(String name, String email)
    {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isEmpty() || !user.get().getName().equals(name)) {
            throw new GlobalException(ErrorStatus.USER_NOT_FOUND);
        }
        return true;
    }



    @Transactional
    public String modifyPassword(UserDto.UserPasswordDto userPasswordDto)
    {
        String email=userPasswordDto.getEmail();
        Optional<User> user=userRepository.findUserByEmail(email);
        if(user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User userExists = user.get();
        //새로 입력한 비번이랑 다시한번 입력하는 새로운 비번이랑 같은지 확인
        if(!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword()))
        {
            throw new InvalidPasswordException(PASSWORD_MISMATCH);
        }
        String password=bCryptPasswordEncoder.encode(userPasswordDto.getNewPassword());
        userExists.setPassword(password);
        userRepository.save(userExists);
        return email;
    }

    public String modifyProfile(CustomUserDetails userDetails, UserDto.UserProfileDto userProfileDto)
    {
        String email=userDetails.getEmail();
        Optional<User> user=userRepository.findUserByEmail(email);
        if(user.isEmpty())
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        User userExists = user.get();
        userExists.setProfileImage(userProfileDto.getProfileImage());
        userRepository.save(userExists);
        return email;
    }

    @Transactional
    public String modifyEmail(CustomUserDetails userDetails, UserDto.UserEmailDto userEmailDto) throws Exception {
        String email=userDetails.getEmail();
        log.info("email {}",email);
        Optional<User> user = userRepository.findUserByEmail(email);
        if(user.isEmpty()) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        if(!email.equals(userEmailDto.getCurrentEmail())) {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        Optional<User> newUser = userRepository.findUserByEmail(userEmailDto.getNewEmail());
        if(newUser.isPresent()) {
            throw new UserFoundException(USER_FOUND);
        }

        User userExists = user.get();
        userExists.setEmail(userEmailDto.getNewEmail());
        userRepository.save(userExists);

        //프로젝트 및 작업에 저장된 이메일 변경
        List<Project> projectList=getUserProject(user.orElse(null));
        log.info("projectList {}",projectList.get(0).getProjectName());
        for(Project project:projectList)
        {
            List<String> taskIds= project.getTaskIds();
            log.info("editors {}",taskIds.get(0));
            List<Task> taskList=taskRepository.findByIds(taskIds);
            if (taskIds != null && !taskIds.isEmpty())
            {
                for (Task task : taskList) {
                    List<String> editors = task.getEditors();
                    log.info("editors {}",editors.get(0));
                    if (editors != null && editors.contains(email))
                    {
                        editors.remove(email);
                        editors.add(userEmailDto.getNewEmail());
                        task.setEditors(editors);
                        taskRepository.save(task);
                    }
                }
            }
        }
        //결제 엔티티에 저장된 이메일 변경
        List<PaymentEntity> paymentEntityList=paymentRepository.findByUserEmail(email);
        for(PaymentEntity paymentEntity:paymentEntityList)
        {
            paymentEntity.setUserEmail(userEmailDto.getNewEmail());
            paymentRepository.save(paymentEntity);
        }

        return userEmailDto.getNewEmail();
    }
    @Transactional
    public String removeUser(CustomUserDetails userDetails)
    {
        String email= userDetails.getEmail();
        Optional<User> user = userRepository.findUserByEmail(email);

        if(user.isEmpty())
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }
        List<Project> projectList=getUserProject(user.orElse(null));
        if(projectList==null)
        {
            throw new ProjectNotFoundException();
        }
        for(Project project:projectList)
        {
            List<String> taskIds= project.getTaskIds();
            List<Task> taskList=taskRepository.findByIds(taskIds);
            if (taskIds != null && !taskIds.isEmpty())
            {
                for (Task task : taskList) {
                    List<String> editors = task.getEditors();
                    if (editors != null && editors.contains(email))
                    {
                        editors.remove(email);
                        taskRepository.save(task);
                    }
                }
            }
        }
        List<ProjectUser> projectUserList=projectUserRepository.findByUserId(email);
        projectUserRepository.deleteAll(projectUserList);
        userRepository.delete(user.get());

        return email;
    }

    public List<Project> getUserProject(User user)
    {
        return projectUserRepository.findProjectsByUserId(user.getEmail());
    }

    public List<CalendarTaskDto> getUserTask(CustomUserDetails userDetails)
    {
        String email=userDetails.getEmail();
        log.info(email);
        Optional<User> user=userRepository.findUserByEmail(email);
        if(user.isEmpty())
        {
            throw new UserNotFoundException(USER_NOT_FOUND);
        }

        List<Project> projectList=getUserProject(user.orElse(null));
        log.info("projectList {}",projectList.get(0).getProjectName());
        List<CalendarTaskDto> calendarTaskDtoList=new ArrayList<>();
        for(Project project:projectList)
        {
            List<String> taskIds= project.getTaskIds();
            if(taskIds == null || taskIds.isEmpty()) {
                continue;
            }
            log.info("taskIds: {}", taskIds);
            List<Task> tasks = taskRepository.findByProjectId(project.getId());
            for (Task task : tasks) {
                CalendarTaskDto calendarTaskDto=CalendarTaskDto.from(task);
                calendarTaskDtoList.add(calendarTaskDto);
            }
        }
        return calendarTaskDtoList;


    }






}
