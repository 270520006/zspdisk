package com.zsp;

import com.zsp.mapper.UserMapper;
import com.zsp.pojo.User;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

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

}
