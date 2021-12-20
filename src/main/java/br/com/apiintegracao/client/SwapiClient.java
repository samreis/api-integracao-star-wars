package br.com.apiintegracao.client;

import br.com.apiintegracao.client.context.ContextClient;
import br.com.apiintegracao.client.request.PlanetasRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * Anotamos a interface com @FeignClient e isto informa ao OpenFeign que estamos definindo um cliente REST.
 * O atributo value é livre e serve apenas para identificação.
 * Em url faremos referência à propriedade api.star.wars definida no arquivo de propriedades
 */
@FeignClient(value = "swapi", url = "${api.star.wars}")
public interface SwapiClient {

//    @GetMapping(value = ContextClient.OBTER_PLANETA, produces = "application/json")
//    Map<String, Object> obterPlanetaMap(@PathVariable Integer numero);

    /**
     * Obter os planetas por página da API do Star Wars, informando o atributo page
     * @param page
     * @return PlanetasRequest
     */
    @GetMapping(value = ContextClient.OBTER_PLANETAS, produces = "application/json")
    PlanetasRequest obterPlanetas(@RequestParam("page") Integer page);

//    @GetMapping(value = ContextClient.OBTER_PLANETAS, produces = "application/json")
//    Map<String, Object> obterPlanetasMap(@RequestParam("page") Integer page);
}