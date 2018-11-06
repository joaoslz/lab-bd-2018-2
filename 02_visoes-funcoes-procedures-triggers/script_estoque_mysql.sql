
create database estoquedb;

use estoquedb;

CREATE TABLE produto (
id INT(11) NOT NULL AUTO_INCREMENT,
status CHAR(1) NOT NULL DEFAULT 'A',
descricao VARCHAR(50) NULL DEFAULT NULL,
estoque_minimo INT(11) NULL DEFAULT NULL,
estoque_maximo INT(11) NULL DEFAULT NULL,
PRIMARY KEY (id));

insert into produto (descricao, estoque_minimo, estoque_maximo) values ('Caneta', 10, 200);
insert into produto (descricao, estoque_minimo, estoque_maximo) values ('Lapis', 10, 200);
insert into produto (descricao, estoque_minimo, estoque_maximo) values ('Borracha', 10, 200);
insert into produto (descricao, estoque_minimo, estoque_maximo) values ('Apontador', 5, 100);
insert into produto (descricao, estoque_minimo, estoque_maximo) values ('Grafite', 10, 100);
insert into produto (descricao, estoque_minimo, estoque_maximo) values ('Corretivo', 5, 50);

select * from produto;

/*
TABELA ENTRADA_PRODUTO: serão gravadas todas as compras de 
produtos efetuadas para papelaria.
*/

CREATE TABLE entrada_produto (
id INT(11) NOT NULL AUTO_INCREMENT,
id_produto INT(11) NULL DEFAULT NULL,
qtde INT(11) NULL DEFAULT NULL,
valor_unitario DECIMAL(9,2) NULL DEFAULT '0.00',
data_entrada DATE NULL DEFAULT NULL,
PRIMARY KEY (id));

alter table entrada_produto add foreign key (id_produto) references produto(id);


/*TABELA “SAIDA_PRODUTO”: serão gravadas todas as Vendas de produtos */

CREATE TABLE saida_produto (
id INT(11) NOT NULL AUTO_INCREMENT,
id_produto INT(11) NULL DEFAULT NULL,
qtde INT(11) NULL DEFAULT NULL,
data_saida DATE NULL DEFAULT NULL,
valor_unitario DECIMAL(9,2) NULL DEFAULT '0.00',
PRIMARY KEY (id));

alter table saida_produto add foreign key (id_produto) references produto(id);

/* Cenário desejado: Essa tabela somente recebe os dados conforme 
as ações executadas nas tabelas de “ENTRADA_PRODUTO” e “SAIDA_PRODUTO”. 
O usuário não terá interação direta como INSERÇÕES, UPDATES E EXCLUSÕES, 
a tabela “ESTOQUE” é somente o resultado das ações de compra e venda 
de produtos.
*/
CREATE TABLE estoque (
id INT(11) NOT NULL AUTO_INCREMENT,
id_produto INT(11) NULL DEFAULT NULL,
qtde INT(11) NULL DEFAULT NULL,
valor_unitario DECIMAL(9,2) NULL DEFAULT '0.00',
PRIMARY KEY (id),
constraint foreign key (id_produto) references produto(id)
);


/* ####################################### */
/* ------------- PROCEDURES -------------- */

-- Exemplo básico
delimiter $$

select 10 + 10 as conta $$


create procedure nome() 
begin
    select 30 + 10 as conta;
end $$

select 10 + 10 as conta$$

delimiter ;

call nome();

drop procedure nome;

delimiter $$

create procedure soma(op1 int, op2 int) 
begin
  select op1 + op2 as resultado;
end $$

call soma(34, 65)$$


drop procedure if exists sp_atualizaEstoque;


delimiter $$
create procedure sp_atualizaEstoque(idProd int, 
                                    qtdeComprada int, 
                                    valorUnitario decimal(9,2))  
begin
  declare produtoExistente int;
  
  select id_produto into produtoExistente 
  from estoque 
  where id_produto = idProd;
  
  if (produtoExistente is not null) then
     update estoque set qtde = qtde + qtdeComprada, 
            valor_unitario = valorUnitario 
			where id_produto = idProd;
  else    
     insert into estoque (id_produto, qtde, valor_unitario) 
            values (idProd, qtdeComprada, valorUnitario);
  end if;
end $$
delimiter ;

call sp_atualizaEstoque(1, 15, 5.50);
call sp_atualizaEstoque(2, 12, 5.50);

call sp_atualizaEstoque(4, 30, 15.50);

select * from produto;
select * from estoque;


