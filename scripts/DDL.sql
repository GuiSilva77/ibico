CREATE TABLE tb_user_role
(
    id_role BIGINT     NOT NULL,
    id_user BINARY(16) NOT NULL,
    CONSTRAINT pk_tb_user_role PRIMARY KEY (id_role, id_user)
);

CREATE TABLE user_skills
(
    id_skills BINARY(16) NOT NULL,
    id_user   BINARY(16) NOT NULL,
    CONSTRAINT pk_user_skills PRIMARY KEY (id_skills, id_user)
);

CREATE TABLE users
(
    id               BINARY(16)   NOT NULL,
    cpf              VARCHAR(11)  NOT NULL,
    name             VARCHAR(255) NOT NULL,
    passwd           VARCHAR(255) NOT NULL,
    date_of_creation datetime     NOT NULL,
    img_url          VARCHAR(255) NOT NULL,
    active           BIT(1)       NOT NULL,
    telephone        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE roles
(
    id     BIGINT AUTO_INCREMENT NOT NULL,
    `role` VARCHAR(30)           NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE skills
(
    id            BINARY(16)  NOT NULL,
    name          VARCHAR(50) NOT NULL,
    `description` VARCHAR(50) NOT NULL,
    CONSTRAINT pk_skills PRIMARY KEY (id)
);

ALTER TABLE skills
    ADD CONSTRAINT skills_uq_name UNIQUE (name);

ALTER TABLE users
    ADD CONSTRAINT user_uq_cpf UNIQUE (cpf);

ALTER TABLE user_skills
    ADD CONSTRAINT FK_USER_SKILLS_USERS FOREIGN KEY (id_user) REFERENCES users (id);

ALTER TABLE user_skills
    ADD CONSTRAINT FK_USER_SKILLS_SKILLS FOREIGN KEY (id_skills) REFERENCES skills (id);

ALTER TABLE tb_user_role
    ADD CONSTRAINT FK_USER_ROLES_USERS FOREIGN KEY (id_user) REFERENCES users (id);

ALTER TABLE tb_user_role
    ADD CONSTRAINT FK_USER_ROLES_ROLES FOREIGN KEY (id_role) REFERENCES roles (id);


