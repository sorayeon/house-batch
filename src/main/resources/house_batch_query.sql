select * from lawd;

select * from lawd
where exist = false
  and lawd_dong like '서울특별시%';

delete from lawd;

update lawd set exist = false;

select distinct substr(lawd_cd, 1, 5) gu_lawd_cd, lawd_dong
from lawd
where exist = true;

select substr(lawd_cd, 1, 5) gu_lawd_cd, lawd_dong
from lawd
where exist = true
  and lawd_cd like '%00000'
and lawd_cd not like '%00000000';


select count(*) from apt; --9228

select count(*) from apt_deal; -- 31774

select *
from apt_deal ad
         left join apt a on a.apt_id = ad.apt_id
order by ad.deal_amount desc;

select *
from apt_deal ad
where deal_canceled = false
  and deal_date = '2021-07-01';

insert into apt_notification (email, gu_lawd_cd, enabled, created_at, updated_at)
values ('test@naver.com', '41463', true, now(), now());
insert into apt_notification (email, gu_lawd_cd, enabled, created_at, updated_at)
values ('test@naver.com', '11440', true, now(), now());
insert into apt_notification (email, gu_lawd_cd, enabled, created_at, updated_at)
values ('test@naver.com', '11110', false, now(), now());

select *
from apt_notification;

select *
from apt_deal ad
         left join apt a on a.apt_id = ad.apt_id
where ad.deal_date = '2021-07-01'
and gu_lawd_cd = '41463'
order by ad.deal_amount desc;