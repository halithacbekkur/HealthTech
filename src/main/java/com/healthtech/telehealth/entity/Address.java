package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Kullanıcı adresi — User ile OneToOne ilişkili.
 */
@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String city;          // İl
    private String district;      // İlçe
    private String neighborhood;  // Mahalle

    @Column(columnDefinition = "TEXT")
    private String fullAddress;   // Tam adres

    private String postalCode;    // Posta kodu
}
