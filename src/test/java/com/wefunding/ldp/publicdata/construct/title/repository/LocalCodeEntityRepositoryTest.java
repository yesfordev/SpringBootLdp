package com.wefunding.ldp.publicdata.construct.title.repository;

import com.wefunding.ldp.publicdata.construct.common.entity.LocalCodeEntity;
import com.wefunding.ldp.publicdata.construct.common.repository.LocalCodeEntityRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by yes on 2020/09/24
 */

@RunWith(SpringRunner.class)
//@DataJpaTest // 슬라이싱 테스트 할 때는 인메모리 데이터베이스 필요
@SpringBootTest // 이 어노테이션 사용시, 모든 테스트에 필요한 스프링 빈을 등록하므로 postgresql 이용시 사용.
public class LocalCodeEntityRepositoryTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RegisterTitleEntityRepository registerTitleEntityRepository;

    @Autowired
    private LocalCodeEntityRepository localCodeEntityRepository;

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

    @Test
    public void getCdTest() throws SQLException {

        Optional<LocalCodeEntity> localCodeEntity = localCodeEntityRepository.findById(2);

        assertThat(localCodeEntity).isNotNull();

        System.out.println(localCodeEntity);

        List<LocalCodeEntity> localCodeEntityList = localCodeEntityRepository.getSeoulConstruct();

        assertThat(localCodeEntityList).isNotNull();

        System.out.println(localCodeEntityList);
    }

}