package com.capstone.domain.payment.repository;


import com.capstone.domain.payment.entity.PaymentEntity;
import com.capstone.domain.project.repository.custom.CustomProjectRepository;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends MongoRepository<PaymentEntity, String> , CustomProjectRepository {

}

