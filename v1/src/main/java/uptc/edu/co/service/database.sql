CREATE DATABASE IF NOT EXISTS seguridad_shiro;
USE seguridad_shiro;

CREATE TABLE IF NOT EXISTS usuarios (
    username VARCHAR(50) PRIMARY KEY,
    password_hash VARCHAR(255),
    rol VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS roles_permissions (
    rol VARCHAR(20),
    permission VARCHAR(50)
);
INSERT INTO roles_permissions VALUES ('admin', 'producto:add');
INSERT INTO roles_permissions VALUES ('admin', 'producto:view');
INSERT INTO roles_permissions VALUES ('admin', 'producto:update');
INSERT INTO roles_permissions VALUES ('admin', 'producto:delete');
INSERT INTO roles_permissions VALUES ('user', 'producto:add');
INSERT INTO roles_permissions VALUES ('user', 'producto:view');
INSERT INTO roles_permissions VALUES ('guest', 'producto:view');	
SELECT username, password_hash, rol FROM usuarios WHERE username = 'admin';
DELETE FROM usuarios WHERE username = 'admin';

drop database seguridad_shiro;


select * from usuarios;

