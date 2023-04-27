package com.scfg.core.common.util.mortgageRelief;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

public class ValidateInsuredsResponse implements Serializable {

    private String message;
    private int countInsuredsInOrder;
    private int countPreliminaryObservedeCases;

    public ValidateInsuredsResponse(){}

    public ValidateInsuredsResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public ValidateInsuredsResponse setCountInsuredsInOrder(int countInsuredsInOrder) {
        this.countInsuredsInOrder = countInsuredsInOrder;
        return this;
    }

    public ValidateInsuredsResponse setCountPreliminaryObservedeCases(int countPreliminaryObservedeCases) {
        this.countPreliminaryObservedeCases = countPreliminaryObservedeCases;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public int getCountInsuredsInOrder() {
        return countInsuredsInOrder;
    }

    public int getCountPreliminaryObservedeCases() {
        return countPreliminaryObservedeCases;
    }
    public void increaseInsuredInOrder(){
        this.countInsuredsInOrder++;
    }

    public void increasePreeliminaryObservedCase(){
        this.countPreliminaryObservedeCases++;
    }


}
