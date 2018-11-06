create database estoquedb;

use estoquedb;

drop table produto;

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
drop table estoque;

CREATE TABLE estoque (
id INT(11) NOT NULL AUTO_INCREMENT,
id_produto INT(11) NULL DEFAULT NULL,
qtde INT(11) NULL DEFAULT NULL,
valor_unitario DECIMAL(9,2) NULL DEFAULT '0.00',
PRIMARY KEY (id),
constraint foreign key (id_produto) references produto(id)
);

drop procedure pAtualizaEstoque;

delimiter $$

create procedure pAtualizaEstoque(idProd int, qtdeComprada int, valorUnitario decimal(9,2)) 
begin
  declare total int;
  
  select id_produto into total from estoque where id_produto = idProd;
  
  if (total is not null) then
     update estoque 
     set qtde = qtde + qtdeComprada, 
         valor_unitario = valorUnitario 
     where id_produto = idProd;
  else    
     insert into estoque (id_produto, qtde, valor_unitario) 
            values (idProd, qtdeComprada, valorUnitario);
  end if;
end $$


delimiter ;

call pAtualizaEstoque(2, 10, 3.50);

select * from estoque;

select * from produto;









