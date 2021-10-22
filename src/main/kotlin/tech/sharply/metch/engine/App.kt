package tech.sharply.metch.engine

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import tech.sharply.spring_disruptor_mediatr.mediator.DisruptorMediatorImpl

@EnableScheduling
@SpringBootApplication
class App {

    @Bean
    fun mediator(@Autowired context: ApplicationContext): DisruptorMediatorImpl {
        return DisruptorMediatorImpl(context)
    }

}

fun main(args: Array<String>) {
    runApplication<App>(*args)
}
