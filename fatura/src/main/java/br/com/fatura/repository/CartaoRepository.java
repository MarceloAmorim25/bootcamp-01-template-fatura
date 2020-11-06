package br.com.fatura.repository;

import br.com.fatura.entidades.Cartao;
import org.springframework.data.repository.CrudRepository;

public interface CartaoRepository extends CrudRepository<Cartao, String> {
}
