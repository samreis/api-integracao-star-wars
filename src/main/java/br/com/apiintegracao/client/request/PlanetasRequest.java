package br.com.apiintegracao.client.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
/**
 * Classe que obtém os planetas por página
 */
public class PlanetasRequest {

    // contador total de todos os planetas existentes na API do Star Wars
    private Integer count;
    // próxima página da API do Star Wars
    private String next;
    // próxima anterior da API do Star Wars
    private String previous;
    // resultado dos planetas com suas variáveis da página atual (retorna 10 planetas por página)
    private List<Object> results;
}