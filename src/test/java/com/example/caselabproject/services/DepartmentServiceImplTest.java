package com.example.caselabproject.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
/*import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;*/

@SpringBootTest
/*@Testcontainers*/
public class DepartmentServiceImplTest {

   /* @Container
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("CaseLab")
            .withUsername("root")
            .withPassword("123");

    @Autowired
    private DepartmentServiceImpl departmentService;

    @SpyBean
    private DepartmentRepository departmentRepository;

    @SpyBean
    private UserRepository userRepository;

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.username", postgres::getUsername);
    }


    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @BeforeEach
    public void cleanUp() {
        departmentRepository.deleteAll();
        userRepository.deleteAll();
    }


    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Test
    void create_Test() {
        createDepartment();

        List<Department> list = departmentRepository.findAll();

        Assertions.assertEquals(1, list.size());
    }

    @Test
    void deleteDepartment_Test() {

        createDepartment();

        List<Department> departmentList = departmentRepository.findAll();

        Long id = departmentList.get(0).getId();

        Assertions.assertEquals(RecordState.ACTIVE, departmentList.get(0).getRecordState());

        departmentService.deleteDepartment(id);

        Department department2 = departmentRepository.findById(id).get();

        Assertions.assertEquals(RecordState.DELETED, department2.getRecordState());
    }

    @Test
    void recoverDepartment_Test() {

        createDepartment();

        List<Department> departmentList = departmentRepository.findAll();

        Long id = departmentList.get(0).getId();

        departmentService.deleteDepartment(id);

        Department department1 = departmentRepository.findById(id).get();

        Assertions.assertEquals(RecordState.DELETED, department1.getRecordState());

        departmentService.recoverDepartment(id);

        Department department2 = departmentRepository.findById(id).get();

        Assertions.assertEquals(RecordState.ACTIVE, department2.getRecordState());
    }

    @Test
    void getById_Test() {
        DepartmentRequestDto requestDto = createDepartment();

        DepartmentResponseDto departmentResponseDto = departmentService.getById(1L);

        Assertions.assertEquals(departmentResponseDto.getName(), requestDto.getName());

    }

    DepartmentRequestDto createDepartment() {

        DepartmentRequestDto requestDto = new DepartmentRequestDto();
        requestDto.setName("Name");
        departmentService.create(requestDto);

        return requestDto;
    }*/

}
