package com.example.atividade

data class Produto (
    val nome: String,
    val categoria: String,
    val preco: Double,
    val quantidade: Int
)

class Estoque {

    companion object {
        private val listaProdutos = mutableListOf<Produto>()

        fun adicionarProduto(produto: Produto): Boolean {
            listaProdutos.add(produto)
            return true
        }

        fun calcularValorTotalEstoque(): Double {
            return listaProdutos.sumOf { it.preco * it.quantidade }
        }

        fun calcularQuantidadeTotalProdutos(): Int {
            return listaProdutos.sumOf { it.quantidade }
        }

        fun obterProdutos(): List<Produto> {
            return listaProdutos
        }
    }
}
