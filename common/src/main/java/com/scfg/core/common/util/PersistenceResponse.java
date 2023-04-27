package com.scfg.core.common.util;

import com.scfg.core.common.enums.ActionRequestEnum;
import lombok.*;

import java.io.Serializable;

public class PersistenceResponse<T> implements Serializable {

    private String resourceName;
    private ActionRequestEnum actionRequestEnum;
    private T data;

    public PersistenceResponse() {
    }

    public PersistenceResponse(ActionRequestEnum actionRequestEnum) {
        this.actionRequestEnum = actionRequestEnum;
    }

    public PersistenceResponse(String resourceName, ActionRequestEnum actionRequestEnum, T data) {
        this.resourceName = resourceName;
        this.actionRequestEnum = actionRequestEnum;
        this.data = data;
    }

    public PersistenceResponse(ActionRequestEnum actionRequestEnum, T data) {
        this.actionRequestEnum = actionRequestEnum;
        this.data = data;
    }

    public String getResourceName() {
        return resourceName;
    }

    public PersistenceResponse setResourceName(String resourceName) {
        this.resourceName = resourceName;return this;
    }

    public ActionRequestEnum getActionRequestEnum() {
        return actionRequestEnum;
    }

    public PersistenceResponse setActionRequestEnum(ActionRequestEnum actionRequestEnum) {
        this.actionRequestEnum = actionRequestEnum;
        return this;
    }

    public T getData() {
        return data;
    }

    public PersistenceResponse setData(T data) {
        this.data = data;
        return this;
    }
}
