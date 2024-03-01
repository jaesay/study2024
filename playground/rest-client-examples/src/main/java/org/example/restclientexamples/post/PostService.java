package org.example.restclientexamples.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostClient postClient;

    public List<Post> getPosts() {
        return postClient.findAll();
    }

    public Post getPost(Long id) {
        return postClient.findById(id);
    }

    public Post createPost(Post post) {
        return postClient.create(post);
    }

    public Post updatePost(Long id, Post post) {
        return postClient.update(id, post);
    }

    public void deletePost(Long id) {
        postClient.delete(id);
    }
}
