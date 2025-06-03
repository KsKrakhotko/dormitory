package entity;
import java.time.LocalDate;
import java.time.LocalDateTime;
/**
 * Класс Report представляет отчет по заселению в общежитие
 */
public class Report {
    // Уникальный идентификатор отчета
    private Long id;

    // Номер отчета (например: "REP-2024-001")
    private String reportNumber;

    // Дата и время генерации отчета
    private LocalDateTime generationDate;

    // Начало отчетного периода
    private LocalDate periodStart;

    // Конец отчетного периода
    private LocalDate periodEnd;

    // Идентификатор общежития, по которому составлен отчет
    private Long dormitoryId;

    // Общее количество комнат в общежитии
    private Integer totalRooms;

    // Количество занятых комнат
    private Integer occupiedRooms;

    // Количество свободных комнат
    private Integer availableRooms;

    // Общая вместимость общежития (количество мест)
    private Integer totalCapacity;

    // Количество заселенных студентов
    private Integer occupiedSpaces;

    // Статус отчета (например: DRAFT, FINAL, ARCHIVED)
    private String status;

    // Комментарии к отчету
    private String notes;
}
