package com.yapikredi.Permission.Service.scheduler;

import com.yapikredi.Permission.Service.service.PermissionSchedulerService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class PermissionSchedulerTest {

    @InjectMocks
    private PermissionScheduler permissionScheduler;

    @Mock
    private PermissionSchedulerService permissionSchedulerService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void permissionTask_ShouldInvokeProcessMethodOfPermissionSchedulerService_WithCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();

        permissionScheduler.permissionTask();

        verify(permissionSchedulerService, times(1)).process(any());
    }
}
