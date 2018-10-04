/* ####### Group by e funções de agregação ##################

/* explicar o modelo */

/* Exibindo alunos com matricula  */
select nome 
from aluno
where exists (select matricula.id from matricula where matricula.aluno_id = aluno.id);

select distinct a.nome 
from aluno a join matricula m on a.id = m.aluno_id; 

### consulta aqui

/* Exibindo alunos sem matrícula */
select nome
from aluno
where not exists (select matricula.id from matricula where matricula.aluno_id = aluno.id);

select distinct a.nome /* , m.id */ 
from aluno a 
       left join matricula m on a.id = m.aluno_id
where m.id is not null; 
       

/* Existem exercícios não respondidos? */


/* Mostre o nome do curso e a quantidade de matrículas, agrupadas por curso. */

select c.nome, count(m.id) 
from curso c join matricula m on c.id = m.curso_id;


select c.nome as curso, count(m.id) as "Total de Matriculados"
from curso c join matricula m on c.id = m.curso_id
group by c.nome
order by count(m.id) desc;


select c.nome, count(m.id) from curso c
     join matricula m on m.curso_id=c.id
    group by c.nome;

/* ************************** */
/* Quero as médias das notas por curso? */
select c.nome, avg(n.nota) as media, min(n.nota) nota_minima
from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
group by c.nome


select c.nome, round(avg(n.nota), 2) as media 
 from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
 group by c.nome;
 
 /* Devolva o curso e as médias de notas, 
 levando em conta somente alunos que tenham 
 "Silva" ou "Santos" no sobrenome.*/
 
 select c.nome, round(avg(n.nota), 2) as media 
 from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join matricula m on m.curso_id = c.id
    join aluno a on a.id = m.aluno_id
 where a.nome like '%Silva' or a.nome like '%Santos'   
 group by c.nome;
 
 select c.nome, round(avg(n.nota), 2) as media 
 from aluno a, nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
 where a.id = r.aluno_id and
      ( a.nome like '%Silva' or a.nome like '%Santos')
 group by c.nome;
 
 select a.nome from aluno a where a.nome like '%Silva' or a.nome like '%Santos';
 
 /* Conte a quantidade de respostas por exercício. 
   Exiba a pergunta e o número de respostas.*/

 select e.pergunta, r.resposta_dada
 from exercicio e 
    join resposta r on e.id = r.exercicio_id
 order by e.pergunta;
 
 
 select e.pergunta, count(r.resposta_dada) as total_resposta
 from exercicio e join resposta r on e.id = r.exercicio_id
 group by e.pergunta
 order by total_resposta desc;
 
/* ####### Filtrando agregações e o HAVING ###################### */

/* Selecione a média das notas de um aluno por curso
   Vamos agrupar esse resultado por nome do curso e aluno: */

select a.nome, c.nome, round(avg(n.nota), 2) as media 
from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on a.id = r.aluno_id
group by c.nome, a.nome;

/* Com as médias dos alunos por curso , 
   falta agora ver quais alunos estão com média baixa e vão ficar em recuperação. 
*/   
select a.nome, c.nome, avg(n.nota) as media 
from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on a.id = r.aluno_id
group by a.nome, c.nome
having (avg(n.nota) < 5);


# precisamos da clausula having para filtrar dados de agrupamento


/* consulta: Quais cursos tém menos de 3 alunos. */
select c.nome, count(m.aluno_id) as qtdAlunos
from matricula m join curso c on m.curso_id=c.id
group by c.nome
having count(m.aluno_id)<=3;


/* ##### Selecionando valores distintos, mas sem repetições ### */

/*Querem saber todas as formas de pagamento que temos cadastradas em nosso 
banco de dados, para saber se são as mesmas formas permitidas por eles.
Temos uma coluna tipo na tabela matricula , que representa o tipo de pagamento. 
Vamos selecionar essa coluna para ver os tipos de pagamento:
*/

select tipo from matricula;

/* Vemos que alguns valores se repetem, porque tem v?rios alunos que usaram a 
   mesma forma de pagamento. 

Quero pegar as formas de pagamento distintas, mas sem as repetidas.
*/
select distinct tipo from matricula;


/* ### A clausula IN ###### */

/*
Queremos agora saber quantas matrículas temos, 
mais especificamente para pessoas jurídica ou física, por curso. 
Se eu quero a quantidade de matrículas, tenho que fazer a contagem delas, 
também vamos selecionar o nome do curso e associar a matr?cula com o curso:
*/

select c.nome, m.id 
from 
matricula m join curso c on m.curso_id = c.id;

/* Como pretendemos fazer  a contagem, temos que agrupar 
esse resultado pelo nome do curso: */

select c.nome, count(m.id) 
from matricula m 
     join curso c on m.curso_id = c.id
group by c.nome;


/* Vamos filtrar. Antes, para melhorar, vamos adicionar 
a coluna do tipo da matricula no resultado: */

select c.nome, m.tipo, count(m.id) as quantidade from matricula m 
    join curso c on m.curso_id = c.id 
where m.tipo = 'PAGA_PJ' or 
      m.tipo = 'PAGA_PF'
group by c.nome, m.tipo;

/*Repare que temos um grupo de valores, no nosso caso são 2, PAGA_PJ e PAGA_PF, 
mas poderemos ter mais, e quando queremos passar um grupo de valores em 
uma condição, podemos usar o IN: */

