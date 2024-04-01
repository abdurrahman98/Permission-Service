package com.yapikredi.Permission.Service.service;

import com.yapikredi.Permission.Service.persistence.entity.PermissionTypeEntity;
import com.yapikredi.Permission.Service.persistence.repository.PermissionTypeRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PermissionTypeServiceTest {
    @Mock
    PermissionTypeRepository permissionTypeRepository;
    @InjectMocks
    PermissionTypeService permissionTypeService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllPermissionType_ShouldReturnAllPermissionTypes() {
        PermissionTypeEntity permissionType1 = new PermissionTypeEntity();
        permissionType1.setId(1L);
        permissionType1.setName("Type 1");
        permissionType1.setAmount(BigDecimal.TEN);
        permissionType1.setStartYears(1L);
        permissionType1.setEndYears(5L);

        PermissionTypeEntity permissionType2 = new PermissionTypeEntity();
        permissionType2.setId(2L);
        permissionType2.setName("Type 2");
        permissionType2.setAmount(BigDecimal.valueOf(15));
        permissionType2.setStartYears(6L);
        permissionType2.setEndYears(10L);

        List<PermissionTypeEntity> permissionTypes = Arrays.asList(permissionType1, permissionType2);

        when(permissionTypeRepository.findAll()).thenReturn(permissionTypes);

        List<PermissionTypeEntity> result = permissionTypeService.getAllPermissionType();

        assertEquals(permissionTypes.size(), result.size());
        assertEquals(permissionTypes.get(0).getId(), result.get(0).getId());
        assertEquals(permissionTypes.get(0).getName(), result.get(0).getName());
        assertEquals(permissionTypes.get(0).getAmount(), result.get(0).getAmount());
        assertEquals(permissionTypes.get(0).getStartYears(), result.get(0).getStartYears());
        assertEquals(permissionTypes.get(0).getEndYears(), result.get(0).getEndYears());

        assertEquals(permissionTypes.get(1).getId(), result.get(1).getId());
        assertEquals(permissionTypes.get(1).getName(), result.get(1).getName());
        assertEquals(permissionTypes.get(1).getAmount(), result.get(1).getAmount());
        assertEquals(permissionTypes.get(1).getStartYears(), result.get(1).getStartYears());
        assertEquals(permissionTypes.get(1).getEndYears(), result.get(1).getEndYears());

        verify(permissionTypeRepository, times(1)).findAll();
    }
}
