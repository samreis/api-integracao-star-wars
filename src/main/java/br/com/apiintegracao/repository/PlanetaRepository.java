package br.com.apiintegracao.repository;

import br.com.apiintegracao.model.Planeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanetaRepository extends JpaRepository<Planeta, Long> {

    List<Planeta> findByNomeIgnoreCaseContaining(String nome);
    @Override
    void deleteById(@NonNull Long id);
}
