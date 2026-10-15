package br.pucgoias.ads.filmes;

import java.math.BigDecimal;

public record FilmeResposta(Long id, String titulo, String diretor,
                            Integer ano, BigDecimal nota) {

    public static FilmeResposta de(Filme filme) {
        return new FilmeResposta(filme.getId(), filme.getTitulo(), filme.getDiretor(),
                filme.getAno(), filme.getNota());
    }
}
