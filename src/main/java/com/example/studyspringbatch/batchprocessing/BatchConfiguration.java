package com.example.studyspringbatch.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    /* input */
    // 指定したcsvファイルのデータを各行ごとにPersonへ変換
    @Bean
    public FlatFileItemReader<Person> reader() {
        return new FlatFileItemReaderBuilder<Person>()
            .name("personItemReader")
            .resource(new ClassPathResource("sample-data.csv"))
            .delimited()
            .names(new String[]{"firstName", "lastName"})
            .fieldSetMapper(new BeanWrapperFieldSetMapper<Person>() {{
                setTargetType(Person.class);
            }})
            .build();
    }

    /* processor */
    // データを大文字へ変換するためにPersonItemProcessorのインスタンスを作成
    @Bean
    public PersonItemProcessor processor() {
        return new PersonItemProcessor();
    }

    /* output */
    // ここではItemWriterを作成する。
    @Bean
    public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Person>()
            .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
            .sql("INSERT INTO people (first_name, last_name) VALUES (:firstName, :lastName)")
            .dataSource(dataSource)
            .build();
    }

    /* actual job configuration */
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
            .incrementer(new RunIdIncrementer()) // ジョブが実行状態を維持するのにDBを使用するためincrementerが必要になる
            .listener(listener)
            .flow(step1)
            .end()
            .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10) // 一度に書き込む量を定義
                .reader((reader()))
                .processor(processor())
                .writer(writer)
                .build();

        // chunk()に<Person, Person>というprefixがついている理由
        // chunk()がジェネリックメソッドであるため。(メソッドの引数をメソッド呼び出しの際に決められるメソッドのこと)
        // ここでは、inputの引数としてPerson type, outputの引数としてPerson typeを定義している
    }

    /**
     * バッチ2(コマンドラインから指定して動作できるかのテスト用)
     */
    @Bean
    public Job testJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("testJob")
                .incrementer(new RunIdIncrementer()) // ジョブが実行状態を維持するのにDBを使用するためincrementerが必要になる
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step testJobStep1(JdbcBatchItemWriter<Person> writer) {
        return stepBuilderFactory.get("step1")
                .<Person, Person> chunk(10) // 一度に書き込む量を定義
                .reader((reader()))
                .processor(processor())
                .writer(writer)
                .build();
    }
}
