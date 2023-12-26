package com.example.caselabproject.models.redis;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

/**
 * Description:
 *
 * @author Vladimir Krasnov
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("Organization")
public class OrganizationBill implements Serializable {
    @Id
    private Integer id;
}
