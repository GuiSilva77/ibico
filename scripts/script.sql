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
    username         VARCHAR(255) NOT NULL,
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
    CONSTRAINT pk_skills PRIMARY KEY (id)
);

CREATE TABLE opportunities
(
    id              BINARY(16)   NOT NULL,
    title           VARCHAR(255) NOT NULL,
    `description`   TEXT         NOT NULL,
    start_date_time datetime     NOT NULL,
    end_date_time   datetime     NOT NULL,
    time_load       VARCHAR(255) NOT NULL,
    local           VARCHAR(255) NOT NULL,
    value           DECIMAL      NOT NULL,
    status          VARCHAR(255) NOT NULL,
    created_at      datetime     NOT NULL,
    posted_by       BINARY(16)   NOT NULL,
    selected_candidate BINARY(16)   NULL,
    opportunity_closed_time datetime     NULL,

    CONSTRAINT pk_OPPORTUNITIES PRIMARY KEY (id)
);

CREATE TABLE reviews
(
    id           BINARY(16) NOT NULL,
    review       TEXT       NOT NULL,
    rating       INT        NOT NULL,
    created_at   datetime   NOT NULL,
    user_id      BINARY(16) NOT NULL,
    opportunity_id BINARY(16) NOT NULL,
    CONSTRAINT pk_reviews PRIMARY KEY (id)
);

CREATE TABLE opportunity_skills
(
    id_opportunity BINARY(16) NOT NULL,
    id_skills      BINARY(16) NOT NULL,
    CONSTRAINT pk_opportunity_skills PRIMARY KEY (id_opportunity, id_skills)
);

CREATE TABLE candidatures
(
    id               BINARY(16) NOT NULL,
    candidature_date datetime   NOT NULL,
    user_id          BINARY(16) NOT NULL,
    vacancy_id       BINARY(16) NOT NULL,
    CONSTRAINT pk_candidatures PRIMARY KEY (id)
);

CREATE TABLE password_reset_code
(
    request_id BINARY(16)   NOT NULL,
    user_id    BINARY(16)   NULL,
    code       VARCHAR(255) NOT NULL,
    created_at datetime     NOT NULL,
    expires_at datetime     NOT NULL,
    CONSTRAINT pk_password_reset_code PRIMARY KEY (request_id)
);

CREATE TABLE password_reset_requests
(
    request_id      BINARY(16)   NOT NULL,
    code_id         BINARY(16)   NULL,
    access_token    VARCHAR(255) NOT NULL,
    created_at      datetime     NOT NULL,
    expiration_date datetime     NOT NULL,
    used            BIT(1)       NOT NULL,
    CONSTRAINT pk_password_reset_requests PRIMARY KEY (request_id)
);

ALTER TABLE opportunities
    ADD CONSTRAINT FK_OPPORTUNITIES_ON_SELECTED_CANDIDATE FOREIGN KEY (selected_candidate) REFERENCES users (id);


ALTER TABLE password_reset_requests
    ADD CONSTRAINT UQ_PASSWORD_RESET_REQUEST_ACCESS_TOKEN UNIQUE (access_token);

ALTER TABLE password_reset_requests
    ADD CONSTRAINT FK_PASSWORD_RESET_REQUESTS_ON_CODE FOREIGN KEY (code_id) REFERENCES password_reset_code (request_id);

ALTER TABLE password_reset_code
    ADD CONSTRAINT FK_PASSWORD_RESET_CODE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE candidatures
    ADD CONSTRAINT FK_CANDIDATURES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE candidatures
    ADD CONSTRAINT FK_CANDIDATURES_ON_VACANCY FOREIGN KEY (vacancy_id) REFERENCES opportunities (id);

ALTER TABLE opportunity_skills
    ADD CONSTRAINT FK_OPPORTUNITY_SKILLS_USERS FOREIGN KEY (id_opportunity) REFERENCES opportunities (id);

ALTER TABLE opportunity_skills
    ADD CONSTRAINT FK_OPPORTUNITY_SKILLS_SKILLS FOREIGN KEY (id_skills) REFERENCES skills (id);

ALTER TABLE opportunities
    ADD CONSTRAINT FK_OPPORTUNITIES_ON_POSTED_BY FOREIGN KEY (posted_by) REFERENCES users (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_OPPORTUNITY FOREIGN KEY (opportunity_id) REFERENCES opportunities (id);

ALTER TABLE reviews
    ADD CONSTRAINT FK_REVIEWS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

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

INSERT INTO `roles`
VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_USER'), (3, 'ROLE_GUEST');

