package br.com.fatura.repository;

import br.com.fatura.entidades.CartaoVirtual;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartaoVirtualRepository extends CrudRepository<CartaoVirtual, Field.Str> {
}
