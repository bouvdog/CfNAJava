package sequenceofplay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "tablesandcharts", "map"} )
public class GameTurn {

    public static void main(String[] args) {
        SpringApplication.run(GameTurn.class, args);
    }
}
