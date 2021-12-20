package br.com.apiintegracao.service.implementation;

import br.com.apiintegracao.client.SwapiClient;
import br.com.apiintegracao.client.request.PlanetasRequest;
import br.com.apiintegracao.dto.PlanetaDTO;
import br.com.apiintegracao.model.Planeta;
import br.com.apiintegracao.repository.PlanetaRepository;
import br.com.apiintegracao.service.PlanetaService;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Instant;
import java.util.*;

@Slf4j
@Service
// Classe que é um serviço detentor das regras de negócio, manipula as informações vindas via requisição do controller
// e retornam a resposta para o controller devolver para a aplicação.
public class PlanetaServiceImpl implements PlanetaService {

    private static final Logger logger = LoggerFactory.getLogger(PlanetaServiceImpl.class);

    // O Autowired (@Autowired) é a anotação mais utiliza em relação à injeção de dependências
    // Autowired, indica um ponto aonde a injeção automática deve ser aplicada.
    // Esta pode ser usada em métodos, atributos e construtores.
    @Autowired
    SwapiClient swapiClient;

    @Autowired
    PlanetaRepository planetaRepository;

    // Anotação Override indica que o método esta sendo sobrescrito
    @Override
    // Este mét
    public String obterQtdAparicaoFilme(String nomePlaneta) {

        var qtdAparicaoFilme = "";

        List<PlanetasRequest> planetasRequestList = new ArrayList<>();

        Flowable.range(1, 6)
                .flatMap(v ->
                        Flowable.just(swapiClient.obterPlanetas(v))
                                .subscribeOn(Schedulers.computation())
                                .map(planetasRequestList::add))
                .blockingSubscribe(v -> logger.info("{}", Date.from(Instant.now())));

        for (Object o : planetasRequestList.toArray()) {
            PlanetasRequest request = (PlanetasRequest) o;
            for (Object p : request.getResults()) {
                if (((LinkedHashMap) p).get("name").equals(nomePlaneta)) {
                    LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap) p;
                    ArrayList<Object> arrayList = (ArrayList) linkedHashMap.get("films");
                    qtdAparicaoFilme = String.valueOf(arrayList.size());
                    logger.info("Tempo execução obter quantidade aparição filmes na SWAPI {}", Date.from(Instant.now()));
                }
            }
        }
        return qtdAparicaoFilme.isEmpty() ? "Este planeta não existe na SWAPI - The Star Wars API" : qtdAparicaoFilme;
    }

    @Override
    public Planeta adicionar(PlanetaDTO planeta, Long id) {
        var novoPlaneta = new Planeta(id, planeta.getNome(), planeta.getClima(), planeta.getTerreno(),
                obterQtdAparicaoFilme(planeta.getNome()));
        logger.info("Adicionado novo planeta: {}", planetaRepository.save(novoPlaneta));
        return novoPlaneta;
    }

    @Override
    public List<Planeta> listar() {
        List<Planeta> planetas = planetaRepository.findAll();
        logger.info("Listagem de planetas: {}", planetas);
        return planetas;
    }

    @Override
    public List<Planeta> buscarPorNome(String nome) {
        var planeta = planetaRepository.findByNomeIgnoreCaseContaining(nome);
        logger.info("Listagem de planetas obtidos pelo nome: {}", planeta);
        return planeta;
    }

    @Override
    public Optional<Planeta> buscarPorId(Long id) {
        Optional<Planeta> planeta = planetaRepository.findById(id);
        logger.info("Planeta obtido pelo id: {}", planeta);
        return planeta;
    }

    @Override
    public void remover(@PathVariable Long id) {
        planetaRepository.deleteById(id);
        logger.info("Planeta removido id: {}", id);
    }
}