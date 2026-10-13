package com.baozi.store;

import com.baozi.store.repository.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
class ApiTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;
    @Autowired PedidoRepository pedidos;
    @Autowired ClienteRepository clientes;
    @Autowired ProdutoRepository produtos;

    @BeforeEach void limpar() {
        pedidos.deleteAll(); clientes.deleteAll(); produtos.deleteAll();
    }
    long criar(String rota, String corpo) throws Exception {
        MvcResult resultado = mvc.perform(post(rota).contentType("application/json").content(corpo))
            .andExpect(status().isCreated()).andReturn();
        long id = json.readTree(resultado.getResponse().getContentAsString()).get("id").asLong();
        assertEquals(rota + "/" + id, resultado.getResponse().getHeader("Location"));
        return id;
    }
    long cliente() throws Exception {
        return criar("/clientes", "{\"nome\":\"Fernando Santos Reis4648180\",\"clienteDesde\":\"2026-10-12\"}");
    }
    long produto(boolean estoque) throws Exception {
        return criar("/produtos", "{\"nome\":\"Baozi de porco\",\"preco\":8.50,\"estoque\":" + estoque + "}");
    }
    String pedido(long cliente, long produto, int quantidade) {
        return "{\"clienteId\":" + cliente + ",\"produtoId\":" + produto + ",\"quantidade\":" + quantidade + "}";
    }
    @Test void fluxoCompletoDasTresEntidades() throws Exception {
        long c=cliente(), p=produto(true), v=criar("/pedidos",pedido(c,p,3));
        mvc.perform(get("/clientes/"+c)).andExpect(status().isOk()).andExpect(jsonPath("$.nome").value("Fernando Santos Reis4648180"));
        mvc.perform(get("/produtos/"+p)).andExpect(status().isOk()).andExpect(jsonPath("$.preco").value(8.5));
        mvc.perform(get("/pedidos/"+v)).andExpect(status().isOk())
            .andExpect(jsonPath("$.clienteId").value(c)).andExpect(jsonPath("$.produtoId").value(p))
            .andExpect(jsonPath("$.quantidade").value(3));
        for(String rota:new String[]{"/clientes","/produtos","/pedidos"})
            mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(1));
        mvc.perform(delete("/clientes/"+c)).andExpect(status().isConflict());
        mvc.perform(delete("/produtos/"+p)).andExpect(status().isConflict());
        mvc.perform(delete("/pedidos/"+v)).andExpect(status().isNoContent());
        mvc.perform(delete("/clientes/"+c)).andExpect(status().isNoContent());
        mvc.perform(delete("/produtos/"+p)).andExpect(status().isNoContent());
        for(String rota:new String[]{"/clientes","/produtos","/pedidos"})
            mvc.perform(get(rota)).andExpect(status().isOk()).andExpect(content().json("[]"));
    }
    @Test void idsInexistentes() throws Exception {
        for(String rota:new String[]{"/clientes","/produtos","/pedidos"}) {
            mvc.perform(get(rota+"/999999")).andExpect(status().isNotFound());
            mvc.perform(delete(rota+"/999999")).andExpect(status().isNotFound());
        }
        long c=cliente(), p=produto(true);
        mvc.perform(post("/pedidos").contentType("application/json").content(pedido(999999,p,3))).andExpect(status().isNotFound());
        mvc.perform(post("/pedidos").contentType("application/json").content(pedido(c,999999,3))).andExpect(status().isNotFound());
    }
    @Test void validaDadosEEstoque() throws Exception {
        mvc.perform(post("/clientes").contentType("application/json").content("{\"nome\":\"\",\"clienteDesde\":\"2026-10-12\"}"))
            .andExpect(status().isBadRequest());
        mvc.perform(post("/produtos").contentType("application/json").content("{\"nome\":\"Baozi\",\"preco\":-1,\"estoque\":true}"))
            .andExpect(status().isBadRequest());
        long c=cliente(), p=produto(false);
        mvc.perform(post("/pedidos").contentType("application/json").content(pedido(c,p,0))).andExpect(status().isBadRequest());
        mvc.perform(post("/pedidos").contentType("application/json").content(pedido(c,p,3))).andExpect(status().isConflict());
        mvc.perform(post("/pedidos").contentType("application/json").content("{}" )).andExpect(status().isBadRequest());
        assertEquals(0, pedidos.count());
    }
}
