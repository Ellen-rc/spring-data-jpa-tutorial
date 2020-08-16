package com.jpa.tutorial.jpa.tutorial.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jpa.tutorial.jpa.tutorial.entity.ProdutoEntity;
import com.jpa.tutorial.jpa.tutorial.repository.ProdutoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProdutoService {

	private ProdutoRepository produtoRepository;

	public ProdutoEntity salvarOuAtualizar(ProdutoEntity entity) {
		return produtoRepository.save(entity);
	}

	public Optional<ProdutoEntity> findById(Long id) {
		return produtoRepository.findById(id);
	}

	public Iterable<ProdutoEntity> findAll() {
		return produtoRepository.findAll();
	}

	public void deleteById(Long id) {
		produtoRepository.findById(id);
	}
}
