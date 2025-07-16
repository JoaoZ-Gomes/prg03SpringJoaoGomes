package br.com.ifba.infrastructure.util;

import org.springframework.context.ApplicationContext;

public class SpringContext {
    private static ApplicationContext context;

    public static void init(ApplicationContext context) {
        SpringContext.context = context;
    }

    public static <T> T getBean(Class<T> requiredType) {
        if (context == null) {
            throw new IllegalStateException("Contexto Spring não inicializado!");
        }
        return context.getBean(requiredType);
    }
}
