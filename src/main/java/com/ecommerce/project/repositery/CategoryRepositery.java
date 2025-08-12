package com.ecommerce.project.repositery;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.FluentQuery;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CategoryRepositery implements JpaRepository<CategoryRepositery, Long> {
    @Override
    public void flush() {

    }

    @Override
    public <S extends CategoryRepositery> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends CategoryRepositery> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<CategoryRepositery> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CategoryRepositery getOne(Long aLong) {
        return null;
    }

    @Override
    public CategoryRepositery getById(Long aLong) {
        return null;
    }

    @Override
    public CategoryRepositery getReferenceById(Long aLong) {
        return null;
    }

    @Override
    public <S extends CategoryRepositery> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CategoryRepositery> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends CategoryRepositery> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends CategoryRepositery> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CategoryRepositery> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CategoryRepositery> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CategoryRepositery, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends CategoryRepositery> S save(S entity) {
        return null;
    }

    @Override
    public <S extends CategoryRepositery> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<CategoryRepositery> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public List<CategoryRepositery> findAll() {
        return List.of();
    }

    @Override
    public List<CategoryRepositery> findAllById(Iterable<Long> longs) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(CategoryRepositery entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends CategoryRepositery> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<CategoryRepositery> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<CategoryRepositery> findAll(Pageable pageable) {
        return null;
    }
}
