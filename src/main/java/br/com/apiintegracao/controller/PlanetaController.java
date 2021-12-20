package br.com.apiintegracao.controller;

import br.com.apiintegracao.controller.context.ContextController;
import br.com.apiintegracao.dto.PlanetaDTO;
import br.com.apiintegracao.model.Planeta;
import br.com.apiintegracao.service.PlanetaService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

// Anotação que informa a documentação do Swagger a definição do título
@OpenAPIDefinition(info = @Info(title = "API que contém os dados dos planetas do Star Wars."))
// Anotação indica que a classe é um controlador REST
@RestController
// Anotação que indica a rota em que chegará a requisição no contralador
@RequestMapping(ContextController.API)
// Classe que tem como principal objetivo é direcionar o fluxo da aplicação mapeando e direcionado as ações recebida (request)
// pela camada da apresentação para os respectivos serviços da aplicação.
public class PlanetaController {

    // O Autowired (@Autowired) é a anotação mais utiliza em relação à injeção de dependências
    // Autowired, indica um ponto aonde a injeção automática deve ser aplicada.
    // Esta pode ser usada em métodos, atributos e construtores.
    @Autowired
    PlanetaService planetaService;

    // Anotação obtêm o verbo HTTP e adiciona um resumo na documentação do Swagger
    @Operation(summary = "Adicionar planeta")
    // Anotação que retorna respostas da API com descrição informada
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Planeta adicionado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Planeta não adicionado", content = @Content)})
    // Anotação relacionada ao verbo POST das requisições HTTP
    @PostMapping(value = ContextController.PLANETAS, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    // Anotação @Valid indica que será validado os dados mapeados na classe PlanetaDTO
    // Anotação @RequestBody indica que um parâmetro do método deve ser associado ao corpo da solicitação HTTP
    // O método adicionar tem como finalidade registrar um novo planeta na tabela Planeta
    // BindingResult é uma interface que determina como o objeto que armazena
    // o resultado da validação deve armazenar e recuperar o resultado da validação (erros, tentativa de vinculação a campos não permitidos, etc.)
    public ResponseEntity<Object> adicionar(@Valid @RequestBody PlanetaDTO planetaDto, BindingResult result) {

        // Criado objeto Map de chave String e valor Object que irá retornar os erros caso ocorram na resposta para o requisitante
        Map<String, Object> response = new HashMap<>();
        // Realizada busca no banco de dados pelo nome do Planeta
        var planetaExiste = planetaService.buscarPorNome(planetaDto.getNome());

        // Caso seja retornado algum erro, será retornada uma lista com os erros
        if (result.hasErrors()) {
            // Mapeamento feito para cada erro sendo armazenado em errors
            List<String> errors = result.getFieldErrors().stream().map(err -> "O campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Caso o nome do planeta informado na requisição exista no banco de dados, será retornado essa informação ao requisitante
        if (!planetaExiste.isEmpty()) {
            response.put("Error", "O planeta ".concat(planetaDto.getNome()).concat(" já existe na base de dados"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Caso não exista o planeta, o mesmo será armazenado no banco de dados
        var planeta = planetaService.adicionar(planetaDto, null);
        return ResponseEntity.created(URI.create(ContextController.API + "planetas")).body(planeta);
    }

    @Operation(summary = "Obter planetas ou obter planeta pelo nome")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planetas obtidos ou planeta obtido pelo nome", content = @Content)})
    // Anotação relacionada ao verbo GET das requisições HTTP
    @GetMapping(ContextController.PLANETAS)
    // Este método retorna uma lista de todos os planetas, caso não seja passado nenhum nome
    // caso seja passado o nome irá retornar uma lista que contenha os planos com todos os nomes
    public ResponseEntity<List<Planeta>> listar(@RequestParam(required = false) String nome) {
        return ResponseEntity.ok().body((nome == null || nome.isBlank()) ? planetaService.listar() : planetaService.buscarPorNome(nome));
    }

    @Operation(summary = "Obter planeta pelo id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planeta obtido pelo id", content = @Content)})
    @GetMapping(ContextController.PLANETAS_ID)
    public ResponseEntity<Optional<Planeta>> obterPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(planetaService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar planeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planeta atualizado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Planeta não atualizado", content = @Content)})
    @PatchMapping(ContextController.PLANETAS_ID)
    public ResponseEntity<Object> atualizar(@PathVariable Long id, @Valid @RequestBody PlanetaDTO planetaDto, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        var planetasExistentes = planetaService.listar();
        Optional<Planeta> planetaDestaAtualizacao = planetaService.buscarPorId(id);

        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(err -> "O campo '" + err.getField() + "' " + err.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errors", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if ((planetaDestaAtualizacao.isPresent() && (!planetaDestaAtualizacao.get().getNome().equals(planetaDto.getNome()))) && planetasExistentes.toString().contains(planetaDto.getNome())) {
            response.put("Error", "O planeta ".concat(planetaDto.getNome()).concat(" já existe na base de dados"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var planeta = planetaService.adicionar(planetaDto, id);
        return ResponseEntity.ok(planeta);
    }

    @Operation(summary = "Deletar planeta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Planeta deletado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Planeta não deletado", content = @Content)})
    @DeleteMapping(ContextController.PLANETAS_ID)
    public ResponseEntity<HttpStatus> remover(@PathVariable Long id) {
        planetaService.remover(id);
        return ResponseEntity.ok().build();
    }
}