/* ####################################### */
/* -------------- FUNÇÃO COM CURSOR ----- */


delimiter $$
create function fn_valorTotalEmEstoque() returns decimal(9,2)
begin
  declare quantidade int(11);
  declare valor decimal(9,2);
  declare total_valor decimal(9,2);
  DECLARE done INT DEFAULT FALSE;
  
  # cursor para buscar os registros a serem processados
  # com quantidade > 0
  declare busca_estoque cursor for 
            select qtde, valor_unitario 
            from estoque where qtde > 0;
      
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
     
   # abre o cursor
  open busca_estoque;
     
     set total_valor = 0;   
     # inicia o loop
     estoque: loop
     
         # next() do JDBC
        fetch busca_estoque into quantidade, valor;
          # Código defensivo para sair do laço 
          IF done THEN
			LEAVE estoque;
		  END IF;
          set total_valor = total_valor + (quantidade * valor);
     end loop;
 
  # fecha o cursor   
  close busca_estoque;
        
  return total_valor;

end $$
delimiter ;  
   
select qtde, valor_unitario from estoque;

select qtde, valor_unitario
      from estoque where qtde > 0;
      
select sum(qtde * valor_unitario) from estoque where qtde > 0;      
 
select fn_valorTotalEmEstoque(); 


/* ####################################### */
/* -------------- TRIGGERS --------------- */

# 1. Trigger para auditoria

create table auditoria (
	id int primary key auto_increment,
    usuario varchar(45),
    datahora datetime default current_timestamp,
    operacao varchar(45)
);

alter table auditoria add column estoque_min_old int(11);
alter table auditoria add column estoque_min_new int(11);


desc auditoria;

select * from auditoria;

drop trigger if exists trg_limiteEstoque_BU;

delimiter $$
create trigger trg_limiteEstoque_BU before update on produto
for each row
begin
  if ( (old.estoque_minimo <> new.estoque_minimo) or 
       (old.estoque_maximo <> new.estoque_maximo) ) then
 
     insert into auditoria(usuario, datahora, operacao, estoque_min_old, estoque_min_new) 
              values(current_user, current_timestamp, "update", old.estoque_minimo, new.estoque_minimo);
  end if;
  
end $$

/* execute no terminal */
delimiter ;

select * from auditoria;
select * from produto; 

update produto set estoque_minimo=27 where id=4;
update produto set descricao = 'Lapis 3b' where id=2;

select * from auditoria;



## 2. Trigger que atualiza o estoque quando inserimos na tabela entrada produto

delimiter $$
create trigger trg_entradaProduto_AI after 
                                      insert on entrada_produto
for each row 
begin
  call sp_atualizaEstoque(new.id_produto, new.qtde, new.valor_unitario);

end $$
delimiter ;

/* ----- teste do trigger trg_entradaProduto_AI  ---- */
select * from entrada_produto;
select * from estoque;

insert into entrada_produto (id_produto, qtde, valor_unitario, data_entrada) 
       values (3, 20, 7.60, '2016-10-23');
       
       
 select * from entrada_produto;
 select * from estoque;


/* Triggers para banco de backup separado */

create database backup_estoque;

use backup_estoque;

CREATE TABLE backup_produto as select * from estoquedb.produto;


desc backup_produto;
select * from backup_produto;


use estoquedb;

drop trigger if exists tgr_backup_produto;

delimiter $$

create trigger tgr_backup_produto after insert on estoquedb.produto
for each row
begin
	insert into backup_estoque.backup_produto 
           values(new.id, new.status, new.descricao, new.estoque_minimo, new.estoque_maximo);
end $$

delimiter ;

-- Procedure com parametro INOUT
delimiter $$

create procedure sp_aumenta_estoque_maximo(IN idProd int, INOUT novoEstoqueMaximo int)
begin
    declare produtoExistente int;
  
  select id_produto into produtoExistente 
  from estoque 
  where id_produto = idProd;
  
  if (produtoExistente is not null) then
     update produto set estoque_maximo = novoEstoqueMaximo 
     where id = idProd;
  else    
     SELECT 'Produto Inexistente!' AS mensagem_advertencia; 
  end if;

end $$
-- 181105-001856

delimiter $$

create procedure sp_get_produtos_em_estoque()
begin
    select * from estoque where qtde > 0;
end $$

delimiter ;

-- adiciona a coluna foto na tabela produto
ALTER TABLE estoquedb.produto ADD COLUMN foto BLOB NULL AFTER estoque_maximo;

