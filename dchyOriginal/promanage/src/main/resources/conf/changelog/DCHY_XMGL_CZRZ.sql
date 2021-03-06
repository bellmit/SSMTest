--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CZRZ:1 failOnError:false runOnChange:true runAlways:false
CREATE TABLE DCHY_XMGL_CZRZ (
  CZRZID VARCHAR2(32 BYTE) NOT NULL,
  CZSJ DATE,
  CZR VARCHAR2(32 BYTE),
  CZLX VARCHAR2(32 BYTE),
  CZRID VARCHAR2(32 BYTE),
  SSMKID VARCHAR2(2 BYTE),
  CZCS VARCHAR2(500 BYTE),
  CZLXMC VARCHAR2(32 BYTE)
);
--changeset DCHY_XMGL_CZRZ:2 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZRZID IS '操作日志id';
--changeset DCHY_XMGL_CZRZ:3 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZSJ IS '操作时间';
--changeset DCHY_XMGL_CZRZ:4 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZR IS '操作人';
--changeset DCHY_XMGL_CZRZ:5 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZLX IS '操作类型';
--changeset DCHY_XMGL_CZRZ:6 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZRID IS '操作人id';
--changeset DCHY_XMGL_CZRZ:7 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.SSMKID IS '所属模块id';
--changeset DCHY_XMGL_CZRZ:8 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZCS IS '操作参数';
--changeset DCHY_XMGL_CZRZ:9 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CZRZ.CZLXMC IS '操作类型名称';
--changeset DCHY_XMGL_CZRZ:10 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CZRZ add GLSXID VARCHAR2(32);
--changeset DCHY_XMGL_CZRZ:11 failOnError:false runOnChange:true runAlways:false
comment on column DCHY_XMGL_CZRZ.GLSXID is '关联事项id';

--changeset DCHY_XMGL_CZRZ:12 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CZRZ modify  DCHY_XMGL_CZRZ.CZCS VARCHAR2(3000);

--changeset DCHY_XMGL_CZRZ:13 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_CZRZ add czmk VARCHAR2(32);
alter table DCHY_XMGL_CZRZ add czmkmc VARCHAR2(32);
comment on column DCHY_XMGL_CZRZ.czmk
is '操作模块';
comment on column DCHY_XMGL_CZRZ.czmkmc
is '操作模块名称';
