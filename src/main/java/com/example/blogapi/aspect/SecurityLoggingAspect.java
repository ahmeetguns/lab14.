package com.example.secureblog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(SecurityLoggingAspect.class);

    @Before("execution(* com.example.secureblog.controller.AuthController.login(..))")
    public void logLoginAttempt(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] != null) {
            String username = args[0].toString();
            logger.info("Login attempt for user: {}", maskUsername(username));
        }
    }

    @AfterThrowing(
        pointcut = "execution(* com.example.secureblog.controller.AuthController.*(..))",
        throwing = "ex"
    )
    public void logAuthenticationFailure(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        logger.warn("Authentication failure in method {}: {}", methodName, ex.getMessage());
    }

    @Before("execution(* com.example.secureblog.controller.*.*(..))")
    public void logSecuredEndpointAccess(JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            String methodName = joinPoint.getSignature().getName();
            logger.info("User {} accessing secured endpoint: {}", maskUsername(username), methodName);
        }
    }

    private String maskUsername(String username) {
        if (username == null || username.length() <= 4) {
            return "***";
        }
        return username.substring(0, 2) + "***" + username.substring(username.length() - 2);
    }
}
