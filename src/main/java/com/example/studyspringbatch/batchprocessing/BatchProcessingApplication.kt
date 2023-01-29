package com.example.studyspringbatch.batchprocessing

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/*
@SpringBootApplicationは以下を追加する便利なアノテーションである。
- @Configuration: application contextのBean定義のソースとしてこのクラスをタグ付けする
- @EnableAutoConfiguration: Spring Bootに以下を指示する。
                            クラスパス設定、他のBeanおよびその他様々なプロパティ設定に基づいてBeanの追加を開始すること。
- @ComponentScan: com.exampleパッケージ内の他コンポーネント、設定、サービスを探し、コントローラを見つけさせるようSpringに指示する。
 */
@SpringBootApplication
class BatchProcessingApplication
fun main(args: Array<String>) {
    // SpringApplication.run() と web.xmlの関係（xmlを書かずに済む理由）
    // SpringApplication.exitとSystem.exitは、ジョブ完了時にJVMが終了することを保証する
    System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication::class.java, *args)))
}