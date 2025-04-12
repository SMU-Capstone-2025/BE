//package com.capstone.domain.mypage.service;
//
//
//
//
//
//import com.capstone.domain.mypage.dto.CalendarTaskDto;
//import com.capstone.domain.mypage.dto.UserDto;
//import com.capstone.domain.mypage.exception.InvalidPasswordException;
//import com.capstone.domain.payment.entity.PaymentEntity;
//import com.capstone.domain.payment.repository.PaymentRepository;
//import com.capstone.domain.project.entity.Project;
//import com.capstone.domain.project.exception.ProjectNotFoundException;
//import com.capstone.domain.project.repository.ProjectRepository;
//
//import com.capstone.domain.task.entity.Task;
//import com.capstone.domain.task.repository.TaskRepository;
//import com.capstone.domain.user.entity.User;
//import com.capstone.domain.user.exception.UserFoundException;
//import com.capstone.domain.user.exception.UserNotFoundException;
//import com.capstone.domain.user.repository.UserRepository;
//import com.capstone.global.jwt.JwtUtil;
//import com.capstone.global.mail.service.MailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import static com.capstone.domain.mypage.message.MypageMessages.PASSWORD_MISMATCH;
//import static com.capstone.domain.mypage.message.MypageMessages.PASSWORD_NOT_FOUND;
//import static com.capstone.domain.user.message.UserMessages.USER_FOUND;
//import static com.capstone.domain.user.message.UserMessages.USER_NOT_FOUND;
//
//
//@Service
//@RequiredArgsConstructor
//public class MypageService
//{
//    private final JwtUtil jwtUtil;
//    private final ProjectRepository projectRepository;
//    private final UserRepository userRepository;
//    private final TaskRepository taskRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final PaymentRepository paymentRepository;
//
//    private static final String UNKNOWN_USER = "알수없음";
//    private static final String DELETED_USER_ROLE = "DELETED_USER";
//
//    public UserDto.UserInfoDto getUser(String accessToken)
//    {
//        String email=jwtUtil.getEmail(accessToken);
//        User user=userRepository.findUserByEmail(email);
//        if(user==null)
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//        return user.toDto();
//    }
//
//    @Transactional
//    public String modifyPassword(String accessToken, UserDto.UserPasswordDto userPasswordDto)
//    {
//        String email=jwtUtil.getEmail(accessToken);
//        User user=userRepository.findUserByEmail(email);
//        if(user==null)
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//        String oldPassword=user.getPassword();
//
//        //현재 비번이랑 입력한 비번 같은지 확인
//        if(!bCryptPasswordEncoder.matches(userPasswordDto.getCurrentPassword(),oldPassword))
//        {
//
//            throw new InvalidPasswordException(PASSWORD_NOT_FOUND);
//        }
//
//        //새로 입력한 비번이랑 다시한번 입력하는 새로운 비번이랑 같은지 확인
//        if(!userPasswordDto.getNewPassword().equals(userPasswordDto.getConfirmPassword()))
//        {
//            throw new InvalidPasswordException(PASSWORD_MISMATCH);
//        }
//        String password=bCryptPasswordEncoder.encode(userPasswordDto.getNewPassword());
//        user.setPassword(password);
//        userRepository.save(user);
//        return email;
//    }
//
//    public String modifyProfile(String accessToken, UserDto.UserProfileDto userProfileDto)
//    {
//        String email=jwtUtil.getEmail(accessToken);
//        User user=userRepository.findUserByEmail(email);
//        if(user==null)
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//        user.setProfileImage(userProfileDto.getProfileImage());
//        userRepository.save(user);
//        return email;
//    }
//
//    @Transactional
//    public String modifyEmail(String accessToken, UserDto.UserEmailDto userEmailDto) throws Exception {
//        String email=jwtUtil.getEmail(accessToken);
//        User user=userRepository.findUserByEmail(email);
//        if(user==null)
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//
//        if(!email.equals(userEmailDto.getCurrentEmail()))
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//        User newUser=userRepository.findUserByEmail(userEmailDto.getNewEmail());
//        if(newUser!=null)
//        {
//            throw new UserFoundException(USER_FOUND);
//        }
//        user.setEmail(userEmailDto.getNewEmail());
//        userRepository.save(user);
//
//        //프로젝트 및 작업에 저장된 이메일 변경
//        List<Project> projectList=getUserProject(user);
//        for(Project project:projectList)
//        {
//            Map<String, String> authorities = project.getAuthorities();
//            if (authorities!=null && authorities.containsKey(email)) {
//                String role = authorities.get(email);
//                authorities.remove(email);
//                authorities.put(userEmailDto.getNewEmail(), role);
//                project.setAuthorities(authorities);
//                projectRepository.save(project);
//            }
//            List<String> taskIds= project.getTaskIds();
//            List<Task> taskList=taskRepository.findByIds(taskIds);
//            if (taskIds != null && !taskIds.isEmpty())
//            {
//                for (Task task : taskList) {
//                    List<String> editors = task.getEditors();
//                    if (editors != null && editors.contains(email))
//                    {
//                        editors.remove(email);
//                        editors.add(userEmailDto.getNewEmail());
//                        task.setEditors(editors);
//                        taskRepository.save(task);
//                    }
//                }
//            }
//        }
//        //결제 엔티티에 저장된 이메일 변경
//        List<PaymentEntity> paymentEntityList=paymentRepository.findByUserEmail(email);
//        for(PaymentEntity paymentEntity:paymentEntityList)
//        {
//            paymentEntity.setUserEmail(userEmailDto.getNewEmail());
//            paymentRepository.save(paymentEntity);
//        }
//
//        return userEmailDto.getNewEmail();
//    }
//    @Transactional
//    public String removeUser(String accessToken)
//    {
//        String email=jwtUtil.getEmail(accessToken);
//        User user=userRepository.findUserByEmail(email);
//
//        if(user==null)
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//        List<Project> projectList=getUserProject(user);
//        if(projectList==null)
//        {
//            throw new ProjectNotFoundException();
//        }
//        for(Project project:projectList)
//        {
//            Map<String, String> authorities = project.getAuthorities();
//            authorities.remove(email);
//            authorities.put(UNKNOWN_USER,DELETED_USER_ROLE);
//
//            projectRepository.save(project);
//
//
//            List<String> taskIds= project.getTaskIds();
//            List<Task> taskList=taskRepository.findByIds(taskIds);
//            if (taskIds != null && !taskIds.isEmpty())
//            {
//                for (Task task : taskList) {
//                    List<String> editors = task.getEditors();
//                    if (editors != null && editors.contains(email))
//                    {
//                        editors.remove(email);
//                        editors.add(UNKNOWN_USER);
//                        taskRepository.save(task);
//                    }
//                }
//            }
//        }
//        userRepository.delete(user);
//        return email;
//    }
//
//    public List<Project> getUserProject(User user)
//    {
//        List<String> projectIds= user.getProjectIds();
//        if (projectIds == null || projectIds.isEmpty()) {
//            return new ArrayList<>(); //비어있ㅅ는 리스트 반환
//        }
//        return projectRepository.findAllById(projectIds);
//    }
//
//    public List<CalendarTaskDto> getUserTask(String accessToken)
//    {
//        String email=jwtUtil.getEmail(accessToken);
//        User user=userRepository.findUserByEmail(email);
//        if(user==null)
//        {
//            throw new UserNotFoundException(USER_NOT_FOUND);
//        }
//
//        List<Project> projectList=getUserProject(user);
//        List<CalendarTaskDto> calendarTaskDtoList=new ArrayList<>();
//        for(Project project:projectList)
//        {
//            List<String> taskIds= project.getTaskIds();
//            if(taskIds == null || taskIds.isEmpty()) {
//                continue;
//            }
//            List<Task> tasks = taskRepository.findByIds(taskIds);
//            for (Task task : tasks) {
//                CalendarTaskDto calendarTaskDto=CalendarTaskDto.from(task);
//                calendarTaskDtoList.add(calendarTaskDto);
//            }
//        }
//        return calendarTaskDtoList;
//
//
//    }
//
//
//
//
//
//
//}
