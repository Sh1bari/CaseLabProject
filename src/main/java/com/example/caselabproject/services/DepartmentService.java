package com.example.caselabproject.services;

import com.example.caselabproject.models.DTOs.request.department.DepartmentChildDto;
import com.example.caselabproject.models.DTOs.request.department.DepartmentCreateRequestDto;
import com.example.caselabproject.models.DTOs.request.department.DepartmentRequestDto;
import com.example.caselabproject.models.DTOs.response.application.ApplicationItemGetByIdResponseDto;
import com.example.caselabproject.models.DTOs.response.department.*;
import com.example.caselabproject.models.DTOs.response.user.UserGetByIdResponseDto;
import com.example.caselabproject.models.entities.Department;
import com.example.caselabproject.models.entities.User;
import com.example.caselabproject.models.enums.ApplicationItemStatus;
import com.example.caselabproject.models.enums.RecordState;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.caselabproject.exceptions.*;

import java.util.List;

@Validated
public interface DepartmentService {
    /**
     * Создаёт новый департамент на основе предоставленного DTO.
     * <p>
     * Этот метод преобразует {@link DepartmentCreateRequestDto} в сущность {@link Department} и сохраняет её в базе данных.
     * Если департамент с таким именем уже существует, будет выброшено исключение {@link DepartmentSQLValidationException}.
     * В случае любых других ошибок при создании будет выброшено исключение {@link DepartmentCreateException}.
     * </p>
     *
     * @param departmentRequestDto DTO запроса для создания департамента. Должно быть валидным.
     * @return DTO {@link DepartmentCreateResponseDto} ответа, представляющее созданный департамент.
     * @throws DepartmentSQLValidationException ошибка при сохранении {@link Department} в базе данных.
     * @throws DepartmentCreateException        если произошла ошибка при создании департамента.
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentCreateResponseDto create(@Valid DepartmentCreateRequestDto departmentRequestDto,
                                       @NotBlank(message = "Department creator username can't be blank.") String username);


    /**
     * Назначает родительский департамент для указанного дочернего департамента.
     * <p>
     * Этот метод обновляет иерархию департаментов, устанавливая связь между родительским и дочерним департаментом.
     * При попытке установить некорректную связь (например, привязать родительский департамент к своему дочернему,
     * или привязать департамент к дочерним департаментам своего дочернего департамента) будет выброшено исключение
     * {@link DepartmentChildParentException}. Также предусмотрена проверка на повторное присвоение родителя и
     * попытки перезаписи уже существующего родительского департамента.
     * </p>
     *
     * @param departmentId       Идентификатор родительского департамента.
     * @param DepartmentChildDto DTO дочернего департамента, который будет привязан.
     * @return DTO {@link DepartmentGetByIdResponseDto} ответа, представляющее обновлённый родительский департамент.
     * @throws DepartmentChildParentException если попытка установить связь не соответствует бизнес-логике.
     * @throws DepartmentChildException       если департамент уже содержится в списке дочерних департаментов.
     * @throws DepartmentParentException      если у дочернего департамента уже есть родительский департамент.
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentGetByIdResponseDto setParentDepartment(@Min(value = 1L, message = "Id cant be less than 1") Long departmentId,
                                                     @Valid DepartmentChildDto DepartmentChildDto);


    /**
     * Обновляет имя отдела с заданным идентификатором.
     * <p>
     * Этот метод находит отдел по его идентификатору и обновляет его имя,
     * используя предоставленные данные из DTO. В случае успеха, возвращает
     * DTO, отображающее обновленное состояние отдела.
     * </p>
     *
     * @param departmentId         Идентификатор отдела, который нужно обновить.
     * @param departmentRequestDto DTO, содержащий новое имя для отдела.
     * @return {@link DepartmentUpdateResponseDto} DTO, отображающее обновленное состояние отдела.
     * @throws DepartmentNotFoundException если отдел с данным идентификатором не найден.
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentUpdateResponseDto updateName(@Min(value = 1L, message = "Id cant be less than 1") Long departmentId, @Valid DepartmentRequestDto departmentRequestDto);

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
    DepartmentDeleteRecoverResponseDto deleteDepartment(@Min(value = 1L, message = "Id cant be less than 1") Long departmentId);

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
    DepartmentDeleteRecoverResponseDto recoverDepartment(@Min(value = 1L, message = "Id cant be less than 1") Long departmentId);

    /**
     * Возвращает департамент по указанному ID в виде {@link DepartmentGetByIdResponseDto}.
     * <p>
     * Этот метод осуществляет поиск департамента в базе данных по его ID.
     * Если департамент с заданным ID не найден, будет выброшено исключение {@link DepartmentNotFoundException}.
     * </p>
     *
     * @param departmentId ID департамента, который нужно получить.
     * @return DTO {@link DepartmentCreateResponseDto} ответа, представляющее найденный департамент.
     * @throws DepartmentNotFoundException если департамент с указанным ID не найден.
     * @author Khodov Nikita
     */
    @Transactional
    DepartmentGetByIdResponseDto getById(@Min(value = 1L, message = "Id cant be less than 1") Long departmentId);

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
     * @return Page {@link DepartmentGetAllResponseDto}, представляющий найденные департаменты.
     * @author Khodov Nikita
     */
    @Transactional
    Page<DepartmentGetAllResponseDto> getAllDepartmentsPageByPage(Pageable pageable, String name, RecordState recordState, String serialKey, @NotBlank(message = "Department creator username can't be blank.") String username);

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

    @Transactional(readOnly = true)
    List<ApplicationItemGetByIdResponseDto> findApplicationItemsByDepartmentIdByPage(
            @Min(value = 1L, message = "Id can't be less than 1.") Long id,
            String applicationName,
            ApplicationItemStatus status,
            RecordState recordState,
            Pageable pageable,
            String username);

}
