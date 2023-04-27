package com.scfg.core.application.service;

import com.scfg.core.application.port.in.ChangeLogUseCase;
import com.scfg.core.application.port.out.ChangeLogPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeLogService implements ChangeLogUseCase {

    private final ChangeLogPort changeLogPort;

    @Override
    public Object getByPage(int page, int size) {
        return changeLogPort.findAllByPage(page, size);
    }
}
