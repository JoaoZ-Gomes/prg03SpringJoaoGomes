package br.com.ifba.curso.service;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursoService {

    private final CursoRepository cursoRepository;

    @Autowired
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    public void salvarCurso(Curso curso) {
        if (curso.getNome() == null || curso.getNome().trim().isEmpty()) {
            throw new RuntimeException("O nome do curso é obrigatório.");
        }

        if (curso.getCargaHoraria() == null || curso.getCargaHoraria() <= 0) {
            throw new RuntimeException("A carga horária deve ser maior que zero.");
        }

        cursoRepository.save(curso);
    }

    public void atualizarCurso(Curso curso) {
        if (curso.getId() == null) {
            throw new RuntimeException("ID do curso não pode ser nulo para atualizar.");
        }

        salvarCurso(curso); // Valida e salva
    }

    public void removerCurso(Curso curso) {
        if (curso.getId() == null) {
            throw new RuntimeException("ID do curso não pode ser nulo para remover.");
        }

        cursoRepository.deleteById(curso.getId());
    }

    public List<Curso> listarTodos() {
        return cursoRepository.findAll();
    }

    public Curso buscarPorId(Long id) {
        if (id == null) {
            throw new RuntimeException("ID não pode ser nulo para buscar curso.");
        }

        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado com ID: " + id));
    }
    public List<Curso> buscarPorNome(String nome) {
    return cursoRepository.findByNomeContainingIgnoreCase(nome);
}

public List<Curso> buscarPorCargaMaiorQue(int horas) {
    return cursoRepository.findByCargaHorariaGreaterThan(horas);
}

public List<Curso> buscarEntreHoras(int min, int max) {
    return cursoRepository.findByCargaHorariaBetween(min, max);
}

}
