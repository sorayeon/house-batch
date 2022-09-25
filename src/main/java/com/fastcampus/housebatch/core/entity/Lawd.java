package com.fastcampus.housebatch.core.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lawd")
public class Lawd {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lawdId;

    @Column(nullable = false)
    private String lawdCd;

    @Column(nullable = false)
    private String lawdDong;

    private Boolean exist;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Builder
    public Lawd(String lawdCd, String lawdDong, Boolean exist) {
        this.lawdCd = lawdCd;
        this.lawdDong = lawdDong;
        this.exist = exist;
    }
}
