package com.fastcampus.housebatch.core.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "apt_notification")
public class AptNotification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer aptNotificationId;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 5)
    private String guLawdCd;

    @Column(nullable = false)
    private Boolean enabled = false;

    @Builder
    public AptNotification(String email, String guLawdCd, boolean enabled) {
        this.email = email;
        this.guLawdCd = guLawdCd;
        this.enabled = enabled;
    }
}