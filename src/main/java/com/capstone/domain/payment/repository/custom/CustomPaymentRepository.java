package com.capstone.domain.payment.repository.custom;

import com.capstone.domain.payment.entity.PaymentEntity;

import java.util.List;

public interface CustomPaymentRepository
{
    List<PaymentEntity> findByUserEmail(String userEmail);

}