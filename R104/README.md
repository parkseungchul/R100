create schema r100_schema;
grant all privileges on r100_schema.* to 'user01'@'%';

### 배치 commit size ###
 - --job.name=jpaPageJob v=1 chunkSize=100
 - chunkSize 를 파라미터로 받으면서 속도 차이를 확인
 - BATCH_JOB_EXECUTION, BATCH_JOB_EXECUTION_CONTEXT

### 배치 parallel 
 - --job.name=jpaPageFlowJob v=1 
 - parallel 처리
<pre><code>
select
       count(case when dept_no >= 5000 then 1 end) as job1,
       count(case when dept_no < 5000 then 1 end) as job3
FROM
    dept2;
</code></pre>