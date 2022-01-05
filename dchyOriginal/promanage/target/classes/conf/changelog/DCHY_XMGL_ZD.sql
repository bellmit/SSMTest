--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_ZD:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_ZD
(
  id         VARCHAR2(32) not null,
  dm         VARCHAR2(4) not null,
  mc         VARCHAR2(200) not null,
  fdm        VARCHAR2(50),
  zdlx       VARCHAR2(32),
  qtsx       VARCHAR2(32),
  dzzd       VARCHAR2(50),
  name       VARCHAR2(32),
  tableindex VARCHAR2(20)
);
--changeset DCHY_XMGL_ZD:2 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_ZD add tableindex VARCHAR2(20);
--changeset DCHY_XMGL_ZD:3 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZD.tableindex is '表索引';
--changeset DCHY_XMGL_ZD:4 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_ZD add name VARCHAR2(32);
--changeset DCHY_XMGL_ZD:5 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_ZD.name is '名称';