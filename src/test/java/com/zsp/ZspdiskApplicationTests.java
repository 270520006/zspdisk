package com.zsp;

import cn.hutool.core.util.RandomUtil;
import com.zsp.mapper.UserMapper;
import com.zsp.pojo.User;
import com.zsp.utils.MessageUtils;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

@SpringBootTest
class ZspdiskApplicationTests {
    @Autowired
    DataSource dataSource;
    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
        connection.close();

    }
    @Test
    void contextLoads1() {
        User user = userMapper.queryById(10000);
        System.out.println(user);

    }
    @Test
    void testMessage(){
        String codeNum =""+ RandomUtil.randomInt(1000, 9999);
        System.out.println(codeNum);
        HashMap<String,Object> code =new HashMap<>();
        code.put("code",codeNum);
//        MessageUtils.sendMessage("15396226187",code,"SMS_205455159");
    }

}
