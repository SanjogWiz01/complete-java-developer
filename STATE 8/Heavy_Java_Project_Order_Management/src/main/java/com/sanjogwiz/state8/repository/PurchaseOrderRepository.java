package com.sanjogwiz.state8.repository;

import com.sanjogwiz.state8.domain.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
}

