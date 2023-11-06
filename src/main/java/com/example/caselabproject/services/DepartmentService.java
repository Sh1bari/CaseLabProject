package com.example.caselabproject.services;

import com.example.caselabproject.exceptions.DepartmentCreateException;
import com.example.caselabproject.exceptions.DepartmentNameExistsException;
import com.example.caselabproject.exceptions.DepartmentNotFoundException;
import com.example.caselabproject.exceptions.DepartmentStatusException;
import com.example.caselabproject.models.DTOs.request.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.DepartmentResponseDto;
import com.example.caselabproject.models.DTOs.response.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.RecordState;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DepartmentService {
    /**
     * Создаёт новый департамент на основе предоставленного DTO.
     * <p>
     * Этот метод преобразует {@link DepartmentRequestDto} в сущность {@link Department} и сохраняет её в базе данных.
     * Если департамент с таким именем уже существует, будет выброшено исключение {@link DepartmentNameExistsException}.
     * В случае любых других ошибок при создании будет выброшено исключение {@link DepartmentCreateException}.
     * </p>
     *
     * @param departmentRequestDto DTO запроса для создания департамента. Должно быть валидным.
     * @return DTO {@link DepartmentResponseDto} ответа, представляющее созданный департамент.
     * @throws DepartmentNameExistsException если департамент с указанным именем уже существует.
     * @throws DepartmentCreateException     если произошла ошибка при создании департамента.
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentResponseDto create(@Valid DepartmentRequestDto departmentRequestDto);

    /**
     * Обновляет статус записи департамента на {@link RecordState#DELETED}.
     * <p>
     * Этот метод ищет департамент по его ID и устанавливает его статус записи как "DELETED".
     * Если департамент не найден, будет выброшено исключение {@link DepartmentNotFoundException}.
     * Если статус записи департамента уже является "DELETED", будет выброшено исключение {@link DepartmentStatusException}.
     * </p>
     *
     * @param departmentId ID департамента, статус записи которого нужно удалить.
     * @throws DepartmentNotFoundException если департамент с указанным ID не найден.
     * @throws DepartmentStatusException   если статус записи департамента уже является "DELETED".
     * @author Khodov Nikita
     */
    @Transactional
    boolean deleteDepartment(@Min(value = 1L, message = "Id cant be less than 1")Long departmentId);
    /**
     * Обновляет статус записи департамента на {@link RecordState#ACTIVE}.
     * <p>
     * Этот метод ищет департамент по его ID и устанавливает его статус записи как "ACTIVE".
     * Если департамент не найден, будет выброшено исключение {@link DepartmentNotFoundException}.
     * Если статус записи департамента уже является "ACTIVE", будет выброшено исключение {@link DepartmentStatusException}.
     * </p>
     *
     * @param departmentId ID департамента, статус записи которого нужно восстановить.
     * @throws DepartmentNotFoundException если департамент с указанным ID не найден.
     * @throws DepartmentStatusException   если статус записи департамента уже является "ACTIVE".
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentResponseDto recoverDepartment(@Min(value = 1L, message = "Id cant be less than 1")Long departmentId);
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
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentResponseDto getById(@Min(value = 1L, message = "Id cant be less than 1")Long departmentId);
    /**
     * Возвращает список департаментов с пагинацией и возможностью фильтрации по имени.
     * <p>
     * Этот метод осуществляет поиск департаментов в базе данных на основе указанной страницы и лимита.
     * Если предоставлено имя, поиск будет фильтроваться по содержанию имени т.е если мы забыли полное
     * название департамента, мы можем по первым буквам его найти в выпадающем списке.
     * Если имя не указано или пусто, будет возвращен список всех департаментов на указанной странице.
     * </p>
     *
     * @param pageable Модель страницы для пагинации.
     * @param name     Имя для фильтрации списка департаментов.
     * @return Список {@link DepartmentResponseDto}, представляющий найденные департаменты.
     * @author Khodov Nikita
     */
    @Transactional
    List<DepartmentResponseDto> getAllDepartmentsPageByPage(Pageable pageable, String name, RecordState recordState);
    /**
     * Возвращает список пользователей, имеющих статус записи {@link RecordState#ACTIVE} и ID департамента.
     * <p>
     * Этот метод осуществляет поиск пользователей в базе данных, соответствующих указанному статусу записи
     * и принадлежащих к указанному департаменту.
     * </p>
     *
     * @param recordState  Статус записи для фильтрации пользователей.
     * @param departmentId ID департамента для фильтрации пользователей.
     * @return Список {@link User}, представляющий найденных пользователей.
     * @author Khodov Nikita
     */
    @Transactional
    List<UserGetByIdResponseDto> getAllUsersFilteredByDepartment(RecordState recordState,
                                                                 @Min(value = 1L, message = "Id cant be less than 1") Long departmentId);

}
