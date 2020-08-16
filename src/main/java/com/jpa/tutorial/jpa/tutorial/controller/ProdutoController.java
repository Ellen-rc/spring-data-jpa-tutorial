package com.jpa.tutorial.jpa.tutorial.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jpa.tutorial.jpa.tutorial.entity.ProdutoEntity;
import com.jpa.tutorial.jpa.tutorial.service.ProdutoService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class ProdutoController {

	private static final Logger logger = LogManager.getLogger(ProdutoController.class);

	private ProdutoService produtoService;

	@PostMapping("/produto")
	public ResponseEntity<ProdutoEntity> criarProduto(@RequestBody ProdutoEntity entity) {
		logger.info("Criando novo produto {}", entity);

		ProdutoEntity novoProduto = produtoService.salvarOuAtualizar(entity);

		try {
			return ResponseEntity.created(new URI("/produto/" + novoProduto.getId())).body(novoProduto);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/produto/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
		return produtoService.findById(id).map(produto -> {
			return buscarProdutoPorId(produto);
		}).orElse(ResponseEntity.notFound().build());
	}

	private ResponseEntity<?> buscarProdutoPorId(ProdutoEntity produto) {
		try {
			return ResponseEntity.ok().location(new URI("/produto/" + produto.getId())).body(produto);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/produtos")
	public Iterable<ProdutoEntity> buscarTodosProdutos() {
		return produtoService.findAll();
	}

	@DeleteMapping("/produto/{id}")
	public ResponseEntity<?> deletarProduto(@PathVariable Long id) {

		logger.info("Deletando produto com o id {}", id);

		Optional<ProdutoEntity> existingProduct = produtoService.findById(id);

		return existingProduct.map(p -> {
			try {
				produtoService.deleteById(id);
				return ResponseEntity.ok().build();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/produto/{id}")
	public ResponseEntity<?> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoEntity produtoEntity) {
		logger.info("Atualizando Produto {} com o Id {}", produtoEntity, id);

		Optional<ProdutoEntity> existingProduct = produtoService.findById(id);

		return existingProduct.map(p -> {
			p.setNome(produtoEntity.getNome());
			p.setCodigoBarras(produtoEntity.getCodigoBarras());
			p.setPreco(produtoEntity.getPreco());

			logger.info("Atualizando o produto {}", p);

			if (produtoService.salvarOuAtualizar(p) != null) {
				return buscarProdutoPorId(p);
			} else {
				return ResponseEntity.notFound().build();
			}
		}).orElse(ResponseEntity.notFound().build());
	}
}
