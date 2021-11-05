package com.team4.testingsystem.services.impl;

import com.team4.testingsystem.model.entity.Module;
import com.team4.testingsystem.exceptions.ModuleNotFoundException;
import com.team4.testingsystem.repositories.ModuleRepository;
import com.team4.testingsystem.utils.EntityCreatorUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ModuleServiceImplTest {
    @Mock
    private ModuleRepository moduleRepository;

    @InjectMocks
    private ModuleServiceImpl moduleService;

    @Test
    void getModuleById() {
        Module module = EntityCreatorUtil.createModule();
        Mockito.when(moduleRepository.findById(module.getId())).thenReturn(Optional.of(module));
        Module result = moduleService.getModuleById(module.getId());
        Assertions.assertEquals(module, result);
    }

    @Test
    void moduleByIdNotFoundException() {
        Mockito.when(moduleRepository.findById(1L)).thenThrow(new ModuleNotFoundException());
        Assertions.assertThrows(ModuleNotFoundException.class, () -> moduleService.getModuleById(1L));
    }

    @Test
    void getModuleByName() {
        Module module = EntityCreatorUtil.createModule();
        Mockito.when(moduleRepository.findByName(module.getName())).thenReturn(Optional.of(module));
        Module result = moduleService.getModuleByName(module.getName());
        Assertions.assertEquals(module, result);
    }


    @Test
    void moduleByNameNotFoundException() {
        Mockito.when(moduleRepository.findByName("name")).thenThrow(new ModuleNotFoundException());
        Assertions.assertThrows(ModuleNotFoundException.class, () -> moduleService.getModuleByName("name"));
    }
}