package br.pucgoias.ads.filmes;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilmeService {

    private static final Logger log = LoggerFactory.getLogger(FilmeService.class);

    private final FilmeRepository repositorio;

    public FilmeService(FilmeRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Cacheable(cacheNames = "filmes", key = "#id")
    @Transactional(readOnly = true)
    public FilmeResposta buscar(Long id) {
        log.info(">>> Consultando o PostgreSQL: filme {}", id);
        simularConsultaCustosa();
        return repositorio.findById(id)
                .map(FilmeResposta::de)
                .orElseThrow(() -> new FilmeNaoEncontradoException(id));
    }

    @Transactional
    public FilmeResposta alterarNota(Long id, BigDecimal novaNota) {
        Filme filme = repositorio.findById(id)
                .orElseThrow(() -> new FilmeNaoEncontradoException(id));
        filme.alterarNota(novaNota);
        return FilmeResposta.de(filme);
    }

    @Transactional
    public void remover(Long id) {
        if (!repositorio.existsById(id)) {
            throw new FilmeNaoEncontradoException(id);
        }
        repositorio.deleteById(id);
    }

    /** Representa uma consulta demorada (juncoes, agregacoes, tabelas volumosas). */
    private void simularConsultaCustosa() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
