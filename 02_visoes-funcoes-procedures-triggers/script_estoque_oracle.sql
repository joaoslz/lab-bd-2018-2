drop table produto;

CREATE table produto (
 id NUMBER primary key,
 status CHAR DEFAULT 'A' NOT NULL,
 descricao VARCHAR(50) DEFAULT NULL NULL,
 estoque_minimo NUMBER DEFAULT NULL NULL,
 estoque_maximo NUMBER DEFAULT NULL NULL
);

create sequence seq_idProduto;


insert into produto (id, descricao, estoque_minimo, estoque_maximo) values (seq_idProduto.nextval,  'Caneta', 10, 200);
insert into produto (id, descricao, estoque_minimo, estoque_maximo) values (seq_idProduto.nextval, 'Lapis', 10, 200);
insert into produto (id, descricao, estoque_minimo, estoque_maximo) values (seq_idProduto.nextval, 'Borracha', 10, 200);

insert into produto (id, descricao, estoque_minimo, estoque_maximo) values (seq_idProduto.nextval, 'Apontador', 5, 100);

insert into produto (id, descricao, estoque_minimo, estoque_maximo) values (seq_idProduto.nextval, 'Grafite', 10, 100);
insert into produto (id, descricao, estoque_minimo, estoque_maximo) values (seq_idProduto.nextval, 'Corretivo', 5, 50);

select * from produto;

/*
TABELA ENTRADA_PRODUTO: serão gravadas todas as compras de 
produtos efetuadas para papelaria.
      CREATE TABLE entrada_produto (
      id INT(11) NOT NULL AUTO_INCREMENT,
      id_produto INT(11) NULL DEFAULT NULL,
      qtde INT(11) NULL DEFAULT NULL,
      valor_unitario DECIMAL(9,2) NULL DEFAULT '0.00',
      data_entrada DATE NULL DEFAULT NULL,
      PRIMARY KEY (id));
*/

CREATE TABLE entrada_produto (
  id NUMBER PRIMARY KEY ,
  id_produto NUMBER DEFAULT NULL NULL,
  qtde NUMBER DEFAULT NULL NULL,
  valor_unitario DECIMAL(9,2) DEFAULT 0.00 NULL,
  data_entrada DATE DEFAULT NULL NULL
);

alter table entrada_produto add foreign key (id_produto) references produto(id);

create sequence seq_idEntrada_produto;

/*TABELA “SAIDA_PRODUTO”: serão gravadas todas as Vendas de produtos */

CREATE TABLE saida_produto (
id number NOT NULL,
id_produto number DEFAULT NULL  NULL,
qtde number DEFAULT NULL  NULL,
data_saida DATE DEFAULT NULL NULL,
valor_unitario DECIMAL(9,2) DEFAULT 0.00 NULL,
PRIMARY KEY (id));

alter table saida_produto add foreign key (id_produto) references produto(id);

create sequence seq_idSaida_produto;

/* Cenário desejado: A tabela 'estoque' deve recebe os dados conforme 
as ações executadas nas tabelas de “ENTRADA_PRODUTO” e “SAIDA_PRODUTO”. 

O usuário não terá interação direta como INSERÇÕES, UPDATES E EXCLUSÕES, 
a tabela “ESTOQUE” é somente o resultado das ações de compra e venda 
de produtos.
*/
drop table estoque;

CREATE TABLE estoque (
  id number primary key,
  id_produto number DEFAULT NULL NULL,
  qtde number DEFAULT NULL NULL,
  valor_unitario DECIMAL(9,2) DEFAULT 0.00 NULL
); 

create sequence seq_idEstoque;


/*create procedure pAtualizaEstoque(idProd number, qtdeComprada number, valorUnitario number(9,2) )*/
create or replace procedure pAtualizaEstoque( idProd ESTOQUE.ID%TYPE, 
                                   qtdeComprada ESTOQUE.QTDE%TYPE, 
                                   valorUnitario ESTOQUE.VALOR_UNITARIO%TYPE ) 
is
   total number;
   begin
     select count(id_produto) into total 
     from estoque where id_produto = idProd;
    
     if (total > 0) then
       update estoque 
       set qtde = qtde + qtdeComprada, valor_unitario = valorUnitario 
       where id_produto = idProd;
    else    
       insert into estoque (id, id_produto, qtde, valor_unitario) 
              values (seq_idEstoque.nextval, idProd, qtdeComprada, valorUnitario);
    end if;
