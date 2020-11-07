package br.com.fatura.repository;

import br.com.fatura.entidades.Transacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransacaoRepository extends CrudRepository<Transacao, String> {
}
