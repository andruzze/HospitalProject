package com.akulov.hospital.controller;

import com.akulov.hospital.dao.entitydao.DrugDAO;
import com.akulov.hospital.model.Model;
import com.akulov.hospital.model.dto.entity.DrugDTO;

import java.util.Collection;
import java.util.Map;

public class ControllerImpl {
    private final Model model;
    private final DrugDAO drugDAO;

    public ControllerImpl(Model model, DrugDAO drugDAO) {
        this.model = model;
        this.drugDAO = drugDAO;
    }

    /**
     * Получает все записи из таблицы препаратов.
     */
    public Collection<DrugDTO> getAllDrugs() {
        return model.getAllRecords(drugDAO);
    }

    /**
     * Добавляет новый препарат.
     */
    public void addDrug(DrugDTO drug) {
        model.addRecord(drugDAO, drug);
    }

    /**
     * Обновляет существующий препарат.
     */
    public void updateDrug(DrugDTO drug, Integer id) {
        model.updateRecord(drugDAO, drug, Map.of("id", id));
    }

    /**
     * Удаляет препарат по ID.
     */
    public void deleteDrug(Integer id) {
        model.deleteRecord(drugDAO, id);
    }
}
