package br.com.ifba;

import br.com.ifba.infrastructure.util.SpringContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootCursoApplication {

    
    public static ApplicationContext CONTEXT;

    public static void main(String[] args) {
         CONTEXT = SpringApplication.run(SpringBootCursoApplication.class, args);
    SpringContext.init(CONTEXT); // Passa o contexto real para SpringContext

    javax.swing.SwingUtilities.invokeLater(() -> {
        new br.com.ifba.curso.view.CursoView().setVisible(true);
    });
    }
}
