package com.yapikredi.Permission.Service.scheduler;

import com.yapikredi.Permission.Service.service.PermissionSchedulerService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PermissionScheduler {

    private final PermissionSchedulerService permissionSchedulerService;

    @Scheduled(cron = "0 0 0 * * ?")
    public void permissionTask() {
        permissionSchedulerService.process(LocalDateTime.now());
    }
}
