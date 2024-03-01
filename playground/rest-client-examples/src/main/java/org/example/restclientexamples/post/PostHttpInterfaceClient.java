package org.example.restclientexamples.post;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;

import java.util.List;

public interface PostHttpInterfaceClient {

    @GetExchange("/posts")
    List<Post> findAll();

    @GetExchange("/posts/{id}")
    Post findById(@PathVariable Long id);

    @PostExchange("/posts")
    Post create(Post post);

    @PutExchange("/posts/{id}")
    Post update(@PathVariable Long id, Post post);

    @DeleteExchange("/posts/{id}")
    void delete(@PathVariable Long id);
}