end pAtualizaEstoque;

select * from estoque;

-- Execução da procedure
begin
   pAtualizaEstoque(1, 15, 6.50);
end;

select * from estoque;


-- ############################################################
-- ################ FUNÇÕES NO ORACLE #########################

-- ************** Função valida CPF  no oracle ****************
create or replace function valida_cpf (cpf in char) return varchar2 
is
  m_total number default 0;
  m_digito number default 0;
begin
  for i in 1..9 loop
     m_total := m_total + substr (cpf, i, 1) * (11 - i);
  end loop;
  m_digito := 11 - mod (m_total, 11);
  if m_digito > 9 then
    m_digito := 0;
  end if;
  if m_digito != substr (cpf, 10, 1) then
    return 'I';
  end if;
  m_digito := 0;
  m_total := 0;
  
  for i in 1..10 loop
     m_total := m_total + substr (cpf, i, 1) * (12 - i);
  end loop;
  m_digito := 11 - mod (m_total, 11);
  if m_digito > 9 then
    m_digito := 0;
  end if;
  if m_digito != substr (cpf, 11, 1) then
    return 'I';
  end if;
  return 'V';
end valida_cpf;

-- ******** Execução da função valida_cpf: ********
declare
  res varchar2(1) default null;
  begin
 -- res := valida_cpf('02091678520');
    res := valida_cpf(cpf => '02011648920');
    if res = 'V' then
      dbms_output.put_line('CPF válido: ' || res );
    else
      dbms_output.put_line('CPF inválido:' || res);
    end if;
 end;


/* ####################################### */
/* -------------- TRIGGERS --------------- */

-- 1. Trigger trg_limiteEstoque_BU: usada para monitorar
--    as colunas estoque_min, estoque_max na tabela produto
-- as mudanças serão armazenadas na tabela auditoria

-- inicialmente precisamos criar a tabela auditoria.
create table auditoria (
	  id int primary key,
    usuario varchar2(45),
    datahora date,
    operacao varchar2(45)
);

create sequence seq_idAuditoria;

alter table auditoria add estoque_min_old int;
alter table auditoria add estoque_min_new int;

desc auditoria;

-- Trigger para tabela produto do sistema de estoque
CREATE OR REPLACE TRIGGER trg_limiteestoque_bu 
BEFORE UPDATE OF ESTOQUE_MAXIMO,ESTOQUE_MINIMO ON PRODUTO 
REFERENCING NEW AS NEW OLD AS OLD
for each row when( (old.estoque_minimo <> new.estoque_minimo) or 
                   (old.estoque_maximo <> new.estoque_maximo) )
BEGIN
   insert into auditoria(id, usuario, datahora, operacao, estoque_min_old, estoque_min_new) 
              values(seq_idAuditoria.nextval, SYS_CONTEXT('USERENV', 'SESSION_USER') , 
              SYSDATE, 'update', :old.estoque_minimo, :new.estoque_minimo);
END;

-- TESTE DO TRIGGER trg_limiteestoque_bu
select * from produto;

-- deve atualizar a tabela auditoria
update produto set estoque_minimo=20 where id = 1;

-- não deve atualizar a tabela auditoria
update produto set descricao= 'Lapis 2b' where id = 2;

select * from auditoria;

-- ##################################################

-- Tutoria sobre triggers no Oracle
----------------------------------
-- CREATING DATABASE TRIGGERS
----------------------------------

-- PL/SQL BLOCKS ASSOCIATED WITH TABLES, VIEWS, DATABASE OR SCHEMA
-- EXECUTES WHENEVER A PROGRAMMED EVENT OCCURS
-- USE TO IMPLEMENT BUSINESS LOGIC: VERIFICATION, LOGGING, TRACE, INTEGRITY, ETC
-- TWO MAIN GROUPS:  DML TRIGGER AND DDL TRIGGERS. THERE IS ALSO SYSTEM TRIGGERS

-- 1) DML TRIGGERS

-- TIMING: BEFORE, AFTER (DEFAULT)
-- ALSO: INSTEAD OF TRIGGERS FOR VIEWS

-- EVENT: INSERT, UPDATE, DELETE

-- TYPE: ROW OR STATEMENT (DEFAULT)

