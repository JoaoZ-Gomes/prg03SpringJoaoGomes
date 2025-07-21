package br.com.ifba.infrastructure.util;

import org.springframework.context.ApplicationContext;

/**
 * Classe utilitária para acessar o contexto do Spring em qualquer parte da aplicação.
 * Isso é útil especialmente quando você precisa acessar beans gerenciados pelo Spring
 * em classes que não são gerenciadas automaticamente (como algumas classes utilitárias).
 */
public class SpringContext {

    // Variável estática que armazena o contexto da aplicação Spring
    private static ApplicationContext context;

    /**
     * Inicializa o contexto do Spring. Esse método deve ser chamado assim que a aplicação
     * estiver carregada, normalmente no início da execução.
     *
     * @param context O contexto do Spring que será usado globalmente
     */
    public static void init(ApplicationContext context) {
        SpringContext.context = context;
    }

    /**
     * Recupera um bean do contexto Spring, com base na classe informada.
     * 
     * @param requiredType Classe do bean desejado
     * @param <T> Tipo do bean
     * @return O bean encontrado no contexto
     * @throws IllegalStateException se o contexto ainda não tiver sido inicializado
     */
    public static <T> T getBean(Class<T> requiredType) {
        if (context == null) {
            throw new IllegalStateException("Contexto Spring não inicializado!");
        }
        return context.getBean(requiredType);
    }
}
