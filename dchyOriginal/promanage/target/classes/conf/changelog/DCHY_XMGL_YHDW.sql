--liquibase formatted sql
--preconditions dbms:oracle

--changeset DCHY_XMGL_YHDW:1 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_YHDW add jsdwm VARCHAR2(32);
comment on column DCHY_XMGL_YHDW.jsdwm is '建设单位码';

--changeset DCHY_XMGL_YHDW:2 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_YHDW add isvalid VARCHAR2(1);
alter table DCHY_XMGL_YHDW add frlx VARCHAR2(1);
alter table DCHY_XMGL_YHDW add frzjzl VARCHAR2(2);
alter table DCHY_XMGL_YHDW add frzjhm VARCHAR2(50);
alter table DCHY_XMGL_YHDW add frmc VARCHAR2(50);
alter table DCHY_XMGL_YHDW add yhmc VARCHAR2(50);
alter table DCHY_XMGL_YHDW add yhzjzl VARCHAR2(2);
alter table DCHY_XMGL_YHDW add yhzjhm VARCHAR2(50);
alter table DCHY_XMGL_YHDW add lxr VARCHAR2(50);
alter table DCHY_XMGL_YHDW add lxrdh VARCHAR2(50);
comment on column DCHY_XMGL_YHDW.isvalid
is '是否有效(字典项A.10)';
comment on column DCHY_XMGL_YHDW.frlx
is '法人类型(字典项A.38)';
comment on column DCHY_XMGL_YHDW.frzjzl
is '法人证件种类(字典项A.39)';
comment on column DCHY_XMGL_YHDW.frzjhm
is '法人证件号码';
comment on column DCHY_XMGL_YHDW.frmc
is '法人名称';
comment on column DCHY_XMGL_YHDW.yhmc
is '用户名称';
comment on column DCHY_XMGL_YHDW.yhzjzl
is '用户证件种类(字典项A.39)';
comment on column DCHY_XMGL_YHDW.yhzjhm
is '用户证件号码';
comment on column DCHY_XMGL_YHDW.lxr
is '联系人';
comment on column DCHY_XMGL_YHDW.lxrdh
is '联系人电话';

--changeset DCHY_XMGL_YHDW:3 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_YHDW
  add constraint pk_yhdwid primary key (YHDWID);
alter table DCHY_XMGL_YHDW
  add constraint uk_dchy_xmgl_yhdw_yhid unique (YHID);
alter table DCHY_XMGL_YHDW
  add constraint uk_dchy_xmgl_yhdw_yhzjhm unique (YHZJHM);

--changeset DCHY_XMGL_YHDW:4 failOnError:false runOnChange:true runAlways:false
alter table DCHY_XMGL_YHDW add isdwgly VARCHAR2(50);
comment on column DCHY_XMGL_YHDW.isdwgly
is '是否单位管理员(字典项A.10)';