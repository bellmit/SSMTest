--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_SQXX:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SQXX add XSSQXXID VARCHAR2(32);
--changeset DCHY_XMGL_SQXX:2 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SQXX add XSSQBH VARCHAR2(100);
--changeset DCHY_XMGL_SQXX:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SQXX.XSSQXXID is '线上申请信息id';
--changeset DCHY_XMGL_SQXX:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SQXX.XSSQBH is '线上申请编号';
