package br.com.fatura.repository;

import br.com.fatura.entidades.Renegociacao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RenegociacaoRepository extends CrudRepository<Renegociacao, String> {
}
