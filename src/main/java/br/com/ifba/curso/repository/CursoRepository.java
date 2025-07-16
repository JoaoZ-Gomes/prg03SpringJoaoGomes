package br.com.ifba.curso.repository;

import br.com.ifba.curso.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca todos os cursos cujo nome contenha a string (ignorando maiúsculas/minúsculas)
     * Exemplo: findByNomeContainingIgnoreCase("java") retorna "Java Básico", "Curso de Java", etc.
     */
    List<Curso> findByNomeContainingIgnoreCase(String nome);

    /**
     * Busca cursos com carga horária maior que o valor fornecido
     * Exemplo: findByCargaHorariaGreaterThan(20) retorna cursos com mais de 20 horas
     */
    List<Curso> findByCargaHorariaGreaterThan(Integer cargaHoraria);

    /**
     * Busca cursos com carga horária entre dois valores
     * Exemplo: findByCargaHorariaBetween(10, 40)
     */
    List<Curso> findByCargaHorariaBetween(Integer min, Integer max);

    /**
     * Busca todos os cursos com carga horária igual
     */
    List<Curso> findByCargaHoraria(Integer cargaHoraria);
}
