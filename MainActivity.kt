package com.example.atividade.ui.theme

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.atividade.Estoque
import com.example.atividade.Produto
import com.example.atividade.ui.theme.ui.theme.AtividadeTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppNavigation()
        }
    }
}


@Composable
fun telaCadastroProduto(navController: NavController) {
    var nome by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var preco by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("") }
    val context = LocalContext.current

    var listaProdutos by remember { mutableStateOf(mutableListOf<Produto>()) }

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome do Produto") })

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = categoria, onValueChange = { categoria = it }, label = { Text("Categoria") })

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = preco, onValueChange = { preco = it }, label = { Text("Preço") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(15.dp))

        TextField(value = quantidade, onValueChange = { quantidade = it }, label = { Text("Quantidade em Estoque") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (nome.isEmpty() || categoria.isEmpty() || preco.isEmpty() || quantidade.isEmpty()) {
                Toast.makeText(context, "Preencha todos os campos.", Toast.LENGTH_SHORT).show()
            }
            else if (preco.toDoubleOrNull() == null || preco.toDouble() < 0 ){
                Toast.makeText(context, "O preço deve ser um valor positivo.", Toast.LENGTH_SHORT).show()
            }
            else if(quantidade.toIntOrNull() == null || quantidade.toInt() < 1){
                Toast.makeText(context, "A quantidade deve ser maior que 0.", Toast.LENGTH_SHORT).show()
            }
            else {
                val produto = Produto(
                    nome = nome,
                    categoria = categoria,
                    preco = preco.toDouble(),
                    quantidade = quantidade.toInt()
                )
                Estoque.adicionarProduto(produto)

                Toast.makeText(context, "Produto cadastrado!", Toast.LENGTH_SHORT).show()

                nome = ""
                categoria = ""
                preco = ""
                quantidade = ""
            }
        }) {
            Text("Cadastrar produto")
        }
    }
}

@Composable
fun telaListaProduto(navController: NavController, produtos: List<Produto>) {

    val produtos = Estoque.obterProdutos()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {

            items(produtos) { produto ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${produto.nome} (${produto.quantidade} unidades)")

                    Button(onClick = {

                        val produtoJson = Gson().toJson(produto)

                        navController.navigate("detalhes do produto/$produtoJson")
                    }) {
                        Text("Detalhes")
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = { navController.navigate("estatisticas")}){
            Text("Ver Estatísticas")
        }
    }
}

@Composable
fun telaDetalhesProduto(navController: NavController, produtoJson: String){

    val produto = Gson().fromJson(produtoJson, Produto::class.java)

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Text(text= "Nome: ${produto.nome}")
        Text(text= "Categoria: ${produto.categoria}")
        Text(text= "Preço: ${produto.preco}")
        Text(text= "Quantidade: ${produto.quantidade}")

        Spacer(modifier = Modifier.height(15.dp))

        Button(onClick = {navController.popBackStack()}){

            Text(text = "Voltar")
        }
    }
}

@Composable
fun telaEstatisticas (navController: NavController){

    val valorTotalEstoque = Estoque.calcularValorTotalEstoque()
    val quantidadeTotalProdutos = Estoque.calcularQuantidadeTotalProdutos()

    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

        Text(text ="O valor total do estoque é de R$ $valorTotalEstoque" )
        Text(text= "A quantidade total é de $quantidadeTotalProdutos unidades")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {navController.popBackStack()}){

            Text(text= "Voltar")
        }
    }
}

@Composable
fun AppNavigation(){

    val navController= rememberNavController()
    var listaProdutos by remember { mutableStateOf(mutableListOf<Produto>()) }

    NavHost(navController = navController, startDestination = "cadastroProduto"){
        composable("cadastroProduto"){
            telaCadastroProduto(navController = navController)
        }

        composable("listaProdutos"){
            telaListaProduto(navController = navController, produtos = listaProdutos)
        }

        composable("detalhesProduto/{produtoJson}"){ navBackStackEntry ->
            val produtoJson = navBackStackEntry.arguments?.getString("produtoJson") ?: ""
            telaDetalhesProduto(navController = navController, produtoJson = produtoJson)
        }

        composable("estatisticas"){
            telaEstatisticas(navController = navController)
        }
    }

}


@Preview(showBackground = true)
@Composable
fun Preview() {
    AppNavigation()
}
