--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_CGCC:1 failOnError:false runOnChange:true runAlways:false
CREATE TABLE DCHY_XMGL_CGCC (
  CGCCID VARCHAR2(32 BYTE) NOT NULL,
  CCSJ DATE,
  BABH VARCHAR2(32 BYTE),
  GCBH VARCHAR2(32 BYTE),
  GCMC VARCHAR2(32 BYTE),
  JSDW VARCHAR2(500 BYTE),
  CHXMID VARCHAR2(32 BYTE),
  CHDWMC VARCHAR2(500 BYTE),
  CGPJ VARCHAR2(2 BYTE),
  PJYJ VARCHAR2(200 BYTE),
  SFSD VARCHAR2(2 BYTE),
  PJZT VARCHAR2(2 BYTE),
  SQFS VARCHAR2(2 BYTE),
  DQZT VARCHAR2(2 BYTE),
  CJSJ DATA,
  PJSJ DATA ,
  CJR VARCHAR2(32 BYTE),
  CJRID VARCHAR2(32 BYTE),
  CCR VARCHAR2(32 BYTE),
  CCRID VARCHAR2(32 BYTE),
  PJR VARCHAR2(32 BYTE),
  PJRID VARCHAR2(32 BYTE)
);
--changeset DCHY_XMGL_CGCC:2 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CGCCID IS '成果抽查id';
--changeset DCHY_XMGL_CGCC:3 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CCSJ IS '抽查时间';
--changeset DCHY_XMGL_CGCC:4 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.BABH IS '备案编号';
--changeset DCHY_XMGL_CGCC:5 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.GCBH IS '工程编号';
--changeset DCHY_XMGL_CGCC:6 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.GCMC IS '工程名称';
--changeset DCHY_XMGL_CGCC:7 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.JSDW IS '建设单位';
--changeset DCHY_XMGL_CGCC:8 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CHXMID IS '测绘项目id';
--changeset DCHY_XMGL_CGCC:9 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CHDWMC IS '测绘单位名称';
--changeset DCHY_XMGL_CGCC:10 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CGPJ IS '成果评价(0:合格,1:不合格)';
--changeset DCHY_XMGL_CGCC:11 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.PJYJ IS '评价意见';
--changeset DCHY_XMGL_CGCC:12 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.SFSD IS '是否首单(0:是,1:否)'
--changeset DCHY_XMGL_CGCC:13 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.PJZT IS '评价状态(0:待抽查,1:待评价,2:已评价)';
--changeset DCHY_XMGL_CGCC:14 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.SQFS IS '申请方式(项目来源)';
--changeset DCHY_XMGL_CGCC:15 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.DQZT IS '当前状态(1:审核中,2:待制作,3:制作中,4:待交付,98:已交付,99:已退回)';
--changeset DCHY_XMGL_CGCC:16 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CJSJ IS '抽查时间';
--changeset DCHY_XMGL_CGCC:17 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.PJSJ IS '评价时间';
--changeset DCHY_XMGL_CGCC:18 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CJR IS '创建人';
--changeset DCHY_XMGL_CGCC:19 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CJRID IS '创建人ID';
--changeset DCHY_XMGL_CGCC:20 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CCR  IS '抽查人';
--changeset DCHY_XMGL_CGCC:21 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.CCRID  IS '抽查人ID';
--changeset DCHY_XMGL_CGCC:22 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.PJR  IS '评价人';
--changeset DCHY_XMGL_CGCC:23 failOnError:false runOnChange:true runAlways:false
COMMENT ON COLUMN DCHY_XMGL_CGCC.PJRID IS '评价人ID';
