-- -------------------------------------------------
-- Trabalho Interdisciplinar 2 - Grupo 10 - Burnout
-- Integrantes: André Santos, André Scianni,
-- Carlos Vinícius, Fernando Wagner
-- -------------------------------------------------

-- -------------------------------------------------
-- Schema chillout
-- -------------------------------------------------
CREATE SCHEMA IF NOT EXISTS chillout AUTHORIZATION ti2cc;

-- -------------------------------------------------
-- Table chillout.usuario
-- -------------------------------------------------
CREATE TABLE IF NOT EXISTS chillout.usuario (
    user_id SERIAL,
    user_nome VARCHAR(32),
    user_sobrenome VARCHAR(45),
    user_email VARCHAR(320) NOT NULL UNIQUE,
    user_senha VARCHAR NOT NULL,
    user_avatar_url VARCHAR(2000) DEFAULT './defaultImage.png',

    PRIMARY KEY (user_id),

    CHECK (user_email ~ '^[a-zA-Z][a-zA-Z0-9]*([._][a-zA-Z0-9]{1,})*@[a-zA-Z][a-zA-Z0-9]*([.-][a-zA-Z0-9]{1,}){1,}$'),
    CHECK (user_nome ~ '^[a-zA-Z]{2,}([ ][a-zA-Z]{1,})*$'),
    CHECK (user_sobrenome ~ '^[a-zA-Z]{2,}([ ][a-zA-Z]{1,})*$')
);

-- -------------------------------------------------
-- Table chillout.lista_tarefas
-- -------------------------------------------------
CREATE TABLE IF NOT EXISTS chillout.lista_tarefas (
    lista_id SERIAL,
    lista_titulo VARCHAR(45),
    lista_num_tarefas INT DEFAULT 0,

    PRIMARY KEY (lista_id),

    CHECK (lista_titulo ~ '^[a-zA-Z0-9._-]{1,}([ ][a-zA-Z0-9._-]{1,})*$'),
    CHECK (lista_num_tarefas >= 0)
);

-- -------------------------------------------------
-- Table chillout.definicao_lista_tarefas
-- -------------------------------------------------
CREATE TABLE IF NOT EXISTS chillout.definicao_lista_tarefas (
    def_id_usuario INT NOT NULL,
    def_id_lista INT NOT NULL,

    -- def_id_usuario configuração --
    FOREIGN KEY (def_id_usuario)
    REFERENCES chillout.usuario (user_id)
    ON UPDATE CASCADE ON DELETE CASCADE,

    -- def_id_lista configuração --
    FOREIGN KEY (def_id_lista)
    REFERENCES chillout.lista_tarefas(lista_id)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- -------------------------------------------------
-- Table chillout.tarefa
-- -------------------------------------------------
CREATE TABLE IF NOT EXISTS chillout.tarefa (
    tar_id_lista INT NOT NULL,
    tar_urgencia INT,
    tar_descricao VARCHAR(280),

    -- tar_id_lista configuração --
    FOREIGN KEY (tar_id_lista)
    REFERENCES chillout.lista_tarefas(lista_id)
    ON UPDATE CASCADE ON DELETE CASCADE,

    CHECK (tar_descricao ~ '^[a-zA-Z0-9._-]{1,}([ ][a-zA-Z0-9._-]{1,})*$'),
    CHECK (tar_urgencia >= 0)
);

-- -------------------------------------------------
-- Table chillout.cor
-- -------------------------------------------------
CREATE TABLE IF NOT EXISTS chillout.cor (
    cor_id SERIAL,
    cor_nome VARCHAR(20),
    cor_valor_hex CHAR(7) NOT NULL,
    cor_num_uso INT NOT NULL DEFAULT 0,
    cor_id_usuario INT NOT NULL,
    PRIMARY KEY (cor_id),

    -- cor_id_usuario configuração --
    FOREIGN KEY (cor_id_usuario)
    REFERENCES chillout.usuario (user_id)
    ON UPDATE CASCADE ON DELETE CASCADE,

    CHECK (cor_valor_hex ~ '#[a-fA-F0-9]{6}'),
    CHECK (cor_nome ~ '^[a-zA-Z0-9._-]{1,}([ ][a-zA-Z0-9._-]{1,})*$'),
    CHECK (cor_num_uso >=0)
);

-- -------------------------------------------------
-- Table chillout.desenho
-- -------------------------------------------------
CREATE TABLE IF NOT EXISTS chillout.desenho (
    des_id SERIAL,
    des_padrao TEXT,
    des_id_usuario INT NOT NULL,
    PRIMARY KEY (des_id),

    -- des_id_usuario configuração --
    FOREIGN KEY (des_id_usuario)
    REFERENCES chillout.usuario (user_id)
    ON UPDATE CASCADE ON DELETE CASCADE
);