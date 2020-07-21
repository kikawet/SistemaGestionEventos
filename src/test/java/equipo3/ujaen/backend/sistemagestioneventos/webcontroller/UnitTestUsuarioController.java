package equipo3.ujaen.backend.sistemagestioneventos.webcontroller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import equipo3.ujaen.backend.sistemagestioneventos.dao.EventoDao;
import equipo3.ujaen.backend.sistemagestioneventos.dao.UsuarioDao;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTO;
import equipo3.ujaen.backend.sistemagestioneventos.dtos.UsuarioDTODetails;
import equipo3.ujaen.backend.sistemagestioneventos.interfaces.InterfaceSistemaGestionEventos;

@ActiveProfiles("test")
@WebMvcTest(UsuarioController.class)
public class UnitTestUsuarioController {

    @Autowired
    private MockMvc mvc;

    @MockBean
    InterfaceSistemaGestionEventos sge;

    @MockBean
    private EventoDao eventoDao;

    @MockBean
    private UsuarioDao usuarioDao;

    @MockBean // necesario para testAccesoPerfil
    InMemoryUserDetailsManager manager;

    private static final String rootPath = "/usuario";

    @PostConstruct
    void init() {
	String login = "flo00008";

	BDDMockito.given(manager.loadUserByUsername(login)).willReturn(new UsuarioDTODetails(new UsuarioDTO(login, "1234")));
    }

    @Test
    void testLoginPage() throws Exception {
	mvc.perform(get(rootPath + "/login"))
	.andExpect(status().isOk());
    }

    @Test
    void testLogeandose() throws Exception{

	mvc.perform(post(rootPath + "/login")
		.param("username", "admin")
		.param("password", "secreto")
		.with(csrf()))
	.andExpect(ResultMatcher.matchAll(
		status().is3xxRedirection(),
		redirectedUrl("/")));
    }

    @Test
    void testRegistrandose() throws Exception{
	mvc.perform(post(rootPath + "/registro")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.param("login", "flo00008")
		.param("email", "flo00008@red.ujaen.es")
		.param("password", "6~\\n=X}Q+tTR5fW!")
		.param("terminos", "false")//TODO no debería pasar el test sin aceptar los terminos
		.with(csrf()))
	.andExpect(ResultMatcher.matchAll(
		status().is3xxRedirection(),
		redirectedUrl("/")));
    }

    @Test
    void testErrorRegistro() throws Exception{
	mvc.perform(post(rootPath + "/registro")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
		.param("login", "flo00008")
		.param("email", "flo00008@red.ujaen.es")
		.param("password", "X")//Cotnraseña poco segura
		.param("terminos", "false")//TODO no debería pasar el test sin aceptar los terminos
		.with(csrf()))
	.andExpect(ResultMatcher.matchAll(
		status().isOk(),
		model().attributeHasFieldErrors("usuario", "password")
		));
    }

    @Test
    void testRegsitroPage() throws Exception {
	mvc.perform(get(rootPath + "/registro"))
	.andExpect(status().isOk());
    }

    @Test
    void testPermisosPerfil() throws Exception {
	mvc.perform(get(rootPath + "/perfil"))
	.andExpect(ResultMatcher.matchAll(
		status().is3xxRedirection(),
		redirectedUrlPattern("**" + rootPath + "/login")));
    }

    @WithUserDetails("flo00008")
    @Test
    void testAccesoPerfil() throws Exception{
	mvc.perform(get(rootPath + "/perfil"))
	.andExpect(ResultMatcher.matchAll(
		status().isOk(),
		model().attributeExists("usuario", "creados", "inscritos")
		));
    }



}
