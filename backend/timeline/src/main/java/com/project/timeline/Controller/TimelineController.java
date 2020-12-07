package com.project.timeline.Controller;

import com.project.timeline.model.LikeTable;
import com.project.timeline.model.Post;
import com.project.timeline.service.TimelineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/post")
public class TimelineController {

    @Autowired
    private TimelineService timelineService;

    @PostMapping(value = "/create")
    public Post create(@RequestBody Post post){
        return timelineService.create(post);
    }

    @PostMapping(value = "/like")
    private void like(@RequestBody LikeTable like){
        timelineService.toggleLike(like);
    }

    @GetMapping(value = "{postId}/like")
    private Integer countLikesInAPost(@PathVariable Integer postId){
        return timelineService.countLikesInAPost(postId);
    }

}
