package aiunivers.testspring;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final TestEntityService testEntityService;

    @PostMapping
    public ResponseEntity<?> save() {
        int totalEntities = 10000000;
        int batchSize = 5;
        ExecutorService executorService = Executors.newFixedThreadPool(batchSize);

        Runnable saveTask = () -> {
            long startTaskGeneration = System.currentTimeMillis();
            List<TestEntity> entities = new ArrayList<>();
            for (int i = 0; i < totalEntities; i++) {
                TestEntity testEntity = new TestEntity();
                String randomName = "Name" + new Random().nextInt(10000);
                testEntity.setName(randomName);
                entities.add(testEntity);
            }
            long endTaskGeneration = System.currentTimeMillis();
            long durationTaskGeneration = endTaskGeneration - startTaskGeneration;
            System.out.println(durationTaskGeneration/1000);
            long start = System.currentTimeMillis();

            testEntityService.saveAllJdbc(entities);
            long end = System.currentTimeMillis();
            long duration = end - start;
            System.out.println(duration/1000);
        };


        executorService.submit(saveTask);

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }



        return new ResponseEntity<>("Saved", HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<?> get(@RequestParam String name) {
        return new ResponseEntity<>(testEntityService.getByName(name), HttpStatus.OK);
    }
}
