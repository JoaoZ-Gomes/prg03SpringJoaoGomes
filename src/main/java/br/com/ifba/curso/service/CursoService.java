package br.com.ifba.curso.service;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.repository.CursoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // Marca essa classe como um "serviço" do Spring (parte da lógica do sistema)
@Slf4j // Gera automaticamente um logger (log) chamado "log"
@RequiredArgsConstructor // Cria um construtor com os campos final (cursoRepository, no caso)
public class CursoService {

    private final CursoRepository cursoRepository; // Repositório para acessar os dados no banco

    // Salva um novo curso no banco
    public void salvarCurso(Curso curso) {
        log.info("Salvando curso: {}", curso); // Log informando a tentativa de salvar

        // Verifica se o nome está vazio ou nulo
        if (curso.getNome() == null || curso.getNome().trim().isEmpty()) {
            log.warn("Tentativa de salvar curso sem nome."); // Log de aviso
            throw new RuntimeException("O nome do curso é obrigatório."); // Erro
        }

        // Verifica se a carga horária é inválida
        if (curso.getCargaHoraria() == null || curso.getCargaHoraria() <= 0) {
            log.warn("Tentativa de salvar curso com carga horária inválida: {}", curso.getCargaHoraria());
            throw new RuntimeException("A carga horária deve ser maior que zero.");
        }

        cursoRepository.save(curso); // Salva o curso no banco
        log.info("Curso salvo com sucesso.");
    }

    // Atualiza um curso existente
    public void atualizarCurso(Curso curso) {
        log.info("Atualizando curso: {}", curso);

        // Verifica se o curso tem ID (pra saber que já existe)
        if (curso.getId() == null) {
            throw new RuntimeException("ID do curso não pode ser nulo para atualizar.");
        }

        salvarCurso(curso); // Reaproveita a lógica de salvar com as mesmas validações
    }

    // Remove um curso do banco
    public void removerCurso(Curso curso) {
        log.info("Removendo curso: {}", curso);

        if (curso.getId() == null) {
            throw new RuntimeException("ID do curso não pode ser nulo para remover.");
        }

        cursoRepository.deleteById(curso.getId()); // Remove pelo ID
        log.info("Curso removido com sucesso.");
    }

    // Retorna todos os cursos do banco
    public List<Curso> listarTodos() {
        log.info("Listando todos os cursos.");
        return cursoRepository.findAll();
    }

    // Busca um curso pelo ID
    public Curso buscarPorId(Long id) {
        log.info("Buscando curso por ID: {}", id);

        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo para buscar curso.");
        }

        // Busca o curso e lança erro caso não exista
        return cursoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Curso não encontrado com ID: {}", id);
                    return new RuntimeException("Curso não encontrado com ID: " + id);
                });
    }

    // Busca cursos que contenham parte do nome (ignora maiúsculas/minúsculas)
    public List<Curso> buscarPorNome(String nome) {
        log.info("Buscando cursos por nome: {}", nome);
        return cursoRepository.findByNomeContainingIgnoreCase(nome);
    }

    // Busca cursos com carga horária maior que um valor específico
    public List<Curso> buscarPorCargaMaiorQue(int horas) {
        log.info("Buscando cursos com carga horária maior que {}", horas);
        return cursoRepository.findByCargaHorariaGreaterThan(horas);
    }

    // Busca cursos dentro de um intervalo de carga horária
    public List<Curso> buscarEntreHoras(int min, int max) {
        log.info("Buscando cursos com carga horária entre {} e {}", min, max);
        return cursoRepository.findByCargaHorariaBetween(min, max);
    }
}
