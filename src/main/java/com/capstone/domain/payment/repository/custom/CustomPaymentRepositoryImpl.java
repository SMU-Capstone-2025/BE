package com.capstone.domain.payment.repository.custom;

import com.capstone.domain.payment.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository
{
    private final MongoTemplate mongoTemplate;
    @Override
    public List<PaymentEntity> findByUserEmail(String userEmail) {
        Query query = new Query();
        query.addCriteria(Criteria.where("email").is(userEmail));
        return mongoTemplate.find(query, PaymentEntity.class);
    }

}