select c.nome, count(m.id) as quantidade from matricula m 
    join curso c on m.curso_id = c.id 
where m.tipo in ('PAGA_PJ', 'PAGA_PF')
group by c.nome;

/* Sabendo quais cursos determinados alunos fizeram */
/*
Cenário: um instrutor comentou que três alunos foram exemplares, 
em uma turma que ele deu aula recentemente. Queremos saber todos os 
cursos que esses alunos fizeram.
Os alunos, são o João, o Alberto e a Renata. Ids 1, 3 e 4.
*/

select a.nome, c.nome from curso c
    join matricula m on m.curso_id = c.id
    join aluno a on m.aluno_id = a.id
where m.aluno_id in (1, 3, 4);

/* Para melhorar esse resultado, podemos ordenar pelo nome dos alunos */

select a.nome, c.nome from curso c
    join matricula m on m.curso_id = c.id
    join aluno a on m.aluno_id = a.id
where m.aluno_id in (1, 3, 4)
order by a.nome;

/* Traga todas as perguntas e a quantidade de respostas de cada uma. 
Mas dessa vez, somente dos cursos com ID 1 e 3. */


select e.pergunta, count(r.id) as respostas
from exercicio e 
  join resposta r on e.id = r.exercicio_id
group by e.pergunta
order by respostas desc;
 



select e.pergunta, count(r.id) 
from exercicio e 
  join resposta r on e.id = r.exercicio_id
  join secao s on s.id = e.secao_id
  join curso c on s.curso_id = c.id
where c.id in (1,3)
group by e.pergunta;

/* ########   Sub-queries ############# */
/*
Comparando a média geral de todos os cursos com a média de cada aluno

Cenário: Queremos um relatório de como está a média de cada aluno em cada curso 
comparado com a média geral de todos os cursos, para saber como estão o 
desempenho de um aluno comparado com o desempenho de todos os alunos em geral.

Esse relatório é um pouco mais complexo, 
pois precisamos das seguintes informações: nome do curso, nome do aluno, 
média do curso e a diferença da média do aluno com a média geral.

Vamos primeiro fazer a média de cada aluno. 
Lembre que quando usamos alguma funções de agregação, no caso AVG. 
Queremos agrupar por aluno e curso:
*/
select a.nome, c.nome as curso, avg(n.nota) as media
  from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
group by a.nome, c.nome;

/* Aqui temos a média de cada aluno individualmente. */

/* Como fazemos pra pegar a média geral? */

select avg(n.nota) from nota n;

/* Queremos fazer uma subtraçãoo no select, algo como: */

select a.nome as aluno, 
       c.nome as nome_curso,  
       avg(n.nota) as media_geral, 
       avg(n.nota) - (select avg(nota) from nota) as diferenca
    from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
group by c.nome, a.nome;

/* remover */
select a.nome, 
       c.nome,  
       round(avg(n.nota), 2) as media_geral, 
       round((avg(n.nota) - (select avg(nota) from nota)), 2) as diferenca
    from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
    join matricula m on a.id = m.aluno_id
where year(m.data) = 2014     
group by c.nome, a.nome;

# remover 
select a.nome, round(avg(n1.nota), 2) as media, 
avg(n1.nota) - (select avg(n2.nota) from nota n2) as diferenca
from nota n1
join resposta r on r.id = n1.resposta_id
join exercicio e on e.id = r.exercicio_id
join secao s on s.id = e.secao_id
join aluno a on a.id = r.aluno_id
where 
a.id in (select aluno_id from matricula where year(data) =2014)
group by a.nome;


select a.nome, 
       c.nome, 
       round(avg(n.nota), 2) as media_geral
    from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
group by c.nome, a.nome
having avg(n.nota) > (select avg(nota) from nota);

/* Acabamos de fazer a query para selecionar a média geral, então: */

select a.nome, c.nome, avg(n.nota) as media, 
       avg(n.nota) - (select avg(n.nota) from nota n) as diferenca from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
group by c.nome, a.nome;



/* Cenario gostaria de saber se os alunos estão respondendo todos os exercícios. 
   Quero saber o número de respostas que cada um deu individualmente. */

/* Para selecionar todos os alunos, fazemos:*/
select a.nome from aluno a;

/* Para contar todas as respostas: */
select count(r.id) from resposta r;

/* Precisamos juntar essas duas queries, mas não só juntá-las,
  precisamos relacioná-las atravez da coluna aluno_id 
*/ 
select a.nome, 
    (select count(r.id) from resposta r where r.aluno_id = a.id) as respostas 
from aluno a;


/*
Cenário: Queremos saber a quantidade de cursos que cada aluno faz (ou fez), 
         ou seja, quantas matrículas diferentes um aluno tem, 
         para tentar efetuar futuras vendas de cursos, 
         pois alunos sem ou com poucas matrículas são clientes em potencial.
*/

/* Para selecionar os alunos, fazemos: */
select a.nome from aluno a;

/*E para pegar as matrículas:*/
select count(m.id) from matricula m;

/*Agora, vamos juntar as duas queries:*/
create view matriculas_por_aluno as
select a.nome, 
    (select counmatriculas_por_alunot(m.id) from matricula m where m.aluno_id = a.id) as matriculas 
from aluno a
order by matriculas desc;

/*
select a.nome, count(m.id) as matriculas
from aluno a 
       left join matricula m on a.id = m.aluno_id
group by a.nome
order by matriculas desc;
*/