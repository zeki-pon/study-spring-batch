# study-spring-batch
Spring Batchを使用するアプリケーションの学習用リポジトリ<br>
[公式のチュートリアル](https://spring.io/guides/gs/batch-processing/)を実施

## tips
- 実行時にジョブが起動しないようにする方法
```yml
# application.ymlで以下を設定
spring:
  batch:
    job:
      enabled: false
```
- 1つ以上のジョブを指定して実行する方法
```
java -jar xxxx.jar --spring.batch.job.names={job_name},{job_name}...
```
課題：ymlに上記の設定を入れるとジョブを指定しても動作しなくなる
