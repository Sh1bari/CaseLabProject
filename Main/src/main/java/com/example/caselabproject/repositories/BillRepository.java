package com.example.caselabproject.repositories;


import com.example.caselabproject.models.entities.Bill;
import com.example.caselabproject.models.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findAllByOrganization(Organization organization);
}
