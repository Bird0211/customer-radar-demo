package com.customerradar.user.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.GenericGenerator;

/**
 * User entity
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_customer_radar_user")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    
    @Id
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.customerradar.user.util.SnowflakeId")
    @Column(name = "id", nullable = false, length = 20)
    Long id;

    @Column(name = "name", length = 50, nullable=false)
    String name;

    @Column(name = "address", length = 500)
    String address;

    @Column(name = "phone", length = 13, nullable=false)
    String phone;

}
