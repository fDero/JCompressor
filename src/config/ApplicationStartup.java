package config;



//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "components")
public class ApplicationStartup {

    public static void main(String[] args) {
        //AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Main.class);
        //MessageController printer = context.getBean(MessageController.class);
        //printer.printMessage();
        //context.close();

        System.out.println("Hello, World!");
    }
}