package aiunivers.testspring;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestEntityService {
    private final TestEntityRepository testEntityRepository;
    private final JdbcTemplate  jdbcTemplate;
    public TestEntity save(TestEntity testEntity) {
        return testEntityRepository.save(testEntity);
    }

    public void saveAll(List<TestEntity> entities) {

        testEntityRepository.saveAll(entities);
    }
    public void saveAllJdbc(List<TestEntity> entities) {
        try {
            jdbcTemplate.batchUpdate(
                    "insert into test_entity (id, name) values (nextval('test_entity_id_seq'), ?)",
                    entities,
                    5000,
                    (ps, argument) -> {
                        ps.setString(1, argument.getName());
                    }
            );

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
            //batch update
    }

    public List<TestEntity> getByName(String name) {
        return testEntityRepository.findByName(name);
    }
}
