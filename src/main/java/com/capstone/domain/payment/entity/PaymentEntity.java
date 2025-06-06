package com.capstone.domain.payment.entity;

import com.capstone.global.entity.BaseDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Document(collection = "payment")
public class PaymentEntity extends BaseDocument {
    @Id
    private String id;

    private String userEmail;

    private String impUid;

    private BigDecimal totalPrice;

    private LocalDateTime paidAt;

    private Boolean status = false;



}

