package com.jpa.tutorial.jpa.tutorial.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpa.tutorial.jpa.tutorial.entity.ProdutoEntity;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {


}
