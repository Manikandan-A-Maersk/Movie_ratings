package com.movie_ratings.service;


import com.movie_ratings.dto.Ratings_dto;
import com.movie_ratings.repository.Ratings_repository;
import com.movie_ratings.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class Ratings_Service {

    @Autowired
    private Ratings_repository rp;

    public Flux<Ratings_dto> getallratings() {
        return rp.findAll().map(AppUtils::entityToDto);

    }

    public Mono<Ratings_dto> getrating(String id) {
        return rp.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("No data found for this id")))
                .map(AppUtils::entityToDto)
                .log();
    }

    public Mono<Ratings_dto> saverating(Mono<Ratings_dto> rt) {
        return rt.map(AppUtils::dtoToEntity)
                .flatMap(rp::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<Ratings_dto> updaterating(Mono<Ratings_dto> rt, String id) {
        return rp.findById(id)
                .flatMap(p -> rt.map(AppUtils::dtoToEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(rp::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleterating(String id) {
        return rp.deleteById(id);
    }

}
