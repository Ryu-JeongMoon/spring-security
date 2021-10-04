package io.security.springsecurity.service;

import io.security.springsecurity.domain.entity.Resources;
import io.security.springsecurity.repository.ResourcesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourcesService {

    private final ResourcesRepository ResourcesRepository;

    @Transactional(readOnly = true)
    public Resources selectResources(long id) {
        return ResourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional(readOnly = true)
    public List<Resources> selectResources() {
        return ResourcesRepository.findAll();
    }

    public void insertResources(Resources resources) {
        ResourcesRepository.save(resources);
    }

    public void deleteResources(long id) {
        ResourcesRepository.deleteById(id);
    }
}