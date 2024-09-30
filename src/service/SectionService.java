package src.service;

import src.model.Section;
import src.model.StorageStructure;
import src.repository.GeneralRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SectionService {

    private final GeneralRepository repository;

    public SectionService() {
        this.repository = GeneralRepository.getInstance();
    }

    public Optional<StorageStructure> getStorageStructureById(Section section, Long rackId) {
        List<StorageStructure> racksList = section.getRackList();

        if(racksList.isEmpty()) {
            return Optional.empty();
        }

        return racksList.stream()
                .filter(ss -> Objects.equals(ss.getId(), rackId))
                .findFirst();
    }


}
