CREATE TABLE notas(
     aluno VARCHAR(10), 
     nota1 INT, nota2 INT, nota3 INT, nota4 INT);
     
insert into notas values('Maria', 8, 9, 10, 6);
insert into notas values('Jose', 7, 8, 5, 9);

select * from notas;

delimiter |
CREATE FUNCTION media (nome VARCHAR(10))
  RETURNS FLOAT
  DETERMINISTIC
  BEGIN
    DECLARE n1,n2,n3,n4 INT;
    DECLARE med FLOAT;
    SELECT nota1,nota2,nota3,nota4 INTO n1,n2,n3,n4 FROM notas WHERE aluno = nome;
    SET med = (n1+n2+n3+n4)/4;
    RETURN med;
  END
delimiter ;

select  media('Maria');

select aluno, media(aluno) from notas;