-- WHEN CLAUSE: RESTRICT ROWS THAT FIRE THE TRIGGER 

-- FIRING SEQUENCE:

--  >> BEFORE STATEMENT <<
--  >> BEFORE ROW <<
--  >> AFTER ROW <<

--- >>>!!!CONSTRAINTS CHECK!!! <<<

--  >> AFTER STATEMENT <<


-- SYNTAX:

CREATE [OR REPLACE] TRIGGER <name>
{timing}  event1 | event2 ...
ON {table}
{trigger_body}


-- AUXILIARY TABLE:
drop table tb_aux;

CREATE TABLE TB_AUX
( 
    ID INT
   ,NAME VARCHAR2(100)
)

SELECT * FROM TB_AUX;


-- EXAMPLE 1: BEFORE-STATEMENT TRIGGER

CREATE OR REPLACE TRIGGER BEF_STA
BEFORE INSERT ON TB_AUX
BEGIN

  DBMS_OUTPUT.PUT_LINE('Inside a BEFORE STATEMENT TRIGGER!');  
  
  -- USE THE FOLLOWING LINE  TO CANCEL THE ACTION
  --  RAISE_APPLICATION_ERROR(-20200,'TEST');

END;

-- TESTING

INSERT INTO TB_AUX VALUES(1,'A');
INSERT INTO TB_AUX VALUES(2,'B');
INSERT INTO TB_AUX VALUES(3,'C');

delete from tb_aux;

SELECT * FROM TB_AUX;

DROP TRIGGER BEF_STA

-- EXAMPLE 2: BEFORE-ROW TRIGGER

CREATE OR REPLACE TRIGGER BEF_ROW
BEFORE UPDATE OF NAME ON TB_AUX
FOR EACH ROW
BEGIN

  DBMS_OUTPUT.PUT_LINE('Inside a BEFORE ROW TRIGGER!');  

END;

-- TESTING
-- O trigger não sera executado
UPDATE TB_AUX
SET ID = 0;


-- o trigger será executado
UPDATE TB_AUX
SET NAME = 'teste' where id=0;

SELECT * FROM TB_AUX;

DROP TRIGGER BEF_ROW;

-- EXAMPLE 3: AFTER-STATEMENT TRIGGER AND USING PREDICATES
CREATE OR REPLACE TRIGGER AFT_STA
AFTER INSERT OR UPDATE OR DELETE ON TB_AUX
BEGIN

  IF DELETING THEN
    RAISE_APPLICATION_ERROR(-20200,'DELETE CANCELED!'); 
    -- no mysql poderia ficaria assim
    -- SIGNAL 'DELETE CANCELED!'
  ELSIF INSERTING THEN
    RAISE_APPLICATION_ERROR(-20200,'INSERT CANCELED!'); 
  ELSIF UPDATING('NAME') THEN
    RAISE_APPLICATION_ERROR(-20200,'UPDATE CANCELED!'); 
  END IF;

END;

drop trigger AFT_STA;

-- TESTING
SELECT * FROM TB_AUX;

DELETE TB_AUX WHERE ID = 0;

INSERT INTO TB_AUX VALUES(4,'D')

UPDATE TB_AUX 
SET NAME = 'Z'

DROP TRIGGER AFT_STA;

-- EXAMPLE 4: AFTER-ROW TRIGGER AND THE NEW AND OLD QUALIFIERS

CREATE OR REPLACE TRIGGER AFT_ROW
AFTER INSERT OR UPDATE OR DELETE ON TB_AUX
FOR EACH ROW
BEGIN

  IF DELETING THEN
    DBMS_OUTPUT.PUT_LINE('OLD ID:' || TO_CHAR(:OLD.ID) );  
    DBMS_OUTPUT.PUT_LINE('OLD NAME:' || TO_CHAR(:OLD.NAME) );  
  
  ELSIF INSERTING THEN
    DBMS_OUTPUT.PUT_LINE('NEW ID:' || TO_CHAR(:NEW.ID) );  
    DBMS_OUTPUT.PUT_LINE('NEW NAME:' || TO_CHAR(:NEW.NAME) );  
  
  ELSIF UPDATING('NAME') THEN
  
    DBMS_OUTPUT.PUT_LINE('NEW ID:' || TO_CHAR(:NEW.ID) );  
    DBMS_OUTPUT.PUT_LINE('OLD ID:' || TO_CHAR(:OLD.ID) );  
    
    DBMS_OUTPUT.PUT_LINE('NEW NAME:' || TO_CHAR(:NEW.NAME) );  
    DBMS_OUTPUT.PUT_LINE('OLD NAME:' || TO_CHAR(:OLD.NAME) );  

  END IF;

