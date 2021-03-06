--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_SJCLPZ:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SJCLPZ add need INTEGER default 0 not null;
--changeset DCHY_XMGL_SJCLPZ:2 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SJCLPZ.need is '是否必传';
--changeset DCHY_XMGL_SJCLPZ:3 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SJCLPZ add ssclsx VARCHAR2(32);
--changeset DCHY_XMGL_SJCLPZ:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SJCLPZ.SSCLSX_NotUse is '所属测量事项';
