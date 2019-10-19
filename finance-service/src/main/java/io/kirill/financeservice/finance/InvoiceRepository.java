package io.kirill.financeservice.finance;

import io.kirill.financeservice.finance.domain.Invoice;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface InvoiceRepository extends ReactiveMongoRepository<Invoice, String> {
}
