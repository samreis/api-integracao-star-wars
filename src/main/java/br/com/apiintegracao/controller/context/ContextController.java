package br.com.apiintegracao.controller.context;

/**
 * Classe com os contextos das palavras a serem usadas no pacote controller
 */
public class ContextController {

    // Obter as Strings de valor constante (que não se altera) através do método final,
    // e através do método static não é preciso instanciar a classe
    public static final String API = "api/";
    public static final String PLANETAS = "planetas";
    public static final String PLANETAS_ID = PLANETAS + "/{id}";

    // Construtor privado para indicar que a classe não será instanciada
    private ContextController() {
    }
}