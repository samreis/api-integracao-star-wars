package br.com.apiintegracao.dto;

import lombok.Data;

// Anotação do projeto Lombok que obtem os getters e setters, hashCode, equals e toString
@Data
// Esta classe é um padrão usado no Java, é um DTO (Data Transfer Object) ou objetos de transferência de dados são objetos
// que transportam dados entre processos para reduzir o número de chamadas de método
// além de com os DTOs podemos construir visões diferentes de nossos modelos de domínio
// otimizando-as conforme a necessidade das requisições, sem afetar o design dos modelos de domínio
public class PlanetaDTO {

    private Long id;

    private String nome;

    private String clima;

    private String terreno;

    private String qtdAparicaoFilmes;
}
