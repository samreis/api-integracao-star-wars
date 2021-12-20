package br.com.apiintegracao.client.context;

/**
 * Classe com os contextos das palavras a serem usadas no pacote client
 */
public class ContextClient {

    // Obter as Strings de valor constante (que não se altera) através do método final,
    // e através do método static não é preciso instanciar a classe
    public static final String OBTER_PLANETAS = "planets/";
    public static final String OBTER_PLANETA = OBTER_PLANETAS + "{numero}/";

    // Construtor privado para indicar que a classe não será instanciada
    private ContextClient() {
    }
}