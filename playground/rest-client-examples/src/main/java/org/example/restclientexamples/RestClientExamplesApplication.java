package org.example.restclientexamples;

import org.example.restclientexamples.post.Post;
import org.example.restclientexamples.post.PostClient;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class RestClientExamplesApplication {

    public static void main(String[] args) {
        System.setProperty("jdk.httpclient.HttpClient.log", "all");
        SpringApplication.run(RestClientExamplesApplication.class, args);
    }

//    @Bean
//    ApplicationRunner applicationRunner(PostClient postClient) {
//        return args -> {
//            List<Post> posts = postClient.findAll();
//            System.out.println(posts);
//        };
//    }
}
