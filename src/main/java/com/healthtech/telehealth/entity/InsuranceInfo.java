package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Sigorta / SGK bilgisi — User ile OneToOne ilişkili.
 */
@Entity
@Table(name = "insurance_info")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsuranceInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    private String insuranceType;      // SGK, Özel Sigorta
    private String insuranceCompany;   // Sigorta şirketi adı
    private String policyNumber;       // Poliçe numarası
    private String sgkNo;              // SGK numarası

    @Column(columnDefinition = "TEXT")
    private String notes;              // Ek notlar
}
