package com.example.caselabproject.services.implementations;

import com.example.caselabproject.exceptions.*;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;
import com.example.caselabproject.repositories.DepartmentRepository;
import com.example.caselabproject.repositories.UserRepository;
import com.example.caselabproject.services.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Validated
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final UserRepository userRepository;
    private final Integer limit = 10;


    /**
     * Создаёт новый департамент на основе предоставленного DTO.
     * <p>
     * Этот метод преобразует {@link DepartmentRequestDto} в сущность {@link Department} и сохраняет её в базе данных.
     * Если департамент с таким именем уже существует, будет выброшено исключение {@link DepartmentNameExistsException}.
     * В случае любых других ошибок при создании будет выброшено исключение {@link DepartmentCreateException}.
     * </p>
     *
     * @param requestDto DTO запроса для создания департамента. Должно быть валидным.
     * @return DTO {@link DepartmentResponseDto} ответа, представляющее созданный департамент.
     * @throws DepartmentNameExistsException если департамент с указанным именем уже существует.
     * @throws DepartmentCreateException     если произошла ошибка при создании департамента.
     */
    @Override
    public DepartmentResponseDto create(@Valid DepartmentRequestDto requestDto) {
        try {
            Department department = requestDto.mapToEntity();
            department.setRecordState(RecordState.ACTIVE);
            department.setUsers(new ArrayList<>());
            departmentRepository.save(department);

            return DepartmentResponseDto.mapFromEntity(department);

        } catch (DataIntegrityViolationException ex) {
            throw new DepartmentNameExistsException(requestDto.getName());
        } catch (Exception e) {
            throw new DepartmentCreateException();
        }

    }

    /**
     * Обновляет статус записи департамента на {@link RecordState#DELETED}.
     * <p>
     * Этот метод ищет департамент по его ID и устанавливает его статус записи как "УДАЛЁННЫЙ".
     * Если департамент не найден, будет выброшено исключение {@link DepartmentNotFoundException}.
     * Если статус записи департамента уже является "УДАЛЁННЫМ", будет выброшено исключение {@link DepartmentStatusException}.
     * </p>
     *
     * @param departmentId ID департамента, статус записи которого нужно обновить.
     * @throws DepartmentNotFoundException если департамент с указанным ID не найден.
     * @throws DepartmentStatusException   если статус записи департамента уже является "УДАЛЁННЫМ".
     */
    @Override
    public void updateRecordState(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        if (department.getRecordState().equals(RecordState.DELETED)) {
            throw new DepartmentStatusException(departmentId);
        }

        department.setRecordState(RecordState.DELETED);
        departmentRepository.save(department);
    }

    /**
     * Возвращает департамент по указанному ID в виде {@link DepartmentResponseDto}.
     * <p>
     * Этот метод осуществляет поиск департамента в базе данных по его ID.
     * Если департамент с заданным ID не найден, будет выброшено исключение {@link DepartmentNotFoundException}.
     * </p>
     *
     * @param departmentId ID департамента, который нужно получить.
     * @return DTO {@link DepartmentResponseDto} ответа, представляющее найденный департамент.
     * @throws DepartmentNotFoundException если департамент с указанным ID не найден.
     */
    @Override
    public DepartmentResponseDto getById(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        return DepartmentResponseDto.mapFromEntity(department);
    }

    /**
     * Возвращает список департаментов с пагинацией и возможностью фильтрации по имени.
     * <p>
     * Этот метод осуществляет поиск департаментов в базе данных на основе указанной страницы и лимита.
     * Если предоставлено имя, поиск будет фильтроваться по содержанию имени т.е если мы забыли полное
     * название департамента, мы можем по первым буквам его найти в выпадающем списке.
     * Если имя не указано или пусто, будет возвращен список всех департаментов на указанной странице.
     * </p>
     *
     * @param page Номер страницы для пагинации.
     * @param name Имя для фильтрации списка департаментов.
     * @return Список {@link DepartmentResponseDto}, представляющий найденные департаменты.
     */
    @Override
    public List<DepartmentResponseDto> getAllDepartmentsPageByPage(Integer page, String name) {
        Pageable pageable = PageRequest.of(page, limit);

        Page<Department> departments;

        if (name != null && !name.isEmpty()) {
            departments = departmentRepository.findDepartmentsByNameContaining(name, pageable);
        } else {
            departments = departmentRepository.findAll(pageable);
        }


        return departments.getContent().stream()
                .map(DepartmentResponseDto::mapFromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список пользователей, имеющих статус записи {@link RecordState#ACTIVE} и ID департамента.
     * <p>
     * Этот метод осуществляет поиск пользователей в базе данных, соответствующих указанному статусу записи
     * и принадлежащих к указанному департаменту. Если ни одного пользователя не найдено по указанным критериям,
     * будет выброшено исключение {@link CustomUserException}.
     * </p>
     *
     * @param recordState  Статус записи для фильтрации пользователей.
     * @param departmentId ID департамента для фильтрации пользователей.
     * @return Список {@link User}, представляющий найденных пользователей.
     * @throws CustomUserException если не найдено ни одного пользователя по указанным критериям или
     *                             для указанного ID департамента.
     */
    @Override
    public List<User> GetAllUsersFilteredByDepartment(RecordState recordState, Long departmentId) {
        List<User> users = userRepository.findByRecordStateAndDepartment_Id(recordState, departmentId)
                .orElseThrow(CustomUserException::new);

        if (users.isEmpty()) {
            throw new CustomUserException();
        }

        return users;
    }


}
