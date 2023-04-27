package com.scfg.core.adapter.web;

import com.scfg.core.application.port.in.ChangeLogUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(path = ChangeLogEndPoint.BASE)
@Api(value = "API REST Bitácora")
public class ChangeLogController implements ChangeLogEndPoint {

    private final ChangeLogUseCase changeLogUseCase;

    @GetMapping(value = ChangeLogEndPoint.PARAM_PAGE)
    @ApiOperation(value = "Retorna una lista de bitácoras")
    ResponseEntity get(@PathVariable int page, @PathVariable int size) {
        Object changeLogs = changeLogUseCase.getByPage(page, size);
        return ok(changeLogs);
    }
}
