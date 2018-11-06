/* ####### Junções, sub-consultas, group by, funções de agregação e visões ###########

/* explicar o modelo */

/* clausula EXISTS */

/* Exibindo alunos com matricula com a clausula exists */
select a.nome from aluno a
where exists (select m.id from matricula m where m.aluno_id = a.id);

/* Exibindo alunos com matricula com join */
select distinct nome
from aluno join matricula on aluno.id = matricula.aluno_id;


/* Exibindo alunos sem matrícula com a clausula exists */
select nome from aluno 
where not exists (select m.id from matricula m where m.aluno_id = aluno.id);

select nome, matricula.id
from aluno left join matricula on aluno.id = matricula.aluno_id;

# Busca alunos sem matricula */
select distinct nome
from aluno left join matricula on aluno.id = matricula.aluno_id
where matricula.id is null;

/* Seleciona todos os alunos que se matricularam nos últimos 
   12 meses */
select a.nome from aluno a
where exists (select m.id 
              from matricula m 
              where m.aluno_id = a.id and 
                    m.data > now() - interval 12 month);


/* Existem exercícios não respondidos? */
select e.pergunta from exercicio e 
where not exists (select r.id from resposta r where r.exercicio_id = e.id); 


 ########## Limitando a quantidade de respostas e Paginação ###########
 select * from aluno limit 0,3;
 
 select * from aluno limit 3;

 select * from aluno limit 3, 4;

/* Mostre o nome do curso e a quantidade de matrículas, 
   agrupadas por curso. */
#-----------------------------------------
select nome, count(matricula.id)
from curso 
       join matricula on curso.id = matricula.curso_id;
#-----------------------------------------

   
select nome, count(matricula.id) as 'total_matriculas'
from curso 
       join matricula on curso.id = matricula.curso_id
group by nome;


select c.nome, count(m.id) 
from curso c
     join matricula m on m.curso_id=c.id
    group by c.nome;

/* ************************** */
/* Quero as médias das notas por curso? */

select c.nome, avg(n.nota)  
 from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id;

select c.nome, round(avg(n.nota), 2) as media 
 from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
 group by c.nome;

select c.nome, avg(n.nota) as media, min(n.nota) nota_minima*/
from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
group by c.nome
having c.nome like 'S%';
 
 /* Devolva o curso e as médias de notas, 
 levando em conta somente alunos que tenham 
 "Silva" ou "Santos" no sobrenome.*/
 
 select a.nome, c.nome, round(avg(n.nota), 2) as media 
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
from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on a.id = r.aluno_id
 where a.nome like '%Silva' or a.nome like '%Santos'   
 group by c.nome;
 
select c.nome, a.nome as aluno, round(avg(n.nota), 2) as media 
from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on a.id = r.aluno_id
group by c.nome, a.nome;


 
 select c.nome, round(avg(n.nota), 2) as media 
 from aluno a, nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
 where a.id = r.aluno_id and
      ( a.nome like '%Silva' or a.nome like '%Santos')
 group by c.nome;
 
 select a.nome
 from aluno a 
 where a.nome like '%Silva' or a.nome like '%Santos';
 
 /* Conte a quantidade de respostas por exercício. 
   Exiba a pergunta e o número de respostas.*/

 select e.pergunta, r.resposta_dada
 from exercicio e join resposta r on e.id = r.exercicio_id;
 
 
 select e.pergunta, count(r.resposta_dada) as total_resposta
 from exercicio e join resposta r on e.id = r.exercicio_id
 group by e.pergunta
 order by total_resposta desc;
 

/* ####### Filtrando agregações e o HAVING ###################### */

/* Selecionando a média das notas de um aluno por curso
   Agora precisamos então pegar a média das notas de um aluno em cada curso.
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
   falta agora ver quais alunos estão com média baixa e 
   vão ficar em recuperação. 
*/   
select a.nome, c.nome, avg(n.nota) as media from nota n
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on a.id = r.aluno_id
group by a.nome, c.nome
having (avg(n.nota) < 5);

# precisamos da clausula having para filtrar dados de agrupamento


/* consulta: Quais cursos tem menos de 3 alunos. */
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

/*Observe que temos um grupo de valores na consulta anterior, 'PAGA_PJ e PAGA_PF', 
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


select e.pergunta, count(r.id) from
exercicio e join resposta r on e.id = r.exercicio_id
join secao s on s.id = e.secao_id
join curso c on s.curso_id = c.id
where c.id in (1,3)
group by e.pergunta;

/* ########   Sub-queries ############# */
/*
Comparando a média geral de todos os cursos com a média de cada aluno

Cenário: Queremo um relatório de como está a média de cada aluno em cada curso 
comparado com a média geral de todos os cursos, para saber como estão o 
desempenho de um aluno comparado com o desempenho de todos os alunos em geral.

Esse relatório é um pouco mais complexo, 
pois precisamos das seguintes informações: nome do curso, nome do aluno, 
média do curso e a diferença da média do aluno com a média geral.

Vamos primeiro fazer a média de cada aluno. 
Lembre que quando usamos alguma funções de agregação, no caso AVG. 
Queremos agrupar por aluno e curso:
*/
select a.nome, c.nome, round(avg(n.nota),2) 
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
group by c.nome, a.nome;

create view alunos_acima_media as
select a.nome as aluno, 
       c.nome as curso, 
       round(avg(n.nota), 2) as media_geral
    from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
group by c.nome, a.nome
having avg(n.nota) > (select avg(nota) from nota);

select * from alunos_acima_media;

/* Acabamos de fazer a query para selecionar a média geral, então: */

select a.nome, c.nome, avg(n.nota) as media, 
       avg(n.nota) - (select avg(n.nota) from nota n) as diferenca from nota n 
    join resposta r on r.id = n.resposta_id
    join exercicio e on e.id = r.exercicio_id
    join secao s on s.id = e.secao_id
    join curso c on c.id = s.curso_id
    join aluno a on r.aluno_id = a.id
group by c.nome, a.nome;


/* E se precisarmos listar apenas os alunos que estão acima ou abaixo da média */

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
         pois alunos sem ou com poucas matr?culas s?o clientes em potencial.
*/

/* Para selecionar os alunos, fazemos: */
select a.nome from aluno a;

/*E para pegar as matrículas:*/
select count(m.id) from matricula m;

/*Agora, vamos juntar as duas queries:*/
select a.nome, 
    (select count(m.id) from matricula m where m.aluno_id = a.id) as matriculas 
from aluno a;


