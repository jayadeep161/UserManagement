package com.project.UserManagementSystem.Service;

import com.project.UserManagementSystem.Config.JWTUtil;
import com.project.UserManagementSystem.DTO.ReqRes;
import com.project.UserManagementSystem.Entity.OurUsers;
import com.project.UserManagementSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes RegisterUser(ReqRes registration){
        ReqRes reqRes=new ReqRes();
        try{
            OurUsers users=new OurUsers();
            users.setEmail(registration.getEmail());
            users.setName(registration.getName());
            //users.setPassword(registration.getPassword());
            users.setPassword(passwordEncoder.encode(registration.getPassword()));
            users.setCity(registration.getCity());
            users.setRole(registration.getRole());
            OurUsers saveuser=userRepository.save(users);
            System.out.println(saveuser);
            if(saveuser.getId()>0)
            {
                reqRes.setStatusCode(200);
                reqRes.setMessage("inserted successfully");
            }

        }
        catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage(e.getMessage());
        }
        return reqRes;
    }

    public ReqRes login(ReqRes loginrequest){
       ReqRes response=new ReqRes();
       try{
           Authentication authentication= authenticationManager.
                   authenticate(new UsernamePasswordAuthenticationToken(loginrequest.getEmail(),loginrequest.getPassword()));


           OurUsers ourUsers=userRepository.findByEmail(loginrequest.getEmail()).orElseThrow();
           String token=jwtUtil.generateToken(ourUsers.getEmail()); // we are passing user for which token has to be generated
           response.setStatusCode(200);
           response.setToken(token);
           response.setRole(ourUsers.getRole());
           //response.setRefreshToken(refreshToken);
           response.setExpirationTime("24Hrs");
           response.setMessage("Successfully Logged In");

       }
       catch (Exception e){
           response.setStatusCode(500);
           response.setMessage(e.getMessage());
       }
       return response;
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();

        try {
            List<OurUsers> result =userRepository.findAll();
            if (!result.isEmpty()) {
                reqRes.setOurUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
            return reqRes;
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
            return reqRes;
        }
    }

    public ReqRes getUsersById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            OurUsers usersById = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not found"));
            reqRes.setOurUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("Users with id '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                userRepository.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while deleting user: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, OurUsers updatedUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = userRepository.findById(userId);
            if (userOptional.isPresent()) {
                OurUsers existingUser = userOptional.get();
                existingUser.setEmail(updatedUser.getEmail());
                existingUser.setName(updatedUser.getName());
                existingUser.setCity(updatedUser.getCity());
                existingUser.setRole(updatedUser.getRole());

                // Check if password is present in the request
                if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                    // Encode the password and update it
                    existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
                }

                OurUsers savedUser = userRepository.save(existingUser);
                reqRes.setOurUsers(savedUser);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User updated successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while updating user: " + e.getMessage());
        }
        return reqRes;
    }


    public ReqRes getMyInfo(String email){
        ReqRes reqRes = new ReqRes();
        try {
            Optional<OurUsers> userOptional = userRepository.findByEmail(email);
            if (userOptional.isPresent()) {
                reqRes.setOurUsers(userOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for update");
            }

        }catch (Exception e){
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred while getting user info: " + e.getMessage());
        }
        return reqRes;

    }


}
