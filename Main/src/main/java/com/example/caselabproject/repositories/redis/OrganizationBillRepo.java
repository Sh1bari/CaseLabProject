package com.example.caselabproject.repositories.redis;

import com.example.caselabproject.models.redis.OrganizationBill;
import lombok.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Repository
public interface OrganizationBillRepo extends CrudRepository<OrganizationBill, Integer> {
}
