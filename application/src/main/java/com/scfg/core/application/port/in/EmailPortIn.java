package com.scfg.core.application.port.in;

import com.scfg.core.domain.Emailbody;

import javax.mail.MessagingException;

public interface EmailPortIn {

    boolean sendEmail(Emailbody emailbody) throws MessagingException;
    boolean sendNewEmail(Emailbody emailbody);
    void saveEmail(Emailbody emailbody);
}
