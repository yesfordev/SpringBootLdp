package com.wefunding.ldp.publicdata.construct.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
//@DataJpaTest // 슬라이싱 테스트 할 때는 인메모리 데이터베이스 필요
@SpringBootTest // 이 어노테이션 사용시, 모든 테스트에 필요한 스프링 빈을 등록하므로 postgresql 이용시 사용.
public class TestLeeEntityRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestLeeEntityRepository testLeeEntityRepository;

    @Autowired
    private TestLeeResultEntityRepository testLeeResultEntityRepository;

    /*
    Database Connection Test
     */
    @Test
    public void di() throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            System.out.println(metaData.getURL());
            System.out.println(metaData.getDriverName());
            System.out.println(metaData.getUserName());
        }
    }

//    @Test
//    public void testLeeTest() throws SQLException {
//
//        Optional<TestLee> testLeeById = testLeeRepository.findById(5);
//
//        assertThat(testLeeById).isNotNull();
//
//        List<TestLee> testLeeBySigungucd = testLeeRepository.findBySigungucd("41500");
//
//        assertThat(testLeeBySigungucd).isNotNull();
//
//        System.out.println(testLeeBySigungucd);
//    }
}