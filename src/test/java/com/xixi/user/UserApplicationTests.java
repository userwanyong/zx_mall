package com.xixi.user;

import com.xixi.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserApplicationTests {

    //    @Test
//    void contextLoads() {
//    }
    @Autowired
    private UserMapper userMapper;
//    @Test
//    public void testSelectList(){
//        List<User> userList = userMapper.selectList(null);
//        userList.forEach(System.out::println);
//
//    }
//    @Test
//    void product(){
//        userMapper.productList(1).forEach(System.out::println);
//    }


}
