package service_impl;

import connect.Response;
import dto.DormitoryDTO;
import entity.Dormitory;
import service.DormitoryService;
import workWithHibernate.DormitoryHibernate;

import java.util.ArrayList;
import java.util.List;

public class DormitoryServiceImpl implements DormitoryService {

    @Override
    public Response addDormitory(DormitoryDTO dormitoryDTO) {
        try {
            // Validate required fields
            if (dormitoryDTO == null) {
                return new Response(-1, "Данные общежития не могут быть пустыми");
            }
            if (dormitoryDTO.getNumber() == null) {
                return new Response(-1, "Номер общежития не может быть пустым");
            }
            if (dormitoryDTO.getName() == null || dormitoryDTO.getName().trim().isEmpty()) {
                return new Response(-1, "Название общежития не может быть пустым");
            }
            if (dormitoryDTO.getAddress() == null || dormitoryDTO.getAddress().trim().isEmpty()) {
                return new Response(-1, "Адрес общежития не может быть пустым");
            }
            if (dormitoryDTO.getTotalCapacity() == null || dormitoryDTO.getTotalCapacity() <= 0) {
                return new Response(-1, "Вместимость общежития должна быть положительным числом");
            }

            // Create and populate the dormitory entity
            Dormitory dormitory = new Dormitory();
            dormitory.setNumber(dormitoryDTO.getNumber());
            dormitory.setName(dormitoryDTO.getName().trim());
            dormitory.setAddress(dormitoryDTO.getAddress().trim());
            // This will set both totalCapacity and capacity fields
            dormitory.setTotalCapacity(dormitoryDTO.getTotalCapacity());
            dormitory.setRooms(new ArrayList<>());
            dormitory.setApplications(new ArrayList<>());

            DormitoryHibernate.addDormitory(dormitory);
            return new Response(1, "Общежитие успешно добавлено");
        } catch (Exception e) {
            return new Response(-1, "Ошибка при добавлении общежития: " + e.getMessage());
        }
    }

    @Override
    public Response updateDormitory(DormitoryDTO dormitoryDTO) {
        try {
            // Validate required fields
            if (dormitoryDTO == null) {
                return new Response(-1, "Данные общежития не могут быть пустыми");
            }
            if (dormitoryDTO.getId() == null) {
                return new Response(-1, "ID общежития не может быть пустым");
            }
            if (dormitoryDTO.getNumber() == null) {
                return new Response(-1, "Номер общежития не может быть пустым");
            }
            if (dormitoryDTO.getAddress() == null || dormitoryDTO.getAddress().trim().isEmpty()) {
                return new Response(-1, "Адрес общежития не может быть пустым");
            }
            if (dormitoryDTO.getTotalCapacity() == null || dormitoryDTO.getTotalCapacity() <= 0) {
                return new Response(-1, "Вместимость общежития должна быть положительным числом");
            }

            Dormitory dormitory = DormitoryHibernate.getDormitoryById(dormitoryDTO.getId());
            if (dormitory == null) {
                return new Response(-1, "Общежитие не найдено");
            }

            dormitory.setNumber(dormitoryDTO.getNumber());
            dormitory.setAddress(dormitoryDTO.getAddress().trim());
            dormitory.setTotalCapacity(dormitoryDTO.getTotalCapacity());

            DormitoryHibernate.updateDormitory(dormitory);
            return new Response(1, "Общежитие успешно обновлено");
        } catch (Exception e) {
            return new Response(-1, "Ошибка при обновлении общежития: " + e.getMessage());
        }
    }

    @Override
    public Response deleteDormitory(Long id) {
        try {
            Dormitory dormitory = DormitoryHibernate.getDormitoryById(id);
            if (dormitory == null) {
                return new Response(-1, "Общежитие не найдено");
            }

            if (!dormitory.getRooms().isEmpty()) {
                return new Response(-1, "Невозможно удалить общежитие, в котором есть комнаты");
            }

            DormitoryHibernate.deleteDormitory(id);
            return new Response(1, "Общежитие успешно удалено");
        } catch (Exception e) {
            return new Response(-1, "Ошибка при удалении общежития: " + e.getMessage());
        }
    }

    @Override
    public Response getDormitory(Long id) {
        try {
            Dormitory dormitory = DormitoryHibernate.getDormitoryById(id);
            if (dormitory == null) {
                return new Response(-1, "Общежитие не найдено");
            }

            DormitoryDTO dto = convertToDTO(dormitory);
            return new Response(1, dto);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при получении информации об общежитии: " + e.getMessage());
        }
    }

    @Override
    public Response getAllDormitories() {
        try {
            List<Dormitory> dormitories = DormitoryHibernate.getAllDormitories();
            List<DormitoryDTO> dtos = new ArrayList<>();

            for (Dormitory dormitory : dormitories) {
                dtos.add(convertToDTO(dormitory));
            }

            return new Response(1, dtos);
        } catch (Exception e) {
            return new Response(-1, "Ошибка при получении списка общежитий: " + e.getMessage());
        }
    }

    private DormitoryDTO convertToDTO(Dormitory dormitory) {
        int occupiedSpaces = DormitoryHibernate.getOccupiedSpaces(dormitory.getId());
        int availablePlaces = dormitory.getTotalCapacity() - occupiedSpaces;

        return new DormitoryDTO(
            dormitory.getId(),
            dormitory.getNumber(),
            dormitory.getName(),
            dormitory.getAddress(),
            dormitory.getTotalCapacity(),
            availablePlaces
        );
    }
} 