package org.example.restclientexamples.post;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@Slf4j
public class PostClient {

    private final RestClient restClient;

    public PostClient(RestClient.Builder builder, ClientHttpRequestInterceptor myInterceptor) {
//        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
//        factory.setReadTimeout(10_000);

//        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

        this.restClient = builder
                .baseUrl("https://jsonplaceholder.typicode.com")
//                .requestFactory(factory)
//                .requestInterceptor(myInterceptor)
                .requestInterceptor((request, body, execution) -> {
                    log.info("Intercepting request: " + request.getURI());
                    request.getHeaders().add("x-request-id", "Custom-Value");
                    return execution.execute(request, body);
                })
                .build();
    }

    public List<Post> findAll() {
        return restClient.get()
                .uri("/posts")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public Post findById(Long id) {
        return restClient.get()
                .uri("/posts/{id}", id)
                .retrieve()
                .body(Post.class);
    }

    public Post create(Post post) {
        return restClient.post()
                .uri("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .retrieve()
                .body(Post.class);
    }

    public Post update(Long id, Post post) {
        return restClient.put()
                .uri("/posts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .body(post)
                .retrieve()
                .body(Post.class);
    }

    public void delete(Long id) {
        restClient.delete()
                .uri("/posts/{id}", id)
                .retrieve()
                .toBodilessEntity();
    }
}
