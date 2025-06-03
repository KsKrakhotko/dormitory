package service;

import connect.Response;
import dto.DormitoryDTO;

public interface DormitoryService {
    Response addDormitory(DormitoryDTO dormitoryDTO);
    Response updateDormitory(DormitoryDTO dormitoryDTO);
    Response deleteDormitory(Long id);
    Response getDormitory(Long id);
    Response getAllDormitories();
} 