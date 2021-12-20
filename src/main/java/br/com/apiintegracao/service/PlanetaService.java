package br.com.apiintegracao.service;

import br.com.apiintegracao.dto.PlanetaDTO;
import br.com.apiintegracao.model.Planeta;

import java.util.List;
import java.util.Optional;

/**
 * Interface com os métodos que devem ser implementados obrigatoriamente na classe PlanetaServiceImpl
 */
public interface PlanetaService {

    String obterQtdAparicaoFilme(String nomePlaneta);
    Planeta adicionar(PlanetaDTO planeta, Long id);
    List<Planeta> listar();
    List<Planeta> buscarPorNome(String nome);
    Optional<Planeta> buscarPorId(Long id);
    void remover(Long id);
}