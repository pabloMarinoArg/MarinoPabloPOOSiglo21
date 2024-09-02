package src.service;

import src.model.StorageStructure;
import src.repository.GeneralRepository;

import java.util.List;

public class StorableItemService {
    private GeneralRepository repository;

    public StorableItemService(GeneralRepository repository) {
        this.repository = repository;
    }

    public List<StorageStructure> getListOfStorageStructures() {
        return repository.getStorageStructureRepository();
    }
}
