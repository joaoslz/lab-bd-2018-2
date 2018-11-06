
create view media_curso(curso, media)
as
select c.nome, avg(n.nota) from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
group by c.nome;


select * from media_curso;

create table tmp_media_curso as select * from media_curso;

select * from tmp_media_curso;

drop procedure sp_mediaPorCurso;

delimiter $$
create procedure sp_mediaPorCurso() 
begin
    
    delete from tmp_media_curso;
    insert into tmp_media_curso select * from media_curso;
    select * from tmp_media_curso;
end
$$
delimiter ;


call sp_mediaPorCurso();

/* ------------------------*/

select * from aluno;
select * from aluno limit 2, 3;


drop procedure selecionar_alunos;

delimiter $$
CREATE PROCEDURE selecionar_alunos(IN quantidade INT)
BEGIN
	SELECT * FROM aluno LIMIT quantidade;
END $$
DELIMITER ;


call selecionar_alunos(4);

drop procedure sp_quantidadeAlunos;

DELIMITER $$

CREATE PROCEDURE sp_quantidadeAlunos(OUT quantidade INT)
BEGIN
	SELECT COUNT(*) INTO quantidade FROM aluno;	
END 
$$
DELIMITER ;


call sp_quantidadeAlunos(@total);
select @total;








