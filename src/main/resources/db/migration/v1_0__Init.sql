create table organization (
    ORG_GUID CHAR(36) NOT NULL,
    NAME VARCHAR(50) NOT NULL,
    DESCRIPTION VARCHAR(250) NOT NULL,
    PHONE VARCHAR(10),
    EMAIL VARCHAR(100),
    WEB_URL VARCHAR(100) NOT NULL,
    FACEBOOK VARCHAR(100),
    TWITTER VARCHAR(100),
    INSTAGRAM VARCHAR(100),
    PRIMARY KEY (ORG_GUID)
);