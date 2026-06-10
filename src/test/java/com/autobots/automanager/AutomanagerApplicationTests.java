package com.autobots.automanager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
public class AutomanagerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testEmpresaEndpoint() throws Exception {
		mockMvc.perform(get("/empresa").with(user("admin").roles("ADMINISTRADOR")))
				.andExpect(status().isOk());
	}

	@Test
	public void testUsuarioEndpoint() throws Exception {
		mockMvc.perform(get("/usuario").with(user("admin").roles("ADMINISTRADOR")))
				.andExpect(status().isOk());
	}

	@Test
	public void testVeiculoEndpoint() throws Exception {
		mockMvc.perform(get("/veiculo").with(user("admin").roles("ADMINISTRADOR")))
				.andExpect(status().isOk());
	}

	@Test
	public void testMercadoriaEndpoint() throws Exception {
		mockMvc.perform(get("/mercadoria").with(user("admin").roles("ADMINISTRADOR")))
				.andExpect(status().isOk());
	}

	@Test
	public void testServicoEndpoint() throws Exception {
		mockMvc.perform(get("/servico").with(user("admin").roles("ADMINISTRADOR")))
				.andExpect(status().isOk());
	}

	@Test
	public void testVendaEndpoint() throws Exception {
		mockMvc.perform(get("/venda").with(user("admin").roles("ADMINISTRADOR")))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "CLIENTE")
	public void testClienteNaoPodeExcluirMercadoria() throws Exception {
		mockMvc.perform(delete("/mercadoria/excluir/1"))
				.andExpect(status().isForbidden());
	}
}
