DROP TABLE IF EXISTS LAWD;
DROP TABLE IF EXISTS APT_DEAL;
DROP TABLE IF EXISTS APT;
DROP TABLE IF EXISTS APT_NOTIFICATION;

-- 동 코드 테이블 생성
CREATE TABLE LAWD  (
                       LAWD_ID SERIAL NOT NULL,
                       LAWD_CD CHAR(10) NOT NULL,
                       LAWD_DONG VARCHAR(100) NOT NULL,
                       EXIST BOOLEAN NOT NULL,
                       CREATED_AT TIMESTAMP NOT NULL,
                       UPDATED_AT TIMESTAMP NOT NULL,
                       CONSTRAINT PK_LAWD PRIMARY KEY (LAWD_ID)
) ;

COMMENT ON COLUMN LAWD.LAWD_ID is '법정동 ID';
COMMENT ON COLUMN LAWD.LAWD_CD is '법정동코드';
COMMENT ON COLUMN LAWD.LAWD_DONG is '법정동명';
COMMENT ON COLUMN LAWD.EXIST is '폐지여부';
COMMENT ON COLUMN LAWD.CREATED_AT is '등록일시';
COMMENT ON COLUMN LAWD.UPDATED_AT is '수정일시';

-- 아파트 테이블 생성
CREATE TABLE APT  (
                      APT_ID BIGSERIAL NOT NULL,
                      APT_NAME VARCHAR(100) NOT NULL,
                      GU_LAWD_CD CHAR(5) NOT NULL,
                      JIBUN VARCHAR(20) NOT NULL,
                      DONG VARCHAR(100) NOT NULL,
                      BUILT_YEAR INT NOT NULL,
                      CREATED_AT TIMESTAMP NOT NULL,
                      UPDATED_AT TIMESTAMP NOT NULL,
                      CONSTRAINT PK_APT PRIMARY KEY (APT_ID)
) ;

COMMENT ON COLUMN APT.APT_ID is '아파트 ID';
COMMENT ON COLUMN APT.APT_NAME is '아파트명';
COMMENT ON COLUMN APT.GU_LAWD_CD is '지역코드';
COMMENT ON COLUMN APT.JIBUN is '지번';
COMMENT ON COLUMN APT.DONG is '법정동';
COMMENT ON COLUMN APT.BUILT_YEAR is '건축년도';
COMMENT ON COLUMN APT.CREATED_AT is '등록일시';
COMMENT ON COLUMN APT.UPDATED_AT is '수정일시';

-- 아파트 거래내역 테이블 생성
CREATE TABLE APT_DEAL  (
                           APT_DEAL_ID BIGSERIAL NOT NULL,
                           APT_ID BIGINT NOT NULL,
                           EXCLUSIVE_AREA NUMERIC(6, 2) NOT NULL,
                           DEAL_DATE DATE NOT NULL,
                           DEAL_AMOUNT BIGINT NOT NULL,
                           FLOOR INT NOT NULL,
                           DEAL_CANCELED BOOLEAN NOT NULL DEFAULT FALSE,
                           DEAL_CANCELED_DATE DATE NULL,
                           CREATED_AT TIMESTAMP NOT NULL,
                           UPDATED_AT TIMESTAMP NOT NULL,
                           CONSTRAINT PK_APT_DEAL PRIMARY KEY(APT_DEAL_ID)
) ;

-- 아파트 거래내역 -> 아파트 외래키
ALTER TABLE APT_DEAL
    ADD CONSTRAINT FK_APT_DEAL_APT_ID FOREIGN KEY (APT_ID)
        REFERENCES APT(APT_ID) ON DELETE CASCADE ON UPDATE CASCADE ;

COMMENT ON COLUMN APT_DEAL.APT_DEAL_ID is '아파트거래 ID';
COMMENT ON COLUMN APT_DEAL.APT_ID is '아파트 ID';
COMMENT ON COLUMN APT_DEAL.EXCLUSIVE_AREA is '전용면적';
COMMENT ON COLUMN APT_DEAL.DEAL_DATE is '거래일자';
COMMENT ON COLUMN APT_DEAL.DEAL_AMOUNT is '거래금액';
COMMENT ON COLUMN APT_DEAL.FLOOR is '층수';
COMMENT ON COLUMN APT_DEAL.DEAL_CANCELED is '해제여부';
COMMENT ON COLUMN APT_DEAL.DEAL_CANCELED_DATE is '해재사유발생일';
COMMENT ON COLUMN APT_DEAL.CREATED_AT is '등록일시';
COMMENT ON COLUMN APT_DEAL.UPDATED_AT is '수정일시';

-- 아파트 실거래가 알림 테이블 생성
CREATE TABLE APT_NOTIFICATION  (
                                   APT_NOTIFICATION_ID SERIAL NOT NULL,
                                   EMAIL VARCHAR(100) NOT NULL,
                                   GU_LAWD_CD CHAR(5) NOT NULL,
                                   ENABLED BOOLEAN NOT NULL,
                                   CREATED_AT TIMESTAMP NOT NULL,
                                   UPDATED_AT TIMESTAMP NOT NULL,
                                   CONSTRAINT PK_APT_NOTIFICATION PRIMARY KEY(APT_NOTIFICATION_ID)
) ;

-- 아파트 실거래가 알림 유니크 조건 추가
ALTER TABLE APT_NOTIFICATION
    ADD CONSTRAINT UK_EMAIL_GU_LAWD_CD UNIQUE (EMAIL, GU_LAWD_CD);

COMMENT ON COLUMN APT_NOTIFICATION.APT_NOTIFICATION_ID is '아파트 알림 ID';
COMMENT ON COLUMN APT_NOTIFICATION.EMAIL is '이메일주소';
COMMENT ON COLUMN APT_NOTIFICATION.GU_LAWD_CD is '지역코드';
COMMENT ON COLUMN APT_NOTIFICATION.ENABLED is '등록여부';
COMMENT ON COLUMN APT_NOTIFICATION.CREATED_AT is '등록일시';
COMMENT ON COLUMN APT_NOTIFICATION.UPDATED_AT is '수정일시';