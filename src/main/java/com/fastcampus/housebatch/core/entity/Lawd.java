package com.fastcampus.housebatch.core.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "lawd")
@EqualsAndHashCode(of = {"lawdCd", "lawdDong", "exist"}, callSuper = false)
public class Lawd extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lawdId;

    @Column(nullable = false)
    private String lawdCd;

    @Column(nullable = false)
    private String lawdDong;

    private Boolean exist;

    @Builder
    public Lawd(String lawdCd, String lawdDong, Boolean exist) {
        this.lawdCd = lawdCd;
        this.lawdDong = lawdDong;
        this.exist = exist;
    }

    public void updateLawd(Lawd lawd) {
        this.lawdCd = lawd.getLawdCd();
        this.lawdDong = lawd.getLawdDong();
        this.exist = lawd.getExist();
    }
}
