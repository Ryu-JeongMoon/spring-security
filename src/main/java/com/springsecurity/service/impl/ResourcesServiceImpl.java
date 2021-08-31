package com.springsecurity.service.impl;

import com.springsecurity.domain.entity.Resources;
import com.springsecurity.repository.ResourcesRepository;
import com.springsecurity.service.ResourcesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class ResourcesServiceImpl implements ResourcesService {

    @Autowired
    private com.springsecurity.repository.ResourcesRepository resourcesRepository;

    @Transactional
    public Resources selectResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> selectResources() {
        return resourcesRepository.findAll();
    }

    @Transactional
    public void insertResources(Resources resources){
        ResourcesRepository.save(resources);
    }

    @Transactional
    public void deleteResources(long id) {
        ResourcesRepository.deleteById(id);
    }
}