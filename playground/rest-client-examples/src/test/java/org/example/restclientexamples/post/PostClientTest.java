package org.example.restclientexamples.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(PostClient.class)
class PostClientTest {

    @Autowired
    MockRestServiceServer server; // 현재 SimpleRestClientHttpRequestFactory 외 다른 구현체를 사용하면 목킹이 안되는 이슈가 있음

    @Autowired
    PostClient postClient;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldFindAll() throws JsonProcessingException {
        // given
        List<Post> data = List.of(
                new Post(1, 1, "title", "body"),
                new Post(2, 2, "title2", "body2")
        );

        server.expect(request -> {
            assertEquals("https://jsonplaceholder.typicode.com/posts", request.getURI().toString());
        }).andRespond(withSuccess(objectMapper.writeValueAsString(data), MediaType.APPLICATION_JSON));

        // when
        List<Post> posts = postClient.findAll();

        // then
        assertEquals(2, posts.size());
    }
}