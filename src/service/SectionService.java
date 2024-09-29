package src.service;

import src.errors.EmptyException;
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

    public Optional<StorageStructure> getStorageStructureById(Section section, Long rackId) throws EmptyException {
        List<StorageStructure> racksList = section.getRackList();

        if(racksList.isEmpty()) {
            throw new EmptyException("La estanteria no existe, "+rackId);
        }

        return racksList.stream()
                .filter(ss -> Objects.equals(ss.getId(), rackId))
                .findFirst();
    }


}
