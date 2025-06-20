package commands;

public enum CommandType {
    // Аутентификация и пользователи
    AUTHORIZATION,
    REGISTRATION,
    FULL_LIST_USERS,
    DELETE_USER,
    ADD_USER,
    UPDATE_USER,
    GET_ID,

    // Студенты
    ADD_STUDENT,
    UPDATE_STUDENT,
    GET_STUDENT,
    GET_STUDENT_BY_USER,
    LIST_STUDENTS,

    // Общежития
    ADD_DORMITORY,
    UPDATE_DORMITORY,
    DELETE_DORMITORY,
    GET_DORMITORY,
    LIST_DORMITORIES,

    // Комнаты
    ADD_ROOM,
    UPDATE_ROOM,
    DELETE_ROOM,
    GET_ROOM,
    LIST_ROOMS,
    LIST_AVAILABLE_ROOMS,

    // Заявления
    SUBMIT_APPLICATION,
    PROCESS_APPLICATION,
    CANCEL_APPLICATION,
    GET_APPLICATION,
    LIST_STUDENT_APPLICATIONS,
    LIST_DORMITORY_APPLICATIONS,
    LIST_ALL_APPLICATIONS
}
