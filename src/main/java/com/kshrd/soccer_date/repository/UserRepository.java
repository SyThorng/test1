package com.kshrd.soccer_date.repository;

import com.kshrd.soccer_date.model.UserApp;
import com.kshrd.soccer_date.model.request.UserRequest;
import com.kshrd.soccer_date.model.request.UserSetupRequest;
import com.kshrd.soccer_date.model.request.UserUpdateRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRepository {

    @Select("""
            select name from role join
            user_detail ud on role.id = ud.role_id where user_id=#{userId};
            """)
    List<String> getRoleUser(Integer userId);

    @Select("""
            SELECT * FROM app_user where email = #{email}
            """)
    @Results(id = "userMap", value = {
            @Result(property = "userId", column = "id"),
            @Result(property = "firstName", column = "first_name"),
            @Result(property = "lastName", column = "last_name"),
            @Result(property = "profile", column = "profile_image"),
            @Result(property = "roles", column = "id", many = @Many(
                    select = "getRoleUser"
            ))
    })
    UserApp getUserByEmail(String email);

    @Select("""
            insert into app_user(first_name,last_name,email,password)
            values (#{user.firstName},#{user.lastName},#{user.email},#{user.password})
            returning id
            """)
    Integer register(@Param("user") UserRequest userRequest);

    @Select("""
            INSERT INTO  user_detail( user_id, role_id) VALUES (#{userId},#{roleId})
            """)
    void addUserIdToUserDetail(Integer userId, int roleId);

    @Select("""
            SELECT * FROM app_user where id=#{userId}
            """)
    @ResultMap("userMap")
    UserApp searchUserById(Integer userId);

    @Select("""
            delete from app_user where id=#{userId}
            """)
    void deleteUserById(Integer userId);

    //    ----------------------- images ---------------------------------
    @Select("""
            update app_user set profile_image=#{fileName} where id=#{userId}
            """)
    void updateUserProfile(String fileName, Integer userId);

    @Select("""
            UPDATE app_user set password=#{password} where id=#{userId}
            """)
    void updatePasswordByUserID(Integer userId, String password);

    @Select("""
            DELETE FROM app_user where email=#{email}
            """)
    void deleteUserByEmailUser(String email);

    //-------------------------- verify user -----------------------
    @Select("""
            select is_verify from app_user where id=#{userId}
            """)
    Boolean isVerifyUser(Integer userId);

    @Select("""
            insert into verify(user_id, verify) values (#{userId},#{code})
            """)
    void verifyCodeToUser(Integer userId, String code);

    @Select("""
            select user_id from verify where verify=#{code};
            """)
    Integer findUserIdByVerifyCode(String code);

    @Select("""
            update app_user set is_verify=true where id=#{userId}
            """)
    void updateStatusByUserId(Integer userId);

    @Select("""
            select time_create from verify where verify=#{code};
            """)
    String findExpiredDateUserByCode(String code);

    @Select("""
            select time_create from verify where user_id=#{id};
            """)
    String findExpiredDateUserById(Integer id);

    @Select("""
            delete  from verify where verify=#{code};
            """)
    void deleteCodeVerify(String code);

    @Select("""
            Select from verify where user_id=#{id};
            """)
    List searchCodeVerifyUserById(Integer id);

    @Select("""
            SELECT * FROM app_user where email=#{email}
            """)
    @ResultMap("userMap")
    UserApp searchUserByEmail(String email);


    @Select("""
            SELECT verify.verify from verify where user_id=#{id}
            """)
    List<String> getAllVerifyCode(Integer id);


    @Select("""
            DELETE  from verify where user_id=#{id}
            """)
    void deleteAllVerify(Integer id);

    @Select("""
            select is_verify from app_user where email=#{email}
            """)
    boolean findIsVerifyByEmail(String email);

    @Select("""
            SELECT id from app_user where email=#{email}
            """)
    Integer getUserIdByEmail(String email);

    @Select("""
            update app_user
            set first_name=#{user.firstName},
                last_name=#{user.lastName},
                skill=#{user.skill},
                address=#{user.address},
                contact=#{user.contact},
                number=#{user.number},
                profile_image=#{fileName}
                where email=#{userId} returning *
            """)
    @ResultMap("userMap")
    UserApp updateInformationUser(@Param("user") UserUpdateRequest userUpdateRequest, String fileName, String userId);

    @Select("""
             update app_user
                set skill=#{user.skill},
                address=#{user.address},
                contact=#{user.contact},
                number=#{user.number},
                profile_image=#{user.image},
                is_skip=true
                where email=#{email} returning *
            """)
    @ResultMap("userMap")
    UserApp setUpProfile(@Param("user") UserSetupRequest userSetupRequest, String email);


    @Select("""
            SELECT * FROM app_user limit #{pageSize} offset #{pageNo}
            """)
    @ResultMap("userMap")
    List<UserApp> getAllUser(Integer pageSize,Integer pageNo);

    @Select("""
            SELECT first_name from app_user where id=#{userId}
            """)
    String getFirstNameByUserId(Integer userId);

    @Select("""
            SELECT last_name from app_user where id=#{userId}
            """)
    String getLastNameByUserId(Integer userId);

    @Select("""
            SELECT * FROM app_user WHERE CONCAT(first_name,'', last_name) ilike '%${userName}%'
            limit #{pageSize} offset #{pageNo}
            """)
    @ResultMap("userMap")
    List<UserApp> searchUserByName(String userName,Integer pageSize,Integer pageNo);

    @Select("""
            SELECT number from app_user where id=#{userIdByEmail} 
            """)
    Integer getNumberByUserId(Integer userIdByEmail);

    @Select("""
            Select profile_image from app_user where email=#{email}
            """)
    String getProfileImageByEmail(String email);

    @Select("""
            Select verify from verify where user_id=#{userId} and verify=#{code}
            """)
    String searchVerifyCodeByUserId(Integer userId, String code);

    @Select("""
            Update  app_user set is_skip=true where id=#{userId} returning *
            """)
    @ResultMap("userMap")
    UserApp updateIsSkipByCurrentUser(Integer userId);

    @Select("""
            SELECT password from app_user where id=#{userId}
            """)
    String getPasswordByUserId(Integer userId);

    @Select("""
            SELECT verify.verify from verify where verify.verify=#{code2}
            """)
    String searchVerifyByCode(String code2);
}