package com.springsecurity.service;


import com.springsecurity.domain.entity.Resources;

import java.util.List;

public interface ResourcesService {

    Resources selectResources(long id);

    List<Resources> selectResources();

    void insertResources(Resources Resources);

    void deleteResources(long id);
}