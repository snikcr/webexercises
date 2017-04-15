package practice;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserRestApiTest {

	@Autowired
	private WebApplicationContext wac;
	
	private MockMvc mockMvc;
	
	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testGetAllUsers() throws Exception {
		
		this.mockMvc
				.perform(get("/api/user/getall").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$", hasSize(8)))
				.andExpect(jsonPath("$[5].id", is(6)))
				.andExpect(jsonPath("$[5].name", is("Meriadoc")))
				.andExpect(jsonPath("$[5].birthdate", is("08-12-1976")));
	}
	
	@Test
	public void testGetOneUser() throws Exception {
		
		//if the User exists, it is returned
		this.mockMvc.perform(get("/api/user/get/2").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.name", is("Aragorn")))
				.andExpect(jsonPath("$.birthdate", is("20-10-1958")));
		
		//if the User does not exist, the returned status is 404 (not found) 
		this.mockMvc.perform(get("/api/user/get/50").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isNotFound());
		
		//if the User id is invalid (less than 1), the returned status is 500 (bad request)
		this.mockMvc.perform(get("/api/user/get/-2").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
	}
	
	@Test
	public void testCreateUser() throws Exception {
		
		String jsonUserToCreate = "{ \"name\": \"Boromir\", \"birthdate\": \"18-01-1972\" }";
		
		this.mockMvc.perform(post("/api/user/create").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonUserToCreate))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(9)))
				.andExpect(jsonPath("$.name", is("Boromir")))
				.andExpect(jsonPath("$.birthdate", is("18-01-1972")));
		
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		
		//check existence of User 5
		this.mockMvc.perform(get("/api/user/get/5").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(5)))
				.andExpect(jsonPath("$.name", is("Samsagaz")))
				.andExpect(jsonPath("$.birthdate", is("10-02-1971")));
		
		/*
		 * Will update User with id 5: Samsagaz -> Sam; 10-02-1971 -> 25-02-1971
		 */
		String jsonUserToUpdate = "{ \"id\": 5, \"name\": \"Sam\", \"birthdate\": \"25-02-1971\" }";
		
		this.mockMvc.perform(post("/api/user/update").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonUserToUpdate))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(5)))
				.andExpect(jsonPath("$.name", is("Sam")))
				.andExpect(jsonPath("$.birthdate", is("25-02-1971")));
		
		//if one of the fields is invalid , the returned status is 500 (bad request)
		/// invalid id -5
		this.mockMvc.perform(get("/api/user/get/-5").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		/// invalid birth date aaaa
		String jsonIncorrectDateToUpdate = "{ \"id\": 5, \"name\": \"Sam\", \"birthdate\": \"aaaa\" }";
		this.mockMvc.perform(post("/api/user/update").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonIncorrectDateToUpdate))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		//if any of the fields is missing, the returned status is 500 (bad request)
		String jsonMissingNameToUpdate = "{ \"id\": 5, \"birthdate\": \"25-02-1971\" }";
		this.mockMvc.perform(post("/api/user/update").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
				.content(jsonMissingNameToUpdate))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		
	}
	
	@Test
	public void testRemoveUser() throws Exception {
		
		//check existence of User 8
		this.mockMvc.perform(get("/api/user/get/7").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(7)))
				.andExpect(jsonPath("$.name", is("Peregrin")))
				.andExpect(jsonPath("$.birthdate", is("28-08-1968")));
		
		//if the User id is invalid (less than 1), the returned status is 500 (bad request)
		this.mockMvc.perform(get("/api/user/remove/-7").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
		
		this.mockMvc.perform(get("/api/user/remove/7").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isOk());
		
		//after removal, get user should return not found state
		this.mockMvc.perform(get("/api/user/get/7").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(status().isNotFound());
		

	}
}
