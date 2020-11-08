package br.com.fatura.repository;

import br.com.fatura.entidades.Parcela;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelaRepository extends CrudRepository<Parcela, String> {
}
