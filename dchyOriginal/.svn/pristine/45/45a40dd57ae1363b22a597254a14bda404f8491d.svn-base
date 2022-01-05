--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_SJCL:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SJCL add clsx VARCHAR2(32);
--changeset DCHY_XMGL_SJCL:2 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SJCL.clsx is '测量事项';
--changeset DCHY_XMGL_SJCL:3 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_SJCL add sjclpzid VARCHAR2(32);
--changeset DCHY_XMGL_SJCL:4 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_SJCL.sjclpzid is '收件材料配置编号';

