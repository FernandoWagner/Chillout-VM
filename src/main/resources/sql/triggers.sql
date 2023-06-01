-- -------------------------------------------------
-- Trabalho Interdisciplinar 2 - Grupo 10 - Burnout
-- Integrantes: André Santos Alves, André Scianni,
-- Carlos Vinícius, Fernando Wagner
-- -------------------------------------------------

-- -------------------------------------------------
-- Trigger update_num_tarefas() que atualiza o valor
-- armazenado pela tupla lista_num_tarefas toda vez
-- que ocorre uma remoção ou inserção de uma tupla
-- na tabela tarefa.
-- -------------------------------------------------
CREATE FUNCTION update_num_tarefas()
RETURNS TRIGGER AS $$
BEGIN
  IF (TG_OP = 'INSERT') THEN
    UPDATE burnout.lista_tarefas
    SET lista_num_tarefas = lista_num_tarefas + 1
    WHERE lista_id = NEW.tar_id_lista;
    RETURN NEW;
  ELSIF (TG_OP = 'DELETE') THEN
    UPDATE burnout.lista_tarefas
    SET lista_num_tarefas = lista_num_tarefas - 1
    WHERE lista_id = OLD.tar_id_lista;
    RETURN OLD;
  END IF;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_num_tarefas_trigger
BEFORE INSERT OR DELETE ON burnout.tarefa
FOR EACH ROW
EXECUTE FUNCTION update_num_tarefas();

-- -------------------------------------------------------
-- Trigger impedir_novo_num_tarefas() que funciona
-- impedindo que inserts ou updates realizados diretamente
-- pelo usuario alterem a coluna lista_num_tarefas.
-- -------------------------------------------------------
CREATE OR REPLACE FUNCTION impedir_novo_num_tarefas()
RETURNS TRIGGER AS $$
BEGIN
  IF (TG_OP = 'INSERT') THEN
    RAISE NOTICE 'Contador de número de tarefa inicializado em zero';
    NEW.lista_num_tarefas := 0;
  ELSIF pg_trigger_depth() <= 1 AND NEW.lista_num_tarefas != OLD.lista_num_tarefas THEN
    RAISE WARNING 'Não é possível alterar o contador de número de tarefas';
    NEW.lista_num_tarefas := OLD.lista_num_tarefas;
  END IF;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER impedir_novo_num_tarefas_trigger
BEFORE INSERT OR UPDATE ON burnout.lista_tarefas
FOR EACH ROW
EXECUTE FUNCTION impedir_novo_num_tarefas();