END;


drop trigger AFT_ROW;
-- TESTING

SELECT * FROM TB_AUX;

DELETE TB_AUX WHERE ID = 0

INSERT INTO TB_AUX VALUES(4,'D')

UPDATE TB_AUX 
SET NAME = 'Z'

DROP TRIGGER AFT_ROW;

-- EXAMPLE 5: WHEN CLAUSE

CREATE OR REPLACE TRIGGER AFT_WHEN
AFTER INSERT ON TB_AUX
FOR EACH ROW WHEN (NEW.ID = 10)
BEGIN
  DBMS_OUTPUT.PUT_LINE('FIRING THE TRIGGER ONLY WHEN THE NEW ID = 10!');  
END;

-- TESTING
SELECT * FROM TB_AUX;

INSERT INTO TB_AUX VALUES(9,'M');

INSERT INTO TB_AUX VALUES(10,'N');

DROP TRIGGER AFT_WHEN

-- EXAMPLE 6: INSTEAD OF TRIGGERS

CREATE VIEW VW_AUX
AS 
SELECT *
FROM TB_AUX;

CREATE OR REPLACE TRIGGER TR_IO
INSTEAD OF INSERT ON VW_AUX
FOR EACH ROW
BEGIN
   INSERT INTO TB_AUX VALUES(:NEW.ID, :NEW.NAME);
END;

-- TESTING
delete from tb_aux where id = 0;

SELECT * FROM VW_AUX;
SELECT * FROM TB_AUX;

INSERT INTO VW_AUX VALUES(99,'W');

DROP TRIGGER TR_IO;

DROP VIEW VW_AUX;

DROP TABLE TB_AUX;

-- 2) DDL TRIGGERS


-- FIRES THE TRIGGER FOR SPECIFIC DDL USER ACTIONS

-- SYNTAX:

CREATE [OR REPLACE] TRIGGER <name>
{timming}  [ddl_event1, ddl_event2...]
ON {database | schema }
{trigger_body}

-- EXAMPLE 1: CREATE A TRIGGER FOR A DDL STATEMENT

CREATE OR REPLACE TRIGGER TRIG_CREATE
AFTER CREATE ON DATABASE
BEGIN
    DBMS_OUTPUT.PUT_LINE('Someone used the CREATE command!');  
END;

CREATE TABLE T_X
(
      F1 INT
)

DROP TABLE T_X

DROP TRIGGER TRIG_CREATE

end;


-- ############################################################
-- Trigger para atualizar o estoque quando houver alterações
-- nas tabelas entrada_produto ou saida produto

CREATE OR REPLACE TRIGGER trg_ajustar_entrada_estoque
AFTER INSERT OR UPDATE OR DELETE ON ENTRADA_PRODUTO
FOR EACH ROW
BEGIN
  IF INSERTING THEN
     pAtualizaEstoque( :new.id_produto, :new.qtde, :new.valor_unitario );
  
  ELSIF UPDATING THEN
     pAtualizaEstoque( :new.id_produto, :new.qtde - :old.qtde, :new.valor_unitario);
  
  ELSIF DELETING THEN
     pAtualizaEstoque( :old.id_produto, :old.qtde * -1, :old.valor_unitario);
     DBMS_OUTPUT.PUT_LINE('Antes de excluir...!'); 
  END IF;
END;

-- ##################################################
/* ----- teste do trigger trg_entradaProduto_AI  ---- */
select * from entrada_produto;

select * from estoque;

select * from produto;

-- precisamos descobrir o idioma para usar o 
-- formato de datas correto no oracle.
select * from v$nls_parameters;


insert into entrada_produto (id, id_produto, qtde, valor_unitario, data_entrada) 
       values (seq_idEntrada_produto.nextval, 4, 20, 6.5, '24-OUT-2016');

update entrada_produto set qtde=qtde+20 where id_produto = 4;     
  
select * from entrada_produto where id_produto=3;                                
delete from entrada_produto where id_produto=3;