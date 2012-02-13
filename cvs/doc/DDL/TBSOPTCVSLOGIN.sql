DROP TABLE "SOPAADMIN"."TBSOPTCVSLOGIN" CASCADE CONSTRAINTS;

CREATE TABLE "SOPAADMIN"."TBSOPTCVSLOGIN"
  (
    "SID"          NUMBER(10,0) NOT NULL,
    "FLAG"         CHAR(1 BYTE) NOT NULL,
    "STATUS"       CHAR(1 BYTE) NOT NULL,
    "DESCRIPTION"  VARCHAR2(3000 BYTE),
    "CREATOR"      VARCHAR2(50 BYTE),
    "CREATETIME"   TIMESTAMP (6),
    "MODIFIER"     VARCHAR2(50 BYTE),
    "LASTUPDATE"   TIMESTAMP (6),
    CONSTRAINT "TBSOPTCVSLOGIN_PK" PRIMARY KEY ("SID") USING INDEX TABLESPACE "SOPA_INDEX"
  ) TABLESPACE "SOPA_DATA";