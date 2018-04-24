create table organization (
    ORGANIZATION_GUID CHAR(36) NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    DESCRIPTION VARCHAR(250) NOT NULL,
    ADDRESS1 VARCHAR(100) NULL,
    CITY VARCHAR(50) NULL,
    STATE VARCHAR(2) NULL,
    ZIP_CODE VARCHAR(5) NULL,
    PHONE_NUMBER VARCHAR(10) NULL,
    EMAIL VARCHAR(100) NULL,
    WEBSITE_URL VARCHAR(100) NOT NULL,
    FACEBOOK_URL VARCHAR(100),
    TWITTER_URL VARCHAR(100),
    INSTAGRAM_URL VARCHAR(100),
    PRIMARY KEY (ORGANIZATION_GUID)
);

create table user (
  USER_GUID CHAR(36) NOT NULL,
  FIRST_NAME VARCHAR(50) NOT NULL,
  LAST_NAME VARCHAR(50) NOT NULL,
  EMAIL VARCHAR(50) NOT NULL,
  ROLE VARCHAR(50) NOT NULL,
  PRIMARY KEY (USER_GUID));

create table project (
  PROJECT_GUID CHAR(36) NOT NULL,
  ORGANIZATION_GUID CHAR(36) NOT NULL,
  NAME VARCHAR(50) NOT NULL,
  DESCRIPTION VARCHAR(250) NOT NULL,
  START_DATE TIMESTAMP NOT NULL,
  END_DATE TIMESTAMP null default null,
  CONSTRAINT PRJ_ORG_FK FOREIGN KEY (ORGANIZATION_GUID) REFERENCES organization (ORGANIZATION_GUID),
  PRIMARY KEY (PROJECT_GUID));

create table user_organization (
  ORGANIZATION_GUID CHAR(36) NOT NULL,
  USER_GUID CHAR(36) NOT NULL,
  CONSTRAINT USR_ORG_U_FK FOREIGN KEY (USER_GUID) REFERENCES user (USER_GUID),
  CONSTRAINT USR_ORG_O_FK FOREIGN KEY (ORGANIZATION_GUID) REFERENCES organization (ORGANIZATION_GUID),
  PRIMARY KEY (ORGANIZATION_GUID,USER_GUID));

create table user_project (
  PROJECT_GUID CHAR(36) NOT NULL,
  USER_GUID CHAR(36) NOT NULL,
  CONSTRAINT USR_PRJ_U_FK FOREIGN KEY (USER_GUID) REFERENCES user (USER_GUID),
  CONSTRAINT USR_PRJ_P_FK FOREIGN KEY (PROJECT_GUID) REFERENCES project (PROJECT_GUID),
  PRIMARY KEY (PROJECT_GUID,USER_GUID));

create table login_token (
  USER_GUID CHAR(36) NOT NULL,
  TOKEN CHAR(200) NOT NULL,
  TOKEN_EXP_DATE TIMESTAMP NOT NULL,
  CONSTRAINT login_user_FK FOREIGN KEY (USER_GUID) REFERENCES user (USER_GUID),
  PRIMARY KEY (USER_GUID,TOKEN,TOKEN_EXP_DATE));

create table check_in (
  USER_GUID CHAR(36) NOT NULL,
  PROJECT_GUID CHAR(36) NOT NULL,
  TIME_IN TIMESTAMP NOT NULL,
  TIME_OUT TIMESTAMP null DEFAULT null,
  PRIMARY KEY (USER_GUID,PROJECT_GUID,TIME_IN));