package com.example.swproj22.bootstrap;

import com.example.swproj22.domain.Tag;
import com.example.swproj22.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TagLoader implements CommandLineRunner {

    private final TagRepository tagRepository;

    @Autowired
    public TagLoader(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    @Override
    public void run(String... args)throws Exception{
        loadTags();
    }
    
    private void loadTags(){
        if(tagRepository.count() == 0){
            tagRepository.save(new Tag(null, "로그인"));
            tagRepository.save(new Tag(null, "회원가입"));
            tagRepository.save(new Tag(null, "이슈생성"));
            tagRepository.save(new Tag(null, "코멘트"));
        }
    }

}
