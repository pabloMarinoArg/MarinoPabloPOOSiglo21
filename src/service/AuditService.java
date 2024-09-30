package src.service;

import src.model.Audits;
import src.repository.GeneralRepository;

public class AuditService {

    private GeneralRepository repository;

    public AuditService() {
        this.repository = GeneralRepository.getInstance();
    }

    public void addAuditToList(Audits audit) {
       var events = repository.getAuditEvents();
       events.add(audit);
    }
}
