package br.com.ifba.curso.controller;

import br.com.ifba.curso.entity.Curso;
import br.com.ifba.curso.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
public class CursoController {

    private final CursoService cursoService;

    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    // POST /api/cursos
    @PostMapping
    public void salvarCurso(@RequestBody Curso curso) {
        cursoService.salvarCurso(curso);
    }

    // GET /api/cursos/{id}
    @GetMapping("/{id}")
    public Curso buscarCurso(@PathVariable Long id) {
        return cursoService.buscarPorId(id);
    }

    // PUT /api/cursos
    @PutMapping
    public void atualizarCurso(@RequestBody Curso curso) {
        cursoService.atualizarCurso(curso);
    }

    // DELETE /api/cursos/{id}
    @DeleteMapping("/{id}")
    public void removerCurso(@PathVariable Long id) {
        Curso curso = cursoService.buscarPorId(id); // Garante que existe antes de deletar
        cursoService.removerCurso(curso);
    }

    // GET /api/cursos
    @GetMapping
    public List<Curso> listarCursos() {
        return cursoService.listarTodos();
    }

    // GET /api/cursos/busca?nome=java
    @GetMapping("/busca")
    public List<Curso> buscarPorNome(@RequestParam String nome) {
        return cursoService.buscarPorNome(nome);
    }

    // GET /api/cursos/carga-maior?horas=20
    @GetMapping("/carga-maior")
    public List<Curso> buscarPorCargaMaiorQue(@RequestParam int horas) {
        return cursoService.buscarPorCargaMaiorQue(horas);
    }

    // GET /api/cursos/entre?min=10&max=40
    @GetMapping("/entre")
    public List<Curso> buscarEntreHoras(@RequestParam int min, @RequestParam int max) {
        return cursoService.buscarEntreHoras(min, max);
    }
}
