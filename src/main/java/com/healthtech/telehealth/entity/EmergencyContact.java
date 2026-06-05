package com.healthtech.telehealth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Acil durum iletişim kişisi — Bir kullanıcının birden fazla acil durum kişisi olabilir.
 */
@Entity
@Table(name = "emergency_contacts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String fullName;       // Kişinin adı soyadı
    private String phone;          // Telefon numarası
    private String relationship;   // Yakınlık (anne, baba, eş, kardeş vb.)
}
