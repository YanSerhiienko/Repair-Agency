package com.bitsva.RepairAgency.config;

import com.bitsva.RepairAgency.feature.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 100L;

    UserRole role() default UserRole.ROLE_CLIENT;
}
