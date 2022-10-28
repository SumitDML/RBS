package com.dml.project.rbs.service.Impl;

import com.dml.project.rbs.dto.UserProfileDto;
import com.dml.project.rbs.entity.ItemEntity;
import com.dml.project.rbs.entity.OrdersEntity;
import com.dml.project.rbs.entity.RoleEntity;
import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.request.SignUpRequest;
import com.dml.project.rbs.model.response.MessageResponse;
import com.dml.project.rbs.repository.RoleRepository;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.UserService;
import com.dml.project.rbs.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PasswordEncoder passwordEncoder;

    public MessageResponse registerNewUser(SignUpRequest signUpRequest){
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(signUpRequest,UserEntity.class);
        RoleEntity roleEntity = roleRepository.findById("User").get();
        UserEntity user =userRepository.findByEmail(userEntity.getEmail());

        if(user ==null){
            Set<RoleEntity> roleEntities = new HashSet<>();
            roleEntities.add(roleEntity);
            userEntity.setRoleEntity(roleEntities);
            userEntity.setPassword(getEncryptedPassword(userEntity.getPassword()));
            userRepository.save(userEntity);
            return new MessageResponse(userEntity.getFirstName()+" "+userEntity.getLastName()+" Added Successfully!");
        }else{
             throw new EntityNotFoundException("User Already Exists");
        }
    }



    public String getEncryptedPassword(String password){

        return passwordEncoder.encode(password);
    }

    public void initRolesAndUser(){
        RoleEntity adminRoleEntity = new RoleEntity();
        adminRoleEntity.setRoleName("Admin");
        adminRoleEntity.setRoleDescription("Admin Role For CRUD");
        roleRepository.save(adminRoleEntity);

        RoleEntity userRoleEntity = new RoleEntity();
        userRoleEntity.setRoleName("User");
        userRoleEntity.setRoleDescription("User Role For List and Find");
        roleRepository.save(userRoleEntity);

        UserEntity adminUserEntity = new UserEntity();
        adminUserEntity.setFirstName("Admin");
        adminUserEntity.setLastName("Admin");
        adminUserEntity.setEmail("admin@123");
        adminUserEntity.setPhoneNumber("+918338002929");
        adminUserEntity.setPassword(getEncryptedPassword("admin@pass"));
        Set<RoleEntity> adminRoleEntities = new HashSet<>();
        adminRoleEntities.add(adminRoleEntity);
        adminUserEntity.setRoleEntity(adminRoleEntities);
        userRepository.save(adminUserEntity);

    }

    @Override
    public UserProfileDto userProfile(String tokenHeader) {

        String jwtToken = tokenHeader.substring(7);
        String email = jwtUtil.extractEmailFromToken(jwtToken);

        UserEntity user = userRepository.findByEmail(email);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserProfileDto userProfileDto = modelMapper.map(user,UserProfileDto.class);
        return userProfileDto;
    }

    public MessageResponse clearOrderHistory(String tokenHeader) {
        String jwtToken = tokenHeader.substring(7);
        String email = jwtUtil.extractEmailFromToken(jwtToken);

        UserEntity existingUserEntity =  userRepository.findByEmail(email);
        List<OrdersEntity> ordersEntityList = existingUserEntity.getOrders();

        if(ordersEntityList.isEmpty()){
            return new MessageResponse("No Orders Yet!");
        }
        ordersEntityList.removeAll(ordersEntityList);
        existingUserEntity.setOrders(ordersEntityList);

        userRepository.save(existingUserEntity);

            return new MessageResponse("Order History Cleared!");

    }

}
