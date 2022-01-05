--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_JSDW:1 failOnError:false runOnChange:true runAlways:false
create table DCHY_XMGL_JSDW
(
  jsdwid   VARCHAR2(32) not null,
  dwmc     VARCHAR2(100) not null,
  dwbh     VARCHAR2(100),
  tyshxydm VARCHAR2(100) not null,
  lxr      VARCHAR2(50),
  lxdh     VARCHAR2(50),
  jsdwm    VARCHAR2(32) not null,
  lrr      VARCHAR2(32),
  lrrid    VARCHAR2(32),
  lrsj     DATE
);
comment on table DCHY_XMGL_JSDW
  is '多测合一建设单位';
-- Add comments to the columns
comment on column DCHY_XMGL_JSDW.jsdwid
  is '建设单位id';
comment on column DCHY_XMGL_JSDW.dwmc
  is '建设单位名称';
comment on column DCHY_XMGL_JSDW.dwbh
  is '建设单位编号';
comment on column DCHY_XMGL_JSDW.tyshxydm
  is '统一社会信用代码';
comment on column DCHY_XMGL_JSDW.lxr
  is '联系人';
comment on column DCHY_XMGL_JSDW.lxdh
  is '联系电话';
comment on column DCHY_XMGL_JSDW.jsdwm
  is '建设单位码';
comment on column DCHY_XMGL_JSDW.lrr
  is '录入人';
comment on column DCHY_XMGL_JSDW.lrrid
  is '录入人id';
comment on column DCHY_XMGL_JSDW.lrsj
  is '录入时间';

  alter table DCHY_XMGL_JSDW
  add primary key (JSDWID)
