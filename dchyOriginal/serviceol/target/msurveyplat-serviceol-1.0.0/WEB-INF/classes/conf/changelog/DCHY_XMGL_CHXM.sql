--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CHXM:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM add wtzt VARCHAR2(100);
comment on column DCHY_XMGL_CHXM.wtzt is '委托状态';
alter table DCHY_XMGL_CHXM add ishy VARCHAR2(100);
comment on column DCHY_XMGL_CHXM.ishy is '是否核验';
alter table DCHY_XMGL_CHXM add sfsd VARCHAR2(100);
comment on column DCHY_XMGL_CHXM.sfsd is '是否首单';
alter table DCHY_XMGL_CHXM add ccnum VARCHAR2(100);
comment on column DCHY_XMGL_CHXM.ccnum is '抽查次数';
alter table DCHY_XMGL_CHXM add ccnum VARCHAR2(100);
comment on column DCHY_XMGL_CHXM.ccnum is '抽查次数';
--changeset DCHY_XMGL_CHXM:8 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM add zdxm VARCHAR2(2);
--changeset DCHY_XMGL_CHXM:9 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM.zdxm is '是否重大建设项目';
--changeset DCHY_XMGL_CHXM:10 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM add babh VARCHAR2(20);
--changeset DCHY_XMGL_CHXM:11 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM.babh is '备案编号';
--changeset DCHY_XMGL_CHXM:12 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM add wjsftb VARCHAR2(2);
--changeset DCHY_XMGL_CHXM:13 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CHXM.wjsftb is '文件是否同步';



--changeset DCHY_XMGL_CHXM:16 failOnError:false runOnChange:true runAlways:false
create sequence SEQ_XMBH
minvalue 1
maxvalue 99999999999999999999
start with 1241
increment by 1
cache 10;

--changeset DCHY_XMGL_CHXM:17 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM add tyjfqrsj date;
alter table DCHY_XMGL_CHXM add tyjfzt varchar2(1);
alter table DCHY_XMGL_CHXM add tyjfqrczr varchar2(50);
alter table DCHY_XMGL_CHXM add tyjfqrczrmc varchar2(50);
comment on column DCHY_XMGL_CHXM.tyjfqrsj
is '统一交付确认时间';
comment on column DCHY_XMGL_CHXM.tyjfzt
is '统一交付状态(字典项A.10)';
comment on column DCHY_XMGL_CHXM.tyjfqrczr
is '统一交付确认操作人id';
comment on column DCHY_XMGL_CHXM.tyjfqrczrmc
is '统一交付确认操作人名称';

--changeset DCHY_XMGL_CHXM:18 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CHXM add sfgq VARCHAR2(1);
comment on column DCHY_XMGL_CHXM.sfgq is '是否挂起（0解挂，1挂起）';